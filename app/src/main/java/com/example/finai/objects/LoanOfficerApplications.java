package com.example.finai.objects;

public class LoanOfficerApplications {

    //object for Loan Officers, used to read and write from the database

    private String loanOfficerID;
    private String username;
    private String email;
    private Long openLoans;
    private String phoneNumber;

    //empty constructor needed for reading from database

    public LoanOfficerApplications() {}

    public LoanOfficerApplications(String loanOfficerID, Long openLoans) {
        setLoanOfficerID(loanOfficerID);
        setOpenLoans(openLoans);
    }

    public String getLoanOfficerID() {
        return loanOfficerID;
    }

    public void setLoanOfficerID(String loanOfficerID) {
        this.loanOfficerID = loanOfficerID;
    }

    public Long getOpenLoans() {
        return openLoans;
    }

    public void setOpenLoans(Long openLoans) {
        this.openLoans = openLoans;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
