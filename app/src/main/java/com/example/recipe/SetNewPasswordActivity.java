package com.example.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetNewPasswordActivity extends AppCompatActivity {

    private Button btnNext;
    private TextInputLayout passwordChange,passwordConfirmed;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        btnNext = findViewById(R.id.btnForgetNext);
        passwordChange = findViewById(R.id.etPasswordChange);
        passwordConfirmed = findViewById(R.id.etPasswordChangeConfirmed);


    }

    public void setNewPasswordBtn(View view){

        //Check Internet

        if(!validatePassword() | !validateConfirmPassword()){
            return;
        }
        //progressBar.setVisibility(View.VISIBLE);

        String str_newPass = passwordChange.getEditText().getText().toString().trim();
        String str_phoneNo = getIntent().getStringExtra("phoneNo");

        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users");
        reference.child(str_phoneNo).child("password").setValue(str_newPass);

        startActivity(new Intent(getApplicationContext(),ForgetPasswordMessageActivity.class));
        finish();

    }
    private boolean validatePassword(){

        String str_password = passwordChange.getEditText().getText().toString().trim();

        if(str_password.isEmpty()){
            passwordChange.setError("Please enter your new password.");
            passwordChange.requestFocus();
            return false;
        } else {
            passwordChange.setError(null);
            passwordChange.setErrorEnabled(false);
            return true;

        }
    }
    private boolean validateConfirmPassword(){

        String str_password2 = passwordConfirmed.getEditText().getText().toString().trim();
        String str_password = passwordChange.getEditText().getText().toString().trim();

        if(str_password2.isEmpty()){
            passwordConfirmed.setError("Please enter your new password again.");
            passwordConfirmed.requestFocus();
            return false;
        } else if(!str_password2.equals(str_password)) {
            passwordConfirmed.setError("Password does not match.");
            passwordConfirmed.requestFocus();
            return false;
        } else {
                passwordConfirmed.setError(null);
                passwordConfirmed.setErrorEnabled(false);
                return true;

            }
        }
    }