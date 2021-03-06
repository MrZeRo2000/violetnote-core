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

    private final AESCryptConfiguration cryptConfiguration;

    public AESCryptService(AESCryptConfiguration cryptConfiguration) {
        this.cryptConfiguration = cryptConfiguration;
    }

    public AESCryptService() {
        this(AESCryptConfigurationFactory.createDefault());
    }

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
              factory = SecretKeyFactory.getInstance(cryptConfiguration.secretKeyInstance);
        } catch (NoSuchAlgorithmException e) {
            throw new AESCryptException(e.getMessage());
        }
        //key spec
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, cryptConfiguration.iterations, cryptConfiguration.keyLen);

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
            cipher = Cipher.getInstance(cryptConfiguration.cipherInstance);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            throw new AESCryptException(e.getMessage());
        }

        //generate salt
        salt = generateSalt(cryptConfiguration.saltLen);
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
            cipher = Cipher.getInstance(cryptConfiguration.cipherInstance);
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
    public OutputStream generateCryptOutputStream (OutputStream output, String password) throws AESCryptException, IOException {
        generateEncryptCipher(password);
        output.write(getSalt());
        output.write(getIv());

        return new CipherOutputStream(output, getCipher());
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
    public InputStream generateCryptInputStream(InputStream input, String password) throws AESCryptException, IOException {
        int readBytes;

        byte[] inSalt = new byte[cryptConfiguration.saltLen];
        readBytes = input.read(inSalt, 0, inSalt.length);
        if (readBytes != inSalt.length) {
            throw new AESCryptException("Error reading salt : " + readBytes);
        }

        byte[] inIV = new byte[cryptConfiguration.IVLen];
        readBytes = input.read(inIV, 0, inIV.length);
        if (readBytes != inIV.length) {
            throw new AESCryptException("Error reading IV : " + readBytes);
        }

        generateDecryptCipher(password, inSalt, inIV);

        return new CipherInputStream(input, getCipher());
    }

}
