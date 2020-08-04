package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;

import java.io.InputStream;

/**
 * Reads PassData from InputStream version 1
 */
public class FilePassDataReaderV1 extends FilePassDataReader<PassData> {
    public FilePassDataReaderV1(InputStream inputStream, String password) {
        super(inputStream, password);
    }

    @Override
    public PassData readPassData(InputStream cryptStream) throws DataReadWriteException {
        return (new XMLPassDataReader()).readStream(cryptStream);
    }
}
