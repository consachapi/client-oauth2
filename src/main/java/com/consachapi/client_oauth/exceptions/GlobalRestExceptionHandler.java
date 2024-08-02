package com.consachapi.client_oauth.exceptions;

import com.consachapi.client_oauth.config.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class GlobalRestExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({
            AccessDeniedException.class
    })
    @ResponseBody
    public ResponseEntity<ErrorResponse> unauthorizedRequest(Exception ex, HttpServletRequest request){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .setStatus(HttpStatus.UNAUTHORIZED.value())
                .setTimestamp(new Date())
                .setMessage(HttpStatus.UNAUTHORIZED.toString())
                .setError(ex.getLocalizedMessage())
                .setPath(request.getRequestURI())
                .build();
        return new ResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
