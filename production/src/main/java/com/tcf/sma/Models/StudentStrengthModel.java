package com.tcf.sma.Models;

/**
 * Created by Zubair Soomro on 2/6/2017.
 */

public class StudentStrengthModel {
    private int Profile_Count;

    private String newprofiles;
    private String Dropouts;
    private String visitindate;
    private String comment;

    public StudentStrengthModel(String newprofiles, String dropouts, String visitindate, String comment) {
        this.newprofiles = newprofiles;
        Dropouts = dropouts;
        this.visitindate = visitindate;
        this.comment = comment;
    }

    public String getNewprofiles() {
        return newprofiles;
    }

    public void setNewprofiles(String newprofiles) {
        this.newprofiles = newprofiles;
    }

    public String getDropouts() {
        return Dropouts;
    }

    public void setDropouts(String dropouts) {
        Dropouts = dropouts;
    }

    public String getVisitindate() {
        return visitindate;
    }

    public void setVisitindate(String visitindate) {
        this.visitindate = visitindate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
