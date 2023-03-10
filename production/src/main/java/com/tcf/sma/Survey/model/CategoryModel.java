package com.tcf.sma.Survey.model;

public class CategoryModel {

    int fieldId, id, code, qouta, surveyed, project_ID;
    String title;

    public int getProject_ID() {
        return project_ID;
    }

    public void setProject_ID(int project_ID) {
        this.project_ID = project_ID;
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getQouta() {
        return qouta;
    }

    public void setQouta(int qouta) {
        this.qouta = qouta;
    }

    public int getSurveyed() {
        return surveyed;
    }

    public void setSurveyed(int surveyed) {
        this.surveyed = surveyed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
