package com.tcf.sma.Interfaces;

public interface NotificationProgressInterface {

    void onNotificationProgressStarted();
    void onNotificationProgressChanged(String module, int moduleId,boolean isDownloading);
    void onNotificationProgressCompleted();
    void onNotificationProgressCanceled();
}
