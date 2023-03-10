package com.tcf.sma.Models.Fees_Collection;

import com.google.gson.annotations.SerializedName;

public class SessionInfoModel {
    private int Id;
    private String SessionInfo;
    private int SchoolId;
    private int NewAdmission;
    private int ReAdmmission;
    @SerializedName("Transfer")
    private int Transfers;
    private int Withdrawal;
    private int Graduate;

    public SessionInfoModel() {

    }

    public SessionInfoModel(int id, String sessionInfo, int schoolId, int newAdmission, int reAdmmission, int transfers, int withdrawl, int graduate) {
        Id = id;
        SessionInfo = sessionInfo;
        SchoolId = schoolId;
        NewAdmission = newAdmission;
        ReAdmmission = reAdmmission;
        Transfers = transfers;
        Withdrawal = withdrawl;
        Graduate = graduate;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getTransfers() {
        return Transfers;
    }

    public void setTransfers(int transfers) {
        Transfers = transfers;
    }

    public String getSessionInfo() {
        return SessionInfo;
    }

    public void setSessionInfo(String sessionInfo) {
        SessionInfo = sessionInfo;
    }

    public int getSchoolid() {
        return SchoolId;
    }

    public void setSchoolid(int schoolid) {
        this.SchoolId = schoolid;
    }

    public int getNewAdmission() {
        return NewAdmission;
    }

    public void setNewAdmission(int newAdmission) {
        NewAdmission = newAdmission;
    }

    public int getReAdmmission() {
        return ReAdmmission;
    }

    public void setReAdmmission(int reAdmmission) {
        ReAdmmission = reAdmmission;
    }

    public int getWithdrawl() {
        return Withdrawal;
    }

    public void setWithdrawl(int withdrawl) {
        Withdrawal = withdrawl;
    }

    public int getGraduate() {
        return Graduate;
    }

    public void setGraduate(int graduate) {
        Graduate = graduate;
    }
}
