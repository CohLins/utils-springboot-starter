package com.colins.springutils.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("my-utils.config")
public class MybatisUtilsConfig {
    private boolean sqlLog;   // sql日志
    private boolean requestLog; // 请求日志
    private String aesKey;
    private String rsaPublicKey;
    private String rsaPrivateKey;

    public boolean isSqlLog() {
        return sqlLog;
    }

    public void setSqlLog(boolean sqlLog) {
        this.sqlLog = sqlLog;
    }

    public boolean isRequestLog() {
        return requestLog;
    }

    public void setRequestLog(boolean requestLog) {
        this.requestLog = requestLog;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public String getRsaPublicKey() {
        return rsaPublicKey;
    }

    public void setRsaPublicKey(String rsaPublicKey) {
        this.rsaPublicKey = rsaPublicKey;
    }

    public String getRsaPrivateKey() {
        return rsaPrivateKey;
    }

    public void setRsaPrivateKey(String rsaPrivateKey) {
        this.rsaPrivateKey = rsaPrivateKey;
    }
}
