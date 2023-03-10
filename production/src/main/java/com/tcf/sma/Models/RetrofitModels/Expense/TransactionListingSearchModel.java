package com.tcf.sma.Models.RetrofitModels.Expense;

public class TransactionListingSearchModel {
    int schoolId, jvNo, isActive, bucketId, subheadId;
    String startDate, endDate, status, bucket, subHead;

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getJvNo() {
        return jvNo;
    }

    public void setJvNo(int jvNo) {
        this.jvNo = jvNo;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getBucketId() {
        return bucketId;
    }

    public void setBucketId(int bucketId) {
        this.bucketId = bucketId;
    }

    public int getSubheadId() {
        return subheadId;
    }

    public void setSubheadId(int subheadId) {
        this.subheadId = subheadId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getSubHead() {
        return subHead;
    }

    public void setSubHead(String subHead) {
        this.subHead = subHead;
    }
}
