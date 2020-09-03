package com.example.doctorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPassword extends AppCompatActivity {

    private static final String TAG = "TAG";
    EditText email;
    Button btn_sendEmail;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getSupportActionBar().hide();

        email = findViewById(R.id.email);
        btn_sendEmail = findViewById(R.id.btn_snd_email);

        firebaseAuth = FirebaseAuth.getInstance();

        btn_sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senEmail(email.getText().toString());
            }
        });
    }



    public void senEmail(String email) {
        Log.d(TAG, "senEmail: ");
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: Success");
                    Toast.makeText(ForgotPassword.this,"An email has been sent to you.",Toast.LENGTH_SHORT);
                }else {
                    Log.d(TAG, "onComplete: Fail");
                    Toast.makeText(ForgotPassword.this,"Failed",Toast.LENGTH_SHORT);

                }
            }
        });
    }
}