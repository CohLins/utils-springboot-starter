package com.colins.springutils.entity;


import java.util.Date;

public class RequestLogEntity {

    /**
     * 操作模块
     */
    private String title;

    private String describe;


    private Integer requestType;


    /**
     * 请求方法
     */

    private String method;

    /**
     * 请求方式
     */
    private String requestMethod;


    /**
     * 请求url
     */
    private String requestUrl;

    /**
     * 操作地址
     */
    private String requestIp;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 返回参数
     */
    private String requestResult;

    /**
     * 操作状态（0正常 1异常）
     */
    private Integer status;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 操作时间
     */
    private Date requestTime;


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n---------------------------------------------------------------------------------------------------------->请求IP ：" + requestIp).append("\n");
        stringBuilder.append("---------------------------------------------------------------------------------------------------------->请求地址 ：" + requestUrl).append("\n");
        stringBuilder.append("---------------------------------------------------------------------------------------------------------->请求方法 ：" + method).append("\n");
        stringBuilder.append("---------------------------------------------------------------------------------------------------------->请求参数 ：" + requestParam).append("\n");
        stringBuilder.append("---------------------------------------------------------------------------------------------------------->返回参数 ：" + requestResult).append("\n");
        stringBuilder.append("---------------------------------------------------------------------------------------------------------->错误消息 ：" + errorMsg).append("\n");
        return stringBuilder.toString();
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Integer getRequestType() {
        return requestType;
    }

    public void setRequestType(Integer requestType) {
        this.requestType = requestType;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    public String getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }

    public String getRequestResult() {
        return requestResult;
    }

    public void setRequestResult(String requestResult) {
        this.requestResult = requestResult;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
