package com.tcf.sma.Survey.httpServices;

import android.content.Context;

public interface IHttpRequester {

    public void onRequestSuccess(String json, int request_code, Context mContext);

    public void onRequestFailure(int error_code, int request_code);


}
