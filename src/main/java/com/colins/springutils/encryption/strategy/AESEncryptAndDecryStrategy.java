package com.colins.springutils.encryption.strategy;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

import com.colins.springutils.config.UtilsConfig;
import com.colins.springutils.encryption.IEncryptAndDecryStrategy;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AESEncryptAndDecryStrategy implements IEncryptAndDecryStrategy {

    private final static Logger log = LoggerFactory.getLogger(AESEncryptAndDecryStrategy.class);
    private final SymmetricCrypto aes;


    public AESEncryptAndDecryStrategy(UtilsConfig utilsConfig){
        this.aes= SecureUtil.aes(SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), utilsConfig.getAesKey().getBytes()).getEncoded());
    }


    @Override
    public String encrypt(String value) {
        try {
            String resResult = aes.encryptBase64(value);
            return StringUtil.isBlank(resResult) ? value : resResult;
        } catch (Exception e) {
            log.warn("Mybatis AES Encrypt Fail：{}", e.getMessage());
        }
        return value;
    }

    @Override
    public String decrypt(String value) {
        try {
            String resResult = aes.decryptStr(value);
            return StringUtil.isBlank(resResult) ? value : resResult;
        } catch (Exception e) {
            log.warn("Mybatis AES Decrypt Fail：{}", e.getMessage());
        }
        return value;
    }

}
