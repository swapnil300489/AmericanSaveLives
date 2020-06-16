package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.myapplication.Config.KEY;
import com.example.myapplication.Config.Utility;
import com.example.myapplication.userView.MapsActivity;

public class SplashScreen extends AppCompatActivity {

    private Intent mainIntent;
    private Utility utility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        utility = new Utility(getApplicationContext());


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(utility.getLoginPreferences(KEY.USER_LOGIN)){

                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }else {
                    Intent intent = new Intent(getApplicationContext(), UserLogin_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        }, 3000);


    }
}
