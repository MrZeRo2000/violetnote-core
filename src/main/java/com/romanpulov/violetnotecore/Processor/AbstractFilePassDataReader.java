package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptException;
import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractFilePassDataReader<T> extends AbstractFileDataProcessor {

    protected final InputStream inputStream;
    protected final String password;

    public AbstractFilePassDataReader(InputStream inputStream, String password) {
        this.inputStream = inputStream;
        this.password = password;
    }

    protected void readHeader() throws IOException, DataReadWriteException {}

    protected void readVersion() throws IOException, DataReadWriteException {}

    protected final T readFile() throws AESCryptException, IOException, DataReadWriteException {
        readHeader();
        readVersion();

        AESCryptService aesCryptService = createCryptService();

        try (InputStream cryptStream =  aesCryptService.generateCryptInputStream(inputStream, password))
        {
            return readPassData(cryptStream);
        }
    };

    public abstract T readPassData(InputStream cryptStream) throws AESCryptException, IOException, DataReadWriteException;
}
