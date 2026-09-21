package com.romanpulov.violetnotecore.Service;

import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Processor.FilePassDataWriterV3;
import com.romanpulov.violetnotecore.Processor.TestFilePassDataWriter;
import com.romanpulov.violetnotecore.TestConfiguration;
import com.romanpulov.violetnotecore.TestPassData2Generator;
import com.romanpulov.violetnotecore.TestUtils;
import org.junit.jupiter.api.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestPassData2ReaderServiceV3 {
    private static final String TEST_FILE_NAME = TestConfiguration.resolveTestFileName("test_out_service.vn3");
    private static final String TEST_PASSWORD = "1#23Yffuy8";
    private static final String TEST_WRONG_PASSWORD = "eid,93";

    @AfterAll
    static void afterAll() {
        TestUtils.deleteFileIfExists(TEST_FILE_NAME);
    }

    @Test
    @Order(1)
    public void testWriteFile() throws Exception {
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
    public void testReadWrongPasswordFile() throws Exception {
        // A V3 file with a wrong password fails only after the cipher stream has been opened.
        // The service then tries to reset() and fall back to V2/V1; that fallback cannot succeed
        // here, so the original V3 failure must surface rather than a V1/V2 parse error.
        try (InputStream inputStream = new FileInputStream(TEST_FILE_NAME))
        {
            assertThrows(DataReadWriteException.class, () ->
                PassData2ReaderServiceV3.fromStream(inputStream, TEST_WRONG_PASSWORD));
        }
    }
}
