package com.mianki.servicio.servicepart.models.dtos;

import com.mianki.servicio.servicepart.models.entities.RolesEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSecurityDto implements UserDetails {

    @Getter
    private Long id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserSecurityDto(Long id, String username, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.authorities = authorities;
    }

    public UserSecurityDto(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setAuthoritiesAsRole(List<RolesEntity> roles) {
        authorities = roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName())).toList();
    }
}
