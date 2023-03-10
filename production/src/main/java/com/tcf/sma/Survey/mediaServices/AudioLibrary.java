package com.tcf.sma.Survey.mediaServices;

import android.content.Context;

import com.tcf.sma.Survey.activities.WebviewActivity;
import com.tcf.sma.Survey.model.SurveyAppModel;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AudioLibrary {

    public static String user = "";
    public static String projectID = "";
    public static String surveyorID = "";
    private final String libraryFolder;
    private final String filePrefix;
    private final String fileExtention;
    private AudioPlayer player;
    private AudioRecorder recorder;
    private ArrayList<String> audioFiles;
    private ArrayList<String> fileTags;
    private String currentRecordingFilename;
    private String currentPlayingFilename;
    private String storageRoot;
    private MediaEventsHandler eventsHandler;

    public AudioLibrary(MediaEventsHandler handler, Context context) throws IOException {
        eventsHandler = handler;

//		libraryFolder = "recnplayAudios";
        libraryFolder = "pictures"; //"DSDS/pictures";

        if (new File(SurveyAppModel.getInstance().getSurveyPath(context)).exists()) {
            storageRoot = SurveyAppModel.getInstance().getSurveyPath(context) + libraryFolder + "/";
        }/*else if(new File(Environment.getDataDirectory().getPath()).exists()) {
			storageRoot = Environment.getDataDirectory().getPath() + "/" + libraryFolder + "/";
		}*/ else {
            throw new IOException();
        }
        filePrefix = "audio_";
        fileExtention = ".mp4";

        player = new AudioPlayer();
        recorder = new AudioRecorder();
        audioFiles = new ArrayList<>();
        fileTags = new ArrayList<>();
        currentRecordingFilename = "";
        currentPlayingFilename = "";

        File f = new File(storageRoot);
        if (!f.exists() || !f.isDirectory()) {
            if (!f.mkdir()) {
                throw new IOException();
            }
        }
        LoadExistingFiles();

        player.setMediaEventsHandler(handler);
        recorder.setMediaEventsHandler(handler);
    }

    public void setOrCreateLibraryFolder(String folderName, Context context) throws IOException {
        if (new File(SurveyAppModel.getInstance().getSurveyPath(context)).exists()) {
            storageRoot = SurveyAppModel.getInstance().getSurveyPath(context) + libraryFolder + "/" + folderName + "/";
        } else {
            throw new IOException();
        }

        File f = new File(storageRoot);
        if (!f.exists() || !f.isDirectory()) {
            if (!f.mkdir()) {
                throw new IOException();
            }
        }
        LoadExistingFiles();
    }

    @SuppressWarnings("unchecked")
    public ArrayList<String> GetAvailabeAudios() {
        if (fileTags != null)
            return (ArrayList<String>) fileTags.clone();
        else return null;
    }

    public void startRecording() {    // VERSION 0.5.5 SHAZ 01/13/2015
        currentRecordingFilename = getNewFileName();
        WebviewActivity.recorded_file_path = currentRecordingFilename;    // VERSION 0.5.5 SHAZ 01/13/2015
        recorder.startRecording(currentRecordingFilename);
        ////if(eventHandler != null) eventHandler.onRecordStarted(currentRecordingFilename);
    }

    public void stopRecording() {

        recorder.stopRecording();
        File f = new File(currentRecordingFilename);
        if (f.exists()) {
            audioFiles.add(f.getAbsolutePath());
            fileTags.add(f.getName());
            currentRecordingFilename = "";
            if (eventsHandler != null) eventsHandler.onMediaAdded(f.getName());
        } else {
            currentRecordingFilename = "";
        }
    }

    public boolean busyRecording() {
        return recorder.isRecordingAudio();
    }

    public boolean startPlaying(String fileTag) {
        String filename = "";
        int index = fileTags.indexOf(fileTag);

        if (index != -1) {
            filename = audioFiles.get(index);
            currentPlayingFilename = filename;
            return true & player.startPlaying(filename);
        } else {
            return false;
        }

    }

    public void stopPlaying() {
        player.stopPlaying();
    }

    public void pausePlaying() {
        player.pausePlaying();
    }

    public void resumePlaying() {
        if (player.resumePlaying()) return;
        else if (!currentPlayingFilename.equals("")) {
            player.stopPlaying();
            player.startPlaying(currentPlayingFilename);
        }
    }

    public void updatePlayPosition(int position) {
        player.setPosition(position);
    }

    private String getNewFileName() {

        Date date = new Date();
        String fileName = "";
        do {
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd(hhmmss)");
            String datetime = s.format(new Date());

            // VERSION 0.5.5 SHAZ 01/13/2015
            WebviewActivity.recorded_file_name = filePrefix + "_" + user + "_" + surveyorID + "_" + projectID + "_" + date.getTime() + fileExtention;
            // VERSION 0.5.6 SHAZ 01/14/2015
            fileName = storageRoot + WebviewActivity.recorded_file_name;
        } while (new File(fileName).exists());

        return fileName;
    }

    private void LoadExistingFiles() {
        File f = new File(storageRoot);
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File directory, String fileName) {
                return fileName.endsWith(fileExtention);
            }
        };

        File[] files = f.listFiles(filter);

        audioFiles.clear();
        fileTags.clear();
        for (File fi : files) {
            audioFiles.add(fi.getAbsolutePath());
            fileTags.add(fi.getName());
        }

    }

    public void cancelRecording() {
        recorder.cancelRecording();
        currentRecordingFilename = "";
    }
}
