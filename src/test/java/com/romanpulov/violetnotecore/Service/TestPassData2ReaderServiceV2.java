package com.romanpulov.violetnotecore.Service;

import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Processor.FilePassDataReaderV2;
import com.romanpulov.violetnotecore.Processor.FilePassDataWriterV2;
import com.romanpulov.violetnotecore.Processor.TestFilePassDataWriter;
import com.romanpulov.violetnotecore.TestFileManagement;
import com.romanpulov.violetnotecore.TestPassData2Generator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestPassData2ReaderServiceV2 {
    private static final String TEST_FILE_NAME_V1 = "data\\test1.vnf";
    private static final String TEST_FILE_NAME = "data\\test_out.vn2";
    private static final String TEST_PASSWORD = "1#23T44rr6";
    private static final String TEST_WRONG_PASSWORD = "eid,93";

    @Test
    @Order(1)
    public void testWriteFile() throws Exception {
        // Security.setProperty("crypto.policy", "unlimited");

        (new TestFileManagement(TEST_FILE_NAME)).testDeleteOutputFile();

        try (OutputStream outputStream = new FileOutputStream(new File(TEST_FILE_NAME));)
        {
            PassData2 passData = TestPassData2Generator.generateTestPassData2();
            FilePassDataWriterV2 writer = new FilePassDataWriterV2(outputStream, TEST_PASSWORD, passData);

            (new TestFilePassDataWriter(writer, TEST_FILE_NAME)).testWriteFile();
        }
    }

    @Test
    @Order(2)
    public void testReadWrongPasswordFile()  throws Exception {
        assertThrows(DataReadWriteException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                PassData2 readPassData = PassData2ReaderServiceV2.fromStream(new FileInputStream(TEST_FILE_NAME), TEST_WRONG_PASSWORD);
            }
        });
    }

    @Test
    @Order(3)
    public void testReadWrongVersionFile() throws Exception {
        assertThrows(DataReadWriteException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                PassData2 readPassData = PassData2ReaderServiceV2.fromStream(new FileInputStream(TEST_FILE_NAME_V1), TEST_PASSWORD);
            }
        });
    }

    @Test
    @Order(4)
    public void testDeleteOutputFile() throws Exception {
        (new TestFileManagement(TEST_FILE_NAME)).testDeleteExistingFile();
    }
}
