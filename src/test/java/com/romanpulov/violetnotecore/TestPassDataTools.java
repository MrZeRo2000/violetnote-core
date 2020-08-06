package com.romanpulov.violetnotecore;

import com.romanpulov.violetnotecore.Model.*;

import java.util.ArrayList;
import java.util.List;

public class TestPassDataTools {
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
                        "System " + categoryNum + "" + noteNum,
                        "User " + categoryNum + "" + noteNum,
                        "Password " + categoryNum + "" + noteNum,
                        "Comments " + categoryNum + "" + noteNum,
                        "Custom " + categoryNum + "" + noteNum,
                        "Info " + categoryNum + "" + noteNum)
                );
            }
        }

        PassData passData = new PassData();
        passData.setPassCategoryList(passCategoryList);
        passData.setPassNoteList(passNoteList);

        return passData;
    }

    public static String passDataEquals(PassData2 passData1, PassData2 passData2) {
        if ((passData1 == null) || (passData2 == null)) {
            return "Null passData1 or passData2";
        }

        if (passData1.getCategoryList().size() != passData2.getCategoryList().size()) {
            return "Category list size not match";
        }

        for (int i = 0; i < passData1.getCategoryList().size(); i++) {
            if (!passData1.getCategoryList().get(i).equals(passData2.getCategoryList().get(i))) {
                return "Category " + i + " not matching(" + passData1.getCategoryList().get(i) + ")";
            }

            if (passData1.getCategoryList().get(i).getNoteList().size() != passData2.getCategoryList().get(i).getNoteList().size()) {
                return "Note list size for category " + passData1.getCategoryList().get(i) + " not matching";
            }

            for (int j = 0; j < passData1.getCategoryList().get(i).getNoteList().size(); j++) {
                if (!passData1.getCategoryList().get(i).getNoteList().get(j).equals(passData2.getCategoryList().get(i).getNoteList().get(j))){
                    return "Note for category " + passData1.getCategoryList().get(i) + " not matching:" + passData1.getCategoryList().get(i).getNoteList().get(j);
                }
            }
        }

        return null;
    }
}
