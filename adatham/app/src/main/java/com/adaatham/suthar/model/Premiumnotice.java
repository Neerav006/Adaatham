package com.adaatham.suthar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Premiumnotice {

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


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private boolean isChecked;

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

}