package com.example.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity {

    TextView tvEditProfile,tvSignOut,tvGenReport,tvGenTable;

    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        tvEditProfile = findViewById(R.id.tvEditProfile);
        tvSignOut = findViewById(R.id.tvSignOut);
        tvGenReport = findViewById(R.id.tvGenerateReport);
        tvGenTable = findViewById(R.id.tvGenerateTable);

        tvGenReport.setVisibility(View.GONE);
        tvGenTable.setVisibility(View.GONE);

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Users").child(firebaseUser.getUid());

        reference//.orderByChild("admin")
              //  .equalTo("admins")
             //   .equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("admin")) {
                            tvGenReport.setVisibility(View.VISIBLE);
                            tvGenTable.setVisibility(View.VISIBLE);

                        } else {
                            tvGenReport.setVisibility(View.GONE);
                            tvGenTable.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        tvEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),EditProfActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        tvSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(SettingsActivity.this)
                        .setTitle("Sign Out")
                        .setMessage("Are you sure you want to sign out?")
                        .setPositiveButton("Yes", null)
                        .setNegativeButton("No", null)
                        .show();

                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intToMain = new Intent(SettingsActivity.this, MainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intToMain);
                    }
                });
            }
        });

        tvGenReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ReportActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        tvGenTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TableActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}