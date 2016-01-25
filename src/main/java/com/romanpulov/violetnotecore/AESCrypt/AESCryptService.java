package com.romanpulov.violetnotecore.AESCrypt;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;

/**
 * Created on 22.01.2016.
 */
public class AESCryptService {
    private static final int SALT_LEN = 8;
    private static final int KEY_LEN = 128;
    private static final int ITERATIONS = 65536;

    public static byte[] generateSalt(int length) {
        byte[] salt = new byte [length];
        SecureRandom rnd = new SecureRandom();
        rnd.nextBytes (salt);
        return salt;
    }

    public SecretKey generateKey(String password, byte[] salt) throws AESCryptException {
        //factory
        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new AESCryptException(e.getMessage());
        }
        //key spec
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LEN);

        //secret key
        SecretKey temporaryKey;
        try {
            temporaryKey = factory.generateSecret(spec);
        } catch (InvalidKeySpecException e) {
            throw new AESCryptException(e.getMessage());
        }

        return new SecretKeySpec(temporaryKey.getEncoded(), "AES");
    }

    public Cipher generateCipher(String password) throws AESCryptException {
        Cipher cipher;

        //create cipher
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            throw new AESCryptException(e.getMessage());
        }

        return cipher;
    }

}
