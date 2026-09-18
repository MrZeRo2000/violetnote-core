package com.romanpulov.violetnotecore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestUtils {
    private final static Logger logger = Logger.getLogger(TestUtils.class.getName());

    public static void deleteFileIfExists(String fileName) {
        try {
            Files.deleteIfExists(Paths.get(fileName));
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not delete file " + fileName, e);
        }
    }
}
