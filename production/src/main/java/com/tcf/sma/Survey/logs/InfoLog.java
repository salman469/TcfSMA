package com.tcf.sma.Survey.logs;

import android.content.Context;

import com.tcf.sma.Survey.httpServices.AppConstants;
import com.tcf.sma.Survey.model.SurveyAppModel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class InfoLog {

//	private File logFile;

    public InfoLog() {

    }

    private File getLogFile(Context context) {
        SimpleDateFormat s = new SimpleDateFormat("yyMMdd");
        String datetime = s.format(new Date());

        String filename = SurveyAppModel.getInstance().getSurveyPath(context) + "Log/info_" + datetime + ".log";

        if (new File(filename).exists()) {
            return new File(filename);
        } else {
            File ImgDir = new File(SurveyAppModel.getInstance().getSurveyPath(context) + "Log/");
            if (!ImgDir.exists() || !ImgDir.isDirectory()) {
                ImgDir.mkdirs();
            }
            return new File(filename);
        }
    }

    public void addLog(String caller, String message, Context context) {
        FileWriter writer;
        SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss");
        String datetime = s.format(new Date());
        try {
            writer = new FileWriter(getLogFile(context), true);
            writer.append("\n" + datetime + ": " + caller + ": " + message + ", \n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // TODO - popup message on screen including message description and caller with OK button
            e.printStackTrace();
        }
    }


}
