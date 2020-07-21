package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptException;
import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;

import java.io.IOException;
import java.io.OutputStream;

public abstract class FilePassDataWriter extends FileDataProcessor {

    protected final OutputStream outputStream;
    protected final String password;

    public FilePassDataWriter(OutputStream outputStream, String password) {
        this.outputStream = outputStream;
        this.password = password;
    }

    protected void writeHeader() throws IOException {}

    protected void writeVersion() throws IOException {}

    public final void writeFile(PassData passData) throws AESCryptException, IOException, DataReadWriteException {
        writeHeader();
        writeVersion();
        try (OutputStream cryptStream = AESCryptService.generateCryptOutputStream(outputStream, password))
        {
            writePassData(cryptStream, passData);
        }
    }

    public abstract void writePassData(OutputStream cryptStream, PassData passData) throws DataReadWriteException;
}
