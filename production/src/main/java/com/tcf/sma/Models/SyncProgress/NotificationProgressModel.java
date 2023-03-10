package com.tcf.sma.Models.SyncProgress;

import android.os.Parcel;
import android.os.Parcelable;

public class NotificationProgressModel implements Parcelable {

    private int progress;
    private String TCFModuleName;
    private String SyncType;

    public NotificationProgressModel() {
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(progress);
    }

    private NotificationProgressModel(Parcel in) {
        progress = in.readInt();
    }

    public static final Parcelable.Creator<NotificationProgressModel> CREATOR = new Parcelable.Creator<NotificationProgressModel>() {
        public NotificationProgressModel createFromParcel(Parcel in) {
            return new NotificationProgressModel(in);
        }

        public NotificationProgressModel[] newArray(int size) {
            return new NotificationProgressModel[size];
        }
    };
}

