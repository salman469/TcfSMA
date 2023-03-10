package com.tcf.sma.Helpers.DbTables.Help;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import com.tcf.sma.BuildConfig;
import com.tcf.sma.DataSync.DataSync;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Managers.StorageUtil;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.HR.UploadEmployeeModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTPhaseModel;
import com.tcf.sma.Models.RetrofitModels.Help.FAQsModel;
import com.tcf.sma.Models.RetrofitModels.Help.FeedbackModel;
import com.tcf.sma.Models.RetrofitModels.Help.UploadFeedbackModel;
import com.tcf.sma.Models.RetrofitModels.Help.UserManualModel;
import com.tcf.sma.Models.SyncProgress.SyncDownloadUploadModel;
import com.tcf.sma.Models.UserModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.core.content.FileProvider;

public class HelpHelperClass {

    private static HelpHelperClass instance = null;
    private Context context;

    public HelpHelperClass(Context context) {
        this.context = context;
    }

    public static HelpHelperClass getInstance(Context context) {
        if (instance == null)
            instance = new HelpHelperClass(context);

        return instance;
    }

    public String ID = "id";
    private String Stars = "stars";
    private String Comments = "comments";
    private String CreatedOn_App = "createdOn_app";
    private String CreatedBy = "createdBy";
    private String CreatedOn_Server = "createdOnServer";
    public String Uploaded_On = "uploaded_On";
    private String ModifiedOn = "modifiedOn";
    private String ModifiedBy = "modifiedBy";

    public String TABLE_FEEDBACK = "Feedback";
    public String CREATE_TABLE_FEEDBACK = "CREATE TABLE IF NOT EXISTS " + TABLE_FEEDBACK + " (" +
            ID + " INTEGER PRIMARY KEY," +
            Stars + " INTEGER, " +
            Comments + " TEXT, " +
            CreatedOn_App + " TEXT, " +
            CreatedBy + " INTEGER, " +
            CreatedOn_Server + " TEXT, " +
            Uploaded_On + " TEXT " +
            ");";

    private String Manual_Name = "manual_name";
    private String Filename = "filename";
    private String Filepath = "filepath";
    private String SortRank = "sortRank";
    private String CreatedOn = "createdOn";
    private String IsActive = "isActive";
    private String Version = "version";

    private String TABLE_USER_MANUAL = "User_Manual";
    private String TABLE_POLICIES = "Policies";
    public String CREATE_TABLE_USER_MANUAL = "CREATE TABLE IF NOT EXISTS " + TABLE_USER_MANUAL + " (" +
            ID + " INTEGER PRIMARY KEY," +
            Manual_Name + " TEXT, " +
            Filename + " TEXT, " +
            Filepath + " TEXT, " +
            SortRank + " INTEGER, " +
            CreatedOn + " TEXT, " +
            CreatedBy + " INTEGER, " +
            ModifiedOn + " TEXT, " +
            ModifiedBy + " INTEGER, " +
            IsActive + " BOOLEAN, " +
            Version + " TEXT" +
            ");";

    public String CREATE_TABLE_POLICIES = "CREATE TABLE IF NOT EXISTS " + TABLE_POLICIES + " (" +
            ID + " INTEGER PRIMARY KEY," +
            Manual_Name + " TEXT, " +
            Filename + " TEXT, " +
            Filepath + " TEXT, " +
            SortRank + " INTEGER, " +
            CreatedOn + " TEXT, " +
            CreatedBy + " INTEGER, " +
            ModifiedOn + " TEXT, " +
            ModifiedBy + " INTEGER, " +
            IsActive + " BOOLEAN, " +
            Version + " TEXT" +
            ");";

    private String Head = "head";
    private String SubHead = "subHead";
    private String Question = "question";
    private String Answer = "answer";

    private String TABLE_FAQs = "FAQs";
    public String CREATE_TABLE_FAQs = "CREATE TABLE IF NOT EXISTS " + TABLE_FAQs + " (" +
            ID + " INTEGER PRIMARY KEY," +
            Head + " TEXT, " +
            SubHead + " TEXT, " +
            Question + " TEXT, " +
            Answer + " TEXT, " +
            SortRank + " INTEGER, " +
            CreatedOn + " TEXT, " +
            CreatedBy + " INTEGER, " +
            ModifiedOn + " TEXT, " +
            ModifiedBy + " INTEGER, " +
            IsActive + " BOOLEAN " +
            ");";

    public void addFeedback(ArrayList<FeedbackModel> feedbackModels, SyncDownloadUploadModel syncDownloadUploadModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            DB.beginTransaction();

            int downloadedCount = 0;
            for (FeedbackModel model : feedbackModels) {
                model.setUploadedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

                if (!FindFeedback(model.getID())) {
                    ContentValues values = new ContentValues();
                    values.put(ID, model.getID());
                    values.put(Stars, model.getStars());
                    values.put(Comments, model.getComments());
                    values.put(CreatedBy, model.getCreatedBy());
                    values.put(CreatedOn_Server, model.getCreatedOn_Server());
                    values.put(Uploaded_On, model.getUploadedOn());

                    if (model.getCreatedOn_App() != null) {
                        String createdOnApp = AppModel.getInstance().convertDatetoFormat(model.getCreatedOn_App(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
                        model.setCreatedOn_App(createdOnApp);
                        values.put(CreatedOn_App, model.getCreatedOn_App());
                    }


                    long i = DB.insert(TABLE_FEEDBACK, null, values);
                    if (i == -1)
                        AppModel.getInstance().appendLog(context, "Couldn't insert feedback");
                    else if (i > 0) {
                        AppModel.getInstance().appendLog(context, "Feedback inserted");

                        downloadedCount++;
                    }
                }

                //Update sync progress
                DataSync.getInstance(context).syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
            }

            DB.setTransactionSuccessful();
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In AddOrUpdateFeedback Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            DataSync.areAllServicesSuccessful = false;
        } finally {
            DB.endTransaction();
        }
    }

    private boolean FindFeedback(int id) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_FEEDBACK
                    + " WHERE " + ID + " = " + id;

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);
            boolean count = cursor.getCount() > 0;
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    public void addOrUpdatePolicies(ArrayList<UserManualModel> policiesList, SyncDownloadUploadModel syncDownloadUploadModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            DB.beginTransaction();

            int downloadedCount = 0;
            for (UserManualModel model : policiesList) {

                if (!FindPolicy(model.getID())) {
                    ContentValues values = new ContentValues();
                    values.put(ID, model.getID());
                    values.put(Manual_Name, model.getManual_Name());
                    values.put(Filename, model.getFilename());
                    values.put(Filepath, model.getFilepath());
                    values.put(SortRank, model.getSortRank());
                    values.put(CreatedBy, model.getCreatedBy());
                    values.put(ModifiedOn, model.getModifiedOn());
                    values.put(ModifiedBy, model.getModifiedBy());
                    values.put(IsActive, model.isActive());
                    values.put(Version, model.getVersion());

                    if (model.getCreatedOn() != null) {
                        String createdOn = AppModel.getInstance().convertDatetoFormat(model.getCreatedOn(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
                        model.setCreatedOn(createdOn);
                        values.put(CreatedOn, model.getCreatedOn());
                    }


                    long i = DB.insert(TABLE_POLICIES, null, values);
                    if (i == -1)
                        AppModel.getInstance().appendLog(context, "Couldn't insert Policy");
                    else if (i > 0) {
                        AppModel.getInstance().appendLog(context, "Policy inserted");

                        downloadedCount++;
                    }
                } else {
                    long i = updatePolicy(model);
                    if (i > 0) {
                        downloadedCount++;
                    }
                }

                //Update sync progress
                DataSync.getInstance(context).syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
            }

            DB.setTransactionSuccessful();
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In AddOrUpdateUserManual Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            DataSync.areAllServicesSuccessful = false;
        } finally {
            DB.endTransaction();
        }
    }


    public void addOrUpdateUserManual(ArrayList<UserManualModel> userManualModels, SyncDownloadUploadModel syncDownloadUploadModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            DB.beginTransaction();

            int downloadedCount = 0;
            for (UserManualModel model : userManualModels) {

                if (!FindUserManual(model.getID())) {
                    ContentValues values = new ContentValues();
                    values.put(ID, model.getID());
                    values.put(Manual_Name, model.getManual_Name());
                    values.put(Filename, model.getFilename());
                    values.put(Filepath, model.getFilepath());
                    values.put(SortRank, model.getSortRank());
                    values.put(CreatedBy, model.getCreatedBy());
                    values.put(ModifiedOn, model.getModifiedOn());
                    values.put(ModifiedBy, model.getModifiedBy());
                    values.put(IsActive, model.isActive());
                    values.put(Version, model.getVersion());

                    if (model.getCreatedOn() != null) {
                        String createdOn = AppModel.getInstance().convertDatetoFormat(model.getCreatedOn(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
                        model.setCreatedOn(createdOn);
                        values.put(CreatedOn, model.getCreatedOn());
                    }


                    long i = DB.insert(TABLE_USER_MANUAL, null, values);
                    if (i == -1)
                        AppModel.getInstance().appendLog(context, "Couldn't insert User Manual");
                    else if (i > 0) {
                        AppModel.getInstance().appendLog(context, "User Manual inserted");

                        downloadedCount++;
                    }
                } else {
                    long i = updateUserManual(model);
                    if (i > 0) {
                        downloadedCount++;
                    }
                }

                //Update sync progress
                DataSync.getInstance(context).syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
            }

            DB.setTransactionSuccessful();
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In AddOrUpdateUserManual Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            DataSync.areAllServicesSuccessful = false;
        } finally {
            DB.endTransaction();
        }
    }

    private long updateUserManual(UserManualModel model) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(Manual_Name, model.getManual_Name());
            values.put(Filename, model.getFilename());
            values.put(Filepath, model.getFilepath());
            values.put(SortRank, model.getSortRank());
            values.put(CreatedBy, model.getCreatedBy());
            values.put(ModifiedOn, model.getModifiedOn());
            values.put(ModifiedBy, model.getModifiedBy());
            values.put(IsActive, model.isActive());
            values.put(Version, model.getVersion());

            if (model.getCreatedOn() != null) {
                String createdOn = AppModel.getInstance().convertDatetoFormat(model.getCreatedOn(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
                model.setCreatedOn(createdOn);
                values.put(CreatedOn, model.getCreatedOn());
            }

            long i = DB.update(TABLE_USER_MANUAL, values, ID + " = " + model.getID(), null);

            if (i == 0)
                AppModel.getInstance().appendLog(context, "Couldn't update User Manual");
            else
                AppModel.getInstance().appendLog(context, "User manual updated");

            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private boolean FindUserManual(int id) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_USER_MANUAL
                    + " WHERE " + ID + " = " + id;

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);
            boolean count = cursor.getCount() > 0;
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    private long updatePolicy(UserManualModel model) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(Manual_Name, model.getManual_Name());
            values.put(Filename, model.getFilename());
            values.put(Filepath, model.getFilepath());
            values.put(SortRank, model.getSortRank());
            values.put(CreatedBy, model.getCreatedBy());
            values.put(ModifiedOn, model.getModifiedOn());
            values.put(ModifiedBy, model.getModifiedBy());
            values.put(IsActive, model.isActive());
            values.put(Version, model.getVersion());

            if (model.getCreatedOn() != null) {
                String createdOn = AppModel.getInstance().convertDatetoFormat(model.getCreatedOn(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
                model.setCreatedOn(createdOn);
                values.put(CreatedOn, model.getCreatedOn());
            }

            long i = DB.update(TABLE_POLICIES, values, ID + " = " + model.getID(), null);

            if (i == 0)
                AppModel.getInstance().appendLog(context, "Couldn't update User Manual");
            else
                AppModel.getInstance().appendLog(context, "User manual updated");

            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private boolean FindPolicy(int id) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_POLICIES
                    + " WHERE " + ID + " = " + id;

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);
            boolean count = cursor.getCount() > 0;
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    public void addOrUpdateFAQs(ArrayList<FAQsModel> faqsModels, SyncDownloadUploadModel syncDownloadUploadModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            DB.beginTransaction();

            int downloadedCount = 0;
            for (FAQsModel model : faqsModels) {

                if (!FindFAQs(model.getID())) {
                    ContentValues values = new ContentValues();
                    values.put(ID, model.getID());
                    values.put(Head, model.getHead());
                    values.put(SubHead, model.getSubHead());
                    values.put(Question, model.getQuestion());
                    values.put(Answer, model.getAnswer());
                    values.put(SortRank, model.getSortRank());
                    values.put(CreatedBy, model.getCreatedBy());
                    values.put(ModifiedOn, model.getModifiedOn());
                    values.put(ModifiedBy, model.getModifiedBy());
                    values.put(IsActive, model.isActive());

                    if (model.getCreatedOn() != null) {
                        String createdOn = AppModel.getInstance().convertDatetoFormat(model.getCreatedOn(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
                        model.setCreatedOn(createdOn);
                        values.put(CreatedOn, model.getCreatedOn());
                    }


                    long i = DB.insert(TABLE_FAQs, null, values);
                    if (i == -1)
                        AppModel.getInstance().appendLog(context, "Couldn't insert FAQs");
                    else if (i > 0) {
                        AppModel.getInstance().appendLog(context, "FAQs inserted");

                        downloadedCount++;
                    }
                } else {
                    long i = updateFAQs(model);
                    if (i > 0) {
                        downloadedCount++;
                    }
                }

                //Update sync progress
                DataSync.getInstance(context).syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
            }

            DB.setTransactionSuccessful();
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In AddOrUpdateFAQs Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            DataSync.areAllServicesSuccessful = false;
        } finally {
            DB.endTransaction();
        }
    }

    private long updateFAQs(FAQsModel model) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(Head, model.getHead());
            values.put(SubHead, model.getSubHead());
            values.put(Question, model.getQuestion());
            values.put(Answer, model.getAnswer());
            values.put(SortRank, model.getSortRank());
            values.put(CreatedBy, model.getCreatedBy());
            values.put(ModifiedOn, model.getModifiedOn());
            values.put(ModifiedBy, model.getModifiedBy());
            values.put(IsActive, model.isActive());

            if (model.getCreatedOn() != null) {
                String createdOn = AppModel.getInstance().convertDatetoFormat(model.getCreatedOn(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
                model.setCreatedOn(createdOn);
                values.put(CreatedOn, model.getCreatedOn());
            }

            long i = DB.update(TABLE_FAQs, values, ID + " = " + model.getID(), null);

            if (i == 0)
                AppModel.getInstance().appendLog(context, "Couldn't update FAQs");
            else
                AppModel.getInstance().appendLog(context, "FAQs updated");

            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private boolean FindFAQs(int id) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_FAQs
                    + " WHERE " + ID + " = " + id;

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);
            boolean count = cursor.getCount() > 0;
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    public List<FAQsModel> getFaqsList() {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor cursor = null;
        List<FAQsModel> faQsModelList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_FAQs
                + " WHERE " + IsActive + " = 1"
                + " ORDER BY " + SortRank + " ASC";

        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    FAQsModel model = new FAQsModel();

                    model.setID(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setHead(cursor.getString(cursor.getColumnIndex(Head)));
                    model.setSubHead(cursor.getString(cursor.getColumnIndex(SubHead)));
                    model.setQuestion(cursor.getString(cursor.getColumnIndex(Question)));
                    model.setAnswer(cursor.getString(cursor.getColumnIndex(Answer)));
                    model.setSortRank(cursor.getInt(cursor.getColumnIndex(SortRank)));
                    model.setCreatedOn(cursor.getString(cursor.getColumnIndex(CreatedOn)));
                    model.setCreatedBy(cursor.getInt(cursor.getColumnIndex(CreatedBy)));
                    model.setModifiedOn(cursor.getString(cursor.getColumnIndex(ModifiedOn)));
                    model.setModifiedBy(cursor.getInt(cursor.getColumnIndex(ModifiedBy)));
                    model.setActive(cursor.getString(cursor.getColumnIndex(IsActive)).equals("1"));

                    faQsModelList.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return faQsModelList;
    }

    public long insertIntoFeedback(FeedbackModel model) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        long id = 0;
        try {
            ContentValues cv = new ContentValues();
            cv.put(Stars, model.getStars());
            cv.put(Comments, model.getComments());
            cv.put(CreatedOn_App, model.getCreatedOn_App());
            cv.put(CreatedBy, model.getCreatedBy());

            id = db.insert(TABLE_FEEDBACK, null, cv);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public List<UserManualModel> getPolicies() {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor cursor = null;
        List<UserManualModel> policiesModelList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_POLICIES
                + " WHERE " + IsActive + " = 1"
                + " ORDER BY " + SortRank + " ASC";

        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    UserManualModel model = new UserManualModel();

                    model.setID(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setManual_Name(cursor.getString(cursor.getColumnIndex(Manual_Name)));
                    model.setFilename(cursor.getString(cursor.getColumnIndex(Filename)));
                    model.setFilepath(cursor.getString(cursor.getColumnIndex(Filepath)));
                    model.setVersion("");
                    model.setSortRank(cursor.getInt(cursor.getColumnIndex(SortRank)));
                    model.setCreatedOn(cursor.getString(cursor.getColumnIndex(CreatedOn)));
                    model.setCreatedBy(cursor.getInt(cursor.getColumnIndex(CreatedBy)));
                    model.setModifiedOn(cursor.getString(cursor.getColumnIndex(ModifiedOn)));
                    model.setModifiedBy(cursor.getInt(cursor.getColumnIndex(ModifiedBy)));
                    model.setActive(cursor.getString(cursor.getColumnIndex(IsActive)).equals("1"));
                    model.setVersion(cursor.getString(cursor.getColumnIndex(Version)));

                    policiesModelList.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return policiesModelList;
    }

    public List<UserManualModel> getUserManual() {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor cursor = null;
        List<UserManualModel> userManualModelList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USER_MANUAL
                + " WHERE " + IsActive + " = 1"
                + " ORDER BY " + SortRank + " ASC";

        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    UserManualModel model = new UserManualModel();

                    model.setID(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setManual_Name(cursor.getString(cursor.getColumnIndex(Manual_Name)));
                    model.setFilename(cursor.getString(cursor.getColumnIndex(Filename)));
                    model.setFilepath(cursor.getString(cursor.getColumnIndex(Filepath)));
                    model.setVersion("");
                    model.setSortRank(cursor.getInt(cursor.getColumnIndex(SortRank)));
                    model.setCreatedOn(cursor.getString(cursor.getColumnIndex(CreatedOn)));
                    model.setCreatedBy(cursor.getInt(cursor.getColumnIndex(CreatedBy)));
                    model.setModifiedOn(cursor.getString(cursor.getColumnIndex(ModifiedOn)));
                    model.setModifiedBy(cursor.getInt(cursor.getColumnIndex(ModifiedBy)));
                    model.setActive(cursor.getString(cursor.getColumnIndex(IsActive)).equals("1"));
                    model.setVersion(cursor.getString(cursor.getColumnIndex(Version)));

                    userManualModelList.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return userManualModelList;
    }

    public void openManual(String fileName) {

        String dirPath = StorageUtil.getSdCardPath(context).getAbsolutePath() + "/TCF/TCF Documents/";
        File pdfFile = new File(dirPath + fileName);
        Uri path;
        try {

            if (pdfFile.exists()) {
                if (Build.VERSION.SDK_INT < 24) {
                    path = Uri.fromFile(pdfFile);
                } else {
                    path = FileProvider.getUriForFile(context,
                            BuildConfig.APPLICATION_ID + ".provider",
                            pdfFile);
                }

                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                pdfIntent.setDataAndType(path, "application/pdf");
                pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    context.startActivity(pdfIntent);
                    Toast.makeText(context, "Opening Document", Toast.LENGTH_SHORT).show();
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(context,"Error in openingManual: "+ e.getMessage());
        }
    }

    public List<UploadFeedbackModel> getAllFeedbackForUpload() {
        ArrayList<UploadFeedbackModel> fbList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_FEEDBACK
                    + " WHERE (" + Uploaded_On + " IS NULL OR " + Uploaded_On + " = '')";

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    UploadFeedbackModel model = new UploadFeedbackModel();
                    model.setID(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setStars(cursor.getInt(cursor.getColumnIndex(Stars)));
                    model.setComments(cursor.getString(cursor.getColumnIndex(Comments)));
                    model.setCreatedOn_App(cursor.getString(cursor.getColumnIndex(CreatedOn_App)));
                    model.setCreatedBy(cursor.getInt(cursor.getColumnIndex(CreatedBy)));

                    fbList.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return fbList;
    }

    public int getPendingFeedbackRecordsForSync() {
        int count = 0;
        String query = "select f." + Uploaded_On + " from " + TABLE_FEEDBACK + " f join user usr on usr.id = f."+CreatedBy+"\n" +
                " where (f."+Uploaded_On+" is null or f."+Uploaded_On+" = \"\")";

        Cursor cursor = null;
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                count = cursor.getCount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            return count;
        }

    }

    public String getLatestModifiedOn() {
        Cursor c = null;
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            String lmo = null;
            String selectQuery = "SELECT MAX(m.modifiedOn) as latestModifiedOn FROM (SELECT modifiedOn FROM FAQs\n" +
                    "UNION all SELECT modifiedOn FROM User_Manual) as m";

            c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                lmo = AppModel.getInstance().convertDatetoFormat(c.getString(c.getColumnIndex("latestModifiedOn")),
                        "yyyy-MM-dd'T'hh:mm:ss","dd-MM-yyyy");
            }

            if (lmo == null) {
                lmo = "";
            }
            return lmo;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }finally {
            if (c != null){
                c.close();
            }
        }
    }

    public boolean isShowFeedbackDialog() {
        boolean isShow = true;
        UserModel userModel = DatabaseHelper.getInstance(context).getCurrentLoggedInUser();
        if (userModel != null) {
            String selectQuery = "SELECT * from " + TABLE_FEEDBACK
                    + " WHERE strftime('%Y-%m', " + CreatedOn_App + ") = strftime('%Y-%m',date('now','start of month')) and " + CreatedBy + "=" + userModel.getId();
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            Cursor cursor = null;
            try {
                cursor = db.rawQuery(selectQuery,null);
                if (cursor.getCount() > 0){
                    isShow = false;
                }else {
                    isShow = true;
                }
            }catch (Exception e){
                e.printStackTrace();
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }

        return isShow;
    }

    public boolean isFeedbackGivenToday() {
        boolean shouldGiveFeedback = true;
        UserModel userModel = DatabaseHelper.getInstance(context).getCurrentLoggedInUser();
        String selectQuery = "SELECT * from " + TABLE_FEEDBACK
                + " WHERE strftime('%Y-%m-%d', " + CreatedOn_App + ") = strftime('%Y-%m-%d',date('now')) and " + CreatedBy + "=" + userModel.getId();
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery,null);
            if (cursor.getCount() > 0){
                shouldGiveFeedback = false;
            }else {
                shouldGiveFeedback = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return shouldGiveFeedback;
    }
}
