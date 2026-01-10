package com.mianki.servicio.servicepart.websocket.filter;

import com.mianki.servicio.servicepart.service.JwtSocketService;
import org.jspecify.annotations.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class JwtInterceptor implements HandshakeInterceptor {

    private final JwtSocketService jwtSocketService;

    public JwtInterceptor(JwtSocketService jwtSocketService) {
        this.jwtSocketService = jwtSocketService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if(request instanceof ServletServerHttpRequest reqServlet){
            String jwt = reqServlet.getServletRequest().getParameter("jwt");
            if(jwt == null)
                return false;
            try{
                var socketclaims = jwtSocketService.validationSocket(jwt);
                String username = socketclaims.getUsername();
                attributes.put("username", username);
                return true;
            }catch (Exception ignore){}
            return false;
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception exception) {

    }
}
