package com.adaatham.suthar.model.veryfipayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VeryfiTransactionRes {

    @SerializedName("head")
    @Expose
    private Head head;
    @SerializedName("body")
    @Expose
    private Body body;

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

}