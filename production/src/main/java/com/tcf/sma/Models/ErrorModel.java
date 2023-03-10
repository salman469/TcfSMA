package com.tcf.sma.Models;

public class ErrorModel {
    private String message;
    private int errorCode;
    private String errorConstant;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorConstant() {
        return errorConstant;
    }

    public void setErrorConstant(String errorConstant) {
        this.errorConstant = errorConstant;
    }
}
