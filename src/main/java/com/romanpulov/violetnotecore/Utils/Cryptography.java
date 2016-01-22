package com.romanpulov.violetnotecore.Utils;

import java.security.SecureRandom;

/**
 * Created on 22.01.2016.
 */
public class Cryptography {

    public static byte[] generateSalt(int length) {
        byte[] salt = new byte [8];
        SecureRandom rnd = new SecureRandom();
        rnd.nextBytes (salt);
        return salt;
    }

}
