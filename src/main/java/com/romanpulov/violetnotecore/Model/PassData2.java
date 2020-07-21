package com.romanpulov.violetnotecore.Model;

import java.util.List;

public class PassData2 {

    private List<PassCategory2> categoryList;

    public List<PassCategory2> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<PassCategory2> categoryList) {
        this.categoryList = categoryList;
    }

    public PassData2() {}

    public PassData2(List<PassCategory2> categoryList) {
        this.categoryList = categoryList;
    }

    @Override
    public String toString() {
        return "PassData2{" +
                "categoryList=" + categoryList +
                '}';
    }
}
