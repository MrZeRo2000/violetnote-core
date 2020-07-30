package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptException;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class FilePassDataReaderV2 extends FilePassDataReader<PassData2> {
    public FilePassDataReaderV2(InputStream inputStream, String password) {
        super(inputStream, password);
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
            throw new DataReadWriteException("Error reading version: wrong version:" + version);
        }

    }

    @Override
    public PassData2 readPassData(InputStream cryptStream) throws AESCryptException, IOException, DataReadWriteException {
        return null;
    }
}
