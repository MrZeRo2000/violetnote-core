package com.romanpulov.violetnotecore;

import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Processor.FilePassDataReaderV1;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import java.io.InputStream;

public class TestFilePassDataReaderV1 {
    private static final String TEST_FILE_NAME = "data\\test1.vnf";
    private static final String TEST_PASSWORD = "123456";
    private static final String WRONG_PASSWORD = "1023456";

    @Test
    public void testReadCorrectPassword() throws Exception {
        InputStream inputStream = new FileInputStream(TEST_FILE_NAME);
        FilePassDataReaderV1 readerV1 = new FilePassDataReaderV1(inputStream, TEST_PASSWORD);
        PassData passData = readerV1.readFile();

        assertNotNull(passData);

        assertEquals(4, passData.getPassCategoryList().size());
        assertEquals(7, passData.getPassNoteList().size());
    }

    @Test
    public void testReadWrongPassword() throws Exception {
        InputStream inputStream = new FileInputStream(TEST_FILE_NAME);
        final FilePassDataReaderV1 readerV1 = new FilePassDataReaderV1(inputStream, WRONG_PASSWORD);
        assertThrows(DataReadWriteException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                readerV1.readFile();
            }
        });

    }
}
