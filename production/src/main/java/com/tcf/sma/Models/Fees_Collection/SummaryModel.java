package com.tcf.sma.Models.Fees_Collection;

import java.util.ArrayList;

public class SummaryModel {
    private ArrayList<SessionInfoModel> SchoolSSRSummary;
    private ArrayList<AttendanceSummary> AttendanceSummary;

    public ArrayList<SessionInfoModel> getSchoolSSRSummary() {
        return SchoolSSRSummary;
    }

    public void setSchoolSSRSummary(ArrayList<SessionInfoModel> schoolSSRSummary) {
        SchoolSSRSummary = schoolSSRSummary;
    }

    public ArrayList<com.tcf.sma.Models.Fees_Collection.AttendanceSummary> getAttendanceSummary() {
        return AttendanceSummary;
    }

    public void setAttendanceSummary(ArrayList<com.tcf.sma.Models.Fees_Collection.AttendanceSummary> attendanceSummary) {
        AttendanceSummary = attendanceSummary;
    }
}
