package com.example.doctorapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "TAG";
    private AppBarConfiguration mAppBarConfiguration;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    private DatabaseReference mDatabase;
    TextView headingText;
    RecyclerView recyclerView1, recyclerView2, recyclerView3;
    LinearLayout linearLayout, cardView;
    DoctorListAdapter doctorListAdapter;
    VerDrListAdapter verDrListAdapter;
    LocalDrListAdapter localDrListAdapter;
    Spinner dropdown;

    CardView multiCard, verbalCard, localCard;
    StorageReference storageReference;
    FirebaseStorage storage;
    DatabaseReference databaseReference;

    List<DoctorListModel> drList;
    List<VerbalDrListModal> verbalDr;
    List<LocalDrListModal> localDr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView1 = findViewById(R.id.recy1);
        recyclerView2 = findViewById(R.id.recy2);
        recyclerView3 = findViewById(R.id.recy3);
        multiCard = findViewById(R.id.multiCard);
        verbalCard = findViewById(R.id.verbalCard);
        localCard = findViewById(R.id.localCard);
        headingText = findViewById(R.id.mainHead);
        cardView = findViewById(R.id.cardView);
        linearLayout = findViewById(R.id.heading);
        dropdown = findViewById(R.id.spinner);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        String[] items = new String[]{"Multispeciality", "Verbal", "Local"};

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(layoutManager);
        recyclerView1.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setHasFixedSize(true);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this);
        recyclerView3.setLayoutManager(layoutManager3);
        recyclerView3.setHasFixedSize(true);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        getSupportActionBar().setTitle("Dr. Mob");


        Query recentPostsQuery = mDatabase.child("Doctors").child("Multi Specialist").limitToFirst(100);
        drList = new ArrayList<>();
        recentPostsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getValue());

                Log.d(TAG, "Address:" + dataSnapshot.child("Address").getValue());
                String drName = dataSnapshot.child("drName").getValue().toString();
                String drDegree = dataSnapshot.child("drDegree").getValue().toString();
                String address = dataSnapshot.child("Address").getValue().toString();
                StorageReference reference1 = storageReference.child("images").child("Doctors").child("Multi Specialist").child(drName);
                final Uri[] drProfile = new Uri[1];
                reference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        drProfile[0] = uri;
                        Log.d(TAG, "onSuccess: "+uri);
                    }
                });
                Log.d(TAG, "onChildAdded: "+ drProfile[0]);
                drList.add(new DoctorListModel(drName,drDegree,address, drProfile[0]));            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getValue());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
            }
        });

        StorageReference reference2 = storageReference.child("images").child("Doctors").child("Verbal Level");
        Query recentPostsQuery2 = mDatabase.child("Doctors").child("Verbal Level").limitToFirst(100);
        verbalDr = new ArrayList<>();
        recentPostsQuery2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getValue());

                Log.d(TAG, "Address:" + dataSnapshot.child("Address").getValue());
                String drName = dataSnapshot.child("drName").getValue().toString();
                String drDegree = dataSnapshot.child("drDegree").getValue().toString();
                String address = dataSnapshot.child("Address").getValue().toString();

                verbalDr.add(new VerbalDrListModal(drName,drDegree,address,R.drawable.profile));            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getValue());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
            }
        });

        StorageReference reference3 = storageReference.child("images").child("Doctors").child("Local Level");
        Query recentPostsQuery3 = mDatabase.child("Doctors").child("Local Level").limitToFirst(100);
        localDr = new ArrayList<>();
        recentPostsQuery3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getValue());

                Log.d(TAG, "Address:" + dataSnapshot.child("Address").getValue());
                String drName = dataSnapshot.child("drName").getValue().toString();
                String drDegree = dataSnapshot.child("drDegree").getValue().toString();
                String address = dataSnapshot.child("Address").getValue().toString();

                localDr.add(new LocalDrListModal(drName,drDegree,address,R.drawable.profile));            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getValue());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
            }
        });


        doctorListAdapter = new DoctorListAdapter(drList, getApplicationContext());
        recyclerView1.setAdapter(doctorListAdapter);
//
        verDrListAdapter = new VerDrListAdapter(verbalDr, getApplicationContext());
        recyclerView2.setAdapter(verDrListAdapter);
//
        localDrListAdapter = new LocalDrListAdapter(localDr, getApplicationContext());
        recyclerView3.setAdapter(localDrListAdapter);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("username");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        multiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.setVisibility(View.GONE);
                recyclerView1.setVisibility(View.VISIBLE);
                recyclerView2.setVisibility(View.GONE);
                recyclerView3.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                getSupportActionBar().show();
                headingText.setText("Multispeciality Hospital");
            }
        });
        verbalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.setVisibility(View.GONE);
                recyclerView1.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.VISIBLE);
                recyclerView3.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                getSupportActionBar().show();
                headingText.setText("Verbal Level Hospital");
            }
        });
        localCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.setVisibility(View.GONE);
                recyclerView1.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.GONE);
                recyclerView3.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                getSupportActionBar().show();
                headingText.setText("Local Level Hospital");
            }
        });

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Header View");
                startActivity(new Intent(ProfileActivity.this, userProfile.class));
            }
        });

        final TextView tt1 = (TextView) headerView.findViewById(R.id.username);
        final CircleImageView profileImage = headerView.findViewById(R.id.profile_image);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: "+ dataSnapshot.child("username").getValue());
                String imageUrl = dataSnapshot.child("imageURL").getValue().toString();
                Log.d(TAG, "onDataChange: "+ Uri.parse(imageUrl));
                Uri uri = Uri.parse(imageUrl);
//                imageView.setImageURI(uri);
                Glide.with(getApplicationContext()).load(uri).placeholder(R.drawable.prof).override(200,200).centerCrop().into(profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        StorageReference ref = FirebaseStorage.getInstance().getReference().child("images").child(firebaseUser.getUid() + ".jpeg");
//        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
////                Uri img = Uri.parse("https://images.unsplash.com/photo-1494548162494-384bba4ab999?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80");
//
//                Glide.with(getApplicationContext()).load(uri).placeholder(R.drawable.profile).override(200, 200).centerCrop().into(profileImage);
//
//            }
//
//        });
//        tt1.setText(reference.getKey());

        Log.d(TAG, "onCreate: " + reference);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                dataSnapshot.getChildren();
//                HashMap<String,Object> dataMap = (HashMap<String, Objec t>) dataSnapshot.getValue();
                tt1.setText(dataSnapshot.getValue().toString());
                Log.d(TAG, "onCreate: " + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        TextView tt = (TextView) headerView.findViewById(R.id.email);
        tt.setText(firebaseUser.getEmail());

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: Click On Image!");
//            }
//        });
//
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    //    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.ml) {
            cardView.setVisibility(View.GONE);
            recyclerView1.setVisibility(View.VISIBLE);
            recyclerView2.setVisibility(View.GONE);
            recyclerView3.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            getSupportActionBar().show();
            headingText.setText("Multispeciality Hospital");
            return true;
        } else if (id == R.id.vl) {
            cardView.setVisibility(View.GONE);
            recyclerView1.setVisibility(View.GONE);
            recyclerView2.setVisibility(View.VISIBLE);
            recyclerView3.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            getSupportActionBar().show();
            headingText.setText("Verbal Level Hospital");
            return true;
        } else if (id == R.id.ll) {
            cardView.setVisibility(View.GONE);
            recyclerView1.setVisibility(View.GONE);
            recyclerView2.setVisibility(View.GONE);
            recyclerView3.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            getSupportActionBar().show();
            headingText.setText("Local Level Hospital");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            return true;
        } else if (id == R.id.add_doctor) {
            startActivity(new Intent(getApplicationContext(), AddDoctor.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}