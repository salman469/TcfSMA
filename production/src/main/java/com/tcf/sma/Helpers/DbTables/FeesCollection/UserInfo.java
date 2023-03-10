package com.tcf.sma.Helpers.DbTables.FeesCollection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.UserModel;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class UserInfo {

    public static final String TABLE_USERS = "user";
    public static final String KEY_ID = "id";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_STATUS = "status";
    public static final String KEY_DESIGNATION = "designation";
    public static final String KEY_ROLE = "role";
    public static final String KEY_ROLE_ID = "role_Id";
    public static final String KEY_DEPARTMENT_ID = "department_Id";
    public static final String KEY_DEFAULT_SCHOOL_ID = "default_school_id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_LAST_PASSWORD = "last_password";
    public static final String KEY_LAST_PASSWORD_1 = "LastPassword_1";
    public static final String KEY_LAST_PASSWORD_2 = "LastPassword_2";
    public static final String KEY_LAST_PASSWORD_3 = "LastPassword_3";
    public static final String KEY_PASSWORD_CHANGE_ON_LOGIN = "password_change_onlogin";
    public static final String KEY_PASSWORD_CHANGE_ON_DATETIME = "password_change_on";
    public static final String KEY_LAST_LOGIN_TIME = "last_login";
    public static final String KEY_LAST_METADATA_SYNC_ON = "last_metadata_sync";
    public static final String KEY_SESSION_TOKEN = "token";
    public static final String KEY_FCM_TOKEN = "fcmtoken";
    public static final String KEY_LAST_UPLOADED_ON = "last_uploaded_on";
    private static UserInfo userInfoInstance = null;
    String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + " ("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_FIRST_NAME + " VARCHAR,"
            + KEY_LAST_NAME + " VARCHAR,"
            + KEY_EMAIL + " VARCHAR,"
            + KEY_STATUS + " VARCHAR,"
            + KEY_DESIGNATION + " VARCHAR,"
            + KEY_ROLE + " VARCHAR,"
            + KEY_ROLE_ID + " INTEGER,"
            + KEY_DEFAULT_SCHOOL_ID + " INTEGER,"
            + KEY_DEPARTMENT_ID + " INTEGER,"
            + KEY_USERNAME + " VARCHAR,"
            + KEY_LAST_PASSWORD + " VARCHAR,"
            + KEY_LAST_LOGIN_TIME + " VARCHAR,"
            + KEY_LAST_METADATA_SYNC_ON + " VARCHAR,"
            + KEY_SESSION_TOKEN + " VARCHAR,"
            + KEY_FCM_TOKEN + " VARCHAR,"
            + KEY_LAST_PASSWORD_1 + " VARCHAR,"
            + KEY_LAST_PASSWORD_2 + " VARCHAR,"
            + KEY_LAST_PASSWORD_3 + " VARCHAR,"
            + KEY_PASSWORD_CHANGE_ON_DATETIME + " VARCHAR,"
            + KEY_PASSWORD_CHANGE_ON_LOGIN + " INTEGER"
            + ")";
    private Context context;
    private ArrayList<String> last_passwords = new ArrayList<>();

    private UserInfo(Context context) {
        this.context = context;
    }

    public static UserInfo getInstance(Context context) {
        if (userInfoInstance == null) {
            userInfoInstance = new UserInfo(context.getApplicationContext());
        }
        return userInfoInstance;
    }

    public static String encryptPassword(String password) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sha1;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public long changePassword(String newPassword, int id) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        Cursor cursor = null;

        long i = -1;
        try {
            String lastPass_1, lastPass_2, lastPass_3, lastPass;
            ContentValues values = new ContentValues();
            String current_date = AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss");

            String sqlquerry = "Select * from " + TABLE_USERS + " where " + KEY_ID + "=" + id;
            cursor = DB.rawQuery(sqlquerry, null);

            if (cursor.moveToFirst()) {

                lastPass = cursor.getString(cursor.getColumnIndex(KEY_LAST_PASSWORD));
                lastPass_1 = cursor.getString(cursor.getColumnIndex(KEY_LAST_PASSWORD_1));
                lastPass_2 = cursor.getString(cursor.getColumnIndex(KEY_LAST_PASSWORD_2));
                lastPass_3 = cursor.getString(cursor.getColumnIndex(KEY_LAST_PASSWORD_3));


//                values.put(KEY_LAST_PASSWORD, newPassword);
//                values.put(KEY_LAST_PASSWORD_3, lastPass);
//                values.put(UserInfo.KEY_LAST_UPLOADED_ON, SurveyAppModel.getInstance().getDateTime());
//                values.put(UserInfo.KEY_PASSWORD_CHANGE_ON_DATETIME, current_date);

                if (cursor.isNull(cursor.getColumnIndex(KEY_LAST_PASSWORD_3))) {
                    values.put(KEY_LAST_PASSWORD_3, lastPass);
                    values.put(KEY_LAST_PASSWORD, AppModel.getInstance().encryptPassword(newPassword));
                    values.put(KEY_PASSWORD_CHANGE_ON_DATETIME, current_date);
                    values.put(UserInfo.KEY_LAST_UPLOADED_ON, current_date);
                } else if (cursor.isNull(cursor.getColumnIndex(KEY_LAST_PASSWORD_2))) {
                    values.put(KEY_LAST_PASSWORD_2, lastPass_3);
                    values.put(KEY_LAST_PASSWORD_3, lastPass);
                    values.put(KEY_LAST_PASSWORD, AppModel.getInstance().encryptPassword(newPassword));
                    values.put(KEY_PASSWORD_CHANGE_ON_DATETIME, current_date);
                    values.put(UserInfo.KEY_LAST_UPLOADED_ON, current_date);
                } else {
                    values.put(KEY_LAST_PASSWORD_1, lastPass_2);
                    values.put(KEY_LAST_PASSWORD_2, lastPass_3);
                    values.put(KEY_LAST_PASSWORD_3, lastPass);
                    values.put(KEY_LAST_PASSWORD, AppModel.getInstance().encryptPassword(newPassword));
                    values.put(KEY_PASSWORD_CHANGE_ON_DATETIME, current_date);
                    values.put(UserInfo.KEY_LAST_UPLOADED_ON, current_date);
                }


//                if (cursor.isNull(cursor.getColumnIndex(KEY_LAST_PASSWORD_1))) {
//                    values.put(KEY_LAST_PASSWORD, newPassword);
//                    values.put(KEY_LAST_PASSWORD_1, newPassword);
//                    values.put(KEY_LAST_PASSWORD_2, lastPass);
//                    values.put(KEY_PASSWORD_CHANGE_ON_DATETIME, current_date);
//                } else if (cursor.isNull(cursor.getColumnIndex(KEY_LAST_PASSWORD_2))) {
//                    values.put(KEY_LAST_PASSWORD, newPassword);
//                    values.put(KEY_LAST_PASSWORD_1, newPassword);
//                    values.put(KEY_LAST_PASSWORD_2, lastPass_1);
//                    values.put(KEY_PASSWORD_CHANGE_ON_DATETIME, current_date);
//                } else {
//                    values.put(KEY_LAST_PASSWORD, newPassword);
//                    values.put(KEY_LAST_PASSWORD_1, newPassword);
//                    values.put(KEY_LAST_PASSWORD_2, lastPass_1);
//                    values.put(KEY_LAST_PASSWORD_3, lastPass_2);
//                    values.put(KEY_PASSWORD_CHANGE_ON_DATETIME, current_date);
//                }

                return i = DB.update(TABLE_USERS, values, KEY_ID + "=" + id, null);
            }


        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return i;
    }

    public ArrayList<String> getPreviousPasswords(int id) {
        last_passwords.clear();
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor cursor = null;
        try {
            String sqlquerry = "Select * from " + TABLE_USERS + " where " + KEY_ID + "=" + id;
            cursor = db.rawQuery(sqlquerry, null);

            if (cursor.moveToFirst()) {
                last_passwords.add(cursor.getString(cursor.getColumnIndex(KEY_LAST_PASSWORD)));

                if (cursor.isNull(cursor.getColumnIndex(KEY_LAST_PASSWORD_1))) {
                    last_passwords.add("");
                } else {
                    last_passwords.add(cursor.getString(cursor.getColumnIndex(KEY_LAST_PASSWORD_1)));
                }

                if (cursor.isNull(cursor.getColumnIndex(KEY_LAST_PASSWORD_2))) {
                    last_passwords.add("");
                } else {
                    last_passwords.add(cursor.getString(cursor.getColumnIndex(KEY_LAST_PASSWORD_2)));
                }

                if (cursor.isNull(cursor.getColumnIndex(KEY_LAST_PASSWORD_3))) {
                    last_passwords.add("");
                } else {
                    last_passwords.add(cursor.getString(cursor.getColumnIndex(KEY_LAST_PASSWORD_3)));
                }
            }
            return last_passwords;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public List<UserModel> getUserData(int id) {
        List<UserModel> userList = new ArrayList<>();
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor cursor = null;
        try {
            String sqlquerry = "Select * from " + TABLE_USERS + " where " + KEY_ID + "=" + id;

//            String sqlquerry = "Select " + KEY_LAST_PASSWORD +
//                    "," + KEY_LAST_PASSWORD_1 +
//                    "," + KEY_LAST_PASSWORD_2 +
//                    "," + KEY_LAST_PASSWORD_3 +
//                    "," + KEY_PASSWORD_CHANGE_ON_DATETIME +
//                    "," + KEY_PASSWORD_CHANGE_ON_LOGIN +
//                    "," + KEY_LAST_UPLOADED_ON+
//                    " from " + TABLE_USERS + " where " + KEY_ID + "=" + id;
            cursor = db.rawQuery(sqlquerry, null);

            if (cursor.moveToFirst()) {
                UserModel um = new UserModel();

                um.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                um.setFirstname(cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME)));
                um.setLastname(cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)));
                um.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
                um.setUsername(cursor.getString(cursor.getColumnIndex(KEY_USERNAME)));
                um.setLastpassword(cursor.getString(cursor.getColumnIndex(KEY_LAST_PASSWORD)));
                um.setLastpassword_1(cursor.getString(cursor.getColumnIndex(KEY_LAST_PASSWORD_1)));
                um.setLastpassword_2(cursor.getString(cursor.getColumnIndex(KEY_LAST_PASSWORD_2)));
                um.setLastpassword_3(cursor.getString(cursor.getColumnIndex(KEY_LAST_PASSWORD_3)));
                um.setPassword_change_on(cursor.getString(cursor.getColumnIndex(KEY_PASSWORD_CHANGE_ON_DATETIME)));
                um.setPassword_change_on_login(cursor.getInt(cursor.getColumnIndex(KEY_PASSWORD_CHANGE_ON_LOGIN)));
                um.setLast_uploaded_on(cursor.getString(cursor.getColumnIndex(KEY_LAST_UPLOADED_ON)));

                um.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
                um.setDesignation(cursor.getString(cursor.getColumnIndex(KEY_DESIGNATION)));
                um.setRole(cursor.getString(cursor.getColumnIndex(KEY_ROLE)));
                um.setRoleId(cursor.getInt(cursor.getColumnIndex(KEY_ROLE_ID)));
                um.setDefault_school_id(cursor.getInt(cursor.getColumnIndex(KEY_DEFAULT_SCHOOL_ID)));
                um.setDepartment_Id(cursor.getInt(cursor.getColumnIndex(KEY_DEPARTMENT_ID)));
                um.setLastlogin_time(cursor.getString(cursor.getColumnIndex(KEY_LAST_LOGIN_TIME)));
                um.setMetadata_sync_on(cursor.getString(cursor.getColumnIndex(KEY_LAST_METADATA_SYNC_ON)));
                um.setSession_token(cursor.getString(cursor.getColumnIndex(KEY_SESSION_TOKEN)));

                userList.add(um);
            }
            return userList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public long setPasswordChangeOnLogin(int value, int id) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        ContentValues values = new ContentValues();

        try {
            values.put(KEY_PASSWORD_CHANGE_ON_LOGIN, value);
            int i = db.update(TABLE_USERS, values, KEY_ID + "=" + id, null);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getPasswordChangeOnLogin(int id) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor cursor = null;
        int i = -1;
        try {
            String sqlquerry = "Select " + KEY_PASSWORD_CHANGE_ON_LOGIN +
                    " from " + TABLE_USERS + " where " + KEY_ID + "=" + id;
            cursor = db.rawQuery(sqlquerry, null);

            if (cursor.moveToFirst()) {
                return i = cursor.getInt(cursor.getColumnIndex(KEY_PASSWORD_CHANGE_ON_LOGIN));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return i;
    }

//    private String convertToHex(byte[] data) {
//        StringBuilder buf = new StringBuilder();
//        for (byte b : data) {
//            int halfbyte = (b >>> 4) & 0x0F;
//            int two_halfs = 0;
//            do {
//                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
//                halfbyte = b & 0x0F;
//            } while (two_halfs++ < 1);
//        }
//        return buf.toString();
//    }
//
//    public String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
//        MessageDigest md = MessageDigest.getInstance("SHA-1");
//        byte[] textBytes = text.getBytes("iso-8859-1");
//        md.update(textBytes, 0, textBytes.length);
//        byte[] sha1hash = md.digest();
//        return convertToHex(sha1hash);
//    }

    public String dateOfPasswordChange(int id) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor cursor = null;
        String date = "";
        try {
            String sqlquerry = "Select " + KEY_PASSWORD_CHANGE_ON_DATETIME +
                    " from " + TABLE_USERS + " where " + KEY_ID + "=" + id;
            cursor = db.rawQuery(sqlquerry, null);

            if (cursor.moveToFirst()) {
                date = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD_CHANGE_ON_DATETIME));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return date;
    }

    public void updateMethod(ContentValues values, String userId) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        try {
            int id = db.update(UserInfo.TABLE_USERS, values, UserInfo.KEY_ID + " = " + userId, null);
            Log.d("changePassword", "" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
