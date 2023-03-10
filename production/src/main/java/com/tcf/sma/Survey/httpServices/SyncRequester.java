package com.tcf.sma.Survey.httpServices;

public interface SyncRequester {

    public void onSyncStarted();

    public void onSyncCompleted();

    public void onMediaSyncStarted();

    public void onMediaSyncCompleted();

    public void onSyncFailed();

}
