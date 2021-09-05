package com.adaatham.suthar.model.veryfipayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Body {

@SerializedName("resultInfo")
@Expose
private ResultInfo resultInfo;
@SerializedName("txnId")
@Expose
private String txnId;
@SerializedName("bankTxnId")
@Expose
private String bankTxnId;
@SerializedName("orderId")
@Expose
private String orderId;
@SerializedName("txnAmount")
@Expose
private String txnAmount;
@SerializedName("txnType")
@Expose
private String txnType;
@SerializedName("gatewayName")
@Expose
private String gatewayName;
@SerializedName("bankName")
@Expose
private String bankName;
@SerializedName("mid")
@Expose
private String mid;
@SerializedName("paymentMode")
@Expose
private String paymentMode;
@SerializedName("refundAmt")
@Expose
private String refundAmt;
@SerializedName("txnDate")
@Expose
private String txnDate;

public ResultInfo getResultInfo() {
return resultInfo;
}

public void setResultInfo(ResultInfo resultInfo) {
this.resultInfo = resultInfo;
}

public String getTxnId() {
return txnId;
}

public void setTxnId(String txnId) {
this.txnId = txnId;
}

public String getBankTxnId() {
return bankTxnId;
}

public void setBankTxnId(String bankTxnId) {
this.bankTxnId = bankTxnId;
}

public String getOrderId() {
return orderId;
}

public void setOrderId(String orderId) {
this.orderId = orderId;
}

public String getTxnAmount() {
return txnAmount;
}

public void setTxnAmount(String txnAmount) {
this.txnAmount = txnAmount;
}

public String getTxnType() {
return txnType;
}

public void setTxnType(String txnType) {
this.txnType = txnType;
}

public String getGatewayName() {
return gatewayName;
}

public void setGatewayName(String gatewayName) {
this.gatewayName = gatewayName;
}

public String getBankName() {
return bankName;
}

public void setBankName(String bankName) {
this.bankName = bankName;
}

public String getMid() {
return mid;
}

public void setMid(String mid) {
this.mid = mid;
}

public String getPaymentMode() {
return paymentMode;
}

public void setPaymentMode(String paymentMode) {
this.paymentMode = paymentMode;
}

public String getRefundAmt() {
return refundAmt;
}

public void setRefundAmt(String refundAmt) {
this.refundAmt = refundAmt;
}

public String getTxnDate() {
return txnDate;
}

public void setTxnDate(String txnDate) {
this.txnDate = txnDate;
}

}