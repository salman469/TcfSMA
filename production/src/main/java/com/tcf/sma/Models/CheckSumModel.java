package com.tcf.sma.Models;

public class CheckSumModel {
    private int feesHeaderCount;
    private int feesDetailCount;
    private int feesDepositCount;
    private int studentCount;
    private int schoolClassCount;

    public int getFeesHeaderCount() {
        return feesHeaderCount;
    }

    public void setFeesHeaderCount(int feesHeaderCount) {
        this.feesHeaderCount = feesHeaderCount;
    }

    public int getFeesDetailCount() {
        return feesDetailCount;
    }

    public void setFeesDetailCount(int feesDetailCount) {
        this.feesDetailCount = feesDetailCount;
    }

    public int getFeesDepositCount() {
        return feesDepositCount;
    }

    public void setFeesDepositCount(int feesDepositCount) {
        this.feesDepositCount = feesDepositCount;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public int getSchoolClassCount() {
        return schoolClassCount;
    }

    public void setSchoolClassCount(int schoolClassCount) {
        this.schoolClassCount = schoolClassCount;
    }

    public boolean ifAnyZero() {
        return feesHeaderCount == 0 || feesDepositCount == 0;
    }
}
