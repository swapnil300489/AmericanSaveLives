package com.example.accesl_vashi_mumbai.adapter;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accesl_vashi_mumbai.R;
import com.example.accesl_vashi_mumbai.pojo.User;
import com.example.accesl_vashi_mumbai.views.UserList_Screen;

import java.util.List;

public class UserList_Adapter extends RecyclerView.Adapter<UserList_Adapter.UserList_Holder>{

    private Context context;
    private List<User> list;

    public UserList_Adapter(Context context, List<User> list) {

        this.context = context;
        this.list = list;

    }


    @NonNull
    @Override
    public UserList_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserList_Holder(LayoutInflater.from(context).inflate(R.layout.user_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserList_Holder holder, int position) {

        holder.id_name.setText(list.get(position).getName());
        holder.id_email.setText(list.get(position).getEmail());
        holder.id_contact.setText(list.get(position).getContact());
        holder.id_address.setText(list.get(position).getAddress());
        holder.id_address.setMovementMethod(new ScrollingMovementMethod());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserList_Holder extends RecyclerView.ViewHolder {

        TextView id_name, id_email, id_contact, id_address;

        public UserList_Holder(@NonNull View itemView) {
            super(itemView);

            id_name     = itemView.findViewById(R.id.id_name);
            id_email    = itemView.findViewById(R.id.id_email);
            id_contact  = itemView.findViewById(R.id.id_contact);
            id_address  = itemView.findViewById(R.id.id_address);



        }
    }
}
