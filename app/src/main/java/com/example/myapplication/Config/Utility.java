package com.example.myapplication.Config;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class Utility {

    Context context;
    String  MY_PREFS_NAME="User";
    ProgressDialog progressDialog;


    public Utility(Context context){
        this.context = context;
    }

    public void setPreferences(String KEY,String VALUE){
        SharedPreferences settings =  context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(KEY, VALUE);
        editor.commit();

    }
    public String getPreferences(String KEY)
    {
        SharedPreferences shared = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String value = (shared.getString(KEY, ""));
        return value;
    }

    public void setLoginPreferences(String KEY,boolean VALUE){
        SharedPreferences settings =  context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(KEY, VALUE);
        editor.commit();

    }

    public boolean getLoginPreferences(String KEY)
    {
        SharedPreferences shared = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        boolean value = (shared.getBoolean(KEY, false));
        return value;
    }


}
