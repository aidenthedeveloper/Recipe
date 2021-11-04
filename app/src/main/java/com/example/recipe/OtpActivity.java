package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipe.Model.Ingredients;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class OtpActivity extends AppCompatActivity {

    TextView tvPhone;
    private Button mVerifyCodeBtn;
    private EditText otpEdit;
    private String OTP;
    private FirebaseAuth firebaseAuth;
    FirebaseAnalytics firebaseAnalytics;

    String[] ingredientLists = {"chicken", "fish", "salt", "sugar", "milk", "beef", "vegetables","egg"};
    private List<String> ingredientList;

    String fullName,username,password,phoneNo,gender;

    int age;

    ProgressDialog pd;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat,simpleYearFormat,simpleMonthFormat;
    String date,year,month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        tvPhone = findViewById(R.id.tvPhone);

        tvPhone.setText(getIntent().getStringExtra("phoneNumber"));

         phoneNo = getIntent().getStringExtra("phoneNumber");
         fullName = getIntent().getStringExtra("fullName");
         username = getIntent().getStringExtra("username");
         password = getIntent().getStringExtra("password");
         gender = getIntent().getStringExtra("gender");
         age = getIntent().getIntExtra("age",0);

        ingredientList = new ArrayList<>();
        ingredientList.add("chicken");
        ingredientList.add("fish");
        ingredientList.add("salt");
        ingredientList.add("sugar");
        ingredientList.add("milk");
        ingredientList.add("beef");
        ingredientList.add("vegetables");


        mVerifyCodeBtn = findViewById(R.id.btnVerify);
        otpEdit = findViewById(R.id.etOTPCode);

        firebaseAuth = FirebaseAuth.getInstance();

        OTP = getIntent().getStringExtra("auth");
        mVerifyCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(OtpActivity.this);
                pd.setMessage("Please wait...");
                pd.show();
                String verification_code = otpEdit.getText().toString();
                if(!verification_code.isEmpty()){
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OTP , verification_code);
                 //  Intent intent = new Intent(OtpActivity.this,NewProfileActivity.class);
                   //  intent.putExtra("phoneNumber",phoneNumber);

                   // String phoneNumber = tvPhone.getText().toString();
                    // Intent intent = new Intent(OtpActivity.this,LogOutActivity.class);
                    //intent.putExtra("phoneNumber",phoneNumber);
                    signIn(credential);
                }else{
                    Toast.makeText(OtpActivity.this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void signIn(PhoneAuthCredential credential){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    register(username,fullName,phoneNo,password,gender);
                }else{
                    Toast.makeText(OtpActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void register(final String username, final String fullName, String phoneNo, String password, String gender) {
        calendar = Calendar.getInstance();
        simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleYearFormat= new SimpleDateFormat("yyyy");
        simpleMonthFormat= new SimpleDateFormat("MM");
        date = simpleDateFormat.format(calendar.getTime());
        year = simpleYearFormat.format(calendar.getTime());
        month = simpleMonthFormat.format(calendar.getTime());

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String userID = firebaseUser.getUid();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users").child(userID);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid",userID);
        hashMap.put("username",username.toLowerCase());
        hashMap.put("fullName",fullName);
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
                            Toast.makeText(OtpActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OtpActivity.this, "Error.", Toast.LENGTH_SHORT).show();

                        }
                    });

                    HashMap<String, Object> hashMap = new HashMap<>();
                    //hashMap.put("ingredients", Arrays.asList(recoIngList));
                    hashMap.put("ingredients", Arrays.asList(ingredientLists));

                    fStore.collection("recommend").document(firebaseUser.getUid()).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            DatabaseReference reference2 = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Recommend");
                            reference2.child(firebaseUser.getUid()).setValue(ingredientList);
                           // Toast.makeText(OtpActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OtpActivity.this, "Error.", Toast.LENGTH_SHORT).show();

                        }
                    });

                    Intent intent = new Intent(OtpActivity.this,HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }

}