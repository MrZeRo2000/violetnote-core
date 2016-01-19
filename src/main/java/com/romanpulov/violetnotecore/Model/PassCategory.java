package com.romanpulov.violetnotecore.Model;

/**
 * Created on 16.01.2016.
 */
public class PassCategory {
    private String categoryName;
    private PassCategory parentCategory;

    public PassCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public PassCategory getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(PassCategory parentCategory) {
        this.parentCategory = parentCategory;
    }

    @Override
    public String toString() {
        if (parentCategory == null) {
            return "{categoryName=" + categoryName + "}";
        } else {
            return "{categoryName=" + categoryName + ", parentCategory=" + parentCategory.categoryName + "}";
        }
    }
}
