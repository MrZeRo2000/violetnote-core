package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotecore.Model.PassNote;
import com.romanpulov.violetnotecore.Processor.Exception.*;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteParserException;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteIOException;

import java.io.*;
import java.util.*;

/**
 * Created on 16.01.2016.
 */
public class PinsDataReader {
    private static final String FILE_DELIMITER = ";";
    private static final int FILE_FIELD_COUNT = 9;

    private List<PassCategory> passCategoryList = new ArrayList<>();
    private List<PassNote> passNoteList = new ArrayList<>();

    public List<PassCategory> getPassCategoryList() {
        return passCategoryList;
    }

    public List<PassNote> getPassNoteList() {
        return passNoteList;
    }

    private Map<String, PassCategory> categoryNoteList = new HashMap<>();

    private void clearData() {
        passCategoryList.clear();
        passNoteList.clear();
        categoryNoteList.clear();
    }

    public void readStream(InputStream stream) throws DataReadWriteException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            try {
                clearData();

                String line = null;
                boolean isFirstLine = true;
                while ((line = reader.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                    } else {
                        String[] splitLine = line.split(FILE_DELIMITER);
                        if (splitLine.length != FILE_FIELD_COUNT) {
                            throw new DataReadWriteParserException(String.format(
                                    "Line %s: expected %d fields, actual: %d", line, FILE_FIELD_COUNT, splitLine.length));
                        }
                        parseData(splitLine);
                    }
                }
            } finally {
                reader.close();
            }
        }
        catch (FileNotFoundException e) {
            throw new DataReadWriteFileNotFoundException(e.getMessage());
        }
        catch (IOException e) {
            throw new DataReadWriteIOException(e.getMessage());
        }
    }

    private void parseData(String[] data) {
        String category = data[0];
        PassCategory passCategory = categoryNoteList.get(category);
        if (null == passCategory) {
            passCategory = new PassCategory(category);
            categoryNoteList.put(category, passCategory);
            passCategoryList.add(passCategory);
        }
        PassNote passNote = new PassNote(passCategory,
                data[1].trim(), data[2].trim(), data[3].trim(), data[4].trim(), data[5].trim(), data[8].trim());
        passNoteList.add(passNote);
    }
}
