package com.romanpulov.violetnotecore.Converter;

import com.romanpulov.violetnotecore.Model.*;

import java.util.ArrayList;
import java.util.List;

public final class PassData2Converter {

    public static PassNote2 from(PassNote passNote) {
        return new PassNote2(
                passNote.getSystem(),
                passNote.getUser(),
                passNote.getPassword(),
                passNote.getCustom() + passNote.getComments(),
                passNote.getInfo(),
                passNote.getCreatedDate(),
                passNote.getModifiedDate()
        );
    }

    public static PassCategory2 from(PassCategory passCategory) {
        return new PassCategory2(passCategory.getCategoryName());
    }

    public static PassData2 from(PassData passData) {
        List<PassCategory2> passCategory2List = new ArrayList<>();

        for (PassCategory passCategory: passData.getPassCategoryList()) {
            PassCategory2 passCategory2 = from(passCategory);

            List<PassNote2> passNote2List = new ArrayList<>();

            for (PassNote passNote: passData.getPassNoteList()) {
                if (passNote.getPassCategory().getCategoryName().equals(passCategory2.getCategoryName())) {
                    passNote2List.add(from(passNote));
                }
            }

            passCategory2.setNoteList(passNote2List);

            passCategory2List.add(passCategory2);
        }

        return new PassData2(passCategory2List);
    }
}
