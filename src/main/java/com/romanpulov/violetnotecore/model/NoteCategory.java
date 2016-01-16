package com.romanpulov.violetnotecore.Model;

/**
 * Created on 16.01.2016.
 */
public class NoteCategory {
    private String categoryName;
    private NoteCategory parentCategory;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public NoteCategory getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(NoteCategory parentCategory) {
        this.parentCategory = parentCategory;
    }
}
