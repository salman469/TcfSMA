package com.tcf.sma.Models.RetrofitModels.Help;

public class FAQsModel {

    private int ID;
    private String Head;
    private String SubHead;
    private String Question;
    private String Answer;
    private int SortRank;
    private String CreatedOn;
    private int CreatedBy;
    private String ModifiedOn;
    private int ModifiedBy;
    private boolean IsActive;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getHead() {
        return Head;
    }

    public void setHead(String head) {
        Head = head;
    }

    public String getSubHead() {
        return SubHead;
    }

    public void setSubHead(String subHead) {
        SubHead = subHead;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public int getSortRank() {
        return SortRank;
    }

    public void setSortRank(int sortRank) {
        SortRank = sortRank;
    }

    public String getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        CreatedOn = createdOn;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(int createdBy) {
        CreatedBy = createdBy;
    }

    public String getModifiedOn() {
        return ModifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        ModifiedOn = modifiedOn;
    }

    public int getModifiedBy() {
        return ModifiedBy;
    }

    public void setModifiedBy(int modifiedBy) {
        ModifiedBy = modifiedBy;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }
}
