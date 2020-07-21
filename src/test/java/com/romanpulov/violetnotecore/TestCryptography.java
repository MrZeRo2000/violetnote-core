package com.romanpulov.violetnotecore;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Utils.HexConverter;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


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
        Map<String, Long> timeline = new HashMap<>();
        try {
            long prevTime = System.nanoTime();
            byte[] salt = AESCryptService.generateSalt(8);
            long currentTime = prevTime;
            timeline.put("GenerateSalt", currentTime - prevTime);

            String password = "Password1fg sdfg sdfg sdfg sdfg dsgf sdfg sdsfg ";

            prevTime = System.nanoTime();
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            currentTime = System.nanoTime();
            timeline.put("Get SecretKeyFactorty instance", currentTime - prevTime);

            prevTime = System.nanoTime();
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEYLEN_SIZE);
            currentTime = System.nanoTime();
            timeline.put("Get KeySpec", currentTime - prevTime);

            prevTime = System.nanoTime();
            SecretKey key = factory.generateSecret(spec);
            currentTime = System.nanoTime();
            timeline.put("generateSecret", currentTime - prevTime);

            prevTime = System.nanoTime();
            SecretKey secret = new SecretKeySpec(key.getEncoded(), "AES");
            currentTime = System.nanoTime();
            timeline.put("get SecretKeySpec", currentTime - prevTime);

            /* Encrypt the message. */
            prevTime = System.nanoTime();
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            currentTime = System.nanoTime();
            timeline.put("get Cipher instance", currentTime - prevTime);

            prevTime = System.nanoTime();
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            currentTime = System.nanoTime();
            timeline.put("init Cipher", currentTime - prevTime);

            prevTime = System.nanoTime();
            AlgorithmParameters params = cipher.getParameters();
            byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            currentTime = System.nanoTime();
            timeline.put("init IV", currentTime - prevTime);
            System.out.println("iv size:" + iv.length);

            prevTime = System.nanoTime();
            byte[] ciphertext = cipher.doFinal("Hello, World!".getBytes("UTF-8"));
            System.out.println(HexConverter.bytesToHex(ciphertext));
            currentTime = System.nanoTime();
            timeline.put("crypt message", currentTime - prevTime);

            System.out.println("*** Timeline ***");
            for (Map.Entry<String, Long> entry : timeline.entrySet()) {
                System.out.println(entry.getKey() + " - " + entry.getValue()/1000);
            }
            System.out.println("***");

            /* Decrypt the message, given derived key and initialization vector. */
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
            String plaintext = new String(cipher.doFinal(ciphertext), "UTF-8");
            System.out.println(plaintext);

        } catch (NoSuchAlgorithmException e) {
            throw e;
        }
    }

    @Test
    public void multipleKeyGen() throws Exception {
        for (int i = 0; i < 10; i ++)
            keyGen();
    }

}
