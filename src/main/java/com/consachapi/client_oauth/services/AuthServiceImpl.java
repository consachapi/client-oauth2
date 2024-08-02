package com.consachapi.client_oauth.services;

import com.consachapi.client_oauth.config.auth.AuthUser;
import com.consachapi.client_oauth.dtos.LoginRequestDto;
import com.consachapi.client_oauth.dtos.TokenDto;
import com.consachapi.client_oauth.dtos.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthServiceImpl implements AuthService {
    private static final String GRANT_TYPE_PASSWORD = "password";
    private static final String GRANT_TYPE_REFRESH = "refresh_token";
    private static final String PROTOCOL = "protocol";
    private static final String OPENID = "openid-connect";

    @Value("${service.auth.client-id}")
    private String clientId;

    @Value("${service.auth.client-secret}")
    private String clientSecret;

    @Value("${service.auth.issuer-url}")
    private String issuerUrl;

    @Autowired private RestTemplate restTemplate;
    @Autowired private AuthUser authUser;

    @Override
    public TokenDto authenticate(LoginRequestDto loginRequestDto) {
        return getAccessToken(loginRequestDto);
    }

    @Override
    public UserInfo getUserInfo() {
        String userInfoUrl = issuerUrl + "/" + PROTOCOL + "/" + OPENID + "/" + "userinfo";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authUser.getToken());
        ResponseEntity<UserInfo> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, new HttpEntity<>(headers), UserInfo.class);
        if(response.getStatusCode() != HttpStatus.OK) {
            throw new BadCredentialsException("Invalid params");
        }
        return response.getBody();
    }

    @Override
    public TokenDto refreshToken(String refreshToken) {
        return getRefreshToken(refreshToken);
    }

    @Override
    public void logout(String refreshToken) {
        String logoutUrl = issuerUrl + "/" + PROTOCOL + "/" + OPENID + "/" + "logout";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("refresh_token", refreshToken);

        ResponseEntity<Object> response = restTemplate.postForEntity(
                logoutUrl,
                new HttpEntity<>(requestBody, headers),
                Object.class
        );
    }

    private TokenDto getAccessToken(LoginRequestDto login) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", GRANT_TYPE_PASSWORD);
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("scope", "openid profile email");
        requestBody.add("username", login.getUsername());
        requestBody.add("password", login.getPassword());

        ResponseEntity<TokenDto> response = restTemplate.postForEntity(
                getTokenUrl(),
                new HttpEntity<>(requestBody, headers),
                TokenDto.class
        );
        return response.getBody();
    }

    private TokenDto getRefreshToken(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", GRANT_TYPE_REFRESH);
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("refresh_token", refreshToken);

        ResponseEntity<TokenDto> response = restTemplate.postForEntity(
                getTokenUrl(),
                new HttpEntity<>(requestBody, headers),
                TokenDto.class
        );
        return response.getBody();
    }

    private String getTokenUrl(){
        return issuerUrl + "/" + PROTOCOL + "/" + OPENID + "/" + "token";
    }

}
