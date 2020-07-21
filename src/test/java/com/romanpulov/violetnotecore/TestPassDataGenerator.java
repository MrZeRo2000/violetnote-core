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
}
