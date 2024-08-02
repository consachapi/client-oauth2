package com.consachapi.client_oauth.controllers;

import com.consachapi.client_oauth.config.RestAdapter;
import com.consachapi.client_oauth.dtos.LoginRequestDto;
import com.consachapi.client_oauth.dtos.RefreshRequest;
import com.consachapi.client_oauth.dtos.TokenDto;
import com.consachapi.client_oauth.dtos.UserInfo;
import com.consachapi.client_oauth.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestAdapter
@RequestMapping(AuthController.AUTH)
public class AuthController {
    public static final String AUTH = "/authorize";
    private static final String LOGIN = "/iniciar";
    private static final String USER_INFO = "/userinfo";
    private static final String REFRESH = "/actualizar";
    private static final String LOGOUT = "/salir";

    @Autowired private AuthService authService;

    @PostMapping(LOGIN)
    public ResponseEntity<TokenDto> init(@RequestBody LoginRequestDto login) {
        TokenDto result = authService.authenticate(login);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(USER_INFO)
    public ResponseEntity<UserInfo> profileInfo() {
        return new ResponseEntity<>(authService.getUserInfo(), HttpStatus.OK);
    }

    @PostMapping(REFRESH)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TokenDto> refreshToken(@RequestBody RefreshRequest refreshRequest) {
        return new ResponseEntity<>(authService.refreshToken(refreshRequest.getRefreshToken()), HttpStatus.OK);
    }

    @PostMapping(LOGOUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestBody RefreshRequest refreshRequest) {
        authService.logout(refreshRequest.getRefreshToken());
    }

}
