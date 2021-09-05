package com.adaatham.suthar.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VillageListData {

@SerializedName("villagelist")
@Expose
private List<Villagelist> villagelist = null;

public List<Villagelist> getVillagelist() {
return villagelist;
}

public void setVillagelist(List<Villagelist> villagelist) {
this.villagelist = villagelist;
}

}