package com.tcf.sma.Models.Fees_Collection;

public class GetReceiptBodyModel {
    private int schoolYearId;
    private int lastServerId;

    public int getSchoolYearId() {
        return schoolYearId;
    }

    public void setSchoolYearId(int schoolYearId) {
        this.schoolYearId = schoolYearId;
    }

    public int getLastServerId() {
        return lastServerId;
    }

    public void setLastServerId(int lastServerId) {
        this.lastServerId = lastServerId;
    }

    private String lastModifiedOn;

    public String getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(String lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

}
