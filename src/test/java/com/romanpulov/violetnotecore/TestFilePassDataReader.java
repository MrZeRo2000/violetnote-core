package com.romanpulov.violetnotecore;

import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Processor.FilePassDataReader;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

public class TestFilePassDataReader {
    private final FilePassDataReader<PassData> reader;

    public TestFilePassDataReader(FilePassDataReader<PassData> reader) {
        this.reader = reader;
    }

    public void testReadCorrectPassword() throws Exception {
        PassData passData = reader.readFile();

        assertNotNull(passData);

        assertEquals(4, passData.getPassCategoryList().size());
        assertEquals(7, passData.getPassNoteList().size());
    }

    public void testReadWrongPassword() throws Exception {
        assertThrows(DataReadWriteException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                reader.readFile();
            }
        });
    }

    public void testReadGeneratedData() throws Exception {
        PassData passData = reader.readFile();

        assertNotNull(passData);

        assertEquals(1, passData.getPassCategoryList().size());
        assertEquals(1, passData.getPassNoteList().size());

        assertEquals("System", passData.getPassNoteList().get(0).getSystem());
    }
}
