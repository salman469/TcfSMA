package com.tcf.sma.Models.SyncProgress;

import android.os.Parcel;
import android.os.Parcelable;

public class SyncDownloadUploadModel implements Parcelable {

    private int progress;
    private int currentFileSize;
    private int totalFileSize;
    private String TCFModuleName;
    private String SyncType;
    private int schoolId;
    private int TCFModuleId;

    public SyncDownloadUploadModel(){
    }

    public SyncDownloadUploadModel(int totalFileSize, String TCFModuleName, String syncType, int schoolId) {
        this.totalFileSize = totalFileSize;
        this.TCFModuleName = TCFModuleName;
        this.SyncType = syncType;
        this.schoolId = schoolId;
    }

    public SyncDownloadUploadModel(int totalFileSize, String TCFModuleName, int TCFModuleId, String syncType, int schoolId) {
        this.totalFileSize = totalFileSize;
        this.TCFModuleName = TCFModuleName;
        this.SyncType = syncType;
        this.schoolId = schoolId;
        this.TCFModuleId = TCFModuleId;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getCurrentFileSize() {
        return currentFileSize;
    }

    public void setCurrentFileSize(int currentFileSize) {
        this.currentFileSize = currentFileSize;
    }

    public int getTotalFileSize() {
        return totalFileSize;
    }

    public void setTotalFileSize(int totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    public String getTCFModuleName() {
        return TCFModuleName;
    }

    public void setTCFModuleName(String TCFModuleName) {
        this.TCFModuleName = TCFModuleName;
    }

    public String getSyncType() {
        return SyncType;
    }

    public void setSyncType(String syncType) {
        SyncType = syncType;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getTCFModuleId() {
        return TCFModuleId;
    }

    public void setTCFModuleId(int TCFModuleId) {
        this.TCFModuleId = TCFModuleId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(progress);
        dest.writeInt(currentFileSize);
        dest.writeInt(totalFileSize);
    }

    private SyncDownloadUploadModel(Parcel in) {

        progress = in.readInt();
        currentFileSize = in.readInt();
        totalFileSize = in.readInt();
    }

    public static final Parcelable.Creator<SyncDownloadUploadModel> CREATOR = new Parcelable.Creator<SyncDownloadUploadModel>() {
        public SyncDownloadUploadModel createFromParcel(Parcel in) {
            return new SyncDownloadUploadModel(in);
        }

        public SyncDownloadUploadModel[] newArray(int size) {
            return new SyncDownloadUploadModel[size];
        }
    };
}
