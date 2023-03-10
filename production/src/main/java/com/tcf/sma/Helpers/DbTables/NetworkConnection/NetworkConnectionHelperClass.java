package com.tcf.sma.Helpers.DbTables.NetworkConnection;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.tcf.sma.DataSync.DataSync;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Managers.StorageUtil;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.NetwrokConnection.NetworkConnectionInfo;
import com.tcf.sma.Models.RetrofitModels.Help.FAQsModel;
import com.tcf.sma.Models.RetrofitModels.Help.FeedbackModel;
import com.tcf.sma.Models.RetrofitModels.Help.UploadFeedbackModel;
import com.tcf.sma.Models.RetrofitModels.Help.UserManualModel;
import com.tcf.sma.Models.SyncProgress.SyncDownloadUploadModel;
import com.tcf.sma.Models.UserModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NetworkConnectionHelperClass {

    private static NetworkConnectionHelperClass instance = null;
    private Context context;

    public NetworkConnectionHelperClass(Context context) {
        this.context = context;
    }

    public static NetworkConnectionHelperClass getInstance(Context context) {
        if (instance == null)
            instance = new NetworkConnectionHelperClass(context);

        return instance;
    }

    private final String ID = "id";
    private final String Longitude = "longitude";
    private final String Latitude = "latitude";
    private final String DeviceId = "deviceId";
    private final String SIM1 = "SIM1";
    public final String SIM2 = "SIM2";
    private final String MobileOperator = "mobileOperator";
    private final String ConnectionType = "connectionType";
    private final String DownloadSpeed = "downloadSpeed";
    private final String UploadSpeed = "uploadSpeed";
    private final String Latency = "latency";
    private final String ManufacturerModel = "manufacturerModel";
    private final String CreatedOn = "createdOn";
    public final String AppVersion = "appVersion";
    public final String IMEI = "IMEI";
    public final String IMEI2 = "IMEI2";
    public final String BatteryStats = "batteryStats";


    public String TABLE_NETWORK_CONNECTION_INFO = "NetworkConnectionInfo";
    public String CREATE_TABLE_NETWORK_CONNECTION_INFO = "CREATE TABLE IF NOT EXISTS " + TABLE_NETWORK_CONNECTION_INFO + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Longitude + " REAL, " +
            Latitude + " REAL, " +
            DeviceId + " TEXT, " +
            SIM1 + " TEXT, " +
            SIM2 + " TEXT, " +
            MobileOperator + " TEXT, " +
            ConnectionType + " TEXT, " +
            DownloadSpeed + " REAL, " +
            UploadSpeed + " REAL, " +
            Latency + " REAL, " +
            ManufacturerModel + " TEXT, " +
            CreatedOn + " TEXT, " +
            AppVersion + " TEXT, " +
            IMEI + " TEXT, " +
            IMEI2 + " TEXT, " +
            BatteryStats + " INTEGER " +
            ");";

    public void addNetworkConnectionInfo(NetworkConnectionInfo model) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            if (!FindNetworkConnectionInfo(model)){
                ContentValues values = new ContentValues();
                values.put(Longitude, model.getLongitude());
                values.put(Latitude, model.getLatitude());
                values.put(DeviceId, model.getDeviceId());
                values.put(SIM1, model.getSIM1());
                values.put(SIM2, model.getSIM2());
                values.put(MobileOperator, model.getMobileOperator());
                values.put(ConnectionType, model.getConnectionType());
                values.put(DownloadSpeed, model.getDownloadSpeed());
                values.put(UploadSpeed, model.getUploadSpeed());
                values.put(Latency, model.getLatency());
                values.put(ManufacturerModel, model.getManufacturerModel());
                values.put(CreatedOn, model.getCreatedOn());
                values.put(AppVersion, model.getAppVersion());
                values.put(IMEI, model.getIMEI());
                values.put(IMEI2, model.getIMEI2());
                values.put(BatteryStats, model.getBatteryStats());


                long i = DB.insert(TABLE_NETWORK_CONNECTION_INFO, null, values);
                if (i == -1)
                    AppModel.getInstance().appendLog(context, "Couldn't insert NetworkConnectionInfo");
                else if (i > 0) {
                    AppModel.getInstance().appendLog(context, "NetworkConnectionInfo inserted");
                }
            }

        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In addNetworkConnectionInfo Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean FindNetworkConnectionInfo(NetworkConnectionInfo model) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_NETWORK_CONNECTION_INFO
                    + " WHERE " + CreatedOn + " = '" + model.getCreatedOn() + "' AND " + DeviceId + " = '" + model.getDeviceId() + "'"
                    + " AND " + ConnectionType + " = '" + model.getConnectionType() + "'";

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

    public void deleteAllNetworkConnectionInfo() {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            long id = DB.delete(TABLE_NETWORK_CONNECTION_INFO, null, null);
            AppModel.getInstance().appendLog(context, "Network Info deleted id=" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteNetworkConnectionInfo(NetworkConnectionInfo model) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            long id = DB.delete(TABLE_NETWORK_CONNECTION_INFO, CreatedOn + " = '" + model.getCreatedOn() + "' AND "
                    + DeviceId + " = '" + model.getDeviceId() + "'"
                    + " AND " + ConnectionType + " = '" + model.getConnectionType() + "'", null);
            AppModel.getInstance().appendLog(context, "Network Info deleted id=" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<NetworkConnectionInfo> getAllNetworkInfoDataForUpload() {
        String selectQuery = "SELECT * FROM " + TABLE_NETWORK_CONNECTION_INFO;
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        List<NetworkConnectionInfo> networkConnectionInfoList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery,null);
            if (cursor.moveToFirst()){
                do {
                    NetworkConnectionInfo model = new NetworkConnectionInfo();
                    model.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setLongitude(cursor.getDouble(cursor.getColumnIndex(Longitude)));
                    model.setLatitude(cursor.getDouble(cursor.getColumnIndex(Latitude)));
                    model.setDeviceId(cursor.getString(cursor.getColumnIndex(DeviceId)));
                    model.setSIM1(cursor.getString(cursor.getColumnIndex(SIM1)));
                    model.setSIM2(cursor.getString(cursor.getColumnIndex(SIM2)));
                    model.setMobileOperator(cursor.getString(cursor.getColumnIndex(MobileOperator)));
                    model.setConnectionType(cursor.getString(cursor.getColumnIndex(ConnectionType)));
                    model.setDownloadSpeed(cursor.getDouble(cursor.getColumnIndex(DownloadSpeed)));
                    model.setUploadSpeed(cursor.getDouble(cursor.getColumnIndex(UploadSpeed)));
                    model.setLatency(cursor.getDouble(cursor.getColumnIndex(Latency)));
                    model.setManufacturerModel(cursor.getString(cursor.getColumnIndex(ManufacturerModel)));
                    model.setCreatedOn(cursor.getString(cursor.getColumnIndex(CreatedOn)));
                    model.setAppVersion(cursor.getString(cursor.getColumnIndex(AppVersion)));
                    model.setIMEI(cursor.getString(cursor.getColumnIndex(IMEI)));
                    model.setIMEI2(cursor.getString(cursor.getColumnIndex(IMEI2)));
                    model.setBatteryStats(cursor.getInt(cursor.getColumnIndex(BatteryStats)));

                    networkConnectionInfoList.add(model);
                }
                while (cursor.moveToNext());
            }

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return networkConnectionInfoList;
    }
}
