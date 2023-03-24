package com.colins.springutils.encryption.strategy;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

import com.colins.springutils.config.MybatisUtilsConfig;
import com.colins.springutils.encryption.IEncryptAndDecryStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AESEncryptAndDecryStrategy implements IEncryptAndDecryStrategy {

    private final static Logger log= LoggerFactory.getLogger(AESEncryptAndDecryStrategy.class);
    private final SymmetricCrypto aes;


    public AESEncryptAndDecryStrategy(MybatisUtilsConfig mybatisUtilsConfig){
        this.aes= SecureUtil.aes(SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(),mybatisUtilsConfig.getAesKey().getBytes()).getEncoded());
    }

    @Override
    public String encrypt(String value) {
        try{
            value = aes.encryptBase64(value);
        }catch (Exception e){
            log.warn("Mybatis AES Encrypt Fail：{}",e.getMessage());
        }
        return value;
    }

    @Override
    public String decrypt(String value) {
        try{
            value = aes.decryptStr(value);
        }catch (Exception e){
            log.warn("Mybatis AES Decrypt Fail：{}",e.getMessage());
        }
        return value;
    }


}
