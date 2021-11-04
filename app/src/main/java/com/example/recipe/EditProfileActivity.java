package com.example.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {

    TextInputLayout fullName,username, bio, password, phoneNo;
    TextView fullNameLabel,usernameLabel;

    String str_fullName,str_username,str_bio,str_password, str_phoneNo;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users");

        fullName = findViewById(R.id.etFullNameUpdate);
        username = findViewById(R.id.etUsernameUpdate);
        bio = findViewById(R.id.etBioUpdate);
        password = findViewById(R.id.etPasswordUpdate);

        showAllUserData();


    }

    private void showAllUserData() {

        Intent intent = getIntent();
        str_fullName = intent.getStringExtra("fullName");
        str_username = intent.getStringExtra("username");
        str_bio = intent.getStringExtra("bio");
        str_password = intent.getStringExtra("password");
        str_phoneNo = intent.getStringExtra("phoneNo");

        fullNameLabel.setText(str_fullName);
        usernameLabel.setText(str_username);
        fullName.getEditText().setText(str_fullName);
        username.getEditText().setText(str_username);
        bio.getEditText().setText(str_bio);
        password.getEditText().setText(str_password);
        phoneNo.getEditText().setText(str_phoneNo);
    }

    public void update(View view){

        if(isNameChanged() || isPasswordChanged() || isUsernameChanged() || isBioChanged()){
            Toast.makeText(this,"Data has been updated",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this,"Data cannot be updated",Toast.LENGTH_LONG).show();
        }

    }

    private boolean isUsernameChanged() {
        if(!str_username.equals(username.getEditText().getText().toString())){
            reference.child(str_username).child("username").setValue(username.getEditText().getText().toString());
            str_username = username.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }

    }

    private boolean isBioChanged() {
        if(!str_bio.equals(bio.getEditText().getText().toString())){
            reference.child(str_bio).child("bio").setValue(bio.getEditText().getText().toString());
            str_bio = bio.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }

    }

    private boolean isPasswordChanged() {
        if(!str_password.equals(password.getEditText().getText().toString())){
            reference.child(str_password).child("password").setValue(password.getEditText().getText().toString());
            str_password = password.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }

    }

    private  boolean isNameChanged() {

        if(!str_fullName.equals(fullName.getEditText().getText().toString())){
            reference.child(str_fullName  ).child("fullName").setValue(fullName.getEditText().getText().toString());
            str_fullName = fullName.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }

    }
}

