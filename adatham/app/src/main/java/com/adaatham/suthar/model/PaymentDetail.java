package com.adaatham.suthar.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentDetail {

    @SerializedName("premiumlist")
    @Expose
    private List<Premiumlist> premiumlist = null;

    public List<Premiumlist> getPremiumlist() {
        return premiumlist;
    }

    public void setPremiumlist(List<Premiumlist> premiumlist) {
        this.premiumlist = premiumlist;
    }
}

