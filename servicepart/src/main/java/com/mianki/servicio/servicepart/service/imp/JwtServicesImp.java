package com.mianki.servicio.servicepart.service.imp;


import com.mianki.servicio.servicepart.exceptions.RefreshAccessException;
import com.mianki.servicio.servicepart.exceptions.RestartSeccionException;
import com.mianki.servicio.servicepart.models.dtos.LoginTokenClaims;
import com.mianki.servicio.servicepart.models.dtos.TokenTimes;
import com.mianki.servicio.servicepart.models.dtos.UserSecurityDto;
import com.mianki.servicio.servicepart.models.dtos.WebSocketClaims;
import com.mianki.servicio.servicepart.models.entities.LoginEntity;
import com.mianki.servicio.servicepart.models.entities.UserEntity;
import com.mianki.servicio.servicepart.repositories.LoginRepository;

import com.mianki.servicio.servicepart.service.JwtAccessService;
import com.mianki.servicio.servicepart.service.JwtLoginService;
import com.mianki.servicio.servicepart.service.JwtSocketService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class JwtServicesImp implements JwtLoginService, JwtAccessService, JwtSocketService {
    @Value("${jwt.access.key}")
    private String accesskey;
    @Value("${jwt.login.key}")
    private String loginkey;
    @Value("${jwt.socket.key}")
    private String socketkey;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private TokenTimes tokenTimes;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private SecretKey getKey(String keyname) {
        String keyview = switch (keyname) {
            case "access" -> accesskey;
            case "login" -> loginkey;
            case "socket" -> socketkey;
            default -> throw new RuntimeException("Invalid key name: " + keyname);
        };

        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(keyview));
    }


    @Override
    public LoginTokenClaims loginvalidation(String token) {
        try {
            SecretKey secretKey = getKey("login");
            Claims claims = Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(token).getPayload();
            Long idlogin = Long.parseLong((String) claims.get("idlogin"));
            String username = claims.getSubject();
            Long id = Long.parseLong((String) claims.get("id"));
            var opLogin = loginRepository.findByUsernameAndId(username, idlogin);
            if (opLogin.isEmpty() || !opLogin.get().getState()) throw new RestartSeccionException();
            return LoginTokenClaims.builder().id(id).idlogin(id).username(username).build();
        } catch (ExpiredJwtException e) {
            throw new RestartSeccionException();
        }
    }

    @Override
    public String loginToken(UserEntity user) {
        SecretKey secretKey = getKey("login");
        String username = user.getUsername();
        Long id = user.getId();
        var prelogin = LoginEntity.builder().user(user).build();
        var login = loginRepository.save(prelogin);
        Claims claims = Jwts.claims().add("idlogin", login.getId().toString())
                .add("id", id.toString()).build();

        return Jwts.builder().signWith(secretKey).claims(claims)
                .issuedAt(new Date())
                .subject(username)
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenTimes.getLoginseconds()))
                .compact();
    }

    @Override
    public void logout(LoginTokenClaims loginTokenClaims) {
        LoginEntity loginEntity = loginRepository
                .findByUsernameAndId(loginTokenClaims.getUsername(), loginTokenClaims.getIdlogin())
                .orElseThrow();
        loginEntity.setState(false);
    }

    @Override
    public UserSecurityDto accessValidation(String token) {
        try {
            SecretKey secretKey = getKey("access");
            Claims claims = Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(token).getPayload();
            String authjwson = (String) claims.get("authorities");
            Long id = Long.parseLong((String) claims.get("id"));
            String username = claims.getSubject();
            List<String> authnames = List.of(
                    objectMapper.readValue(authjwson, String[].class)
            );
            var authorities = authnames.stream()
                    .map(SimpleGrantedAuthority::new).toList();
            return new UserSecurityDto(id, username, authorities);
        } catch (ExpiredJwtException e) {
            throw new RefreshAccessException();
        }
    }

    @Override
    public String accessToken(UserSecurityDto userSecurityDto) {
        SecretKey secretKey = getKey("access");
        List<String> authnames = userSecurityDto.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();
        String authjeson = objectMapper.writeValueAsString(authnames);
        String username = userSecurityDto.getUsername();
        Long id = userSecurityDto.getId();
        Claims claims = Jwts.claims().add("authorities", authjeson)
                .add("id", id.toString()).build();
        return Jwts.builder().signWith(secretKey).claims(claims)
                .issuedAt(new Date())
                .subject(username)
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenTimes.getAccesseconds()))
                .compact();
    }

    @Override
    public WebSocketClaims validationSocket(String token) {
        SecretKey secretKey = getKey("socket");
        Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
        String username = claims.getSubject();
        Long idlogin = Long.parseLong(claims.getId());
        if(loginRepository.findByUsernameAndId(username, idlogin).isEmpty())
            throw new RefreshAccessException();
        return new WebSocketClaims(username, idlogin);
    }

    @Override
    public String webSocketToken(WebSocketClaims webSocketClaims) {
        SecretKey secretKey = getKey("socket");
        String username = webSocketClaims.getUsername();
        Long id = webSocketClaims.getIdlogin();
        return Jwts.builder().signWith(secretKey)
                .subject(username)
                .id(id.toString())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenTimes.getSocketseconds()))
                .issuedAt(new Date())
                .compact();
    }
}
