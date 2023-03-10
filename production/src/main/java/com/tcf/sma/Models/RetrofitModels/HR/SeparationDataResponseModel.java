package com.tcf.sma.Models.RetrofitModels.HR;

import java.util.ArrayList;

public class SeparationDataResponseModel {

    public ArrayList<EmployeePendingApprovalModel> AllSeparations;
    public ArrayList<EmployeePendingApprovalModel> PendingApprovals;
    public ArrayList<EmployeeSeparationModel> SeparationDetail;
    public ArrayList<EmployeeModel> Users;
    public ArrayList<SeparationAttachmentsModel> ResignReceivedImages;

    public ArrayList<EmployeePendingApprovalModel> getPendingApprovals() {
        return PendingApprovals;
    }

    public void setPendingApprovals(ArrayList<EmployeePendingApprovalModel> pendingApprovals) {
        PendingApprovals = pendingApprovals;
    }

    public ArrayList<EmployeeSeparationModel> getSeparationDetail() {
        return SeparationDetail;
    }

    public void setSeparationDetail(ArrayList<EmployeeSeparationModel> separationDetail) {
        SeparationDetail = separationDetail;
    }

    public ArrayList<EmployeeModel> getUsers() {
        return Users;
    }

    public void setUsers(ArrayList<EmployeeModel> users) {
        Users = users;
    }

    public ArrayList<EmployeePendingApprovalModel> getAllSeparations() {
        return AllSeparations;
    }

    public void setAllSeparations(ArrayList<EmployeePendingApprovalModel> allSeparations) {
        AllSeparations = allSeparations;
    }

    public ArrayList<SeparationAttachmentsModel> getResignReceivedImages() {
        return ResignReceivedImages;
    }

    public void setResignReceivedImages(ArrayList<SeparationAttachmentsModel> resignReceivedImages) {
        ResignReceivedImages = resignReceivedImages;
    }
}
