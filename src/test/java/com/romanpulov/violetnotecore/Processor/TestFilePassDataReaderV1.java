package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.Processor.FilePassDataReaderV1;
import com.romanpulov.violetnotecore.Processor.TestFilePassDataReader;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestFilePassDataReaderV1 {
    private static final String TEST_FILE_NAME = "data\\test1.vnf";
    private static final String TEST_PASSWORD = "123456";
    private static final String WRONG_PASSWORD = "1023456";

    @Test
    public void testReadCorrectPassword() throws Exception {
        InputStream inputStream = new FileInputStream(TEST_FILE_NAME);
        FilePassDataReaderV1 readerV1 = new FilePassDataReaderV1(inputStream, TEST_PASSWORD);

        (new TestFilePassDataReader(readerV1)).testReadCorrectPassword();
    }

    @Test
    public void testReadWrongPassword() throws Exception {
        InputStream inputStream = new FileInputStream(TEST_FILE_NAME);
        final FilePassDataReaderV1 readerV1 = new FilePassDataReaderV1(inputStream, WRONG_PASSWORD);

        (new TestFilePassDataReader(readerV1)).testReadWrongPassword();
    }


}
