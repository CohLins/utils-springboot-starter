package com.colins.springutils.encryption.strategy;


import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import com.colins.springutils.config.UtilsConfig;
import com.colins.springutils.encryption.IEncryptAndDecryStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RSAEncryptAndDecryStrategy implements IEncryptAndDecryStrategy {
    private final static Logger log= LoggerFactory.getLogger(RSAEncryptAndDecryStrategy.class);

    private final String publicKey;
    private final String privateKey;

    public RSAEncryptAndDecryStrategy(UtilsConfig utilsConfig) {
        this.privateKey = utilsConfig.getRsaPrivateKey();
        this.publicKey = utilsConfig.getRsaPublicKey();
    }


    @Override
    public String encrypt(String value) {
        try{
            value = SecureUtil.rsa(privateKey,publicKey).encryptBase64(value, KeyType.PrivateKey);
        }catch (Exception e){
            log.warn("Mybatis RSA Encrypt Fail：{}",e.getMessage());
        }
        return value;
    }

    @Override
    public String decrypt(String value) {
        try{
            value = SecureUtil.rsa(privateKey,publicKey).decryptStr(value, KeyType.PublicKey);
        }catch (Exception e){
            log.warn("Mybatis RSA Decrypt Fail：{}",e.getMessage());
        }
        return value;
    }

}
