package com.adaatham.suthar.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MakePay {

@SerializedName("pay")
@Expose
private List<Pay> pay = null;

    public String getL_id() {
        return l_id;
    }

    public void setL_id(String l_id) {
        this.l_id = l_id;
    }

    @SerializedName("l_id")
@Expose
private String l_id;

public List<Pay> getPay() {
return pay;
}

public void setPay(List<Pay> pay) {
this.pay = pay;
}

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getT_amount() {
        return t_amount;
    }

    public void setT_amount(String t_amount) {
        this.t_amount = t_amount;
    }

    @SerializedName("p_name")
    @Expose
    private String p_name;

    @SerializedName("p_id")
    @Expose
    private String p_id;

    @SerializedName("t_amount")
    @Expose
    private String t_amount;

}