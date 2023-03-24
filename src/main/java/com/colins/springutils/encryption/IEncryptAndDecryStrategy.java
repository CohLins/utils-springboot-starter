package com.colins.springutils.encryption;


public interface IEncryptAndDecryStrategy {

    String encrypt(String value);

    String decrypt(String value);
}
