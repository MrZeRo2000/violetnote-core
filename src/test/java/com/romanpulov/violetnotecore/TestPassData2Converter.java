package com.romanpulov.violetnotecore;

import com.romanpulov.violetnotecore.Converter.PassData2Converter;
import com.romanpulov.violetnotecore.Model.PassCategory2;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Model.PassNote2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class TestPassData2Converter {
    private static final int NUM_CATEGORIES = 12;
    private static final int NUM_NOTES = 52;

    @Test
    public void testConverter() {
        PassData passData = TestPassDataTools.generateTestPassData(NUM_CATEGORIES, NUM_NOTES);
        PassData2 passData2 = TestPassData2Generator.generateTestPassData2(NUM_CATEGORIES, NUM_NOTES);

        PassData2 convertedPassData2 = PassData2Converter.from(passData);

        for (PassCategory2 passCategory2: passData2.getCategoryList())
            for (PassNote2 passNote2: passCategory2.getNoteList()) {
                String url = passNote2.getUrl();
                String numberString = url.substring(url.lastIndexOf(" ") + 1);
                String newUrl = String.format("Custom %sComments %s", numberString, numberString);
                passNote2.setUrl(newUrl);
            }

        String equalityCheck = TestPassDataTools.passDataEquals(passData2, convertedPassData2);
        if (equalityCheck != null) {
            fail(equalityCheck);
        }
    }
}
