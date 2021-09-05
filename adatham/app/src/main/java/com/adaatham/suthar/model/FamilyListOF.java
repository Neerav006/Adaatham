package com.adaatham.suthar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FamilyListOF {

@SerializedName("family_list")
@Expose
private List<FamilyList> familyList = null;

public List<FamilyList> getFamilyList() {
return familyList;
}

public void setFamilyList(List<FamilyList> familyList) {
this.familyList = familyList;
}

}