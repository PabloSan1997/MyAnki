package com.mianki.servicio.servicepart.service.imp;

import com.mianki.servicio.servicepart.exceptions.MyBadRequestException;
import com.mianki.servicio.servicepart.exceptions.RestartSeccionException;
import com.mianki.servicio.servicepart.models.dtos.*;
import com.mianki.servicio.servicepart.models.entities.RolesEntity;
import com.mianki.servicio.servicepart.models.entities.UserEntity;
import com.mianki.servicio.servicepart.repositories.RoleRepository;
import com.mianki.servicio.servicepart.repositories.UserRepository;
import com.mianki.servicio.servicepart.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImp implements UserService, InitService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JwtAccessService jwtAccessService;
    @Autowired
    private JwtLoginService jwtLoginService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtSocketService jwtSocketService;

    @Override
    public DoubleTokenDto login(LoginDto loginDto) {
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();
        var authtoken = new UsernamePasswordAuthenticationToken(username, password);
        try {
            var auth = (UserSecurityDto) authenticationManager.authenticate(authtoken).getPrincipal();
            String accesstoken = jwtAccessService.accessToken((UserSecurityDto) auth);
            var user = userRepository.findByUsername(username).orElseThrow();
            String loginToken = jwtLoginService.loginToken(user);
            return new DoubleTokenDto(accesstoken, loginToken);
        } catch (Exception e) {
            throw new MyBadRequestException("Nombre de usuario o contraseña incorrectos");
        }
    }

    @Override
    public UserInfo userInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user = userRepository.findByUsername(username)
                .orElseThrow(RestartSeccionException::new);
        return new UserInfo(user);
    }

    @Override
    public DoubleTokenDto register(RegisterDto registerDto) {
        if (userRepository.findByUsername(registerDto.getUsername()).isPresent())
            throw new MyBadRequestException("Nombre de usuario ocupado");

        String username = registerDto.getUsername();
        String nickname = registerDto.getNickname();
        String password = passwordEncoder.encode(registerDto.getPassword());
        RolesEntity role = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Roles no existen"));
        List<RolesEntity> roles = new ArrayList<>();
        roles.add(role);
        var preuser = UserEntity.builder().roles(roles).logins(new ArrayList<>())
                .nickname(nickname)
                .password(password)
                .username(username)
                .build();

        var user = userRepository.save(preuser);
        return login(new LoginDto(user.getUsername(), registerDto.getPassword()));
    }

    @Override
    public void logout(String token) {
        LoginTokenClaims logins = jwtLoginService.loginvalidation(token);
        jwtLoginService.logout(logins);
    }

    @Override
    public TokenDto refreshToken(String token) {
        LoginTokenClaims login = jwtLoginService.loginvalidation(token);
        UserEntity user = userRepository.findById(login.getId()).orElseThrow(RestartSeccionException::new);
        UserSecurityDto userSecurityDto = new UserSecurityDto(user.getId(), user.getUsername(), user.getPassword());
        userSecurityDto.setAuthoritiesAsRole(user.getRoles());
        String jwt = jwtAccessService.accessToken(userSecurityDto);
        return new TokenDto(jwt);
    }

    @Override
    public TokenDto socketToken(String token) {
        LoginTokenClaims login = jwtLoginService.loginvalidation(token);
        var claims = new WebSocketClaims(login.getUsername(), login.getIdlogin());
        String jwt = jwtSocketService.webSocketToken(claims);
        return new TokenDto(jwt);
    }

    @Override
    public void init() {
        String[] roles = {"USER", "ADMIN"};
        for (String names : roles) {
            if (roleRepository.findByName(names).isEmpty())
                roleRepository.save(RolesEntity.builder()
                        .name(names).users(new ArrayList<>()).build());
        }

        System.out.println("\nRoles revizados\n");
    }
}
