package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.TestFileManagement;
import com.romanpulov.violetnotecore.TestPassData2Generator;
import com.romanpulov.violetnotecore.TestPassDataTools;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestFilePassDataReaderWriterV3 {
    private static final String TEST_FILE_NAME_V1 = "data\\test1.vnf";
    private static final String TEST_FILE_NAME = "data\\test_out.vn3";
    private static final String TEST_PASSWORD = "1#23Yffuy8";

    @Test
    @Order(1)
    public void testWriteFile() throws Exception {
        // Security.setProperty("crypto.policy", "unlimited");

        (new TestFileManagement(TEST_FILE_NAME)).testDeleteOutputFile();

        try (OutputStream outputStream = new FileOutputStream(new File(TEST_FILE_NAME));)
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

            String passDataComparison = TestPassDataTools.passDataEquals(passData, readPassData);
            if (passDataComparison != null) {
                fail(passDataComparison);
            }

        }
    }

    @Test
    @Order(3)
    public void testReadWrongVersionFile() throws Exception {
        final FilePassDataReaderV3 reader = new FilePassDataReaderV3(new FileInputStream(TEST_FILE_NAME_V1), TEST_PASSWORD);

        assertThrows(DataReadWriteException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                PassData2 readPassData = reader.readFile();
            }
        });
    }


    @Test
    @Order(4)
    public void testDeleteOutputFile() throws Exception {
        (new TestFileManagement(TEST_FILE_NAME)).testDeleteExistingFile();
    }
}
