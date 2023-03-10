package com.tcf.sma.Helpers.DbTables.Global;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.ClassModel;
import com.tcf.sma.Models.RetrofitModels.ElectiveSubjectModel;
import com.tcf.sma.Models.RetrofitModels.ElectiveSubjectStudentModel;
import com.tcf.sma.Models.RetrofitModels.NationalityModel;
import com.tcf.sma.Models.RetrofitModels.ReligionModel;

import java.util.ArrayList;
import java.util.List;

public class GlobalHelperClass {
    private static GlobalHelperClass instance;
    private Context context;

    public GlobalHelperClass(Context context) {
        this.context = context;
    }

    public static GlobalHelperClass getInstance(Context context){
        if (instance == null)
            instance = new GlobalHelperClass(context);

        return instance;
    }

    //Student Religion Table
    public final String TABLE_RELIGION = "Religion";
    public final String ID = "id";

    public final String title = "title";
    public final String isActive = "isActive";
    public final String createdOn = "createdOn";
    public final String modifiedOn = "modifiedOn";

    //Elective Subjects
    public final String electiveSubjectID = "electiveSubjectID";
    public final String studentID = "studentID";


    public String CREATE_TABLE_RELIGION = "CREATE TABLE IF NOT EXISTS " + TABLE_RELIGION + " (" +
            ID + " INTEGER PRIMARY KEY," +
            title + " TEXT ," +
            isActive + " Boolean ," +
            createdOn + " TEXT ," +
            modifiedOn + " TEXT" +
            ");";

    public ArrayList<ReligionModel> getAllReligions() {
        ArrayList<ReligionModel> religionModelList = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_RELIGION + " where " + isActive + "= 1 order by " + ID + " ASC";
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    ReligionModel religionModel = new ReligionModel();
                    boolean b = cursor.getString(cursor.getColumnIndex(isActive)).equals("1");
                    religionModel.setActive(b);
                    religionModel.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    religionModel.setTitle(cursor.getString(cursor.getColumnIndex(title)));
                    religionModel.setCreatedOn(cursor.getString(cursor.getColumnIndex(createdOn)));
                    religionModel.setModifiedOn(cursor.getString(cursor.getColumnIndex(modifiedOn)));
                    religionModelList.add(religionModel);
                }while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return religionModelList;
    }

    public boolean insertReligion(List<ReligionModel> modelList){
        int counter = 0;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            for (ReligionModel model : modelList) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(ID, model.getId());
                contentValues.put(title, model.getTitle());
                contentValues.put(isActive, model.isActive());
                contentValues.put(createdOn, model.getCreatedOn());
                contentValues.put(modifiedOn, model.getModifiedOn());
                long i = DB.insert(TABLE_RELIGION, null, contentValues);
                if(i > 0){
                    counter++;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return counter == modelList.size();
    }

    public boolean updateReligion(List<ReligionModel> modelList){
        int counter = 0;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            for (ReligionModel model : modelList) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(ID, model.getId());
                contentValues.put(title, model.getTitle());
                contentValues.put(isActive, model.isActive());
                contentValues.put(createdOn, model.getCreatedOn());
                contentValues.put(modifiedOn, model.getModifiedOn());
                long i = DB.update(TABLE_RELIGION,  contentValues,ID + "=" + model.getId(),null);
                if(i > 0){
                    counter++;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return counter == modelList.size();
    }

    public void insertTempReligionTable() {
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            String insertQuery = "Insert into " + TABLE_RELIGION + " ("
                    + ID + ","
                    + title + ","
                    + isActive + ","
                    + createdOn + ","
                    + modifiedOn + ") VALUES ";

            insertQuery += "(1,'Muslim',1,'25-6-2022T03:29:00','25-6-2022T03:29:00'),";
            insertQuery += "(2,'Christian',1,'25-6-2022T03:29:00','25-6-2022T03:29:00'),";
            insertQuery += "(3,'Hindu',1,'25-6-2022T03:29:00','25-6-2022T03:29:00'),";
            insertQuery += "(4,'Buddhist',1,'25-6-2022T03:29:00','25-6-2022T03:29:00'),";
            insertQuery += "(5,'Sikh',1,'25-6-2022T03:29:00','25-6-2022T03:29:00'),";
            insertQuery += "(6,'Muslim',1,'25-6-2022T03:29:00','25-6-2022T03:29:00'),";
            insertQuery += "(7,'Jew',1,'25-6-2022T03:29:00','25-6-2022T03:29:00'),";
            insertQuery += "(8,'Parsi',1,'25-6-2022T03:29:00','25-6-2022T03:29:00'),";
            insertQuery += "(9,'Other',1,'25-6-2022T03:29:00','25-6-2022T03:29:00')";
            DB.execSQL(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dropTempReligionTable() {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        String query = "Drop table if exists " + TABLE_RELIGION;
        try {
            DB.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long addReligion(ReligionModel rm) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(ID, rm.getId());
            values.put(title, rm.getTitle());
            values.put(createdOn, rm.getCreatedOn());
            values.put(modifiedOn, rm.getModifiedOn());
            values.put(isActive, rm.isActive());

            long i = DB.insert(TABLE_RELIGION, null, values);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public long updateReligion(ReligionModel rm) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(ID, rm.getId());
            values.put(title, rm.getTitle());
            values.put(createdOn, rm.getCreatedOn());
            values.put(modifiedOn, rm.getModifiedOn());
            values.put(isActive, rm.isActive());

            long i = DB.update(TABLE_RELIGION, values, ID + " = " + rm.getId(), null);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    //Nationality Table
    public final String TABLE_NATIONALITY = "Nationality";

    public String CREATE_TABLE_NATIONALITY = "CREATE TABLE IF NOT EXISTS " + TABLE_NATIONALITY + " (" +
            ID + " INTEGER PRIMARY KEY," +
            title + " TEXT ," +
            isActive + " Boolean ," +
            createdOn + " TEXT ," +
            modifiedOn + " TEXT" +
            ");";

    public ArrayList<NationalityModel> getAllNationalities() {
        ArrayList<NationalityModel> nationalityModelList = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_NATIONALITY + " where " + isActive + "= 1 order by " + ID + " ASC";
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    NationalityModel nationalityModel = new NationalityModel();
                    boolean b = cursor.getString(cursor.getColumnIndex(isActive)).equals("1");
                    nationalityModel.setActive(b);
                    nationalityModel.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    nationalityModel.setTitle(cursor.getString(cursor.getColumnIndex(title)));
                    nationalityModel.setCreatedOn(cursor.getString(cursor.getColumnIndex(createdOn)));
                    nationalityModel.setModifiedOn(cursor.getString(cursor.getColumnIndex(modifiedOn)));
                    nationalityModelList.add(nationalityModel);
                }while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return nationalityModelList;
    }

    public boolean insertNationality(List<NationalityModel> modelList){
        int counter = 0;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            for (NationalityModel model : modelList) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(ID, model.getId());
                contentValues.put(title, model.getTitle());
                contentValues.put(isActive, model.isActive());
                contentValues.put(createdOn, model.getCreatedOn());
                contentValues.put(modifiedOn, model.getModifiedOn());
                long i = DB.insert(TABLE_NATIONALITY, null, contentValues);
                if(i > 0){
                    counter++;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return counter == modelList.size();
    }

    public boolean updateNationality(List<NationalityModel> modelList){
        int counter = 0;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            for (NationalityModel model : modelList) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(ID, model.getId());
                contentValues.put(title, model.getTitle());
                contentValues.put(isActive, model.isActive());
                contentValues.put(createdOn, model.getCreatedOn());
                contentValues.put(modifiedOn, model.getModifiedOn());
                long i = DB.update(TABLE_NATIONALITY,  contentValues,ID + "=" + model.getId(),null);
                if(i > 0){
                    counter++;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return counter == modelList.size();
    }

    public void insertTempNationalityTable() {
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            String insertQuery = "Insert into " + TABLE_NATIONALITY + " ("
                    + ID + ","
                    + title + ","
                    + isActive + ","
                    + createdOn + ","
                    + modifiedOn + ") VALUES ";

            insertQuery += "(1,'Pakistani',1,'25-6-2022T03:29:00','25-6-2022T03:29:00'),";
            insertQuery += "(2,'Non-Pakistani',1,'25-6-2022T03:29:00','25-6-2022T03:29:00')";
            DB.execSQL(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dropTempNationalityTable() {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        String query = "Drop table if exists " + TABLE_NATIONALITY;
        try {
            DB.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long addNationality(NationalityModel nm) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(ID, nm.getId());
            values.put(title, nm.getTitle());
            values.put(createdOn, nm.getCreatedOn());
            values.put(modifiedOn, nm.getModifiedOn());
            values.put(isActive, nm.isActive());

            long i = DB.insert(TABLE_NATIONALITY, null, values);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public long updateNationality(NationalityModel nm) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(ID, nm.getId());
            values.put(title, nm.getTitle());
            values.put(createdOn, nm.getCreatedOn());
            values.put(modifiedOn, nm.getModifiedOn());
            values.put(isActive, nm.isActive());

            long i = DB.update(TABLE_NATIONALITY, values, ID + " = " + nm.getId(), null);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    //Elective Subjects Table
    public final String TABLE_ELECTIVE_SUBJECTS = "ElectiveSubjects";

    public String CREATE_TABLE_ELECTIVE_SUBJECTS = "CREATE TABLE IF NOT EXISTS " + TABLE_ELECTIVE_SUBJECTS + " (" +
            ID + " INTEGER PRIMARY KEY," +
            title + " TEXT ," +
            isActive + " Boolean ," +
            createdOn + " TEXT ," +
            modifiedOn + " TEXT" +
            ");";

    public ArrayList<ElectiveSubjectModel> getAllElectiveSubjects() {
        ArrayList<ElectiveSubjectModel> electiveSubjectModelList = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_ELECTIVE_SUBJECTS + " where " + isActive + "= 1 order by " + ID + " ASC";
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    ElectiveSubjectModel electiveSubjectModel = new ElectiveSubjectModel();
                    boolean b = cursor.getString(cursor.getColumnIndex(isActive)).equals("1");
                    electiveSubjectModel.setActive(b);
                    electiveSubjectModel.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    electiveSubjectModel.setTitle(cursor.getString(cursor.getColumnIndex(title)));
                    electiveSubjectModel.setCreatedOn(cursor.getString(cursor.getColumnIndex(createdOn)));
                    electiveSubjectModel.setModifiedOn(cursor.getString(cursor.getColumnIndex(modifiedOn)));
                    electiveSubjectModelList.add(electiveSubjectModel);
                }while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return electiveSubjectModelList;
    }

    public boolean insertElectiveSubjects(List<ElectiveSubjectModel> modelList){
        int counter = 0;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            for (ElectiveSubjectModel model : modelList) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(ID, model.getId());
                contentValues.put(title, model.getTitle());
                contentValues.put(isActive, model.isActive());
                contentValues.put(createdOn, model.getCreatedOn());
                contentValues.put(modifiedOn, model.getModifiedOn());
                long i = DB.insert(TABLE_ELECTIVE_SUBJECTS, null, contentValues);
                if(i > 0){
                    counter++;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return counter == modelList.size();
    }

    public boolean updateElectiveSubjects(List<ElectiveSubjectModel> modelList){
        int counter = 0;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            for (ElectiveSubjectModel model : modelList) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(ID, model.getId());
                contentValues.put(title, model.getTitle());
                contentValues.put(isActive, model.isActive());
                contentValues.put(createdOn, model.getCreatedOn());
                contentValues.put(modifiedOn, model.getModifiedOn());
                long i = DB.update(TABLE_ELECTIVE_SUBJECTS,  contentValues,ID + "=" + model.getId(),null);
                if(i > 0){
                    counter++;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return counter == modelList.size();
    }

    public void insertTempElectiveSubjects() {
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            String insertQuery = "Insert into " + TABLE_ELECTIVE_SUBJECTS + " ("
                    + ID + ","
                    + title + ","
                    + isActive + ","
                    + createdOn + ","
                    + modifiedOn + ") VALUES ";

            insertQuery += "(1,'Biology',1,'25-6-2022T03:29:00','25-6-2022T03:29:00'),";
            insertQuery += "(2,'Computer',1,'25-6-2022T03:29:00','25-6-2022T03:29:00')";
            DB.execSQL(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dropTempElectiveSubjectsTable() {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        String query = "Drop table if exists " + TABLE_ELECTIVE_SUBJECTS;
        try {
            DB.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long addElectiveSubject(ElectiveSubjectModel esm) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(ID, esm.getId());
            values.put(title, esm.getTitle());
            values.put(createdOn, esm.getCreatedOn());
            values.put(modifiedOn, esm.getModifiedOn());
            values.put(isActive, esm.isActive());

            long i = DB.insert(TABLE_ELECTIVE_SUBJECTS, null, values);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public long updateElectiveSubject(ElectiveSubjectModel esm) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(ID, esm.getId());
            values.put(title, esm.getTitle());
            values.put(createdOn, esm.getCreatedOn());
            values.put(modifiedOn, esm.getModifiedOn());
            values.put(isActive, esm.isActive());

            long i = DB.update(TABLE_ELECTIVE_SUBJECTS, values, ID + " = " + esm.getId(), null);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }
}
