package com.romanpulov.violetnotecore.Service;

import com.romanpulov.violetnotecore.Model.PassDataAttribute;
import com.romanpulov.violetnotecore.Model.PassCategory2;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Model.PassNote2;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Processor.FilePassDataWriterV3;
import com.romanpulov.violetnotecore.Processor.TestFilePassDataWriter;
import com.romanpulov.violetnotecore.TestConfiguration;
import com.romanpulov.violetnotecore.TestPassData2Generator;
import com.romanpulov.violetnotecore.TestPassDataTools;
import com.romanpulov.violetnotecore.TestUtils;
import org.junit.jupiter.api.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestPassData2ReaderWriterServiceV3 {
    private static final String TEST_FILE_NAME = TestConfiguration.resolveTestFileName("test_service_reader_writer_attr.vn3");
    private static final String TEST_PASSWORD = "7+534ffy";
    private static final String TEST_WRONG_PASSWORD = "g4*dd=";

    PassData2 testPassData = TestPassData2Generator.generateTestPassData2Attrs();

    @AfterAll
    static void afterAll() {
        TestUtils.deleteFileIfExists(TEST_FILE_NAME);
    }

    @Test
    @Order(1)
    public void generateTestPassData() throws Exception {
        try (OutputStream outputStream = new FileOutputStream(TEST_FILE_NAME))
        {
            FilePassDataWriterV3 writer = new FilePassDataWriterV3(outputStream, TEST_PASSWORD, testPassData);

            (new TestFilePassDataWriter(writer, TEST_FILE_NAME)).testWriteFile();
        }
    }

    @Test
    @Order(2)
    public void testReadTestPassData() throws Exception {
        try (InputStream inputStream = new FileInputStream(TEST_FILE_NAME))
        {
            PassData2 passData2 = PassData2ReaderServiceV3.fromStream(inputStream, TEST_PASSWORD);
            assertEquals(2, passData2.getCategoryList().size());

            PassCategory2 c1 = passData2.getCategoryList().get(0);

            PassNote2 n11 = c1.getNoteList().get(0);
            assertNull(n11.getAttributes());

            PassNote2 n12 = c1.getNoteList().get(1);
            assertEquals(2, n12.getAttributes().size());

            PassDataAttribute a1 =  n12.getAttributes().get(0);
            assertEquals("Personal account", a1.name());
            assertEquals("03344", a1.value());

            PassDataAttribute a2 =  n12.getAttributes().get(1);
            assertEquals("Alias", a2.name());
            assertEquals("Navy", a2.value());

            String equalityCheck = TestPassDataTools.passDataEquals(passData2, passData2);
            if (equalityCheck != null) {
                fail(equalityCheck);
            }
        }
    }

    @Test
    @Order(2)
    public void testReadWrongPasswordFile() throws Exception {
        try (InputStream inputStream = new FileInputStream(TEST_FILE_NAME))
        {
            assertThrows(DataReadWriteException.class, () ->
                    PassData2ReaderServiceV3.fromStream(inputStream, TEST_WRONG_PASSWORD));
        }
    }
}
