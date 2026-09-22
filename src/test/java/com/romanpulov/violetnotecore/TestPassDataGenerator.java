package com.romanpulov.violetnotecore;

import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Model.PassNote;

import java.util.ArrayList;
import java.util.List;

public class TestPassDataGenerator {
    public static PassData generateTestPassData() {
        List<PassCategory> passCategoryList = new ArrayList<>();
        passCategoryList.add(new PassCategory("New category"));
        List<PassNote> passNoteList = new ArrayList<>();
        passNoteList.add(new PassNote(
                passCategoryList.get(0),
                "System",
                "User",
                "Password",
                "Comments",
                "Custom",
                "Info")
        );

        PassData passData = new PassData();
        passData.setPassCategoryList(passCategoryList);
        passData.setPassNoteList(passNoteList);

        return passData;
    }

    public static PassData generateTestPassData(int numCategories, int numNotes) {

        List<PassCategory> passCategoryList = new ArrayList<>();
        List<PassNote> passNoteList = new ArrayList<>();

        for (int categoryNum = 0; categoryNum < numCategories; categoryNum ++) {
            PassCategory passCategory = new PassCategory("Category " + categoryNum);
            passCategoryList.add(passCategory);

            for (int noteNum = 0; noteNum < numNotes; noteNum ++) {
                passNoteList.add(new PassNote(
                        passCategory,
                        "System " + categoryNum + noteNum,
                        "User " + categoryNum + noteNum,
                        "Password " + categoryNum + noteNum,
                        "Comments " + categoryNum + noteNum,
                        "Custom " + categoryNum + noteNum,
                        "Info " + categoryNum + noteNum)
                );
            }
        }

        PassData passData = new PassData();
        passData.setPassCategoryList(passCategoryList);
        passData.setPassNoteList(passNoteList);

        return passData;
    }
}
