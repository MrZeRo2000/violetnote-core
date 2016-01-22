package com.romanpulov.violetnotecore;

import com.romanpulov.violetnotecore.Utils.Cryptography;
import com.romanpulov.violetnotecore.Utils.HexConverter;
import org.junit.Test;

import javax.crypto.SecretKeyFactory;

import java.security.NoSuchAlgorithmException;

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
        byte[] salt = Cryptography.generateSalt(8);
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
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            throw e;
        }
    }

}
