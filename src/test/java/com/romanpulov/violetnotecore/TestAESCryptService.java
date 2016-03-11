package com.romanpulov.violetnotecore;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Utils.HexConverter;
import org.junit.Test;

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by 4540 on 28.01.2016.
 */
public class TestAESCryptService {
    private static final String TEST_STREAM_FILE_NAME = "data\\stream_test.bin";
    private static final String TEST_IV_FILE_NAME = "data\\stream_test.bin";
    private static final String TEST_PASSWORD = "password1";
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
        AESCryptService encrypt = new AESCryptService();
        encrypt.generateEncryptCipher(TEST_PASSWORD);
        byte[] ciphertext = encrypt.getCipher().doFinal("Hello, World!".getBytes("UTF-8"));
        System.out.println("cipherText=" + HexConverter.bytesToHex(ciphertext));

        AESCryptService decrypt = new AESCryptService();
        decrypt.generateDecryptCipher(TEST_PASSWORD, encrypt.getSalt(), encrypt.getIv());
        String plaintext = new String(decrypt.getCipher().doFinal(ciphertext), "UTF-8");
        System.out.println("plainText=" + plaintext);
    }

    @Test
    public void testWriteReadMessage() throws Exception {
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
    }

}
