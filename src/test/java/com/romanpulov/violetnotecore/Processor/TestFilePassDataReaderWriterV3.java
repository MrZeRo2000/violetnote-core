package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.TestConfiguration;
import com.romanpulov.violetnotecore.TestPassData2Generator;
import com.romanpulov.violetnotecore.TestPassData2Tools;
import com.romanpulov.violetnotecore.TestUtils;
import org.junit.jupiter.api.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestFilePassDataReaderWriterV3 {
    private static final String TEST_FILE_NAME_V1 = TestConfiguration.resolveTestFileName("test1.vnf");
    private static final String TEST_FILE_NAME = TestConfiguration.resolveTestFileName("test_out.vn3");
    private static final String TEST_PASSWORD = "1#23Yffuy8";
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
            FilePassDataWriterV3 writer = new FilePassDataWriterV3(outputStream, TEST_PASSWORD, passData);

            (new TestFilePassDataWriter(writer, TEST_FILE_NAME)).testWriteFile();
        }
    }

    @Test
    @Order(2)
    public void testReadWrittenFile() throws Exception {

        try (InputStream inputStream = new FileInputStream(TEST_FILE_NAME))
        {
            FilePassDataReaderV3 reader = new FilePassDataReaderV3(inputStream, TEST_PASSWORD);

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
        try(InputStream inputStream = new FileInputStream(TEST_FILE_NAME_V1)) {
            final FilePassDataReaderV3 reader = new FilePassDataReaderV3(inputStream, TEST_PASSWORD);

            assertThrows(DataReadWriteException.class, reader::readFile);
        }
    }

    @Test
    @Order(4)
    public void testReadWrongPasswordFile() throws Exception {
        try (InputStream inputStream = new FileInputStream(TEST_FILE_NAME))
        {
            final FilePassDataReaderV3 reader = new FilePassDataReaderV3(inputStream, TEST_WRONG_PASSWORD);

            assertThrows(DataReadWriteException.class, reader::readFile);
        }
    }
}
