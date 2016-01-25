package com.romanpulov.violetnotecore;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Utils.HexConverter;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

import static org.junit.Assert.*;


/**
 * Created by on 22.01.2016.
 */
public class TestCryptography {

    @Test
    public void method1() {
        assertEquals(1, 1);
    }

    public void saltGenConvert() {
        byte[] salt = AESCryptService.generateSalt(8);
        String saltString = HexConverter.bytesToHex(salt);
        byte[] salt1 = HexConverter.hexToBytes(HexConverter.bytesToHex(salt));
        assertArrayEquals(salt, salt1);
        String saltString1 = HexConverter.bytesToHex(salt1);
        assertEquals(saltString, saltString1);
    }

    @Test
    public void saltGenConvertTest() {
        for (int i=0; i < 10000; i++)
            saltGenConvert();
    }

    @Test
    public void keyGen() throws Exception {
        final int ITERATIONS = 65536;
        final int KEYLEN_SIZE = 128;
        try {
            byte[] salt = AESCryptService.generateSalt(8);
            String password = "Password1fg sdfg sdfg sdfg sdfg dsgf sdfg sdsfg ";
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEYLEN_SIZE);
            SecretKey key = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(key.getEncoded(), "AES");

            /* Encrypt the message. */
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            AlgorithmParameters params = cipher.getParameters();
            byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            System.out.println("iv size:" + iv.length);

            byte[] ciphertext = cipher.doFinal("Hello, World!".getBytes("UTF-8"));
            System.out.println(HexConverter.bytesToHex(ciphertext));


            /* Decrypt the message, given derived key and initialization vector. */
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
            String plaintext = new String(cipher.doFinal(ciphertext), "UTF-8");
            System.out.println(plaintext);

        } catch (NoSuchAlgorithmException e) {
            throw e;
        }
    }

}
