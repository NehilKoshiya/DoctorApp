package com.example.doctorapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddDoctor extends AppCompatActivity {

    private static final String TAG = "Tag";
    Spinner spinner;
    String hosLevel;
    EditText drName,drDegree,address;
    Button btn_submit;
    Uri filePath;
    CircleImageView drImage;
    DatabaseReference reference;
    StorageReference storageReference;
    FirebaseStorage storage;

    final int PICK_IMAGE_REQUEST = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);

        drName = findViewById(R.id.drName);
        drDegree = findViewById(R.id.drDegree);
        address = findViewById(R.id.address);
        btn_submit = findViewById(R.id.btn_submit);
        drImage = findViewById(R.id.dr_image);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        spinner = findViewById(R.id.codeSpinner);
        String[] items = new String[]{"Multi Specialist", "Verbal Level", "Local Level"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hosLevel = String.valueOf(spinner.getSelectedItem());
                Log.d(TAG, "onItemSelected: "+hosLevel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onItemSelected: "+hosLevel);
                Log.d(TAG, "onItemSelected: "+drName.getText());
                Log.d(TAG, "onItemSelected: "+drDegree.getText());
                Log.d(TAG, "onItemSelected: "+address.getText());
                Log.d(TAG, "onItemSelected: "+filePath);
                uploadDrData();
            }
        });
        drImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
    }
    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image From Here...."),PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                drImage.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void uploadDrData() {
        reference = FirebaseDatabase.getInstance().getReference("Doctors").child(hosLevel).child(drName.getText().toString());

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("drName", drName.getText().toString());
        hashMap.put("drDegree", drDegree.getText().toString());
        hashMap.put("Address", address.getText().toString());
        hashMap.put("Hosptal Type", hosLevel);
        hashMap.put("imageUrl","default");

        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AddDoctor.this,"Succuss",Toast.LENGTH_SHORT).show();
                if(filePath != null) {
                    final ProgressDialog progressDialog = new ProgressDialog(AddDoctor.this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    StorageReference reference = storageReference.child("images").child("Doctors").child(hosLevel).child(drName.getText().toString());

                    reference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddDoctor.this,"Image Uploaded!",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(AddDoctor.this,ProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            StorageReference reference = storageReference.child("images").child("Doctors").child(hosLevel).child(drName.getText().toString());
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Doctors").child(hosLevel).child(drName.getText().toString());
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("drName", drName.getText().toString());
                                    hashMap.put("drDegree", drDegree.getText().toString());
                                    hashMap.put("Address", address.getText().toString());
                                    hashMap.put("Hosptal Type", hosLevel);
                                    hashMap.put("imageUrl",uri.toString());
                                    reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddDoctor.this,"Success",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddDoctor.this,"Failed"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Loading... " + ((int) progress) + "%");

                        }
                    });
                }else{
                    StorageReference reference = storageReference.child("images").child("Doctors").child(hosLevel).child(drName.getText().toString());
                    Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.profile);
                    reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d(TAG, "onSuccess: Success");
                            Intent intent = new Intent(AddDoctor.this, ProfileActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });

    }
}