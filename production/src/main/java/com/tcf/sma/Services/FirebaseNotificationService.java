package com.tcf.sma.Services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tcf.sma.DataSync.DataSync;
import com.tcf.sma.Models.AppModel;

/**
 * Created by Zubair Soomro on 1/26/2017.
 */

public class FirebaseNotificationService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }


        if (remoteMessage.getMessageType().equals("/topics/updateMeta")) {
            DataSync ds = new DataSync(getApplicationContext());
            ds.syncMetaData(getApplicationContext(), AppModel.getInstance().getSelectedSchool(getApplicationContext()));
        }


    }
}
