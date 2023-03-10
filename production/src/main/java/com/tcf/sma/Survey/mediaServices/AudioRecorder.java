package com.tcf.sma.Survey.mediaServices;

import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class AudioRecorder implements OnErrorListener {

    boolean timerRunning;
    TimerTask timertask;
    Timer timer = new Timer();
    private MediaRecorder mediaRecorder;
    private boolean isRecording;
    private String currentRecordingFile;
    private MediaEventsHandler eventsHandler;
    private int positionUpdateInterval = 1000;

    public AudioRecorder() {
        isRecording = false;
        currentRecordingFile = "";
    }

    public boolean isRecordingAudio() {
        return isRecording;
    }

    public boolean startRecording(String audioFilePath) //throws IOException
    {
        if (isRecording) {
            //if(mediaRecorder!=null) stopRecording();
            return false;
        }

        try {
            mediaRecorder = new MediaRecorder();

            mediaRecorder.setOnErrorListener(this);

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setOutputFile(audioFilePath);
            //mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.prepare();

            mediaRecorder.start();
            isRecording = true;
            currentRecordingFile = audioFilePath;
            startPositionUpdateTimer();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if (eventsHandler != null) eventsHandler.onRecordStarted(currentRecordingFile);
        return true;
    }

    public void stopRecording() {
        stopPositionUpdateTimer();
        if (mediaRecorder != null && isRecording) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;

            if (eventsHandler != null) eventsHandler.onRecordStoped(currentRecordingFile);
            currentRecordingFile = "";

        }
    }

    public void cancelRecording() {
        stopPositionUpdateTimer();
        if (mediaRecorder != null && isRecording) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;

            File f = new File(currentRecordingFile);
            if (f.exists()) {
                f.delete();
            }
            if (eventsHandler != null) eventsHandler.onRecordCanceled(currentRecordingFile);
            currentRecordingFile = "";
        }
    }

    public void setMediaEventsHandler(MediaEventsHandler handler) {
        eventsHandler = handler;
    }

    @Override
    public void onError(MediaRecorder arg0, int arg1, int arg2) {
        if (eventsHandler != null) eventsHandler.onRecordError(arg1, arg2);

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
                    if (eventsHandler != null && mediaRecorder != null && isRecording) {
                        eventsHandler.onRecordIntervalElapsed();
                    }
                    poll();
                }
            }
        };

        timer.schedule(timertask, positionUpdateInterval);
    }

    private void startPositionUpdateTimer() {
        timerRunning = true;
        poll();
    }


}
