package com.tcf.sma.Survey.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tcf.sma.Models.UserModel;

public class User {

    //private variables
    String _username;
    String _password;
    String _status;
//    String _isSignedIn;

    // Empty constructor
    public User() {

    }

    // constructor
    public User(String username, String password, String status) {
        this._username = username;
        this._password = password;
        this._status = status;
        //   this._isSignedIn = signin;
    }

    // getting name
    public String getUsername() {

        return "admin";
    }


    /*public UserModel getCurrentLoggedInUser() {
        Cursor cursor = null;
        UserModel um = null;

        try {
            String selectQuery = "SELECT username FROM " + TABLE_USERS +
                    " ORDER BY id DESC";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                um = new UserModel();

                um.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                um.setFirstname(cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME)));
                um.setLastname(cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)));
                um.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
                um.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
                um.setDesignation(cursor.getString(cursor.getColumnIndex(KEY_DESIGNATION)));
                um.setRole(cursor.getString(cursor.getColumnIndex(KEY_ROLE)));
                um.setRoleId(cursor.getInt(cursor.getColumnIndex(KEY_ROLE_ID)));
                um.setDefault_school_id(cursor.getInt(cursor.getColumnIndex(KEY_DEFAULT_SCHOOL_ID)));
                um.setDepartment_Id(cursor.getInt(cursor.getColumnIndex(KEY_DEPARTMENT_ID)));
                um.setUsername(cursor.getString(cursor.getColumnIndex(KEY_USERNAME)));
                um.setLastpassword(cursor.getString(cursor.getColumnIndex(KEY_LAST_PASSWORD)));
                um.setLastlogin_time(cursor.getString(cursor.getColumnIndex(KEY_LAST_LOGIN_TIME)));
                um.setMetadata_sync_on(cursor.getString(cursor.getColumnIndex(KEY_LAST_METADATA_SYNC_ON)));
                um.setSession_token(cursor.getString(cursor.getColumnIndex(KEY_SESSION_TOKEN)));
                um.setLastpassword_3(cursor.getString(cursor.getColumnIndex(KEY_LAST_PASSWORD_3)));
                um.setLastpassword_2(cursor.getString(cursor.getColumnIndex(KEY_LAST_PASSWORD_2)));
                um.setLastpassword_1(cursor.getString(cursor.getColumnIndex(KEY_LAST_PASSWORD_1)));
                um.setPassword_change_on_login(cursor.getInt(cursor.getColumnIndex(KEY_PASSWORD_CHANGE_ON_LOGIN)));
                return um;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }*/

    // setting name
    public void setUsername(String username) {
        this._username = username;
    }

//    public String get_isSignedIn() {
//		return _isSignedIn;
//	}
//    
//	public void set_isSignedIn(String _isSignedIn) {
//		this._isSignedIn = _isSignedIn;
//	}

    // getting name
    public String getPassword() {
        return this._password;
    }

    // setting name
    public void setPassword(String password) {
        this._password = password;
    }

    public String getStatus() {
        return this._status;
    }

    // setting name
    public void setStatus(String status) {
        this._status = status;
    }

}
