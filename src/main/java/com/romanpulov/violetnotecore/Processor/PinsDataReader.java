package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotecore.Model.PassData;
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
    protected static final String FILE_DELIMITER = "; ";
    protected static final int FILE_FIELD_COUNT = 9;

    private List<PassCategory> passCategoryList;
    private List<PassNote> passNoteList;
    private Map<String, PassCategory> categoryNoteList;

    public PassData readStream(InputStream stream) throws DataReadWriteException {
        passCategoryList = new ArrayList<>();
        passNoteList = new ArrayList<>();
        categoryNoteList = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            try {
                String line = null;
                boolean isFirstLine = true;
                while ((line = reader.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                    } else {
                        String[] splitLine = line.split(FILE_DELIMITER);
                        /*
                        if ((splitLine.length != FILE_FIELD_COUNT)) {
                            throw new DataReadWriteParserException(String.format(
                                    "Line %s: expected %d fields, actual: %d", line, FILE_FIELD_COUNT, splitLine.length));
                        }
                        */
                        parseData(splitLine);
                    }
                }
            } finally {
                reader.close();
            }
            return new PassData(passCategoryList, passNoteList);
        }
        catch (FileNotFoundException e) {
            throw new DataReadWriteFileNotFoundException(e.getMessage());
        }
        catch (IOException e) {
            throw new DataReadWriteIOException(e.getMessage());
        }
    }

    private static String parseString(String line) {
        return line.trim().replaceAll("\\|\\|", "\n");
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
                data.length > 1 ? parseString(data[1]) : "",
                data.length > 2 ? parseString(data[2]) : "",
                data.length > 3 ? parseString(data[3]) : "",
                data.length > 4 ? parseString(data[4]) : "",
                data.length > 5 ? parseString(data[5]) : "",
                data.length > 8 ? parseString(data[8]) : ""
        );
        passNoteList.add(passNote);
    }
}
