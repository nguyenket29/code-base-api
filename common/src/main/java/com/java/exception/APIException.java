package com.java.exception;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APIException extends RuntimeException {

    private HttpStatus httpStatus;
    private String message;
    private String code;
    private List<String> data = new ArrayList<>();
    private List<Map<String, String>> errors = new ArrayList<>();

    private APIException() {
    }

    public static APIException from(HttpStatus httpStatus) {
        APIException ret = new APIException();
        ret.httpStatus = httpStatus;
        return ret;
    }

    public APIException withCode(String code) {
        this.code = code;
        return this;
    }

    public APIException withMessage(String message) {
        this.message = message;
        return this;
    }

    public APIException withMessage(List<String> data) {
        this.setData(data);
        return this;
    }

    public APIException withError(String field, String code) {
        Map<String, String> error = new HashMap<>() {{
            put("field", field);
            put("code", code);
        }};
        this.errors.add(error);
        return this;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public List<String> getData() {
        return data;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public List<Map<String, String>> getErrors() {
        return errors;
    }

    public void setErrors(List<Map<String, String>> errors) {
        this.errors = errors;
    }
}
