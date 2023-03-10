package com.tcf.sma.Survey.httpServices;

public interface ProjectsSyncHandler {


    public void surveyFormStarted();

    public void surveyFormEnded();

    public void projectCategoryQoutaStarted();

    public void projectCategoryQoutaEnded();

    public void surveyMediaStarted();

    public void surveyMediaEnded();

    public void surveyDataStarted();

    public void surveyDataEnded();

    public void surveyPictureStarted();

    public void DownloadSurveyPicture();
}
