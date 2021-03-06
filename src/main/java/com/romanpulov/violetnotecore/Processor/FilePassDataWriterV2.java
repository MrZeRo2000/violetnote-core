package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptConfigurationFactory;
import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;

import java.io.OutputStream;

/**
 * Writes PassData2 to outputStream version 2
 */
public class FilePassDataWriterV2 extends AbstractFilePassDataWriter<PassData2> {

    public FilePassDataWriterV2(OutputStream outputStream, String password, PassData2 passData) {
        super(outputStream, password, passData);
        this.header = FILE_DATA_SIGNATURE;
        this.version = new byte[] {0, 2};
    }

    @Override
    protected AESCryptService createCryptService() {
        return new AESCryptService(AESCryptConfigurationFactory.createAES128());
    }

    @Override
    protected void writePassData(OutputStream cryptStream) throws DataReadWriteException {
        (new JSONPassDataWriter(data)).writeStream(cryptStream);
    }
}
