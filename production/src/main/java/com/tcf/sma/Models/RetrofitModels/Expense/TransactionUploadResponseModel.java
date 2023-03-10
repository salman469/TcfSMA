package com.tcf.sma.Models.RetrofitModels.Expense;

public class TransactionUploadResponseModel {
    public int device_id;
    public int server_id;
    public String ErrorMessage;
    public String RejectionComments;
    boolean isActive;

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public int getServer_id() {
        return server_id;
    }

    public void setServer_id(int server_id) {
        this.server_id = server_id;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public String getRejectionComments() {
        return RejectionComments;
    }

    public void setRejectionComments(String rejectionComments) {
        RejectionComments = rejectionComments;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
