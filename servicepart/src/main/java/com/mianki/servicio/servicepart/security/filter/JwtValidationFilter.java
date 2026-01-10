package com.mianki.servicio.servicepart.security.filter;

import com.mianki.servicio.servicepart.exceptions.RefreshAccessException;
import com.mianki.servicio.servicepart.models.dtos.ErroDto;
import com.mianki.servicio.servicepart.models.dtos.UserSecurityDto;
import com.mianki.servicio.servicepart.service.JwtAccessService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JwtValidationFilter extends BasicAuthenticationFilter {

    private final JwtAccessService jwtAccessService;

    public JwtValidationFilter(AuthenticationManager authenticationManager, JwtAccessService jwtAccessService) {
        super(authenticationManager);
        this.jwtAccessService = jwtAccessService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        String token = header.replace("Bearer ", "");
        try {
            UserSecurityDto userSecurity = jwtAccessService.accessValidation(token);
            String username = userSecurity.getUsername();
            var authorities = userSecurity.getAuthorities();
            var auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
            chain.doFilter(request, response);
        } catch (RefreshAccessException e) {
            var errordto = new ErroDto(HttpStatus.UNAUTHORIZED, e.getMessage());
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setStatus(errordto.getStatusCode());
            response.getWriter().write(new ObjectMapper().writeValueAsString(errordto));
        } catch (Exception e) {
            chain.doFilter(request, response);
        }
    }
}
