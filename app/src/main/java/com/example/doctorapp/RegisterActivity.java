package com.example.doctorapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "TAG";
    EditText username, email, password;
    Button btn_register;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressBar progressBar;
    TextView alreadyAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.prgBar);
        alreadyAccount = findViewById(R.id.alreayAcc);
        auth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                btn_register.setVisibility(View.GONE);
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();

                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    progressBar.setVisibility(View.GONE);
                    btn_register.setVisibility(View.VISIBLE);
                    Toast.makeText(RegisterActivity.this, "All Feilds Are Required !", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    progressBar.setVisibility(View.GONE);
                    btn_register.setVisibility(View.VISIBLE);
                    Toast.makeText(RegisterActivity.this, "Password Length Is < 6!", Toast.LENGTH_SHORT).show();
                } else {
                    register(txt_username, txt_email, txt_password);
                }
            }
        });

        alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });

    }

    private void register(final String username, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userid = firebaseUser.getUid();

                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userid);
                    hashMap.put("username", username);
                    hashMap.put("imageURL", "default");


                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                String userid = firebaseUser.getUid();

                                StorageReference ref =  FirebaseStorage.getInstance().getReference().child("images/"+ userid+".jpeg");
                                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.profile);
                                ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Log.d(TAG, "onSuccess: Success");
                                        Intent intent = new Intent(RegisterActivity.this, ProfileActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                            }
                        }
                    });

                } else {
                    progressBar.setVisibility(View.GONE);
                    btn_register.setVisibility(View.VISIBLE);
                    Toast.makeText(RegisterActivity.this, "You Can't Register With This Email!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}