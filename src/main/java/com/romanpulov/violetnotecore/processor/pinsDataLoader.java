package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.Model.NoteCategory;
import com.romanpulov.violetnotecore.Model.PassNote;
import com.romanpulov.violetnotecore.Processor.Exception.DataLoaderException;
import com.romanpulov.violetnotecore.Processor.Exception.DataLoaderFileNotFoundException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

/**
 * Created on 16.01.2016.
 */
public class PinsDataLoader {
    private static final String FILE_DELIMITER = ";";

    private List<NoteCategory> noteCategoryList;
    private List<PassNote> passNoteList;

    public List<NoteCategory> getNoteCategoryList() {
        return noteCategoryList;
    }

    public List<PassNote> getPassNoteList() {
        return passNoteList;
    }

    private void clearData() {
        noteCategoryList = null;
        passNoteList = null;
    }

    public void loadFromFile(String fileName) throws DataLoaderException {
        File file = new File(fileName);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            clearData();
        } catch (FileNotFoundException e) {
            throw new DataLoaderFileNotFoundException(e.getMessage());
        }
    }
}
