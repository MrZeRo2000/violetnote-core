package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptConfigurationFactory;
import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;

import java.io.InputStream;

/**
 * Reads PassData2 from InputStream version 2
 */
public class FilePassDataReaderV2 extends AbstractFilePassDataReader<PassData2> {
    public FilePassDataReaderV2(InputStream inputStream, String password) {
        super(inputStream, password);
        this.header = FILE_DATA_SIGNATURE;
        this.version = new byte[] {0, 2};
    }

    @Override
    protected AESCryptService createCryptService() {
        return new AESCryptService((AESCryptConfigurationFactory.createAES128()));
    }

    @Override
    protected PassData2 readPassData(InputStream cryptStream) throws DataReadWriteException {
        return (new JSONPassDataReader().readStream(cryptStream));
    }
}
