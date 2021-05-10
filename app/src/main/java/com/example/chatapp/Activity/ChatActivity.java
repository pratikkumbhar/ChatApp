package com.example.chatapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.Adaptor.MessagesAdaptor;
import com.example.chatapp.Messages.Messages;
import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
String username,uid,imageuri;
CircleImageView imageView;
TextView userText;
FirebaseDatabase database;
FirebaseAuth auth;
public static String senderiamge;
public static String reciverimage;
String senderuid;
EditText editmessage;
ImageView sendbutton;
RecyclerView chatRecycleview;
String senderRoom,reciverRoom;
ArrayList<Messages> messagesArrayList;
MessagesAdaptor messagesAdaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        imageView = findViewById(R.id.chatactivtiy_iamgeview);
        userText = findViewById(R.id.chatacitvity_username);
        editmessage = findViewById(R.id.chattype_edittext);
        sendbutton = findViewById(R.id.sendbutton);
        chatRecycleview = findViewById(R.id.chat_recycleview);
        messagesArrayList = new ArrayList<>();
        database= FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        imageuri = getIntent().getStringExtra("image");
        username = getIntent().getStringExtra("name");
        uid = getIntent().getStringExtra("uid");
        Picasso.get().load(imageuri).into(imageView);
        userText.setText(""+username);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        chatRecycleview.setLayoutManager(linearLayoutManager);
        messagesAdaptor = new MessagesAdaptor(ChatActivity.this,messagesArrayList);
        chatRecycleview.setAdapter(messagesAdaptor);
        senderuid = auth.getUid();

        senderRoom = senderuid+uid;
        reciverRoom = uid+senderuid;




        DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
        DatabaseReference chatreference = database.getReference().child("Chats").child(senderRoom).child("messages");




        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren())
                {
                    Messages messag = snapshot1.getValue(Messages.class);
                    messagesArrayList.add(messag);
                }
                messagesAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              senderiamge =   snapshot.child("iamgeurl").getValue().toString();
             reciverimage = imageuri;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editmessage.getText().toString();
                if (message.isEmpty())
                {
                    Toast.makeText(ChatActivity.this, "Please Enter Something", Toast.LENGTH_SHORT).show();
                    return;
                }
                editmessage.setText("");
               Date date = new Date();
                Messages messages = new Messages(message,senderuid,date.getTime());
                database.getReference().child("Chats").child(senderRoom).child("messages").push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        database.getReference().child("Chats").child(reciverRoom).child("messages").push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                });
            }
        });

    }
}