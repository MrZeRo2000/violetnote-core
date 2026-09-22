package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.*;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestFilePassDataReaderWriterV2 {
    private static final String TEST_FILE_NAME_V1 = TestConfiguration.resolveTestFileName("test1.vnf");
    private static final String TEST_FILE_NAME = TestConfiguration.resolveTestFileName("test_out.vn2");
    private static final String TEST_PASSWORD = "1#23T44rr6";
    private static final String TEST_WRONG_PASSWORD = "eid,93";

    @AfterAll
    static void afterAll() {
        TestUtils.deleteFileIfExists(TEST_FILE_NAME);
    }

    @Test
    @Order(1)
    public void testWriteFile() throws Exception {
        // Security.setProperty("crypto.policy", "unlimited");

        TestUtils.deleteFileIfExists(TEST_FILE_NAME);

        try (OutputStream outputStream = new FileOutputStream(TEST_FILE_NAME))
        {
            PassData2 passData = TestPassData2Generator.generateTestPassData2();
            FilePassDataWriterV2 writer = new FilePassDataWriterV2(outputStream, TEST_PASSWORD, passData);

            (new TestFilePassDataWriter(writer, TEST_FILE_NAME)).testWriteFile();
        }
    }

    @Test
    @Order(2)
    public void testReadWrittenFile() throws Exception {

        try (InputStream inputStream = new FileInputStream(TEST_FILE_NAME))
        {
            FilePassDataReaderV2 reader = new FilePassDataReaderV2(inputStream, TEST_PASSWORD);

            PassData2 readPassData = reader.readFile();
            PassData2 passData = TestPassData2Generator.generateTestPassData2();

            String passDataComparison = TestPassData2Tools.passDataEquals(passData, readPassData);
            if (passDataComparison != null) {
                fail(passDataComparison);
            }

        }
    }

    @Test
    @Order(3)
    public void testReadWrongVersionFile() throws Exception {
        final FilePassDataReaderV2 reader = new FilePassDataReaderV2(new FileInputStream(TEST_FILE_NAME_V1), TEST_PASSWORD);

        assertThrows(DataReadWriteException.class, reader::readFile);
    }

    @Test
    @Order(4)
    public void testReadWrongPasswordFile() throws Exception {
        final FilePassDataReaderV2 reader = new FilePassDataReaderV2(new FileInputStream(TEST_FILE_NAME), TEST_WRONG_PASSWORD);

        assertThrows(DataReadWriteException.class, reader::readFile);
    }
}
