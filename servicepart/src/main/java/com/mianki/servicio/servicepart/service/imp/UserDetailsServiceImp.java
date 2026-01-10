package com.mianki.servicio.servicepart.service.imp;

import com.mianki.servicio.servicepart.models.dtos.UserSecurityDto;
import com.mianki.servicio.servicepart.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException(""));
        UserSecurityDto userSecurity = new UserSecurityDto(
                user.getId(),
                username,
                user.getPassword()
        );
        userSecurity.setAuthoritiesAsRole(user.getRoles());
        return userSecurity;
    }
}
