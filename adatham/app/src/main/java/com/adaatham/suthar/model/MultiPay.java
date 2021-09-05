package com.adaatham.suthar.model;

/**
 * Created by USER on 12-10-2017.
 */

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MultiPay {

    @SerializedName("family")
    @Expose
    private List<Family> family = null;

    public List<Family> getFamily() {
        return family;
    }

    public void setFamily(List<Family> family) {
        this.family = family;
    }

    public String getL_id() {
        return l_id;
    }

    public void setL_id(String l_id) {
        this.l_id = l_id;
    }

    @SerializedName("l_id")
    @Expose
    private String l_id;


    public ArrayList<Pay> getPayArrayList() {
        return payArrayList;
    }

    public void setPayArrayList(ArrayList<Pay> payArrayList) {
        this.payArrayList = payArrayList;
    }

    @SerializedName("pay")
    @Expose
    private ArrayList<Pay> payArrayList;
}