package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptException;
import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;

import java.io.IOException;
import java.io.InputStream;

public class FilePassDataReaderV1 extends FilePassDataReader<PassData> {
    public FilePassDataReaderV1(InputStream inputStream, String password) {
        super(inputStream, password);
    }

    @Override
    public PassData readPassData(InputStream cryptStream) throws AESCryptException, IOException, DataReadWriteException {
        return (new XMLPassDataReader()).readStream(cryptStream);
    }
}
