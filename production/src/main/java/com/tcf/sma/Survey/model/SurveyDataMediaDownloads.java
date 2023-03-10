package com.tcf.sma.Survey.model;

public class SurveyDataMediaDownloads {

    String userID, field_value, type, status;
    int project, field_id, sbjnum;


    public int getSbjnum() {
        return sbjnum;
    }

    public void setSbjnum(int sbjnum) {
        this.sbjnum = sbjnum;
    }

    public int getField_id() {
        return field_id;
    }

    public void setField_id(int field_id) {
        this.field_id = field_id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getProject() {
        return project;
    }

    public void setProject(int project) {
        this.project = project;
    }


    public String getField_value() {
        return field_value;
    }

    public void setField_value(String field_value) {
        this.field_value = field_value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
