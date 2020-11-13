package com.example.finai.objects;

import com.google.firebase.database.DataSnapshot;

public class LoanOfficerApplications {

    private String loanOfficerID;


    private String username;
    private String email;
    private Long openLoans;
    private String pNumber;

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

    public String getpNumber() {
        return pNumber;
    }

    public void setpNumber(String pNumber) {
        this.pNumber = pNumber;
    }
}
