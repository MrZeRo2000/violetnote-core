package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptConfigurationFactory;
import com.romanpulov.violetnotecore.AESCrypt.AESCryptException;
import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;

import java.io.IOException;
import java.io.OutputStream;

public abstract class AbstractFilePassDataWriter<T> extends AbstractFileDataProcessor {

    protected final OutputStream outputStream;
    protected final String password;
    protected final T data;

    public AbstractFilePassDataWriter(OutputStream outputStream, String password, T data) {
        this.outputStream = outputStream;
        this.password = password;
        this.data = data;
    }

    protected void writeHeader() throws IOException {}

    protected void writeVersion() throws IOException {}

    @Override
    protected AESCryptService createCryptService() {
        return new AESCryptService(AESCryptConfigurationFactory.createDefault());
    }

    public final void writeFile() throws AESCryptException, IOException, DataReadWriteException {
        writeHeader();
        writeVersion();

        AESCryptService aesCryptService = createCryptService();

        try (OutputStream cryptStream = aesCryptService.generateCryptOutputStream(outputStream, password))
        {
            writePassData(cryptStream);
            cryptStream.flush();
        }
    }

    protected abstract void writePassData(OutputStream cryptStream) throws DataReadWriteException;
}
