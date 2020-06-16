package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Config.KEY;
import com.example.myapplication.Config.Utility;
import com.example.myapplication.Retrofit.APIClient;
import com.example.myapplication.Retrofit.APIInterface;
import com.example.myapplication.pojo.Login;
import com.example.myapplication.userView.MapsActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLogin_Activity extends AppCompatActivity {

    private TextView id_login_txt, id_Forgotpassword_txt, id_createAccount_txt;
    private EditText id_email_edTxt, id_password_edTxt;
    private APIInterface apiInterface;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9]+\\.+[a-z]+";
    private String userType = "3";
    private ProgressDialog progressBar;
    private Utility utility;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        id_login_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email        = id_email_edTxt.getText().toString();
                String password     = id_password_edTxt.getText().toString();


                if (email.length() == 0){
                    id_email_edTxt.requestFocus();
                    id_email_edTxt.setError("Enter email id");

                }else if(!email.matches(emailPattern)){
                    id_email_edTxt.requestFocus();
                    id_email_edTxt.setError("Enter valid email id");

                }else if(password.length() == 0){
                    id_password_edTxt.requestFocus();
                    id_password_edTxt.setError("Enter password");
                }else {

                    login(email, password, userType);
                }

            }
        });



        id_createAccount_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(UserLogin_Activity.this, UserRegsiter_Activity.class));
            }
        });

    }

    private void login(String email, String password, String userType) {
        progressBar.setMessage("Please wait ...");
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();


        Call<Login> loginCall = apiInterface.LOGIN_CALL(email, password, userType);

        loginCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                progressBar.dismiss();
                Login login = response.body();
                List<Login.Detail> details;
                if (login.getMessageCode() == 1){

                    details = login.getDetails();

                    utility.setLoginPreferences(KEY.USER_LOGIN, true);
                    utility.setPreferences(KEY.USER_ID, details.get(0).getId());
                    utility.setPreferences(KEY.USER_NAME, details.get(0).getName());
                    utility.setPreferences(KEY.USER_CONTACT, details.get(0).getMobileNo());
                    utility.setPreferences(KEY.USER_EMAIL, details.get(0).getEmail());

                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);


                }else {

                    Toast.makeText(getApplicationContext(), "Something went wrong, please try again.", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

                progressBar.dismiss();
                Toast.makeText(getApplicationContext(), "Please check network.", Toast.LENGTH_LONG).show();

            }
        });







    }

    private void init() {

        id_login_txt            = findViewById(R.id.id_login_txt);
        id_Forgotpassword_txt   = findViewById(R.id.id_Forgotpassword_txt);
        id_createAccount_txt    = findViewById(R.id.id_createAccount_txt);

        id_email_edTxt          = findViewById(R.id.id_email_edTxt);
        id_password_edTxt       = findViewById(R.id.id_password_edTxt);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressBar = new ProgressDialog(this);

        utility = new Utility(getApplicationContext());




    }
}
