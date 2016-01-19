package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.Model.NoteCategory;
import com.romanpulov.violetnotecore.Model.PassNote;
import com.romanpulov.violetnotecore.Processor.Exception.DataLoaderException;
import com.romanpulov.violetnotecore.Processor.Exception.DataLoaderFileNotFoundException;
import com.romanpulov.violetnotecore.Processor.Exception.DataLoaderIOException;
import com.romanpulov.violetnotecore.Processor.Exception.DataLoaderParserException;

import java.io.*;
import java.util.*;

/**
 * Created on 16.01.2016.
 */
public class PinsDataLoader {
    private static final String FILE_DELIMITER = ";";
    private static final int FILE_FIELD_COUNT = 9;

    private List<NoteCategory> noteCategoryList = new ArrayList<>();
    private List<PassNote> passNoteList = new ArrayList<>();

    public List<NoteCategory> getNoteCategoryList() {
        return noteCategoryList;
    }

    public List<PassNote> getPassNoteList() {
        return passNoteList;
    }

    private Map<String, NoteCategory> categoryNoteList = new HashMap<>();

    private void clearData() {
        noteCategoryList.clear();
        passNoteList.clear();
        categoryNoteList.clear();
    }

    public void loadFromFile(String fileName) throws DataLoaderException {
        File file = new File(fileName);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            try {
                clearData();

                String line = null;
                boolean isFirstLine = true;
                while ((line = reader.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue;
                    } else {
                        String[] splitLine = line.split(FILE_DELIMITER);
                        if (splitLine.length != FILE_FIELD_COUNT) {
                            throw new DataLoaderParserException(String.format(
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
            throw new DataLoaderFileNotFoundException(e.getMessage());
        }
        catch (IOException e) {
            throw new DataLoaderIOException(e.getMessage());
        }
    }

    private void parseData(String[] data) {
        String category = data[0];
        NoteCategory noteCategory = categoryNoteList.get(category);
        if (null == noteCategory) {
            noteCategory= new NoteCategory(category);
            categoryNoteList.put(category, noteCategory);
            noteCategoryList.add(noteCategory);
        }
        PassNote passNote = new PassNote(noteCategory,
                data[1].trim(), data[2].trim(), data[3].trim(), data[4].trim(), data[5].trim(), data[8].trim());
        passNoteList.add(passNote);
    }
}
