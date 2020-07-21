package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;

import java.io.OutputStream;

public class FilePassDataWriterV1 extends FilePassDataWriter {

    public FilePassDataWriterV1(OutputStream outputStream, String password) {
        super(outputStream, password);
    }

    @Override
    public void writePassData(OutputStream cryptStream, PassData passData) throws DataReadWriteException {
        (new XMLPassDataWriter(passData)).writeStream(cryptStream);
    }
}
