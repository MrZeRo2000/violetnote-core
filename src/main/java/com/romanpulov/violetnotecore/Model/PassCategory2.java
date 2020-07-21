package com.romanpulov.violetnotecore.Model;

import java.util.List;
import java.util.Objects;

public class PassCategory2 {
    private String categoryName;

    private List<PassNote2> noteList;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<PassNote2> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<PassNote2> noteList) {
        this.noteList = noteList;
    }

    public static PassCategory2 createWithNotes(String categoryName, List<PassNote2> noteList) {
        PassCategory2 instance = new PassCategory2(categoryName);
        instance.setNoteList(noteList);

        return instance;
    }

    public PassCategory2() {}

    public PassCategory2(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PassCategory2 that = (PassCategory2) o;
        return categoryName.equals(that.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryName);
    }

    @Override
    public String toString() {
        return "PassCategory2{" +
                "categoryName='" + categoryName + '\'' +
                ", noteList=" + noteList +
                '}';
    }
}
