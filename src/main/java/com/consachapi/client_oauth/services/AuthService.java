package com.consachapi.client_oauth.services;

import com.consachapi.client_oauth.dtos.LoginRequestDto;
import com.consachapi.client_oauth.dtos.TokenDto;
import com.consachapi.client_oauth.dtos.UserInfo;

public interface AuthService {
    TokenDto authenticate(LoginRequestDto loginRequestDto);
    UserInfo getUserInfo();
    TokenDto refreshToken(String refreshToken);
    void logout(String refreshToken);
}
