package com.tcf.sma.Survey.mediaServices;

public interface MediaEventsHandler {

    public void onRecordStarted(String filename);

    public void onRecordStoped(String filename);

    public void onMediaAdded(String fileTag);

    public void onPlayStarted();

    public void onPlayStopped();

    public void onPlayPaused();

    public void onPlayResumed();

    public void onSeekPositionChanged(int currentPosition, int maxLength);

    public void onPlayCompleted();

    public boolean onPlayError(int arg1, int arg2);

    public void onRecordError(int arg1, int arg2);

    public void onRecordCanceled(String currentRecordingFile);

    public void onRecordIntervalElapsed();

}
