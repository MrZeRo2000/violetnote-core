package com.romanpulov.violetnotecore.Service;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptConfigurationFactory;
import com.romanpulov.violetnotecore.AESCrypt.AESCryptException;
import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Utils.HexConverter;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class StringCryptService {
    public static String encryptStringWithService(AESCryptService aesCryptService, String inputString, String password)
            throws AESCryptException, IOException {
        try (
                ByteArrayOutputStream dataOutputStream = new ByteArrayOutputStream();
                OutputStream cryptOutputStream = aesCryptService.generateCryptOutputStream(dataOutputStream, password);
        )
        {
            //convert and write
            byte[] inputStringAsBytes = inputString.getBytes(StandardCharsets.UTF_8);
            cryptOutputStream.write(inputStringAsBytes);
            cryptOutputStream.flush();
            cryptOutputStream.close();

            //prepare and return encrypted string
            byte[] encryptedStringAsBytes = dataOutputStream.toByteArray();
            return HexConverter.bytesToHex(encryptedStringAsBytes);
        }

    }

    public static String decryptStringWithService(AESCryptService aesCryptService, String inputString, String password)
            throws AESCryptException, IOException {
        try (
                ByteArrayInputStream encryptedStringAsBytes = new ByteArrayInputStream(HexConverter.hexToBytes(inputString));
                InputStream cryptInputStream = aesCryptService.generateCryptInputStream(encryptedStringAsBytes, password);
        )
        {
            //read from encrypted stream
            ByteArrayOutputStream decryptedStringAsBytes = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = cryptInputStream.read(buffer)) != -1) {
                decryptedStringAsBytes.write(buffer, 0, length);
            }

            return decryptedStringAsBytes.toString(StandardCharsets.UTF_8.toString());
        }
    }

    public static String encryptStringAES128(String inputString, String password)
            throws AESCryptException, IOException {
        return encryptStringWithService(
                (new AESCryptService(AESCryptConfigurationFactory.createAES128())),
                inputString,
                password
        );
    }

    public static String decryptStringAES128(String inputString, String password)
            throws AESCryptException, IOException {
        return decryptStringWithService(
                (new AESCryptService(AESCryptConfigurationFactory.createAES128())),
                inputString,
                password
        );
    }

    public static String encryptStringAES256(String inputString, String password)
            throws AESCryptException, IOException {
        return encryptStringWithService(
                (new AESCryptService(AESCryptConfigurationFactory.createAES256())),
                inputString,
                password
        );
    }

    public static String decryptStringAES256(String inputString, String password)
            throws AESCryptException, IOException {
        return decryptStringWithService(
                (new AESCryptService(AESCryptConfigurationFactory.createAES256())),
                inputString,
                password
        );
    }

    public static String encryptString(String inputString, String password)
            throws AESCryptException, IOException {
        return encryptStringAES256(inputString, password);
    }

    public static String decryptString(String inputString, String password)
            throws AESCryptException, IOException {
        try {
            return decryptStringAES256(inputString, password);
        } catch (AESCryptException | IOException e) {
            return decryptStringAES128(inputString, password);
        }
    }
}
