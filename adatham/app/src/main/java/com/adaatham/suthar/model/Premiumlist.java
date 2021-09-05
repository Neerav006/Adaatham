package com.adaatham.suthar.model;
import android.util.SparseArray;
import android.util.SparseBooleanArray;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Premiumlist {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("m_id")
    @Expose
    private String mId;
    @SerializedName("l_id")
    @Expose
    private String lId;
    @SerializedName("p_month")
    @Expose
    private String pMonth;
    @SerializedName("p_year")
    @Expose
    private String pYear;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("panalty")
    @Expose
    private String panalty;
    @SerializedName("pay_amount")
    @Expose
    private String payAmount;
    @SerializedName("gen_no")
    @Expose
    private String genNo;
    @SerializedName("document")
    @Expose
    private String document;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("member_id")
    @Expose
    private String memberId;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("group_no")
    @Expose
    private String groupNo;

    public String getCheck_no() {
        return check_no;
    }

    public void setCheck_no(String check_no) {
        this.check_no = check_no;
    }

    @SerializedName("cheque_no")
    @Expose
    private String check_no;

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    @SerializedName("payment_type")
    @Expose
    private String payment_type;

    public String getPE() {
        return PE;
    }

    public void setPE(String PE) {
        this.PE = PE;
    }

    @SerializedName("PE")
    @Expose
    private String PE;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private boolean isChecked;

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    @SerializedName("update_time")
    @Expose
    private String update_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMId() {
        return mId;
    }

    public void setMId(String mId) {
        this.mId = mId;
    }

    public String getLId() {
        return lId;
    }

    public void setLId(String lId) {
        this.lId = lId;
    }

    public String getPMonth() {
        return pMonth;
    }

    public void setPMonth(String pMonth) {
        this.pMonth = pMonth;
    }

    public String getPYear() {
        return pYear;
    }

    public void setPYear(String pYear) {
        this.pYear = pYear;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPanalty() {
        return panalty;
    }

    public void setPanalty(String panalty) {
        this.panalty = panalty;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getGenNo() {
        return genNo;
    }

    public void setGenNo(String genNo) {
        this.genNo = genNo;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public HashMap<Integer, Integer> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<Integer, Integer> hashMap) {
        this.hashMap = hashMap;
    }

    private HashMap<Integer,Integer> hashMap;

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    private String bank_name;

    public String getC_no() {
        return c_no;
    }

    public void setC_no(String c_no) {
        this.c_no = c_no;
    }

    private String c_no;
}