package com.tcf.sma.Survey.httpServices;


public interface IHttpPayloadRequestEventsHandler {

    public void onHttpDownloadCompleted(Object sender, HttpEventsArgs args);

    public void onHttpDownloadFailed(Object sender, HttpEventsArgs args);

    public void onHttpUploadCompleted(Object sender, HttpEventsArgs args);

    public void onHttpUploadFailed(Object sender, HttpEventsArgs args);


}
