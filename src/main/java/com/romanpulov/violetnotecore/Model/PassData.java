package com.romanpulov.violetnotecore.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 19.01.2016.
 */
public class PassData {
    private List<PassCategory> passCategoryList = new ArrayList<>();
    private List<PassNote> passNoteList = new ArrayList<>();

    public PassData(List<PassCategory> passCategoryList, List<PassNote> passNoteList) {
        setPassData(passCategoryList, passNoteList);
    }

    public List<PassCategory> getPassCategoryList() {
        return passCategoryList;
    }

    public void setPassCategoryList(List<PassCategory> passCategoryList) {
        this.passCategoryList = passCategoryList;
    }

    public List<PassNote> getPassNoteList() {
        return passNoteList;
    }

    public void setPassNoteList(List<PassNote> passNoteList) {
        this.passNoteList = passNoteList;
    }

    public void setPassData(List<PassCategory> passCategoryList, List<PassNote> passNoteList) {
        this.passCategoryList = passCategoryList;
        this.passNoteList = passNoteList;
    }
}
