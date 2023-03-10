package com.tcf.sma.Models.RetrofitModels.Help;

public class FeedbackModel {
    private int ID;
    private int Stars;
    private String Comments;
    private String CreatedOn_App;
    private int CreatedBy;
    private String CreatedOn_Server;

    private String UploadedOn;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getStars() {
        return Stars;
    }

    public void setStars(int stars) {
        Stars = stars;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public String getCreatedOn_App() {
        return CreatedOn_App;
    }

    public void setCreatedOn_App(String createdOn_App) {
        CreatedOn_App = createdOn_App;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(int createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedOn_Server() {
        return CreatedOn_Server;
    }

    public void setCreatedOn_Server(String createdOn_Server) {
        CreatedOn_Server = createdOn_Server;
    }

    public String getUploadedOn() {
        return UploadedOn;
    }

    public void setUploadedOn(String uploadedOn) {
        UploadedOn = uploadedOn;
    }
}
