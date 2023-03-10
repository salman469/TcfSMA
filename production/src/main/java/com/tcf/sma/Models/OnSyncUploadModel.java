package com.tcf.sma.Models;

import com.google.gson.annotations.SerializedName;
import com.tcf.sma.Models.RetrofitModels.UploadCashInHandModel;
import com.tcf.sma.Models.SyncProgress.AppSyncStatusMasterModel;

import java.util.ArrayList;

public class OnSyncUploadModel {

    @SerializedName("appSyncStatsMasterModels")
    ArrayList<AppSyncStatusMasterModel> appSyncStatusMasterModels;

    ArrayList<UploadCashInHandModel> cashInHandList;

    public ArrayList<AppSyncStatusMasterModel> getAppSyncStatusMasterModels() {
        return appSyncStatusMasterModels;
    }

    public void setAppSyncStatusMasterModels(ArrayList<AppSyncStatusMasterModel> appSyncStatusMasterModels) {
        this.appSyncStatusMasterModels = appSyncStatusMasterModels;
    }

    public ArrayList<UploadCashInHandModel> getCashInHandList() {
        return cashInHandList;
    }

    public void setCashInHandList(ArrayList<UploadCashInHandModel> cashInHandList) {
        this.cashInHandList = cashInHandList;
    }
}
