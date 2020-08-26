package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptConfigurationFactory;
import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;

import java.io.InputStream;

/**
 * Reads PassData2 from InputStream version 3
 */
public class FilePassDataReaderV3 extends AbstractFilePassDataReader<PassData2> {
    public FilePassDataReaderV3(InputStream inputStream, String password) {
        super(inputStream, password);
        this.header = FILE_DATA_SIGNATURE;
        this.version = new byte[] {0, 3};
    }

    @Override
    protected AESCryptService createCryptService() {
        return new AESCryptService((AESCryptConfigurationFactory.createAES256()));
    }

    @Override
    protected PassData2 readPassData(InputStream cryptStream) throws DataReadWriteException {
        return (new JSONPassDataReader().readStream(cryptStream));
    }
}
