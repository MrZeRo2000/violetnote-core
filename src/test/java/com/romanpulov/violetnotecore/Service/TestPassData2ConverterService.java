package com.romanpulov.violetnotecore.Service;

import com.romanpulov.violetnotecore.Model.PassCategory2;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Model.PassNote2;
import com.romanpulov.violetnotecore.TestPassData2Generator;
import com.romanpulov.violetnotecore.TestPassDataGenerator;
import com.romanpulov.violetnotecore.TestPassData2Tools;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class TestPassData2ConverterService {
    private static final int NUM_CATEGORIES = 12;
    private static final int NUM_NOTES = 52;

    @Test
    public void testConverter() {
        PassData passData = TestPassDataGenerator.generateTestPassData(NUM_CATEGORIES, NUM_NOTES);
        PassData2 passData2 = TestPassData2Generator.generateTestPassData2(NUM_CATEGORIES, NUM_NOTES);

        PassData2 convertedPassData2 = PassData2ConverterService.from(passData);

        for (PassCategory2 passCategory2: passData2.getCategoryList())
            for (PassNote2 passNote2: passCategory2.getNoteList()) {
                String url = passNote2.getUrl();
                String numberString = url.substring(url.lastIndexOf(" ") + 1);
                String newUrl = String.format("Custom %sComments %s", numberString, numberString);
                passNote2.setUrl(newUrl);
            }

        String equalityCheck = TestPassData2Tools.passDataEquals(passData2, convertedPassData2);
        if (equalityCheck != null) {
            fail(equalityCheck);
        }
    }
}
