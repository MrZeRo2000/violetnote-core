package com.romanpulov.violetnotecore.Model;

/**
 * Created on 16.01.2016.
 */
public class PassCategory extends PassDataItem {
    public final static String XML_TAG_NAME = "category";
    public final static String XML_ATTR_CATEGORY_NAME = "name";

    private String categoryName;
    private PassCategory parentCategory;

    public PassCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public PassCategory(String categoryName, PassCategory parentCategory) {
        this.categoryName = categoryName;
        this.parentCategory = parentCategory;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PassCategory category = (PassCategory) o;

        if (!categoryName.equals(category.categoryName)) return false;
        return !(parentCategory != null ? !parentCategory.equals(category.parentCategory) : category.parentCategory != null);
    }

    @Override
    public int hashCode() {
        int result = categoryName.hashCode();
        result = 31 * result + (parentCategory != null ? parentCategory.hashCode() : 0);
        return result;
    }
}
