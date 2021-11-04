package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.arch.core.executor.TaskExecutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.recipe.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class VerifyPhoneNoActivity extends AppCompatActivity {

    PinView pinFromUser;
    Button btnVCode;
    TextView phoneNoEnteredByTheUser;
    String codeBySystem;
    private FirebaseAuth firebaseAuth;
    FirebaseAnalytics firebaseAnalytics;

    String phoneNo,fullName,username,password,gender,whatToDo;

    int age;

    ProgressDialog pd;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat,simpleYearFormat,simpleMonthFormat;
    String date,year,month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_no);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Verify Phone Number");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnVCode = findViewById(R.id.btnVerifyCode);
        phoneNoEnteredByTheUser = findViewById(R.id.otp_description_text);

        pinFromUser = findViewById(R.id.pin_view);

        phoneNo = getIntent().getStringExtra("phoneNo");
        fullName = getIntent().getStringExtra("fullName");
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");
        gender = getIntent().getStringExtra("gender");
        age = getIntent().getIntExtra("age",0);
        whatToDo = getIntent().getStringExtra("whatToDo");

        phoneNoEnteredByTheUser.setText("Enter One Time Password Sent On " + phoneNo);


        sendVerifcationCodeToUser(phoneNo);



    }

    private void sendVerifcationCodeToUser(String phoneNo) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(VerifyPhoneNoActivity.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeBySystem = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if(code!=null){
                        pinFromUser.setText(code);
                        verifyCode(code);
                    }
                }
                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(VerifyPhoneNoActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem,code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            String userID = firebaseUser.getUid();
                            calendar = Calendar.getInstance();

                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference reference = firebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users").child(userID);
                            reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Users").child(userID);

                            simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            simpleYearFormat= new SimpleDateFormat("yyyy");
                            simpleMonthFormat= new SimpleDateFormat("MM");
                            date = simpleDateFormat.format(calendar.getTime());
                            year = simpleYearFormat.format(calendar.getTime());
                            month = simpleMonthFormat.format(calendar.getTime());
                         /*   if ("updateData".equalsIgnoreCase(whatToDo)) {
                                updateOldUsersData();
                            } else */ if ("login".equalsIgnoreCase(whatToDo)) {

                                Intent intent = new Intent(VerifyPhoneNoActivity.this,HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {

                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("id",userID);
                                hashMap.put("username",username.toLowerCase());
                                hashMap.put("fullName",fullName);
                                hashMap.put("email","-");
                                hashMap.put("phoneNo",phoneNo);
                                hashMap.put("gender",gender);
                                hashMap.put("password",password);
                                hashMap.put("bio","");
                                hashMap.put("age",age);
                                hashMap.put("imageurl","");
                                hashMap.put("date",date);
                                hashMap.put("year",year);
                                hashMap.put("month",month);


                                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            pd.dismiss();

                                            FirebaseFirestore fStore = FirebaseFirestore.getInstance();
                                            fStore.collection("users").document(userID).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                    Toast.makeText(VerifyPhoneNoActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(VerifyPhoneNoActivity.this, "Error.", Toast.LENGTH_SHORT).show();

                                                }
                                            });

                                            Intent intent = new Intent(VerifyPhoneNoActivity.this,HomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                });

                            }
                        }
                         else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                pd.dismiss();
                               Toast.makeText(VerifyPhoneNoActivity.this,"OTP Code Incorrect. Verification Failed. Try Again Later.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void updateOldUsersData() {

        Intent intent = new Intent(getApplicationContext(),SetNewPasswordActivity.class);
        intent.putExtra("phoneNo",phoneNo);
        startActivity(intent);
        finish();
    }

    public void callNextScreenFromOTP(View view){
        pd = new ProgressDialog(VerifyPhoneNoActivity.this);
        pd.setMessage("Please wait...");
        pd.show();
        String code = pinFromUser.getText().toString();
        if(!code.isEmpty()){
            verifyCode(code);
        }
    }
 }