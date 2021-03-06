package com.example.finai.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    //Parcelable object for Users, used to read and write from the database, and pass Current user between classes
    private Long appRating;
    private String uid;
    private String username;
    private String email;
    private String gender;
    private String maritalStatus;
    private String loanOfficer;
    private String dependants;
    private String employmentStatus;
    private String education;

    public User(String uid, String username, String email, String gender, String maritalStatus, String dependants, String employmentStatus, String education) {
        setUid(uid);
        setDependants(dependants);
        setEducation(education);
        setEmail(email);
        setEmploymentStatus(employmentStatus);
        setGender(gender);
        setMaritalStatus(maritalStatus);
        setUsername(username);
    }

    public User(String uid, String username, String email) {
        setUid(uid);
        setUsername(username);
        setEmail(email);
        this.loanOfficer = "none";
    }



    public User() {}

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getDependants() {
        return dependants;
    }

    public void setDependants(String dependants) {
        this.dependants = dependants;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getLoanOfficer() {
        return loanOfficer;
    }

    public void setLoanOfficer(String loanOfficer) {
        this.loanOfficer = loanOfficer;
    }

    public Long getAppRating() {
        return appRating;
    }

    public void setAppRating(Long appRating) {
        this.appRating = appRating;
    }

    protected User(Parcel in) {
        username = in.readString();
        email = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUsername() {
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(email);
    }
}
