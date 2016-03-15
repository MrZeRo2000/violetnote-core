package com.romanpulov.violetnotecore.AESCrypt;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

/**
 * Created on 22.01.2016.
 */
public class AESCryptService {
    public static final int SALT_LEN = 8;
    private static final int KEY_LEN = 128;
    public static final int IV_LEN = KEY_LEN / 8;
    private static final int ITERATIONS = 65536;
    private static final int AES_BLOCK_SIZE = 16;

    private byte[] salt;
    SecretKey secretKey;
    private byte[] iv;
    private Cipher cipher;

    public byte[] getSalt() {
        return salt;
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public byte[] getIv() {
        return iv;
    }

    public Cipher getCipher() {
        return cipher;
    }

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
//            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
              factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
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

    public void generateEncryptCipher(String password) throws AESCryptException {
        //create cipher
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            throw new AESCryptException(e.getMessage());
        }

        //generate salt
        salt = generateSalt(SALT_LEN);
        secretKey = generateKey(password, salt);

        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        } catch (InvalidKeyException e) {
            throw new AESCryptException(e.getMessage());
        }

        AlgorithmParameters params = cipher.getParameters();
        try {
            iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        } catch (InvalidParameterSpecException e) {
            throw new AESCryptException(e.getMessage());
        }
    }

    public void generateDecryptCipher(String password, byte[] salt, byte[] iv) throws AESCryptException {
        this.salt = salt;
        this.secretKey = generateKey(password, salt);
        this.iv = iv;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
        } catch(NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new AESCryptException(e.getMessage());
        }
    }

    public static OutputStream generateCryptOutputStream (OutputStream output, String password) throws AESCryptException, IOException {
        AESCryptService s = new AESCryptService();
        s.generateEncryptCipher(password);
        output.write(s.getSalt());
        output.write(s.getIv());

        return new CipherOutputStream(output, s.getCipher());
    }

    public static InputStream generateCryptInputStream(InputStream input, String password) throws AESCryptException, IOException {
        int readBytes;

        byte[] inSalt = new byte[AESCryptService.SALT_LEN];
        readBytes = input.read(inSalt, 0, inSalt.length);
        if (readBytes != inSalt.length) {
            throw new AESCryptException("Error reading salt : " + readBytes);
        }

        byte[] inIV = new byte[AESCryptService.IV_LEN];
        readBytes = input.read(inIV, 0, inIV.length);
        if (readBytes != inIV.length) {
            throw new AESCryptException("Error reading IV : " + readBytes);
        }

        AESCryptService inCipher = new AESCryptService();
        inCipher.generateDecryptCipher(password, inSalt, inIV);

        return new CipherInputStream(input, inCipher.getCipher());
    }

}
