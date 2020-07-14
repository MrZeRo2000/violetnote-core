package com.romanpulov.violetnotecore.AESCrypt;

import com.romanpulov.violetnotecore.Utils.HexConverter;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

/**
 * Created on 22.01.2016.
 */
public class AESCryptService {
    private static final String SECRET_KEY_INSTANCE = "PBKDF2WithHmacSHA1";
    private static final String CIPHER_INSTANCE = "AES/CBC/PKCS5Padding";
    private static final int KEY_LEN = 128;
    private static final int SALT_LEN = 8;
    private static final int ITERATIONS = 65536;

    private byte[] salt;

    public byte[] getSalt() {
        return salt;
    }

    private SecretKey secretKey;

    private byte[] iv;

    public byte[] getIv() {
        return iv;
    }

    private Cipher cipher;

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
              factory = SecretKeyFactory.getInstance(SECRET_KEY_INSTANCE);
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
            cipher = Cipher.getInstance(CIPHER_INSTANCE);
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
            cipher = Cipher.getInstance(CIPHER_INSTANCE);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
        } catch(NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new AESCryptException(e.getMessage());
        }
    }

    /**
     * Generates output stream for encryption and prepares it for writing
     * writes salt and IV
     * @param output
     * @param password
     * @return
     * @throws AESCryptException
     * @throws IOException
     */
    public static OutputStream generateCryptOutputStream (OutputStream output, String password) throws AESCryptException, IOException {
        AESCryptService s = new AESCryptService();
        s.generateEncryptCipher(password);
        output.write(s.getSalt());
        output.write(s.getIv());

        return new CipherOutputStream(output, s.getCipher());
    }

    /**
     * Generates input stream for decryption and prepares it for reading
     * reads salt and IV
     * @param input
     * @param password
     * @return
     * @throws AESCryptException
     * @throws IOException
     */
    public static InputStream generateCryptInputStream(InputStream input, String password) throws AESCryptException, IOException {
        int readBytes;

        byte[] inSalt = new byte[AESCryptService.SALT_LEN];
        readBytes = input.read(inSalt, 0, inSalt.length);
        if (readBytes != inSalt.length) {
            throw new AESCryptException("Error reading salt : " + readBytes);
        }

        byte[] inIV = new byte[AESCryptService.KEY_LEN / 8];
        readBytes = input.read(inIV, 0, inIV.length);
        if (readBytes != inIV.length) {
            throw new AESCryptException("Error reading IV : " + readBytes);
        }

        AESCryptService inCipher = new AESCryptService();
        inCipher.generateDecryptCipher(password, inSalt, inIV);

        return new CipherInputStream(input, inCipher.getCipher());
    }

    /**
     * Encrypts input string with password
     * Utility procedure
     * @param inputString
     * @param password
     * @return
     * @throws AESCryptException
     * @throws IOException
     */
    public static String encryptString(String inputString, String password) throws AESCryptException, IOException {
        //prepare output stream
        ByteArrayOutputStream dataOutputStream = new ByteArrayOutputStream();
        OutputStream cryptOutputStream = AESCryptService.generateCryptOutputStream(dataOutputStream, password);

        //convert and write
        byte[] inputStringAsBytes = inputString.getBytes(StandardCharsets.UTF_8);
        cryptOutputStream.write(inputStringAsBytes);
        cryptOutputStream.flush();
        cryptOutputStream.close();

        //prepare and return encrypted string
        byte[] encryptedStringAsBytes = dataOutputStream.toByteArray();
        return HexConverter.bytesToHex(encryptedStringAsBytes);
    }

    /**\
     * Decrypts input string with password
     * Utility procedure
     * @param inputString
     * @param password
     * @return
     * @throws AESCryptException
     * @throws IOException
     */
    public static String decryptString(String inputString, String password) throws AESCryptException, IOException {
        //prepare input stream
        ByteArrayInputStream encryptedStringAsBytes = new ByteArrayInputStream(HexConverter.hexToBytes(inputString));
        InputStream cryptInputStream = AESCryptService.generateCryptInputStream(encryptedStringAsBytes, password);

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
