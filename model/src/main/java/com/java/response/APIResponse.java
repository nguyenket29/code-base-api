package com.java.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.java.constant.Constants;

import java.util.List;
import java.util.Map;

import static com.java.constant.Constants.StatusCode.FAILED;
import static com.java.constant.Constants.StatusCode.SUCCESS;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"status", "message", "data"})
public class APIResponse<T> {
    @JsonProperty("status")
    private String status;

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("errors")
    private List<Map<String, String>> errors;

    @JsonProperty("data")
    private T data;

    @JsonProperty("dataErrorMessages")
    private T dataErrorMessages;


    public APIResponse() {
    }

    public static <T> APIResponse<T> ok(T data) {
        APIResponse<T> res = new APIResponse<T>();
        res.status = SUCCESS;
        res.data = data;
        return res;
    }

    public static <T> APIResponse<T> ok() {
        APIResponse<T> res = new APIResponse<T>();
        res.status = SUCCESS;
        return res;
    }


    public static <T> APIResponse<T> failed(String code , String message) {
        APIResponse<T> res = new APIResponse<T>();
        res.status = FAILED;
        res.code = code;
        res.message = message;
        return res;
    }

    public static <T> APIResponse<T> failed(String message) {
        APIResponse<T> res = new APIResponse<T>();
        res.status = FAILED;
        res.message = message;
        return res;
    }

    public static <T> APIResponse<T> failed(String code , String message, List<Map<String, String>> errors) {
        APIResponse<T> res = new APIResponse<T>();
        res.status = FAILED;
        res.code = code;
        res.message = message;
        res.errors = errors;
        return res;
    }

    public static <T> APIResponse<T> failed(int code, String messages) {
        APIResponse<T> ret = new APIResponse<>();
        ret.status = FAILED;
        ret.code = String.valueOf(code);
        ret.message = messages;
        return ret;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Map<String, String>> getErrors() {
        return errors;
    }

    public void setErrors(List<Map<String, String>> errors) {
        this.errors = errors;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
