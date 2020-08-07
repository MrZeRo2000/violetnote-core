package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.TestFileManagement;
import com.romanpulov.violetnotecore.TestPassDataTools;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestFilePassDataReaderWriterV1 {
    private static final String TEST_FILE_NAME = "data\\test_out.vnf";
    private static final String TEST_PASSWORD = "123456";

    @Test
    @Order(1)
    public void testWriteFile() throws Exception {
        (new TestFileManagement(TEST_FILE_NAME)).testDeleteOutputFile();

        OutputStream outputStream = new FileOutputStream(new File(TEST_FILE_NAME));
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

    @Test
    @Order(3)
    public void testDeleteOutputFile() throws Exception {
        (new TestFileManagement(TEST_FILE_NAME)).testDeleteExistingFile();
    }
}
