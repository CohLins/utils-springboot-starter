package com.colins.springutils.encryption.strategy;

import cn.hutool.crypto.digest.MD5;
import com.colins.springutils.config.UtilsConfig;
import com.colins.springutils.encryption.IEncryptAndDecryStrategy;



public class MD5EncryptStrategy implements IEncryptAndDecryStrategy {

    public MD5EncryptStrategy(UtilsConfig utilsConfig){

    }

    @Override
    public String encrypt(String value) {
        return MD5.create().digestHex16(value);
    }

    @Override
    public String decrypt(String value) {
        return value;
    }


}
