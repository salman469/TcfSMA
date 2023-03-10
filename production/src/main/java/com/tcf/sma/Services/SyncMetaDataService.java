package com.tcf.sma.Services;

import android.content.Context;
import android.os.AsyncTask;

import com.tcf.sma.HttpServices.HttpConnectionClass;

/**
 * Created by Mohammad.Haseeb on 2/28/2017.
 */

public class SyncMetaDataService extends AsyncTask<String, Void, Integer> {
    HttpConnectionClass connectionClass;
    Context context;

    public SyncMetaDataService(Context context) {
        this.context = context;

    }


    @Override
    protected Integer doInBackground(String... params) {


        return 200;
    }


    @Override
    protected void onPostExecute(Integer responseCode) {
        super.onPostExecute(responseCode);

//        if (responseCode == 200)
//            ((DashboardActivity) context).onRequestSuccess(HttpConnectionClass.responseJson, AppConstants.CODE_GET_METADATA);
//
//        else
//            ((DashboardActivity) context).onRequestFailure(responseCode, AppConstants.CODE_GET_METADATA);

    }
}


