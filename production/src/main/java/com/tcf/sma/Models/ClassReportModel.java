package com.tcf.sma.Models;

public class ClassReportModel {
    private String class_section_name;
    private int studentActiveCount;
    private int capacity;
    private double optUtls;
    private int maxCapacity;

    public String getClass_section_name() {
        return class_section_name;
    }

    public void setClass_section_name(String class_section_name) {
        this.class_section_name = class_section_name;
    }

    public int getStudentActiveCount() {
        return studentActiveCount;
    }

    public void setStudentActiveCount(int studentActiveCount) {
        this.studentActiveCount = studentActiveCount;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getOptUtls() {
        return optUtls;
    }

    public void setOptUtls(double optUtls) {
        this.optUtls = optUtls;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
}
