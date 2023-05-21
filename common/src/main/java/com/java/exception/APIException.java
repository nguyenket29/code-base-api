package com.java.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class APIException extends RuntimeException {

    private HttpStatus httpStatus;
    private String message;
    private String errorCode;
    private List<String> data;

    private APIException() {
    }

    public static APIException from(HttpStatus httpStatus) {
        APIException ret = new APIException();
        ret.httpStatus = httpStatus;
        return ret;
    }

    public APIException withErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public APIException withMessage(String message) {
        this.message = message;
        return this;
    }

    public APIException withMessage(List<String> data) {
        this.data = data;
        return this;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public List<String> getData() {
        return data;
    }
}
