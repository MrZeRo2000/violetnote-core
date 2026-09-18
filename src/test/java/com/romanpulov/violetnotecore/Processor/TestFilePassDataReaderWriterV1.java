package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.TestConfiguration;
import com.romanpulov.violetnotecore.TestPassDataTools;
import com.romanpulov.violetnotecore.TestUtils;
import org.junit.jupiter.api.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestFilePassDataReaderWriterV1 {
    private static final String TEST_FILE_NAME = TestConfiguration.resolveTestFileName("test_out.vnf");
    private static final String TEST_PASSWORD = "123456";

    @AfterAll
    static void afterAll() {
        TestUtils.deleteFileIfExists(TEST_FILE_NAME);
    }

    @Test
    @Order(1)
    public void testWriteFile() throws Exception {
        TestUtils.deleteFileIfExists(TEST_FILE_NAME);

        OutputStream outputStream = new FileOutputStream(TEST_FILE_NAME);
        PassData passData = TestPassDataTools.generateTestPassData();
        FilePassDataWriterV1 writerV1 = new FilePassDataWriterV1(outputStream, TEST_PASSWORD, passData);

        (new TestFilePassDataWriter(writerV1, TEST_FILE_NAME)).testWriteFile();
    }

    @Test
    @Order(2)
    public void testReadWrittenFile() throws Exception {
        FilePassDataReaderV1 readerV1 = new FilePassDataReaderV1(new FileInputStream(TEST_FILE_NAME), TEST_PASSWORD);

        (new TestFilePassDataReader(readerV1)).testReadGeneratedData();
    }
}
