package com.adaatham.suthar.model.paytmmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Body {

    @SerializedName("resultInfo")
    @Expose
    private ResultInfo resultInfo;
    @SerializedName("txnToken")
    @Expose
    private String txnToken;
    @SerializedName("isPromoCodeValid")
    @Expose
    private Boolean isPromoCodeValid;
    @SerializedName("authenticated")
    @Expose
    private Boolean authenticated;

    @SerializedName("amount")
    @Expose
    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @SerializedName("order_id")
    @Expose
    private String orderId;

    public ResultInfo getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(ResultInfo resultInfo) {
        this.resultInfo = resultInfo;
    }

    public String getTxnToken() {
        return txnToken;
    }

    public void setTxnToken(String txnToken) {
        this.txnToken = txnToken;
    }

    public Boolean getIsPromoCodeValid() {
        return isPromoCodeValid;
    }

    public void setIsPromoCodeValid(Boolean isPromoCodeValid) {
        this.isPromoCodeValid = isPromoCodeValid;
    }

    public Boolean getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        this.authenticated = authenticated;
    }

}