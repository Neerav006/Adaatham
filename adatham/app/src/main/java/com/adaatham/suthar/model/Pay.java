package com.adaatham.suthar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pay {

@SerializedName("id")
@Expose
private String mId;

    public String getP_type() {
        return p_type;
    }

    public void setP_type(String p_type) {
        this.p_type = p_type;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getC_no() {
        return c_no;
    }

    public void setC_no(String c_no) {
        this.c_no = c_no;
    }

    @SerializedName("p_type")
private String p_type;

@SerializedName("bank_name")
@Expose
private String bank_name;

@SerializedName("c_no")
@Expose
private String c_no;



public String getMId() {
return mId;
}

public void setMId(String mId) {
this.mId = mId;
}

}