package com.example.chatapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chatapp.Activity.Model.Users;
import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {
EditText username,statues;
ImageView imageView;
FirebaseAuth auth;
FirebaseDatabase database;
FirebaseStorage storage;
CircleImageView circleImageView;
Uri setectedimage;
ProgressDialog progressDialog;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        username = findViewById(R.id.settingusername);
        statues = findViewById(R.id.settingstatues);
        imageView = findViewById(R.id.setting_done);
        circleImageView = findViewById(R.id.settingimageView);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);


        DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
        StorageReference storageReference = storage.getReference().child("uplod").child(auth.getUid());


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    email = snapshot.child("email").getValue().toString();
                    String username1 = snapshot.child("username").getValue().toString();
                    String statues1 = snapshot.child("statues").getValue().toString();
                    String imageuri1 = snapshot.child("iamgeurl").getValue().toString();

                    username.setText(username1);
                    statues.setText(statues1);;
                Picasso.get().load(imageuri1).into(circleImageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);
            }
        });
         imageView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                progressDialog.show();
                   String name = username.getText().toString();
               String   stau =   statues.getText().toString();

                    if (setectedimage!=null)
                    {
                       
                        storageReference.putFile(setectedimage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String finalIamge = uri.toString();
                                        Users users = new Users(auth.getUid(),name,email,finalIamge,stau);
                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(SettingActivity.this, "Data Is Updated", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(SettingActivity.this,HomeActivity.class));
                                                }
                                                else
                                                {       progressDialog.dismiss();
                                                    Toast.makeText(SettingActivity.this, "SomeThing Went Wrong", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                });
                            }
                        });
                    }

                    else {


                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String finalIamge = uri.toString();
                                Users users = new Users(auth.getUid(),name,email,finalIamge,stau);
                                reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            progressDialog.dismiss();
                                            Toast.makeText(SettingActivity.this, "Data Is Updated", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SettingActivity.this,HomeActivity.class));
                                        }
                                        else
                                        {
                                            progressDialog.dismiss();
                                            Toast.makeText(SettingActivity.this, "SomeThing Went Wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        });
                    }


             }
         });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==10)
        {
            if (data!=null)
            {
                setectedimage = data.getData();
                circleImageView.setImageURI(setectedimage);
            }
        }

    }
}