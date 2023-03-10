package com.tcf.sma.Survey.mediaServices;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.Uri;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class AudioPlayer implements OnCompletionListener, OnSeekCompleteListener, OnErrorListener {

    boolean timerRunning;
    TimerTask timertask;
    Timer timer = new Timer();
    private PlayStatus status;
    private MediaPlayer mediaPlayer;
    private MediaEventsHandler eventsHandler;
    private int positionUpdateInterval;

    public AudioPlayer() {
        status = PlayStatus.NotLoaded;
        positionUpdateInterval = 1000;
    }

    public void pausePlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            status = PlayStatus.Paused;
            if (eventsHandler != null) eventsHandler.onPlayPaused();
        }
        stopPositionUpdateTimer();
    }

    public boolean resumePlaying() {
        if (mediaPlayer != null && status == PlayStatus.Paused) {
            mediaPlayer.start();
            status = PlayStatus.Playing;
            if (eventsHandler != null) eventsHandler.onPlayResumed();
            startPositionUpdateTimer();
            return true;
        } else
            return false;
    }

    public int getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        } else return -1;
    }

    public int getTotalLength() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        } else return -1;
    }

    public PlayStatus getPlayStatus() {
        return status;
    }

    public boolean startPlaying(Context ctx, String uriSource) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            status = PlayStatus.NotLoaded;
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnErrorListener(this);
        try {

            Uri uriObj = Uri.parse(uriSource);

            mediaPlayer.setDataSource(ctx, uriObj);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException ex) {
            status = PlayStatus.NotLoaded;
            return false;
        }
        status = PlayStatus.Playing;
        startPositionUpdateTimer();
        if (eventsHandler != null) eventsHandler.onPlayStarted();
        return true;
    }

    public boolean startPlaying(String filename) //throws IOException
    {
/*		playButton.setEnabled(false);
		recordButton.setEnabled(false);
		stopButton.setEnabled(true);*/

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            status = PlayStatus.NotLoaded;
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnErrorListener(this);
        try {
            mediaPlayer.setDataSource(filename);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException ex) {
            status = PlayStatus.NotLoaded;
            return false;
        }
        status = PlayStatus.Playing;
        startPositionUpdateTimer();
        if (eventsHandler != null) eventsHandler.onPlayStarted();
        return true;
    }

    public void stopPlaying() {
        stopPositionUpdateTimer();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            if (eventsHandler != null) eventsHandler.onPlayStopped();
            //recordButton.setEnabled(true);
        }
        status = PlayStatus.NotLoaded;

    }

    public void setPosition(int position) {
        mediaPlayer.seekTo(position);
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        stopPositionUpdateTimer();
        if (eventsHandler != null) eventsHandler.onPlayCompleted();
    }

    @Override
    public void onSeekComplete(MediaPlayer arg0) {

    }

    @Override
    public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
        if (eventsHandler != null) return eventsHandler.onPlayError(arg1, arg2);
        else return false;
    }

    public void setMediaEventsHandler(MediaEventsHandler handler) {
        eventsHandler = handler;
    }

    public void setPositionUpdateInterval(int interval) {
        positionUpdateInterval = interval;
    }

    private void stopPositionUpdateTimer() {
        timerRunning = false;
    }

    private void poll() {
        if (timerRunning == false) return;
        timertask = new TimerTask() {
            @Override
            public void run() {
                if (timerRunning) {
                    if (eventsHandler != null && mediaPlayer != null && mediaPlayer.isPlaying()) {
                        eventsHandler.onSeekPositionChanged(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration());
                    }
                    poll();
                    //AudioPlayer.this.timer.schedule(AudioPlayer.this.timertask, AudioPlayer.this.positionUpdateInterval);
                }
            }
        };

        timer.schedule(timertask, positionUpdateInterval);
    }

    private void startPositionUpdateTimer() {

        timerRunning = true;
        poll();

    }

    public static enum PlayStatus {
        NotLoaded,
        Playing,
        Stopped,
        Paused;
    }
}
