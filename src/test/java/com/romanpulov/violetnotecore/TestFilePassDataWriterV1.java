package com.romanpulov.violetnotecore;

import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Model.PassNote;
import com.romanpulov.violetnotecore.Processor.FilePassDataReaderV1;
import com.romanpulov.violetnotecore.Processor.FilePassDataWriterV1;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestFilePassDataWriterV1 {
    private static final String TEST_FILE_NAME = "data\\test_out.vnf";
    private static final String TEST_PASSWORD = "123456";

    private PassData generateTestPassData() {
        List<PassCategory> passCategoryList = new ArrayList<>();
        passCategoryList.add(new PassCategory("New category"));
        List<PassNote> passNoteList = new ArrayList<>();
        passNoteList.add(new PassNote(
                passCategoryList.get(0),
                "System",
                "User",
                "Password",
                "Comments",
                "Custom",
                "Info")
        );

        PassData passData = new PassData();
        passData.setPassCategoryList(passCategoryList);
        passData.setPassNoteList(passNoteList);

        return passData;
    }

    @Test
    @Order(1)
    public void testWriteFile() throws Exception {
        PassData passData = generateTestPassData();

        File outputFile = new File(TEST_FILE_NAME);
        if (outputFile.exists() && !outputFile.delete()) {
            throw new Exception("Unable to delete test file " + TEST_FILE_NAME);
        }

        OutputStream outputStream = new FileOutputStream(outputFile);
        FilePassDataWriterV1 writerV1 = new FilePassDataWriterV1(outputStream, TEST_PASSWORD);

        writerV1.writeFile(passData);

        outputFile = new File(TEST_FILE_NAME);

        assertTrue(outputFile.exists());
    }

    @Test
    @Order(2)
    public void testReadWrittenFile() throws Exception {
        FilePassDataReaderV1 readerV1 = new FilePassDataReaderV1(new FileInputStream(TEST_FILE_NAME), TEST_PASSWORD);
        PassData passData = readerV1.readFile();

        assertNotNull(passData);

        assertEquals(1, passData.getPassCategoryList().size());
        assertEquals(1, passData.getPassNoteList().size());

        assertEquals("System", passData.getPassNoteList().get(0).getSystem());
    }

    @Test
    @Order(3)
    public void testDeleteOutputFile() throws Exception {
        File outputFile = new File(TEST_FILE_NAME);
        assertTrue(outputFile.exists());
        if (!outputFile.delete()) {
            throw new Exception("Unable to delete test file after tests " + TEST_FILE_NAME);
        }
    }
}
