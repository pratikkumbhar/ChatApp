package com.example.chatapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.Activity.Model.Users;
import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {
private TextView alreadyhaveaccoutl;
private CircleImageView profileimage;
private EditText editTextUsername,editTextemail,editTextpassword;
private Button signupbutton;
FirebaseDatabase firebaseDatabase;
DatabaseReference reference;
FirebaseStorage storage;
FirebaseAuth auth;
    String  imageurlString ;
    Uri imageUri;
    ProgressDialog progressDialog;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        profileimage = findViewById(R.id.profileimage);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating your Account... ");
        progressDialog.setCancelable(false);
        editTextUsername = findViewById(R.id.signup_username_edittext);
        editTextemail = findViewById(R.id.signup_email_edittext);
        editTextpassword = findViewById(R.id.signup_password_edittext);
        alreadyhaveaccoutl = findViewById(R.id.signuptesxt);
        signupbutton =  findViewById(R.id.signupbutton);


        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();



        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String username = editTextUsername.getText().toString();
                String email = editTextemail.getText().toString();
                String password = editTextpassword.getText().toString();
                String status = "Hey there friends.";
                if (TextUtils.isEmpty(username)||TextUtils.isEmpty(email)|| TextUtils.isEmpty(password))
                {
                    Toast.makeText(SignUpActivity.this, "Enter Valid Data", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if (!email.matches(emailPattern))
                {
                    editTextemail.setError("Enter Valid Email");
                    Toast.makeText(SignUpActivity.this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if (password.length()<6){

                    Toast.makeText(SignUpActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                reference = firebaseDatabase.getReference().child("user").child(auth.getUid());
                                StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());

                                if (imageUri != null) {
                                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageurlString = uri.toString();
                                                        Users users = new Users(auth.getUid(), username, email, imageurlString,status);
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                                                                    progressDialog.dismiss();
                                                                } else {
                                                                    Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                } else {
                                    imageurlString = "https://firebasestorage.googleapis.com/v0/b/chatapp-e15c8.appspot.com/o/profileimage.png?alt=media&token=50556dbb-fbf7-4baf-876f-855eb0064634";

                                    Users users = new Users(auth.getUid(), username, email, imageurlString,status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                                            } else {
                                                Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                             else {
                                Toast.makeText(SignUpActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);
            }
        });
        alreadyhaveaccoutl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
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
                imageUri = data.getData();
                profileimage.setImageURI(imageUri);
            }
        }
    }
}