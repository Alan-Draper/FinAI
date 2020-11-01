package com.example.finai;

public class Loan {

    private String loanID;
    private String userID;
    private String gender;
    private String maritalStatus;
    private String dependants;
    private String education;
    private String employment;
    private String income;
    private String coIncome;
    private String loanAmount;
    private String loanTerm;
    private String creditScore;
    private String location;
    private String loanStatus;
    private String loanOfficer;

    public Loan(String loanID, String userID, String gender, String maritalStatus, String dependants, String education, String employment, String income, String coIncome, String loanAmount, String loanTerm, String creditScore, String loanStatus, String location) {
        this.loanID = loanID;
        this.userID = userID;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.dependants = dependants;
        this.education = education;
        this.employment = employment;
        this.income = income;
        this.coIncome = coIncome;
        this.loanAmount = loanAmount;
        this.loanTerm = loanTerm;
        this.creditScore = creditScore;
        this.location = location;
        this.loanStatus = loanStatus;
        this.loanOfficer = " ";
    }

    public String getLoanID() {
        return loanID;
    }

    public void setLoanID(String loanID) {
        this.loanID = loanID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getEmployment() {
        return employment;
    }

    public void setEmployment(String employment) {
        this.employment = employment;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getCoIncome() {
        return coIncome;
    }

    public void setCoIncome(String coIncome) {
        this.coIncome = coIncome;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(String loanTerm) {
        this.loanTerm = loanTerm;
    }

    public String getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(String creditScore) {
        this.creditScore = creditScore;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLoanOfficer() {
        return loanOfficer;
    }

    public void setLoanOfficer(String loanOfficer) {
        this.loanOfficer = loanOfficer;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }
}
