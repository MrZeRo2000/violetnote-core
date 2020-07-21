package com.romanpulov.violetnotecore;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestFileManagement {
    private final String fileName;

    public TestFileManagement(String fileName) {
        this.fileName = fileName;
    }

    public void testDeleteOutputFile() throws Exception {
        File outputFile = new File(fileName);

        if (outputFile.exists() && !outputFile.delete()) {
            throw new Exception("Unable to delete test file  " + fileName);
        }
    }

    public void testDeleteExistingFile() throws Exception {
        File outputFile = new File(fileName);

        assertTrue(outputFile.exists());

        if (!outputFile.delete()) {
            throw new Exception("Unable to delete test file  " + fileName);
        }
    }

}
