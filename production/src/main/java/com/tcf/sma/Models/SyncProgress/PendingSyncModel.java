package com.tcf.sma.Models.SyncProgress;

public class PendingSyncModel {
    private String module;
    private int schoolId;
    private String name;
    private String created_on;
    private String modified_on;

    public PendingSyncModel(String module, int schoolId, String name, String created_on , String modified_on) {
        this.module = module;
        this.schoolId = schoolId;
        this.name = name;
        this.created_on = created_on;
        this.modified_on = modified_on;
    }

    public String getModule() {
        return module;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public String getName() {
        return name;
    }

    public String getCreated_on() {
        return created_on;
    }

    public String getModified_on() {
        return modified_on;
    }
}
