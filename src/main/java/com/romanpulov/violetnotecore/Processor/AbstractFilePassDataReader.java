package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptException;
import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public abstract class AbstractFilePassDataReader<T> extends AbstractFileDataProcessor {

    protected final InputStream inputStream;
    protected final String password;

    public AbstractFilePassDataReader(InputStream inputStream, String password) {
        this.inputStream = inputStream;
        this.password = password;
    }

    protected void readHeader() throws IOException, DataReadWriteException {
        if (this.header != null) {
            byte[] readHeader = new byte[this.header.length];
            int bytes = inputStream.read(readHeader);

            if (bytes != this.header.length) {
                throw new DataReadWriteException("Error reading header: wrong header length");
            }

            if (!Arrays.equals(readHeader, this.header)) {
                throw new DataReadWriteException("Error reading header: wrong header");
            }
        }
    }

    protected void readVersion() throws IOException, DataReadWriteException {
        if (this.version != null) {
            byte[] readVersion = new byte[2];
            int bytes = inputStream.read(readVersion);

            if (bytes != this.version.length) {
                throw new DataReadWriteException("Error reading version: wrong version length");
            }

            if (!Arrays.equals(readVersion, version)) {
                throw new DataReadWriteException("Error reading version: wrong version:" + Arrays.toString(version));
            }
        }
    }

    public final T readFile() throws AESCryptException, IOException, DataReadWriteException {
        readHeader();
        readVersion();

        AESCryptService aesCryptService = createCryptService();

        try (InputStream cryptStream =  aesCryptService.generateCryptInputStream(inputStream, password))
        {
            return readPassData(cryptStream);
        }
    };

    protected abstract T readPassData(InputStream cryptStream) throws AESCryptException, IOException, DataReadWriteException;
}
