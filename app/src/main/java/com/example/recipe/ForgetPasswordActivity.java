package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class ForgetPasswordActivity extends AppCompatActivity {

    private ImageView screenIcon;
    private TextView title, description;
    private Button btnNext;
    private TextInputLayout phoneNoTextField;
    private CountryCodePicker countryCodePicker;
    private RelativeLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        countryCodePicker = findViewById(R.id.countryCodePicker);
        phoneNoTextField = findViewById(R.id.etForgetPhone);
        btnNext = findViewById(R.id.btnForgetNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyPhoneNumber(view);
            }
        });
    }

    public void verifyPhoneNumber(View view){

      //  CheckInternet checkInternet = new CheckInternet();
      //  if(!checkInternet.isConnected(this)) { }

        if(!validateFields()) {
        }
        ///progressBar.setVisibility(View.VISIBLE);


        String str_phoneNo = phoneNoTextField.getEditText().getText().toString().trim();
        if (str_phoneNo.charAt(0) == '0') {
            str_phoneNo = str_phoneNo.substring(1);
        }
        final String str_fullPhoneNo = countryCodePicker.getSelectedCountryCodeWithPlus() + str_phoneNo;

        Query checkUser = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users").orderByChild("phoneNo").equalTo(str_fullPhoneNo);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    phoneNoTextField.setError(null);
                    phoneNoTextField.setErrorEnabled(false);

                    Intent intent = new Intent(getApplicationContext(),VerifyPhoneNoActivity.class);
                    intent.putExtra("phoneNo",str_fullPhoneNo);
                    intent.putExtra("whatToDo","updateData");
                    startActivity(intent);
                    finish();

                    //progressBar.setVisibility(View.GONE);
                } else
                {
                    //progressBar.setVisibility(View.GONE);
                    phoneNoTextField.setError("No such user exist.");
                    phoneNoTextField.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private boolean validateFields(){

        String str_phoneNo = phoneNoTextField.getEditText().getText().toString().trim();

        if(str_phoneNo.isEmpty()){
            phoneNoTextField.setError("Please enter your phone number.");
            phoneNoTextField.requestFocus();
            return false;
        } else {
            phoneNoTextField.setError(null);
            phoneNoTextField.setErrorEnabled(false);
            return true;

        }
    }
}