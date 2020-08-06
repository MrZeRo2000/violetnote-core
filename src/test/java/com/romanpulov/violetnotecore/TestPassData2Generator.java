package com.romanpulov.violetnotecore;

import com.romanpulov.violetnotecore.Model.PassCategory2;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Model.PassNote2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestPassData2Generator {
    public static PassData2 generateTestPassData2() {

        // category 1
        PassCategory2 passCategory1 = new PassCategory2("Category 1");

        // category 1 - list
        List<PassNote2> passNoteList1 = new ArrayList<>();
        passNoteList1.add(new PassNote2("System 1", "User 1", "Password 1", "URL 1", "Info 1", null, null));
        passNoteList1.add(new PassNote2("System 2", "User 2", "Password 2", "URL 2", "Info 2", null, null));

        passCategory1.setNoteList(passNoteList1);

        // category 2
        PassCategory2 passCategory2 = new PassCategory2("Category 2");

        // category 2 - list
        List<PassNote2> passNoteList2 = new ArrayList<>();
        passNoteList2.add(new PassNote2("System 3", "User 3", "Password 3", "URL 3", "Info 3", null, null));

        passCategory2.setNoteList(passNoteList2);

        // list of categories
        List<PassCategory2> passCategoryList = Arrays.asList(passCategory1, passCategory2);

        return new PassData2(passCategoryList);
    }

    public static PassData2 generateTestPassData2(int numCategories, int numNotes) {

        List<PassCategory2> passCategoryList = new ArrayList<>();

        for (int categoryNum = 0; categoryNum < numCategories; categoryNum ++) {
            PassCategory2 passCategory = new PassCategory2("Category " + categoryNum);
            List<PassNote2> passNoteList = new ArrayList<>();

            for (int noteNum = 0; noteNum < numNotes; noteNum ++) {
                passNoteList.add(new PassNote2(
                        "System " + categoryNum + "" + noteNum,
                        "User " + categoryNum + "" + noteNum,
                        "Password " + categoryNum + "" + noteNum,
                        "URL " + categoryNum + "" + noteNum,
                        "Info " + categoryNum + "" + noteNum,
                        null,
                        null
                ));
            }

            passCategory.setNoteList(passNoteList);
            passCategoryList.add(passCategory);
        }

        return new PassData2(passCategoryList);
    }
}
