package com.tcf.sma.Interfaces;

/**
 * Created by Zubair Soomro on 12/16/2016.
 */

public interface IHttpRequester {
    void onRequestSuccess(String json, int request_code);

    void onRequestFailure(int error_code, int request_code);
}
