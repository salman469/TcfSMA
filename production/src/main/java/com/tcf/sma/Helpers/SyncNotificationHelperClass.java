package com.tcf.sma.Helpers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.SyncProgress.NotificationProgressModel;
import com.tcf.sma.R;

public class SyncNotificationHelperClass {

    private Context mContext;
    public static final String NOTIFICATION_DELETED_ACTION = "delete_notification";
    public static boolean isSyncNotificationDeleted = false;

    private static SyncNotificationHelperClass instance = null;
    private NotificationCompat.Builder notificationBuilder = null;
    private NotificationManager notificationManager = null;

    public SyncNotificationHelperClass(Context mContext) {
        this.mContext = mContext;
    }

    public static SyncNotificationHelperClass getInstance(Context context) {
        if (instance == null)
        {
            instance = new SyncNotificationHelperClass(context);
        }
        return instance;
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Do what you want here
            try {
                Log.i("noti","onReceive:"+ intent.getAction());
                if (intent.getAction().equals(SyncNotificationHelperClass.NOTIFICATION_DELETED_ACTION)) {
                    isSyncNotificationDeleted = true;
                    unregisterReceiver();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    public void unregisterReceiver(){
        try {
            mContext.unregisterReceiver(receiver);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void startSyncNotification() {
        notificationBuilder = null;
        notificationManager = null;
        if (notificationManager == null && notificationBuilder == null) {
            Intent intent = new Intent(NOTIFICATION_DELETED_ACTION);
            PendingIntent deleteIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            mContext.registerReceiver(receiver, new IntentFilter(NOTIFICATION_DELETED_ACTION));

            notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationBuilder = new NotificationCompat.Builder(mContext, AppConstants.CHANNEL_ID_Sync_Notificaton);

            notificationBuilder.setContentTitle("Syncing Progress")
                    .setOngoing(false) // if true then notification will stick and cannot be removed
                    .setContentText("Sync Started")
                    .setSmallIcon(android.R.drawable.ic_popup_sync)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher))
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setAutoCancel(true)
                    .setDeleteIntent(deleteIntent);

            AppModel.getInstance().createNotificationChannel(mContext, AppConstants.CHANNEL_ID_Sync_Notificaton, "sync", "sync", true);

            notificationManager.notify(AppConstants.SYNCNotifyId, notificationBuilder.build());
        }
    }

//    public void sendNotification(SyncDownloadUploadModel download) {
//
//        try {
//            if (notificationBuilder != null && notificationManager != null) {
//                sendIntent(download);
//                String school = download.getSchoolId() > 0 ? "School id: " + download.getSchoolId() + " " : "";
//                notificationBuilder.setProgress(100, download.getProgress(), false);
//                notificationBuilder.setContentText(school + download.getTCFModuleName() + " " +
//                        download.getSyncType() + " " + download.getCurrentFileSize() + "/" + download.getTotalFileSize());
//                notificationManager.notify(AppConstants.SYNCNotifyId, notificationBuilder.build());
//            } else {
////                startSyncNotification();
////                sendNotification(download);
//////              onSyncProgressComplete();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void sendProgressNotification(NotificationProgressModel notificationProgressModel) {

        try {
            if (notificationBuilder != null && notificationManager != null) {
//                sendIntent(notificationProgressModel);
//                String school = notificationProgressModel.getSchoolId() > 0 ? "School id: " + notificationProgressModel.getSchoolId() + " " : "";
                notificationBuilder.setProgress(100, notificationProgressModel.getProgress(), false);
                notificationBuilder.setContentText(notificationProgressModel.getTCFModuleName() +
                        " "+notificationProgressModel.getProgress() + "/100%");
                notificationManager.notify(AppConstants.SYNCNotifyId, notificationBuilder.build());
            } else {
//                startSyncNotification();
//                sendNotification(download);
////              onSyncProgressComplete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSyncNotificationProgressComplete() {
        try {
            if (notificationBuilder != null && notificationManager != null) {
                Thread.sleep(5000);
                notificationManager.cancel(AppConstants.SYNCNotifyId);
//                unregisterReceiver();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
