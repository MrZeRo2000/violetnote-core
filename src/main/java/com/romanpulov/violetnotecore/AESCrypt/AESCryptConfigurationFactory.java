package com.romanpulov.violetnotecore.AESCrypt;

public class AESCryptConfigurationFactory {
    public static AESCryptConfiguration createDefault() {
        return AESCryptConfigurationFactory.createAES128();
    }

    public static AESCryptConfiguration createAES128() {
        return new AESCryptConfiguration(
                "PBKDF2WithHmacSHA1",
                "AES/CBC/PKCS5Padding",
                128,
                8,
                65536
        );
    }
}
