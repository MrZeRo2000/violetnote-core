package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptException;
import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;

import java.io.IOException;
import java.io.InputStream;

public abstract class FilePassDataReader<T> extends FileDataProcessor {

    protected final InputStream inputStream;
    protected final String password;

    public FilePassDataReader(InputStream inputStream, String password) {
        this.inputStream = inputStream;
        this.password = password;
    }

    protected void readHeader() throws IOException {}

    protected void readVersion() throws IOException {}

    public final T readFile() throws AESCryptException, IOException, DataReadWriteException {
        readHeader();
        readVersion();
        try (InputStream cryptStream =  AESCryptService.generateCryptInputStream(inputStream, password))
        {
            return readPassData(cryptStream);
        }
    };

    public abstract T readPassData(InputStream cryptStream) throws AESCryptException, IOException, DataReadWriteException;
}
