package com.romanpulov.violetnotecore;

import org.junit.Test;

import java.security.SecureRandom;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by on 22.01.2016.
 */
public class TestCrypto {

    @Test
    public void method1() {
        assertEquals(1, 1);
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public void saltGen() {
        byte[] salt = new byte [8];
        SecureRandom rnd = new SecureRandom();
        rnd.nextBytes (salt);
        String saltString = bytesToHex(salt);
        byte[] salt1 = hexToBytes(bytesToHex(salt));
        assertArrayEquals(salt, salt1);
        String saltString1 = bytesToHex(salt1);
        assertEquals(saltString, saltString1);
    }

    @Test
    public void saltGenIterations() {
        for (int i=0; i < 10000; i++)
            saltGen();
    }

}
