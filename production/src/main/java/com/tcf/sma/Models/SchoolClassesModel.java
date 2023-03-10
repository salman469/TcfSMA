package com.tcf.sma.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mohammad.Haseeb on 2/27/2017.
 */

public class SchoolClassesModel {
    private int id;
    private int SchoolId;
    private int ClassId;
    private int SectionId;
    private boolean isActive;
    private int MaxCapacity;
    private int Capacity;
    private transient int rank;
    @SerializedName("ModifiedOn")
    private String modified_on;

    public SchoolClassesModel() {
    }

    public SchoolClassesModel(int id, int schoolId, int classId, int sectionId, boolean isactive) {
        this.id = id;
        SchoolId = schoolId;
        ClassId = classId;
        SectionId = sectionId;
        this.isActive = isactive;
    }

    public int getCapacity() {
        return Capacity;
    }

    public void setCapacity(int capacity) {
        Capacity = capacity;
    }

    public int getMaxCapacity() {
        return MaxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        MaxCapacity = maxCapacity;
    }

    //Getters

    public boolean is_active() {
        return isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSchoolId() {
        return SchoolId;
    }

    public void setSchoolId(int schoolId) {
        SchoolId = schoolId;
    }

    //Setters

    public int getClassId() {
        return ClassId;
    }

    public void setClassId(int classId) {
        ClassId = classId;
    }

    public int getSectionId() {
        return SectionId;
    }

    public void setSectionId(int sectionId) {
        SectionId = sectionId;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getModified_on() {
        return modified_on;
    }

    public void setModified_on(String modified_on) {
        this.modified_on = modified_on;
    }
}
