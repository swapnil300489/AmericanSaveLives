package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Retrofit.APIClient;
import com.example.myapplication.Retrofit.APIInterface;
import com.example.myapplication.pojo.Register;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegsiter_Activity extends AppCompatActivity {

    private EditText id_FullName_edTxt, id_EmailId_edTxt, id_phone_edTxt, id_password_edTxt, id_Confirmpassword_edTxt;
    private TextView id_regsiter_txt, id_AlreadyAccount_txt;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9]+\\.+[a-z]+";
    private String userType = "3";
    private APIInterface apiInterface;
    private ProgressDialog progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        init();

        id_regsiter_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name            = id_FullName_edTxt.getText().toString();
                String email           = id_EmailId_edTxt.getText().toString();
                String phone           = id_phone_edTxt.getText().toString();
                String password        = id_password_edTxt.getText().toString();
                String confrimPassword = id_Confirmpassword_edTxt.getText().toString();


                if (name.length() == 0){
                    id_FullName_edTxt.requestFocus();
                    id_FullName_edTxt.setError("Enter your name");
                }else if(email.length() == 0){
                    id_EmailId_edTxt.requestFocus();
                    id_EmailId_edTxt.setError("Enter your email id");
                }else if(!email.matches(emailPattern)){
                    id_EmailId_edTxt.requestFocus();
                    id_EmailId_edTxt.setError("Enter valid email id");
                }else if(phone.length() == 0){
                    id_phone_edTxt.requestFocus();
                    id_phone_edTxt.setError("Enter phone number");
                }else if(password.length() == 0){
                    id_password_edTxt.requestFocus();
                    id_password_edTxt.setError("Enter password");
                }else if(password.length() < 5 || password.length() > 10){
                    id_password_edTxt.requestFocus();
                    id_password_edTxt.setError("Password must be 5 to 10 character.");
                }else if(confrimPassword.length() == 0){
                    id_Confirmpassword_edTxt.requestFocus();
                    id_Confirmpassword_edTxt.setError("Enter confirm password");
                }else if(!password.equals(confrimPassword)){
                    id_password_edTxt.requestFocus();
                    id_password_edTxt.setError("Both Password should be same");
                }else {

                    regsiter(name, email, phone, password, userType);
                }
            }
        });

        id_AlreadyAccount_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserRegsiter_Activity.this, UserLogin_Activity.class));
            }
        });

    }

    private void regsiter(String name, String email, String phone, String password, String userType) {


        progressBar.setMessage("Please wait ...");
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();


        Call<Register> registerCall = apiInterface.REGISTER_CALL(name, email, phone, password, userType);

        registerCall.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {

                progressBar.dismiss();

                Register register = response.body();
                if (register.getMessageCode() == 1){

                    Toast.makeText(getApplicationContext(), register.getMessage(), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(UserRegsiter_Activity.this, UserLogin_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }else {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(getApplicationContext(), "Please try again !!!", Toast.LENGTH_LONG).show();
            }
        });



    }

    private void init() {

        id_FullName_edTxt        = findViewById(R.id.id_FullName_edTxt);
        id_EmailId_edTxt         = findViewById(R.id.id_EmailId_edTxt);
        id_phone_edTxt           = findViewById(R.id.id_phone_edTxt);
        id_password_edTxt        = findViewById(R.id.id_password_edTxt);
        id_Confirmpassword_edTxt = findViewById(R.id.id_Confirmpassword_edTxt);

        id_regsiter_txt          = findViewById(R.id.id_regsiter_txt);
        id_AlreadyAccount_txt    = findViewById(R.id.id_AlreadyAccount_txt);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        progressBar = new ProgressDialog(this);
    }
}
