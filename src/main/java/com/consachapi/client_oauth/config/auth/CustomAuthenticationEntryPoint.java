package com.consachapi.client_oauth.config.auth;

import com.consachapi.client_oauth.config.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .setStatus(HttpStatus.UNAUTHORIZED.value())
                .setTimestamp(new Date())
                .setMessage(HttpStatus.UNAUTHORIZED.toString())
                .setError("No autorizado")
                .setPath(request.getRequestURI())
                .build();
        ObjectMapper mapper = new ObjectMapper();
        authException.printStackTrace();
        mapper.writeValue(response.getWriter(), errorResponse);
    }

}
