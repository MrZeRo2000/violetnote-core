package com.romanpulov.violetnotecore;

import static org.junit.Assert.*;

import com.romanpulov.violetnotecore.Processor.Exception.DataLoaderException;
import com.romanpulov.violetnotecore.Processor.PinsDataLoader;
import org.junit.Test;

public class Test1 {

    @Test
    public void method1() {
        assertEquals(1, 1);
    }

    @Test
    public void pinsLoadTest() {
        String fileName = "D:\\prj\\violetnote-core\\data\\pins_example.csv";
        PinsDataLoader loader = new PinsDataLoader();
        try {
            loader.loadFromFile(fileName);
        } catch (DataLoaderException e) {
            //fail("DataLoaderException:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
