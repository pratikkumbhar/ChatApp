package com.example.chatapp.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Activity.ChatActivity;
import com.example.chatapp.Activity.Model.Users;
import com.example.chatapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdaptor extends RecyclerView.Adapter<UserAdaptor.viewHolder> {
Context context;
ArrayList<Users> userslist;

    public UserAdaptor(Context context, ArrayList<Users> userslist) {
        this.context = context;
        this.userslist = userslist;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(context).inflate(R.layout.iteam_homepage,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
            Users users = userslist.get(position);
            holder.name.setText(users.getUsername());
            holder.statues.setText(users.getStatues());
        Picasso.get().load(users.getIamgeurl()).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("name",users.getUsername());
                intent.putExtra("image",users.getIamgeurl());
                        intent.putExtra("uid",users.getUid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userslist.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        CircleImageView imageView;
        TextView name,statues;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iteam_imageview);
            name = itemView.findViewById(R.id.iteam_name);
            statues = itemView.findViewById(R.id.iteam_statues);
        }
    }
}
