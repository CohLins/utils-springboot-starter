package com.colins.springutils.entity;

import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 成功 200
     */
    public static final int SUCCESS = 200;

    /**
     * 失败 400
     */
    public static final int FAIL = 400;

    /**
     * 重新登录
     */
    public static final int RE_LOGIN = 401;

    public static final String SUCCESS_MSG = "success";

    private int code;

    private String msg;

    private T data;

    private Object ext;

    private long total;

    public R() {
        super();
    }

    private R(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static <T> R<T> ok() {
        return restResult(null, SUCCESS, SUCCESS_MSG);
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, SUCCESS, SUCCESS_MSG);
    }

    public static <T> R<T> ok(T data, String msg) {
        return restResult(data, SUCCESS, msg);
    }

    public static <T> R<T> ext(T data, Object ext) {
        return restResult(data, SUCCESS, ext);
    }

    public static <T> R<T> ok(T data, long total) {
        return restResult(data, SUCCESS, SUCCESS_MSG, total);
    }

    public static <T> R<T> ok(T data, String msg, long total) {
        return restResult(data, SUCCESS, msg, total);
    }

    public static <T> R<T> fail() {
        return restResult(null, FAIL, null);
    }

    public static <T> R<T> fail(String msg) {
        return restResult(null, FAIL, msg);
    }


    public static <T> R<T> fail(T data) {
        return restResult(data, FAIL, null);
    }

    public static <T> R<T> fail(T data, String msg) {
        return restResult(data, FAIL, msg);
    }

    public static <T> R<T> fail(int code, String msg) {
        return restResult(null, code, msg);
    }

    public static <T> R<T> fail(int code, String msg,long total) {
        return restResult(null, code, msg,total);
    }

    public static <T> R<T> fail(T data, int code, String msg) {
        return restResult(data, code, msg);
    }


    private static <T> R<T> restResult(T data, int code, String msg) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    private static <T> R<T> restResult(T data, int code, Object ext) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setExt(ext);
        return apiResult;
    }

    private static <T> R<T> restResult(T data, int code, String msg, long total) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        apiResult.setTotal(total);
        return apiResult;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> R<T> reLogin(String msg) {
        return restResult(null, RE_LOGIN, msg);
    }

    public Object getExt() {
        return ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }
}
