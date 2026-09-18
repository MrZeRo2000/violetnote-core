package com.romanpulov.violetnotecore;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TestConfiguration {
    public static final Path TEST_ROOT_PATH = Paths.get("src", "test", "resources", "testdata");

    public static String resolveTestFileName(String fileName) {
        return TEST_ROOT_PATH.resolve(fileName).toString();
    }
}
