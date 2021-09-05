package com.adaatham.suthar.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BusCategoryList implements Parcelable {

    @SerializedName("main_cat")
    @Expose
    private MainCat mainCat;
    @SerializedName("sub_cat")
    @Expose
    private List<SubCat> subCat = null;

    public MainCat getMainCat() {
        return mainCat;
    }

    public void setMainCat(MainCat mainCat) {
        this.mainCat = mainCat;
    }

    public List<SubCat> getSubCat() {
        return subCat;
    }

    public void setSubCat(List<SubCat> subCat) {
        this.subCat = subCat;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mainCat, flags);
        dest.writeList(this.subCat);
    }

    public BusCategoryList() {
    }

    protected BusCategoryList(Parcel in) {
        this.mainCat = in.readParcelable(MainCat.class.getClassLoader());
        this.subCat = new ArrayList<SubCat>();
        in.readList(this.subCat, SubCat.class.getClassLoader());
    }

    public static final Creator<BusCategoryList> CREATOR = new Creator<BusCategoryList>() {
        @Override
        public BusCategoryList createFromParcel(Parcel source) {
            return new BusCategoryList(source);
        }

        @Override
        public BusCategoryList[] newArray(int size) {
            return new BusCategoryList[size];
        }
    };
}
