package com.tcf.sma.Models.RetrofitModels.Help;

import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTEmpSubjTagReasonModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTEmpSubjectTaggingModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTPhaseModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTSubjectsModel;

import java.util.ArrayList;

public class HelpDataResponseModel {

    public ArrayList<FeedbackModel> Feedback;
    public ArrayList<UserManualModel> UserManual;
    public ArrayList<FAQsModel> FAQs;
    public ArrayList<UserManualModel> Policies;

    public ArrayList<FeedbackModel> getFeedback() {
        return Feedback;
    }

    public void setFeedback(ArrayList<FeedbackModel> feedback) {
        Feedback = feedback;
    }

    public ArrayList<UserManualModel> getUserManual() {
        return UserManual;
    }

    public void setUserManual(ArrayList<UserManualModel> userManual) {
        UserManual = userManual;
    }

    public ArrayList<FAQsModel> getFAQs() {
        return FAQs;
    }

    public void setFAQs(ArrayList<FAQsModel> FAQs) {
        this.FAQs = FAQs;
    }

    public ArrayList<UserManualModel> getPolicies() {
        return Policies;
    }

    public void setPolicies(ArrayList<UserManualModel> policies) {
        Policies = policies;
    }
}
