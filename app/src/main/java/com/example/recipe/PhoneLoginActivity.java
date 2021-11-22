package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class PhoneLoginActivity extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    TextInputLayout phoneNo, password;
    ProgressBar progressBar;
    Button btnPhoneLogin,btnSignUp;
    CheckBox rememberMe;
    TextInputEditText etPhoneNo, etPassword;

    String whatToDo;
    FirebaseAuth auth;

    PhoneAuthCredential credential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        countryCodePicker = findViewById(R.id.countryCodePickerLogin);
        phoneNo = findViewById(R.id.etPhoneLogin);
       // password = findViewById(R.id.etPasswordLogin);
        progressBar = findViewById(R.id.progressBar);
        btnPhoneLogin = findViewById(R.id.btnPhoneLogin);
        etPhoneNo = findViewById(R.id.etPhoneLoginET);
       // etPassword = findViewById(R.id.etPasswordET);


        btnPhoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                letTheUserLoggedIn(view);
            }
        });
    }

    public void letTheUserLoggedIn(View view) {


        if(!validateFields()){
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        String str_phoneNo = phoneNo.getEditText().getText().toString().trim();
//        String str_password = password.getEditText().getText().toString().trim();

        if(str_phoneNo.charAt(0)=='0'){
            str_phoneNo = str_phoneNo.substring(1);
        } else if(str_phoneNo.charAt(0)=='+'){
            str_phoneNo = str_phoneNo.substring(2);
        }

        final String str_fullPhoneNo = countryCodePicker.getSelectedCountryCodeWithPlus()+str_phoneNo;

        Query checkUser = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users").orderByChild("phoneNo").equalTo(str_fullPhoneNo);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    phoneNo.setError(null);
                    phoneNo.setErrorEnabled(false);
                 //   String userid = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users").orderByChild("phoneNo").equalTo(str_fullPhoneNo);


                  //  String systemPassword = dataSnapshot.child(str_fullPhoneNo).child("password").getValue(String.class);
                 //   if(systemPassword.equals(str_password)){
                  //      password.setError(null);
                  //      password.setErrorEnabled(false);

                      //  String str_fullName = dataSnapshot.child(str_fullPhoneNo).child("fullName").getValue(String.class);
                      //  String str_phoneNo = dataSnapshot.child(str_fullPhoneNo).child("phoneNo").getValue(String.class);
                      //  String str_userName = dataSnapshot.child(str_fullPhoneNo).child("username").getValue(String.class);
                      //  String str_password = dataSnapshot.child(str_fullPhoneNo).child("password").getValue(String.class);
                     //   String str_bio = dataSnapshot.child(str_fullPhoneNo).child("bio").getValue(String.class);

                        //Create a session
                     //   SessionManager sessionManager = new SessionManager(PhoneLoginActivity.this,SessionManager.SESSION_USERSESSION);
                     //   sessionManager.createLoginSession(str_fullName,str_userName,str_phoneNo,str_password,str_bio);

                        progressBar.setVisibility(View.GONE);
                        callVerifyOTPScreen();
                        //Toast.makeText(PhoneLoginActivity.this, str_fullName+"\n"+str_bio+"\n"+str_userName+"\n"+str_phoneNo, Toast.LENGTH_SHORT).show();
                        //Intent intent = new Intent(getApplicationContext(),LogOutActivity.class);
                        //FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                      //  FirebaseUser firebaseUser = FirebaseAuth.getInstance(FirebaseApp.getInstance()).getCurrentUser();
                      /*  DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users").child(firebaseUser.getUid());


                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                auth.signInWithCredential(credential)
                                        .addOnCompleteListener(PhoneLoginActivity.this,
                                                new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {

                                                            //Intent intent = new Intent(getApplicationContext(),VerifyPhoneNoActivity.class);
                                                           // intent.putExtra("fullName",fullName);
                                                           /// intent.putExtra("username",username);
                                                          //  intent.putExtra("password",password);
                                                          //  intent.putExtra("phoneNo",phoneNo);
                                                          // startActivity(intent);

                                                            //Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                                                            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            //startActivity(intent);
                                                           // finish();

                                                        } else {

                                                            //verification unsuccessful.. display an error message

                                                            String message = "Somthing is wrong, we will fix it soon...";

                                                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                                                message = "Invalid code entered...";
                                                            }
                                                            Toast.makeText(PhoneLoginActivity.this,message,Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                progressBar.setVisibility(View.GONE);

                            }
                        });*/

                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        phoneNo.setError("Phone Number Not Registered.");
                        //Toast.makeText(PhoneLoginActivity.this,"Phone Number not ",Toast.LENGTH_SHORT).show();
                    }


                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private boolean validateFields(){

        String str_phoneNo = phoneNo.getEditText().getText().toString().trim();
        //String str_password = password.getEditText().getText().toString().trim();

        if(str_phoneNo.isEmpty()){
            phoneNo.setError("Please enter your phone number.");
            phoneNo.requestFocus();
            return false;
        /*} else if(str_password.isEmpty()){
            password.setError("Please enter your password.");
            password.requestFocus();
            return false; */
        } else {
            phoneNo.setError(null);
            phoneNo.setErrorEnabled(false);
           // password.setError(null);
           // password.setErrorEnabled(false);
            return true;

        }
    }

    public void callSignUpFromLogin(View view) {
        startActivity(new Intent(getApplicationContext(),PhoneRegisterActivity.class));
    }


    public void callVerifyOTPScreen() {

        String getPhoneNo = phoneNo.getEditText().getText().toString().trim();
        String str_phoneNo = countryCodePicker.getSelectedCountryCodeWithPlus()+getPhoneNo;
        //String str_password = password.getEditText().getText().toString().trim();

        Intent intent = new Intent(getApplicationContext(),VerifyPhoneNoActivity.class);
        //intent.putExtra("password",str_password);
        intent.putExtra("whatToDo","login");
        intent.putExtra("phoneNo",str_phoneNo);

        startActivity(intent);
        }
    }
//}