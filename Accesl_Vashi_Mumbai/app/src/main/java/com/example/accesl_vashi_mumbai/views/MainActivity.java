package com.example.accesl_vashi_mumbai.views;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.accesl_vashi_mumbai.R;
import com.example.accesl_vashi_mumbai.permission.PermissionUtility;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText id_name_ed_text2, id_name_ed_text3;
    private Button id_loginBtn;
    private String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    private PermissionUtility putility;
    private ArrayList<String> permission_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        runTimePermission();
        init();

        id_loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email    = id_name_ed_text2.getText().toString().trim();
                String password = id_name_ed_text3.getText().toString().trim();


                if (email.isEmpty()){
                    id_name_ed_text2.requestFocus();
                    id_name_ed_text2.setError("Enter Email Id");
                }else if(!email.matches(regex)){
                    id_name_ed_text2.requestFocus();
                    id_name_ed_text2.setError("Enter valid email");
                }else if(password.isEmpty()) {
                    id_name_ed_text3.requestFocus();
                    id_name_ed_text3.setError("Enter password");
                }else {
                    login(email, password);
                }

            }
        });




    }

    private void login(String email, String password) {


        Intent intent = new Intent(MainActivity.this,MyLocation.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        startActivity(intent);


    }

    private void init() {

        id_name_ed_text2 = findViewById(R.id.id_name_ed_text2);
        id_name_ed_text3 = findViewById(R.id.id_name_ed_text3);
        id_loginBtn = findViewById(R.id.id_loginBtn);

    }


    private void runTimePermission() {

        putility = new PermissionUtility(this);
        permission_list = new ArrayList<String>();

        permission_list.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permission_list.add(Manifest.permission.ACCESS_FINE_LOCATION);

        putility.setListner(new PermissionUtility.OnPermissionCallback() {
            @Override
            public void OnComplete(boolean is_granted) {
                Log.i("OnPermissionCallback", "is_granted = " + is_granted);
                if (is_granted) {

                } else {
                    putility.checkPermission(permission_list);
                }
            }
        });
        putility.checkPermission(permission_list);
    }
}
