package com.adaatham.suthar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BusinessCategory {

@SerializedName("category_list")
@Expose
private List<BusCategoryList> categoryList = null;

public List<BusCategoryList> getCategoryList() {
return categoryList;
}

public void setCategoryList(List<BusCategoryList> categoryList) {
this.categoryList = categoryList;
}

}