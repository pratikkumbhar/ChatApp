package com.example.chatapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
private TextView alreadyhaveaccounttext;
private EditText email, password;
private Button loginbutton;
private FirebaseAuth auth;
private ProgressDialog progressDialog;
private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        alreadyhaveaccounttext = findViewById(R.id.alreadyhaveaccount_textview);
        email = findViewById(R.id.login_email_edittext);
        password = findViewById(R.id.password_email_edittext);
        loginbutton = findViewById(R.id.loginbutton);
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating your Account... ");
        progressDialog.setCancelable(false);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String emailstring = email.getText().toString();
                String passwordstring = password.getText().toString();
                if (TextUtils.isEmpty(emailstring)||TextUtils.isEmpty(passwordstring)){
                    Toast.makeText(LoginActivity.this, "Enter Vaild Data", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }else if (!emailstring.matches(emailPattern))
                {
                    email.setError("Envalid Email");
                    Toast.makeText(LoginActivity.this, "Envalid Email", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if (passwordstring.length()<6)
                {
                    password.setError("Envalid Password");
                    Toast.makeText(LoginActivity.this, "Enter valid Password", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else {
                    auth.signInWithEmailAndPassword(emailstring,passwordstring).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Error In login In", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        alreadyhaveaccounttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });
    }
}