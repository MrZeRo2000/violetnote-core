package com.romanpulov.violetnotecore;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Utils.HexConverter;
import org.junit.Test;

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import java.io.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by 4540 on 28.01.2016.
 */
public class TestAESCryptService {
    private static final String TEST_STREAM_FILE_NAME = "data\\stream_test.bin";
    private static final String TEST_IV_FILE_NAME = "data\\stream_test.bin";
    private static final String TEST_PASSWORD = "password1";
    private static final String WRONG_PASSWORD = "p1ssword";
    private static final String TEST_MESSAGE = "This is a test message";

    public void testWriteIV() throws Exception {
        FileOutputStream streamIV = new FileOutputStream(TEST_IV_FILE_NAME);
        AESCryptService s = new AESCryptService();
        s.generateEncryptCipher(TEST_PASSWORD);
        streamIV.write(s.getIv());
        streamIV.flush();
        streamIV.close();
    }

    @Test
    public void testStringMessage() throws Exception {
        System.out.println("testStringMessage start");

        AESCryptService encrypt = new AESCryptService();
        encrypt.generateEncryptCipher(TEST_PASSWORD);
        byte[] ciphertext = encrypt.getCipher().doFinal("Hello, World!".getBytes("UTF-8"));
        System.out.println("cipherText=" + HexConverter.bytesToHex(ciphertext));

        AESCryptService decrypt = new AESCryptService();
        decrypt.generateDecryptCipher(TEST_PASSWORD, encrypt.getSalt(), encrypt.getIv());
        String plaintext = new String(decrypt.getCipher().doFinal(ciphertext), "UTF-8");
        System.out.println("plainText=" + plaintext);

        System.out.println("testStringMessage finish");
    }

    @Test
    public void testWriteReadMessage() throws Exception {
        System.out.println("testWriteReadMessage start");

        FileOutputStream stream = new FileOutputStream(TEST_STREAM_FILE_NAME);

        AESCryptService s = new AESCryptService();
        s.generateEncryptCipher(TEST_PASSWORD);
        stream.write(s.getSalt());
        stream.write(s.getIv());
        CipherOutputStream cipherStream = new CipherOutputStream(stream, s.getCipher());
        cipherStream.write(TEST_MESSAGE.getBytes("UTF-8"));
        cipherStream.flush();
        cipherStream.close();
        stream.flush();
        stream.close();

        FileInputStream inStream = new FileInputStream(TEST_STREAM_FILE_NAME);
        byte[] inSalt = new byte[8];
        inStream.read(inSalt, 0, 8);
        assertArrayEquals(s.getSalt(), inSalt);
        byte[] inIv = new byte[16];
        inStream.read(inIv, 0, 16);
        assertArrayEquals(s.getIv(), inIv);

        AESCryptService inCipher = new AESCryptService();
        inCipher.generateDecryptCipher(TEST_PASSWORD, inSalt, inIv);

        CipherInputStream cipherInputStream = new CipherInputStream(inStream, inCipher.getCipher());

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int r = cipherInputStream.read(buffer);
            if (r == -1) break;
            os.write(buffer, 0, r);
        }
        os.flush();
        inStream.close();
        os.close();
        assertEquals(os.toString(), TEST_MESSAGE);

        System.out.println("testWriteReadMessage finish");
    }

    @Test
    public void testCryptStringMessage() throws Exception {
        System.out.println("testCryptStringMessage start");

        String testString = TEST_MESSAGE;
        String aesEncryptedString = AESCryptService.encryptString(testString, TEST_PASSWORD);

        System.out.println("Source string:" + testString);
        System.out.println("Encrypted string AES: " + aesEncryptedString);

        String aesDecryptedString = AESCryptService.decryptString(aesEncryptedString, TEST_PASSWORD);
        System.out.println("Decrypted string AES:" + aesDecryptedString);

        assertEquals(testString, aesDecryptedString);

        System.out.println("testCryptStringMessage finish");
    }

    @Test
    public void testCryptStringWrongPasswordMessage() throws Exception {
        System.out.println("testCryptStringWrongPasswordMessage start");

        String testString = TEST_MESSAGE;
        String aesEncryptedString = AESCryptService.encryptString(testString, TEST_PASSWORD);

        try {
            String aesDecryptedString = AESCryptService.decryptString(aesEncryptedString, WRONG_PASSWORD);
        } catch (Exception e) {
            System.out.println("Expected exception:" + e.getMessage());
            System.out.println("testCryptStringWrongPasswordMessage finish");
            return;
        }

        throw new Exception("Should not get here: wrong password did not throw exception");
    }
}
