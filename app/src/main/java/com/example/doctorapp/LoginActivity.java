package com.example.doctorapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    Button btn_lgn;
    FirebaseAuth auth;
    TextView forgot_password;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_lgn = findViewById(R.id.btn_lgn);
        forgot_password = findViewById(R.id.forgot_password);
        progressBar = findViewById(R.id.prgBar);


        auth = FirebaseAuth.getInstance();

        btn_lgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                progressBar.setVisibility(View.VISIBLE);
                btn_lgn.setVisibility(View.GONE);

                if ( TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password))
                {
                    progressBar.setVisibility(View.GONE);
                    btn_lgn.setVisibility(View.VISIBLE);
                    Toast.makeText(LoginActivity.this, "All Feilds Are Required !", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }  else {
                                progressBar.setVisibility(View.GONE);
                                btn_lgn.setVisibility(View.VISIBLE);
                                Toast.makeText(LoginActivity.this, "Authntication Is Falied!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }


}