package com.mianki.servicio.servicepart.controllers;

import com.mianki.servicio.servicepart.exceptions.RestartSeccionException;
import com.mianki.servicio.servicepart.models.dtos.*;
import com.mianki.servicio.servicepart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenTimes tokenTimes;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        DoubleTokenDto doubleToken = userService.login(loginDto);
        String logintoken = doubleToken.getLoginToken();
        String accesstoken = doubleToken.getAccessToken();
        ResponseCookie cookie = ResponseCookie.from("milog", logintoken)
                .maxAge(tokenTimes.getLoginseconds())
                .path("/")
                .httpOnly(true)
                .sameSite("Lax")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
        TokenDto tokenDto = new TokenDto(accesstoken);
        return ResponseEntity.ok().headers(headers).body(tokenDto);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        DoubleTokenDto doubleToken = userService.register(registerDto);
        String logintoken = doubleToken.getLoginToken();
        String accesstoken = doubleToken.getAccessToken();
        ResponseCookie cookie = ResponseCookie.from("milog", logintoken)
                .maxAge(tokenTimes.getLoginseconds())
                .path("/")
                .httpOnly(true)
                .sameSite("Lax")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
        TokenDto tokenDto = new TokenDto(accesstoken);
        return ResponseEntity.ok().headers(headers).body(tokenDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(value = "milog", required = false) String token) {
        if (token == null || token.isEmpty()) throw new RestartSeccionException();
        userService.logout(token);
        ResponseCookie cookie = ResponseCookie.from("milog", "")
                .maxAge(0)
                .path("/")
                .httpOnly(true)
                .sameSite("Lax")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.noContent().headers(headers).build();
    }

    @PostMapping("/socket")
    public ResponseEntity<?> getsocket(@CookieValue(value = "milog", required = false) String token){
        if (token == null || token.isEmpty()) throw new RestartSeccionException();
        TokenDto tokenDto = userService.socketToken(token);
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(value = "milog", required = false) String token) {
        if (token == null || token.isEmpty()) throw new RestartSeccionException();
        TokenDto tokenDto = userService.refreshToken(token);
        return ResponseEntity.ok(tokenDto);
    }

    @GetMapping("/userinfo")
    public ResponseEntity<?> userinfo() {
        return ResponseEntity.ok(userService.userInfo());
    }
}
