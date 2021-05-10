package com.example.chatapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatapp.Activity.Model.Users;
import com.example.chatapp.R;
import com.example.chatapp.Adaptor.UserAdaptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
FirebaseAuth auth;
RecyclerView recyclerView;
UserAdaptor userAdaptor;
FirebaseDatabase database;
DatabaseReference reference;
ArrayList<Users> userlist;
ImageView signoutimageview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userlist = new ArrayList<>();
        signoutimageview = findViewById(R.id.logout_imageview);

        reference = database.getReference().child("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot dataSnapshot:snapshot.getChildren())
            {
                Users users = dataSnapshot.getValue(Users.class);
                userlist.add(users);
            }
            userAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView = findViewById(R.id.homerecycleview);
        userAdaptor = new UserAdaptor(this,userlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(userAdaptor);

        if (auth.getCurrentUser()==null)
        {
            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
        }

        signoutimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(HomeActivity.this);
                dialog.setContentView(R.layout.dialog_layout);
                TextView yesbutton,nobutton;
                yesbutton = dialog.findViewById(R.id.dialog_yes_button);
                nobutton = dialog.findViewById(R.id.dialog_no_button);
                yesbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                    }
                });
                nobutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });



    }
}