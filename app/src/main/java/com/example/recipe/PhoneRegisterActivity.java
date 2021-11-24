package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipe.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hbb20.CountryCodePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PhoneRegisterActivity extends AppCompatActivity {

    TextInputLayout rName,rUsername,rPhoneNo,rPassword;
    TextView etDate;
    Button btnRegister,btnLogin;

    FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference userRef = fStore.collection("users");
    FirebaseAuth auth;
    DatabaseReference reference;
    CountryCodePicker countryCodePicker;

    private RadioGroup radioGender;
    private RadioButton radioButton;

    ProgressDialog pd;

    int finalAge;
    Boolean phoneB = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        rName = findViewById(R.id.etFullName);
        rUsername = findViewById(R.id.etUsername);
        rPhoneNo = findViewById(R.id.etPhone);
        //rPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        countryCodePicker = findViewById(R.id.countryCodePicker);
        radioGender = (RadioGroup) findViewById(R.id.gender);
        etDate = findViewById(R.id.date);

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        PhoneRegisterActivity.this, datePickListener, year , month , day);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhoneRegisterActivity.this,PhoneLoginActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(PhoneRegisterActivity.this);
                pd.setMessage("Please wait...");
                pd.show();

                int selectedId = radioGender.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);

                firebaseDatabase = FirebaseDatabase.getInstance();
                String username = rUsername.getEditText().getText().toString().trim();
                String getPhone = rPhoneNo.getEditText().getText().toString().trim();
                String fullPhone = countryCodePicker.getSelectedCountryCodeWithPlus()+getPhone;
                reference = firebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Users");

                reference.orderByChild("username")
                        .equalTo(username)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    rUsername.setError("Username Exists.");
                                    pd.dismiss();
                                } else {
                                    rUsername.setError(null);
                                    rUsername.setErrorEnabled(false);

                                    reference.orderByChild("phoneNo")
                                            .equalTo(fullPhone)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.getValue() != null) {
                                                        rPhoneNo.setError("Phone Number Exists.");
                                                        pd.dismiss();
                                                    } else {
                                                        rPhoneNo.setError(null);
                                                        rPhoneNo.setErrorEnabled(false);
                                                        if(!validatePhoneNo() | !validateName() |
                                                                //!validatePassword() |
                                                            !validateUsername()){
                                                            return;
                                                        } else if (radioGender.getCheckedRadioButtonId() == -1) {
                                                            Toast.makeText(PhoneRegisterActivity.this,"Please select your gender.",Toast.LENGTH_SHORT).show();
                                                            pd.dismiss();
                                                        } else if (finalAge == 0) {
                                                            Toast.makeText(PhoneRegisterActivity.this,"Please enter your D.O.B.",Toast.LENGTH_SHORT).show();
                                                            pd.dismiss();
                                                        } else if (finalAge < 13) {
                                                            Toast.makeText(PhoneRegisterActivity.this,"You must be over 13 years old to create an account.",Toast.LENGTH_SHORT).show();
                                                            pd.dismiss();
                                                        } else {
                                                            String str_gender = radioButton.getText().toString();
                                                            callVerifyOTPScreen();
                                                        }
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        });

    }


    private DatePickerDialog.OnDateSetListener datePickListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            String format = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
            etDate.setText(format);
            finalAge = calculateAge(calendar.getTimeInMillis());
        }

    };

    int calculateAge(long date){
        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(date);

        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if(today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)){
            age--;
        }
        finalAge = age;
        return age;
    }

    private Boolean validateName(){
        String val = rName.getEditText().getText().toString();

        if(val.isEmpty()){
            rName.setError("Please enter your name");
            pd.dismiss();
            return false;
        }
        else{
            rName.setError(null);
            rName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername(){
        String val = rUsername.getEditText().getText().toString();
        //String noWhiteSpace = "(?=\\s+$)";
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if(val.isEmpty()){
            rUsername.setError("Please enter your username");
            pd.dismiss();
            return false;
        }
        else if(val.length() >=15) {
            rUsername.setError("Username cannot exceed 15 characters.");
            pd.dismiss();
            return false;
        }
        else if(val.length() <6) {
            rUsername.setError("Username need at least 6 characters.");
            pd.dismiss();
            return false;
        }
        else if(!val.matches(noWhiteSpace)) {
            rUsername.setError("Username cannot contain white spaces.");
            pd.dismiss();
            return false;
        }
        else {
                rUsername.setError(null);
                rUsername.setErrorEnabled(false);
                return true;
            }
        }

    private Boolean validatePhoneNo(){
        String val = rPhoneNo.getEditText().getText().toString();

        if(val.isEmpty()){
            rPhoneNo.setError("Please enter your phone no");
            pd.dismiss();
            return false;
        } else {
            rPhoneNo.setError(null);
            rPhoneNo.setErrorEnabled(false);
            return true;
        }
    }

    public void callVerifyOTPScreen() {
        int selectedId = radioGender.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);

        String fullName = rName.getEditText().getText().toString();
        String username = rUsername.getEditText().getText().toString();
        //String password = rPassword.getEditText().getText().toString();

        String _getUserEnteredPhoneNo = rPhoneNo.getEditText().getText().toString().trim();
        String phoneNo = countryCodePicker.getSelectedCountryCodeWithPlus()+_getUserEnteredPhoneNo;
        String str_gender = radioButton.getText().toString();

                Intent intent = new Intent(getApplicationContext(),VerifyPhoneNoActivity.class);

                intent.putExtra("fullName",fullName);
                intent.putExtra("username",username.toLowerCase());
                intent.putExtra("password","");
                intent.putExtra("phoneNo",phoneNo);
                intent.putExtra("gender",str_gender);
                intent.putExtra("whatToDo","register");
                intent.putExtra("age",finalAge);

                startActivity(intent);

        }

}