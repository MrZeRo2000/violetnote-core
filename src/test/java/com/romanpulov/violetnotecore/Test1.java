package com.romanpulov.violetnotecore;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotecore.Model.PassNote;
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
        String fileName = "data\\pins_example.csv";
        PinsDataLoader loader = new PinsDataLoader();
        try {
            loader.loadFromFile(fileName);

            System.out.println("PassCategory:");
            for (PassCategory c : loader.getPassCategoryList()) {
                System.out.println(c.toString());
            }

            System.out.println("PassNote:");
            for (PassNote n : loader.getPassNoteList()) {
                System.out.println(n.toString());
            }

        } catch (DataLoaderException e) {
            fail("DataLoaderException:" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void xmlCreateTest() {
        
    }
}
