package com.adaatham.suthar.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NoticeList {

@SerializedName("premiumnotice")
@Expose
private List<Premiumnotice> premiumnotice = null;

public List<Premiumnotice> getPremiumnotice() {
return premiumnotice;
}

public void setPremiumnotice(List<Premiumnotice> premiumnotice) {
this.premiumnotice = premiumnotice;
}

}