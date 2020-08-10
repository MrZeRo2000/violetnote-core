package com.romanpulov.violetnotecore.AESCrypt;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptConfigurationFactory;
import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Service.StringCryptService;
import com.romanpulov.violetnotecore.Utils.HexConverter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

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

        long startTime = System.nanoTime();

        String aesEncryptedString = StringCryptService.encryptStringAES128(testString, TEST_PASSWORD);

        System.out.println("Source string:" + testString);
        System.out.println("Encrypted string AES: " + aesEncryptedString);

        String aesDecryptedString = StringCryptService.decryptStringAES128(aesEncryptedString, TEST_PASSWORD);

        long endTime = System.nanoTime();

        System.out.println("Decrypted string AES:" + aesDecryptedString + ", time:" + (endTime - startTime));

        assertEquals(testString, aesDecryptedString);

        System.out.println("testCryptStringMessage finish");
    }

    @Test
    public void testCryptStringMessage256() throws Exception {
        System.out.println("testCryptStringMessage start");
        String testString = TEST_MESSAGE;

        long startTime = System.nanoTime();

        String aesEncryptedString = StringCryptService.encryptStringAES256(testString, TEST_PASSWORD);

        System.out.println("Source string:" + testString);
        System.out.println("Encrypted string AES256: " + aesEncryptedString);

        String aesDecryptedString = StringCryptService.decryptStringAES256(aesEncryptedString, TEST_PASSWORD);

        long endTime = System.nanoTime();

        System.out.println("Decrypted string AES256:" + aesDecryptedString + ", time:" + (endTime - startTime));
        assertEquals(testString, aesDecryptedString);

        System.out.println("testCryptStringMessage256 finish");
    }

    @Test
    public void testDecryptOldString() throws Exception {
        String testString = TEST_MESSAGE;

        final String aesEncryptedString = StringCryptService.encryptStringAES128(testString, TEST_PASSWORD);
        final String aesDecryptedString = StringCryptService.decryptString(aesEncryptedString, TEST_PASSWORD);

        assertEquals(testString, aesDecryptedString);

        assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                String aesDecryptedString = StringCryptService.decryptStringAES256(aesEncryptedString, TEST_PASSWORD);
                fail("Decrypted: " + aesDecryptedString);
            }
        });

    }

    @Test
    public void testCryptStringWrongPasswordMessage() throws Exception {
        System.out.println("testCryptStringWrongPasswordMessage start");

        String testString = TEST_MESSAGE;
        String aesEncryptedString = StringCryptService.encryptStringAES128(testString, TEST_PASSWORD);

        try {
            String aesDecryptedString = StringCryptService.decryptStringAES128(aesEncryptedString, WRONG_PASSWORD);
        } catch (Exception e) {
            System.out.println("Expected exception:" + e.getMessage());
            System.out.println("testCryptStringWrongPasswordMessage finish");
            return;
        }

        throw new Exception("Should not get here: wrong password did not throw exception");
    }

    @Test
    public void testAES2Crypt() throws Exception {
        AESCryptService writeAESCryptService = new AESCryptService(AESCryptConfigurationFactory.createAES256());

        final String testString = "+- df23rd df0w94df sdfswert 00";
        final String testPassword = "123456";

        byte[] bytes = testString.getBytes(StandardCharsets.UTF_8);

        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        OutputStream outputStream = writeAESCryptService.generateCryptOutputStream(outBuffer, testPassword);
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();

        byte[] outputBytes = outBuffer.toByteArray();

        AESCryptService readAESCryptService = new AESCryptService(AESCryptConfigurationFactory.createAES256());

        InputStream inputStream = readAESCryptService.generateCryptInputStream(new ByteArrayInputStream(outputBytes), testPassword);

        ByteArrayOutputStream readBuffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            readBuffer.write(data, 0, nRead);
        }

        byte[] readBytes = readBuffer.toByteArray();

        assertArrayEquals(bytes, readBytes);
        assertEquals(testString, new String(readBytes, StandardCharsets.UTF_8));
    }
}
