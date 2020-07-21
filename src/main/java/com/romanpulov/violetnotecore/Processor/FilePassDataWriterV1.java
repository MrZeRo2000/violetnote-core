package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;

import java.io.OutputStream;

public class FilePassDataWriterV1 extends FilePassDataWriter<PassData> {

    public FilePassDataWriterV1(OutputStream outputStream, String password, PassData passData) {
        super(outputStream, password, passData);
    }

    @Override
    public void writePassData(OutputStream cryptStream) throws DataReadWriteException {
        (new XMLPassDataWriter(data)).writeStream(cryptStream);
    }
}
