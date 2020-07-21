package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptException;
import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;

import java.io.IOException;
import java.io.OutputStream;

public abstract class FilePassDataWriter<T> extends FileDataProcessor {

    protected final OutputStream outputStream;
    protected final String password;
    protected final T data;

    public FilePassDataWriter(OutputStream outputStream, String password, T data) {
        this.outputStream = outputStream;
        this.password = password;
        this.data = data;
    }

    protected void writeHeader() throws IOException {}

    protected void writeVersion() throws IOException {}

    public final void writeFile() throws AESCryptException, IOException, DataReadWriteException {
        writeHeader();
        writeVersion();
        try (OutputStream cryptStream = AESCryptService.generateCryptOutputStream(outputStream, password))
        {
            writePassData(cryptStream);
        }
    }

    public abstract void writePassData(OutputStream cryptStream) throws DataReadWriteException;
}
