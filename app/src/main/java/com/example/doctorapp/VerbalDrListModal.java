package com.example.doctorapp;

public class VerbalDrListModal {
    String drName;
    String deDegree;
    String address;
    int profileImage;

    public VerbalDrListModal(String drName, String deDegree, String address, int profileImage) {
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

    public int getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }
}
