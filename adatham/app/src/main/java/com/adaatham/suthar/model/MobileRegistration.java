package com.adaatham.suthar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MobileRegistration {

@SerializedName("role")
@Expose
private String role;
@SerializedName("name")
@Expose
private String name;
@SerializedName("addr")
@Expose
private String addr;
@SerializedName("mobile")
@Expose
private String mobile;
@SerializedName("n_place")
@Expose
private String nPlace;
@SerializedName("email")
@Expose
private String email;
@SerializedName("city")
@Expose
private String city;

    public String getMem_no() {
        return mem_no;
    }

    public void setMem_no(String mem_no) {
        this.mem_no = mem_no;
    }

    @SerializedName("m_id")
    @Expose
private String mem_no;

public String getRole() {
return role;
}

public void setRole(String role) {
this.role = role;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getAddr() {
return addr;
}

public void setAddr(String addr) {
this.addr = addr;
}

public String getMobile() {
return mobile;
}

public void setMobile(String mobile) {
this.mobile = mobile;
}

public String getNPlace() {
return nPlace;
}

public void setNPlace(String nPlace) {
this.nPlace = nPlace;
}

public String getEmail() {
return email;
}

public void setEmail(String email) {
this.email = email;
}

public String getCity() {
return city;
}

public void setCity(String city) {
this.city = city;
}

}