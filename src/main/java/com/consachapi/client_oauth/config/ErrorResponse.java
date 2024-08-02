package com.consachapi.client_oauth.config;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Date;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ErrorResponse {
    private int status;
    private Date timestamp;
    private String error;
    private String message;
    private String path;

    public ErrorResponse(ErrorResponseBuilder builder) {
        this.status = builder.status;
        this.timestamp = builder.timestamp;
        this.error = builder.error;
        this.message = builder.message;
        this.path = builder.path;
    }

    public int getStatus() {
        return status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder();
    }

    public static class ErrorResponseBuilder {
        private int status;
        private Date timestamp;
        private String error;
        private String message;
        private String path;

        public ErrorResponseBuilder setStatus(int status) {
            this.status = status;
            return this;
        }

        public ErrorResponseBuilder setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ErrorResponseBuilder setError(String error) {
            this.error = error;
            return this;
        }

        public ErrorResponseBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public ErrorResponseBuilder setPath(String path) {
            this.path = path;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(this);
        }

    }

}
