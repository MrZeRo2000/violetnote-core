package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.Model.PassData;

import java.io.IOException;
import java.io.OutputStream;

public class FilePassDataWriterV2 extends FilePassDataWriter {
    public FilePassDataWriterV2(OutputStream outputStream, String password) {
        super(outputStream, password);
    }

    @Override
    protected void writeHeader() throws IOException {
        outputStream.write(FILE_DATA_SIGNATURE);
    }

    @Override
    protected void writeVersion() throws IOException {
        outputStream.write(0);
        outputStream.write(2);
    }

    @Override
    public void writePassData(OutputStream cryptStream, PassData passData) {

    }
}
