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
import com.example.recipe.Utils.StringManipulation;
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

         /*       Query query = reference.orderByChild("phoneNo").equalTo("fullPhone");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }); */



           /* userRef.whereEqualTo("phoneNo", rPhoneNo)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            }); */


           /*     if(!validatePhoneNo() | !validateName() | !validatePassword() | !validateUsername() | phoneB == false){
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
                } */

           /*     DatabaseReference ref = reference.child(username);
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            if(ds.child(username).exists()) {
                                //Do something
                                Toast.makeText(PhoneRegisterActivity.this,"Username exist",Toast.LENGTH_SHORT).show();

                                return;

                            } else {
                                //Do something else
                                rUsername.setError(null);
                                rUsername.setErrorEnabled(false);
                                callVerifyOTPScreen();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                ref.addListenerForSingleValueEvent(valueEventListener);
*/
                //checkUsername();
              /*  reference = FirebaseDatabase.getInstance().getReference();
                Query query = reference
                        .child("Users")
                        .orderByChild("username")//Could be: 765432189
                        .equalTo(rUsername);
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            //Do something
                        } else {
                            //Do something else
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                query.addListenerForSingleValueEvent(valueEventListener); */

              /*  String name = rName.getEditText().getText().toString();
                String username = rUsername.getEditText().getText().toString();
                String phoneNo = rPhoneNo.getEditText().getText().toString();
                String password = rPassword.getEditText().getText().toString();

                UserHelperClass helperClass = new UserHelperClass(name,username,phoneNo,password);

                reference.child(phoneNo).setValue(helperClass); */

            }
        });

    }

  /*  public boolean checkIfUsernameExists(String username, DataSnapshot dataSnapshot){
        Log.d(TAG,"checkIfUsernameExists: checking if " + username + "already exists." );

        User user = new User();
    } */


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

    private void checkPhoneNo(){
        ProgressDialog pd = new ProgressDialog(PhoneRegisterActivity.this);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        firebaseDatabase.getInstance();
        DatabaseReference rootRef = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Users");

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed, how to handle?
                Toast.makeText(PhoneRegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();

            }

        });
    }

    private void checkUsername(){
        DatabaseReference rootRef = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        rootRef.child("Users").child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    rUsername.setError("Username Exists.");

                } else {
                    rUsername.setError(null);
                    rUsername.setErrorEnabled(false);
                    callVerifyOTPScreen();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       /* DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string))
                .orderByChild("username")
                .equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>0) {
                    //username found

                }else{
                    // username not found
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); */

        /*ProgressDialog pd = new ProgressDialog(PhoneRegisterActivity.this);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        firebaseDatabase.getInstance();
        DatabaseReference rootRef = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        DatabaseReference userNameRef = rootRef.child("Users").child("username");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    //create new user
                    rUsername.setError(null);
                    rUsername.setErrorEnabled(false);
                    callVerifyOTPScreen();
                }
                else{
                    rUsername.setError("Username Exist.");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PhoneRegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                //Log.d(TAG, databaseError.getMessage()); //Don't ignore errors!
            }
        };
        userNameRef.addListenerForSingleValueEvent(eventListener); */

               /* if (checkIfUsernameExists(rPhoneNo, snapshot)) {
                    rPhoneNo.setError("Phone Number Exist.");
                    pd.dismiss();
                    return;
                } else {
                    // Don't exist! Do something.
                    rPhoneNo.setError(null);
                    rPhoneNo.setErrorEnabled(false);
                    callVerifyOTPScreen();

                } */

    }


    /*public boolean checkIfUsernameExists(TextInputLayout username, DataSnapshot dataSnapshot){
        User user = new User();

        for (DataSnapshot ds: dataSnapshot.getChildren()){

            user.setUsername(ds.getValue(User.class).getUsername());

            if(StringManipulation.expandUsername(user.getUsername()).equals(username)){
                return true;
            }
        }

        return false;
    } */


    private Boolean validatePassword() {
        String val = rPassword.getEditText().getText().toString();
      /*  String passwordVal2 = "^" +
                "(?=.*[0-9])" +
                "(?=.*[A-Z])" +
                "$"; */

        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            rPassword.setError("Field cannot be empty");
            pd.dismiss();
            return false;
        } else if (!val.matches(passwordVal)) {
            rPassword.setError("Password is too weak");
            pd.dismiss();
            return false;
        } /*else if (!val.matches(passwordVal2)) {
            rPassword.setError("Password need at least 1 numeral digit and 1 upper case letter.");
            return false;
        } */ else {
            rPassword.setError(null);
            rPassword.setErrorEnabled(false);
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

          /*  String name = rName.getEditText().getText().toString();
            String username = rUsername.getEditText().getText().toString();
            String phoneNo = rPhoneNo.getEditText().getText().toString();
            String password = rPassword.getEditText().getText().toString();

            Intent intent = new Intent(getApplicationContext(),VerifyPhoneNoActivity.class);
            intent.putExtra("phoneNo",phoneNo);
            startActivity(intent);


            UserHelperClass helperClass = new UserHelperClass(name, username, phoneNo, password);
            reference.child(username).setValue(helperClass); */

        }

}