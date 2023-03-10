package com.tcf.sma.Survey.helpers;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.tcf.sma.Managers.StorageUtil;
import com.tcf.sma.Survey.model.SurveyAppModel;

import java.io.File;

public class DatabaseContext extends ContextWrapper {

    private static final String DEBUG_CONTEXT = "DatabaseContext";

    public DatabaseContext(Context base) {
        super(base);
    }

    @Override
    public File getDatabasePath(String name) {
        File sdcard = Environment.getExternalStorageDirectory();
        String dbfile = SurveyAppModel.getInstance().getSurveyPath(this) + "Database" + File.separator + name;
        if (!dbfile.endsWith(".db")) {
            dbfile += ".db//";
        }
        File result = new File(dbfile);
        result.setWritable(true);
        if (!result.getParentFile().exists()) {
            result.getParentFile().mkdirs();
        }


        if (Log.isLoggable(DEBUG_CONTEXT, Log.WARN)) {
            Log.w(DEBUG_CONTEXT, "getDatabasePath(" + name + ") = " + result.getAbsolutePath());
        }

        return result;
    }

    /* this version is called for android devices >= api-11. thank to @damccull for fixing this. */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return openOrCreateDatabase(name, mode, factory);
    }

    /* this version is called for android devices < api-11 */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
//        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
//        } else {
//            result = super.openOrCreateDatabase(name, mode, factory);
//        }
//        // SQLiteDatabase result = super.openOrCreateDatabase(name, mode, factory);
//        if (Log.isLoggable(DEBUG_CONTEXT, Log.WARN)) {
//            Log.w(DEBUG_CONTEXT, "openOrCreateDatabase(" + name + ",,) = " + result.getPath());
//        }
        return result;
    }
}
