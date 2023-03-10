package com.tcf.sma.Survey.model;

public class POI_Model {

    int sbjnum, District_ID, project_id, qc_status;
    String CategoryName, Title, RespondentName, RespondentMobile, SurveyData, created_on, surveyor_ID;
    long local_survey_ID = -1;
    ;
    double latitude, longitude;

    public long getLocal_survey_ID() {
        return local_survey_ID;
    }

    public void setLocal_survey_ID(long local_survey_ID) {
        this.local_survey_ID = local_survey_ID;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public int getQc_status() {
        return qc_status;
    }

    public void setQc_status(int qc_status) {
        this.qc_status = qc_status;
    }

    public int getSbjnum() {
        return sbjnum;
    }

    public void setSbjnum(int sbjnum) {
        this.sbjnum = sbjnum;
    }

    public int getDistrict_ID() {
        return District_ID;
    }

    public void setDistrict_ID(int district_ID) {
        District_ID = district_ID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getRespondentName() {
        return RespondentName;
    }

    public void setRespondentName(String respondentName) {
        RespondentName = respondentName;
    }

    public String getRespondentMobile() {
        return RespondentMobile;
    }

    public void setRespondentMobile(String respondentMobile) {
        RespondentMobile = respondentMobile;
    }

    public String getSurveyData() {
        return SurveyData;
    }

    public void setSurveyData(String surveyData) {
        SurveyData = surveyData;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getSurveyor_ID() {
        return surveyor_ID;
    }

    public void setSurveyor_ID(String surveyor_ID) {
        this.surveyor_ID = surveyor_ID;
    }

}
