package com.tcf.sma;

import androidx.multidex.MultiDexApplication;

public class CustomAppplicationClass extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
//        new ANRWatchDog().start();
//        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread thread, Throwable e) {
//                e.printStackTrace();
//            }
//        });
    }
}
