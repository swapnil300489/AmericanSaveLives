package com.example.accesl_vashi_mumbai.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.accesl_vashi_mumbai.R;
import com.example.accesl_vashi_mumbai.adapter.UserList_Adapter;
import com.example.accesl_vashi_mumbai.database.DatabaseHandler;
import com.example.accesl_vashi_mumbai.pojo.User;

import java.util.List;

public class UserList_Screen extends AppCompatActivity {

    private TextView id_text_nodata_found;
    private RecyclerView id_rc;
    private UserList_Adapter adapter;
    private DatabaseHandler db;
    private User user;

    public UserList_Screen() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list__screen);

        init();
    }

    private void init() {

        id_text_nodata_found = findViewById(R.id.id_text_nodata_found);
        id_rc = findViewById(R.id.id_rc);
        id_rc.setLayoutManager(new LinearLayoutManager(this));

        db = new DatabaseHandler(this);

        getUserList();

    }

    private void getUserList() {


        List<User> userID = db.getAllUserList();

        if (userID.size() == 0){
            id_text_nodata_found.setVisibility(View.VISIBLE);
            id_rc.setVisibility(View.GONE);
        }else {

            id_text_nodata_found.setVisibility(View.GONE);
            id_rc.setVisibility(View.VISIBLE);

            adapter = new UserList_Adapter(this, userID);
            id_rc.setAdapter(adapter);

        }


    }
}
