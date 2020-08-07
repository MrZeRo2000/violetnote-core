package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;

import java.io.OutputStream;

/**
 * Writes PassData to outputStream version 1
 */
public class FilePassDataWriterV1 extends AbstractFilePassDataWriter<PassData> {

    public FilePassDataWriterV1(OutputStream outputStream, String password, PassData passData) {
        super(outputStream, password, passData);
    }

    @Override
    protected void writePassData(OutputStream cryptStream) throws DataReadWriteException {
        (new XMLPassDataWriter(data)).writeStream(cryptStream);
    }
}
