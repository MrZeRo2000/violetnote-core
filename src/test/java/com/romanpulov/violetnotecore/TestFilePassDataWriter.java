package com.romanpulov.violetnotecore;

import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Processor.FilePassDataWriter;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class TestFilePassDataWriter {
    private final FilePassDataWriter<?> writer;
    private final String fileName;

    public TestFilePassDataWriter(FilePassDataWriter<?> writer, String fileName) {
        this.writer = writer;
        this.fileName = fileName;
    }

    public void testWriteFile() throws Exception {
        writer.writeFile();

        File outputFile = new File(fileName);

        assertTrue(outputFile.exists());
    }
}
