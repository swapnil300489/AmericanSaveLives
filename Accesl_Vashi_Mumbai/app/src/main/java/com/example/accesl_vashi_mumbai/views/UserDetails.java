package com.example.accesl_vashi_mumbai.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.accesl_vashi_mumbai.R;
import com.example.accesl_vashi_mumbai.database.DatabaseHandler;
import com.example.accesl_vashi_mumbai.pojo.User;

import java.util.Calendar;

public class UserDetails extends AppCompatActivity {

    private TextView id_email_text, id_age_text, id_address_text, id_addBtn_text, id_dob_ed_text, id_goto_list;
    private EditText id_name_ed_text, id_contact_ed_text;
    private String email, password, address, name, contact, DOB, age;
    private int mYear, mMonth, mDay;
    private User user;
    private DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        init();

        id_goto_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUserList();
            }
        });


        id_addBtn_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 name       = id_name_ed_text.getText().toString().trim();
                 contact    = id_contact_ed_text.getText().toString().trim();


                 if (name.isEmpty()){

                     id_name_ed_text.requestFocus();
                     id_name_ed_text.setError("Enter Name");

                 }else if(contact.isEmpty()){

                     id_contact_ed_text.requestFocus();
                     id_contact_ed_text.setError("Enter Contact");

                 }else if(email.isEmpty()){

                     id_email_text.requestFocus();
                     id_email_text.setError("Enter Email");

                 }else if(DOB.isEmpty()){

                     id_dob_ed_text.requestFocus();
                     id_dob_ed_text.setError("Enter Date Of Birth");

                 }else if(age.isEmpty()){

                     id_age_text.requestFocus();
                     id_age_text.setError("Enter Age");

                 }else if(address.isEmpty()){

                     id_address_text.requestFocus();
                     id_address_text.setError("Enter Address");

                 }else {

                     user.setName(name);
                     user.setContact(contact);
                     user.setEmail(email);
                     user.setPassword(password);
                     user.setDOB(DOB);
                     user.setAge(age);
                     user.setAddress(address);

                     db.addUser(user);


                     gotoUserList();

                 }

            }
        });



        id_dob_ed_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_dob_ed_text.setText("");
                id_age_text.setText("");
                age = "";
                DOB = "";
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(UserDetails.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                DOB = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                id_dob_ed_text.setText(DOB);
                                age = getAge( year,  (monthOfYear + 1),  dayOfMonth);
                                id_age_text.setText(age + " Year Old");

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();


            }
        });

    }

    private void gotoUserList() {

        Intent intent = new Intent(UserDetails.this, UserList_Screen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private String getAge(int year, int month, int day) {

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;


    }

    private void init() {

        id_email_text      = findViewById(R.id.id_email_text);
        id_age_text        = findViewById(R.id.id_age_text);
        id_address_text    = findViewById(R.id.id_address_text);
        id_addBtn_text     = findViewById(R.id.id_addBtn_text);
        id_dob_ed_text     = findViewById(R.id.id_dob_ed_text);
        id_name_ed_text    = findViewById(R.id.id_name_ed_text);
        id_contact_ed_text = findViewById(R.id.id_contact_ed_text);
        id_goto_list       = findViewById(R.id.id_goto_list);

        db = new DatabaseHandler(this);
        user = new User();

        email    = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        address  = getIntent().getStringExtra("address");

        id_email_text.setText(email);
        id_address_text.setText(address);

    }
}
