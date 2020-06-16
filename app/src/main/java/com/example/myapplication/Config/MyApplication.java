package com.example.myapplication.Config;

import android.app.Application;
import android.content.Context;


public class MyApplication extends Application {

    public static final String TAG = MyApplication.class
            .getSimpleName();

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
    public static Context getAppContext()
    {
        return mInstance.getApplicationContext();
    }
    public static synchronized MyApplication getInstance() {
        return mInstance;
    }


}