package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptConfigurationFactory;
import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;

import java.io.OutputStream;

/**
 * Writes PassData2 to outputStream version 3
 */
public class FilePassDataWriterV3 extends AbstractFilePassDataWriter<PassData2> {
    public FilePassDataWriterV3(OutputStream outputStream, String password, PassData2 data) {
        super(outputStream, password, data);
        this.header = FILE_DATA_SIGNATURE;
        this.version = new byte[] {0, 3};
    }

    @Override
    protected AESCryptService createCryptService() {
        return new AESCryptService(AESCryptConfigurationFactory.createAES256());
    }

    @Override
    protected void writePassData(OutputStream cryptStream) throws DataReadWriteException {
        (new JSONPassDataWriter(data)).writeStream(cryptStream);
    }
}
