package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptConfigurationFactory;
import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Reads PassData2 from InputStream version 2
 */
public class FilePassDataReaderV2 extends AbstractFilePassDataReader<PassData2> {
    public FilePassDataReaderV2(InputStream inputStream, String password) {
        super(inputStream, password);
    }

    @Override
    protected AESCryptService createCryptService() {
        return new AESCryptService((AESCryptConfigurationFactory.createAES256()));
    }

    @Override
    protected void readHeader() throws IOException, DataReadWriteException {
        byte[] header = new byte[FILE_DATA_SIGNATURE.length];
        int bytes = inputStream.read(header);

        if (bytes != FILE_DATA_SIGNATURE.length) {
            throw new DataReadWriteException("Error reading header: wrong header length");
        }

        if (!Arrays.equals(header, FILE_DATA_SIGNATURE)) {
            throw new DataReadWriteException("Error reading header: wrong header");
        }
    }

    @Override
    protected void readVersion() throws IOException, DataReadWriteException {
        byte[] version = new byte[2];
        int bytes = inputStream.read(version);

        if (bytes != version.length) {
            throw new DataReadWriteException("Error reading version: wrong version length");
        }

        if (!Arrays.equals(version, new byte[]{0, 2})) {
            throw new DataReadWriteException("Error reading version: wrong version:" + Arrays.toString(version));
        }

    }

    @Override
    public PassData2 readPassData(InputStream cryptStream) throws DataReadWriteException {
        return (new JSONPassDataReader().readStream(cryptStream));
    }
}
