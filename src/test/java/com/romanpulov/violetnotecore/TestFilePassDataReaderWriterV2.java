package com.romanpulov.violetnotecore;

import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Processor.FilePassDataReaderV1;
import com.romanpulov.violetnotecore.Processor.FilePassDataWriterV1;
import com.romanpulov.violetnotecore.Processor.FilePassDataWriterV2;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestFilePassDataReaderWriterV2 {
    private static final String TEST_FILE_NAME = "data\\test_out.vn2";
    private static final String TEST_PASSWORD = "1#23T44rr6";

    @Test
    @Order(1)
    public void testWriteFile() throws Exception {
        (new TestFileManagement(TEST_FILE_NAME)).testDeleteOutputFile();

        OutputStream outputStream = new FileOutputStream(new File(TEST_FILE_NAME));
        PassData2 passData = TestPassData2Generator.generateTestPassData2();
        FilePassDataWriterV2 writer = new FilePassDataWriterV2(outputStream, TEST_PASSWORD, passData);

        (new TestFilePassDataWriter(writer, TEST_FILE_NAME)).testWriteFile();
    }

    @Test
    @Order(2)
    public void testReadWrittenFile() throws Exception {
        // FilePassDataReaderV2 reader = new FilePassDataReaderV2(new FileInputStream(TEST_FILE_NAME), TEST_PASSWORD);
    }

    @Test
    @Order(3)
    public void testDeleteOutputFile() throws Exception {
        (new TestFileManagement(TEST_FILE_NAME)).testDeleteExistingFile();
    }

}
