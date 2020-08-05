package com.romanpulov.violetnotecore.AESCrypt;

public class AESCryptConfiguration {
    public final String secretKeyInstance;
    public final String cipherInstance;
    public final int keyLen;
    public final int saltLen;
    public final int IVLen;
    public final int iterations;

    public AESCryptConfiguration(String secretKeyInstance, String cipherInstance, int keyLen, int saltLen, int iterations) {
        this.secretKeyInstance = secretKeyInstance;
        this.cipherInstance = cipherInstance;
        this.keyLen = keyLen;
        this.saltLen = saltLen;
        this.IVLen = 16;
        this.iterations = iterations;
    }
}
