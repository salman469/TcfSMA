package com.tcf.sma.Models;

import java.util.ArrayList;

public class ClassSectionModel {
    private int schoolClassId;
    private int sectionId;
    private int classId;
    private int rank;
    private String class_section_name;

    private ArrayList<ClassSectionModel> csmList;

    private ClassSectionModel csm;

    public ClassSectionModel() {

    }

    public ClassSectionModel(int sectionId, int classId, String ClassSectionName) {
        this.sectionId = sectionId;
        this.classId = classId;
        this.class_section_name = ClassSectionName;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClass_section_name() {
        return class_section_name;
    }

    public void setClass_section_name(String class_section_name) {
        this.class_section_name = class_section_name;
    }

    public int getSchoolClassId() {
        return schoolClassId;
    }

    public void setSchoolClassId(int schoolClassId) {
        this.schoolClassId = schoolClassId;
    }

    public ArrayList<ClassSectionModel> getClassAndSectionsList() {
        return csmList;
    }

    public ClassSectionModel setClassAndSectionsList(ArrayList<ClassSectionModel> csmList) {
        this.csmList = csmList;
        return this;
    }

    public String toString() {
        return getClass_section_name();
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
