package com.example.doctorapp;

import android.net.Uri;
import android.widget.TextView;

public class DoctorListModel {
    String drName;
    String deDegree;
    String address;
    Uri profileImage;

    public DoctorListModel(String drName, String deDegree, String address, Uri profileImage) {
        this.drName = drName;
        this.deDegree = deDegree;
        this.address = address;
        this.profileImage = profileImage;
    }

    public String getDrName() {
        return drName;
    }

    public void setDrName(String drName) {
        this.drName = drName;
    }

    public String getDeDegree() {
        return deDegree;
    }

    public void setDeDegree(String deDegree) {
        this.deDegree = deDegree;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Uri getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Uri profileImage) {
        this.profileImage = profileImage;
    }
}
