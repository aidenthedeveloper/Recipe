package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipe.Model.Ingredients;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout username, fullname, email, password,phoneNo;
    TextView etDate;
    Button register;
    TextView txt_login;
    public String strUsername;

    String[] ingredientLists = {"chicken", "fish","salt","sugar","milk","beef","vegetables","egg"};
    private List<Ingredients> ingredientList;


    private RadioGroup radioGender;
    private RadioButton radioButton;

    FirebaseAnalytics firebaseAnalytics;


    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;
    private static final String TAG = "RegisterActivity";

    private static String users_from_database;
    private ArrayList<String> username_list = new ArrayList<>();

    int finalAge;

    Boolean emailB = false, allChecked = false;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat,simpleYearFormat,simpleMonthFormat;
    String date,year,month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

        ingredientList = new ArrayList<>();
        readDefault();

        username = findViewById(R.id.username);
        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        //phoneNo = findViewById(R.id.phoneNo);
        password = findViewById(R.id.password);
        register = findViewById(R.id.btn_Register);
        txt_login = findViewById(R.id.txt_login);
        radioGender = (RadioGroup) findViewById(R.id.gender);
        etDate = findViewById(R.id.date);

        auth = FirebaseAuth.getInstance();

        //checkUsername();

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        RegisterActivity.this, datePickListener, year , month , day);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();

            }
        });

       txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,EmailLoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(RegisterActivity.this);
                pd.setMessage("Please wait...");
                pd.show();

                int selectedId = radioGender.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);

                String str_username = username.getEditText().getText().toString();
                String str_fullname = fullname.getEditText().getText().toString();
                String str_email = email.getEditText().getText().toString();
                String str_password = password.getEditText().getText().toString();
                //String str_password = password.getText().toString();

                /*    if(TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_fullname) || TextUtils.isEmpty(str_email) ||
                            TextUtils.isEmpty(str_password)){
                        Toast.makeText(RegisterActivity.this,"All fields are required!",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    } else if (str_password.length() < 6) {
                        Toast.makeText(RegisterActivity.this,"Password must be over 6 characters.",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    } else if (radioGender.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(RegisterActivity.this,"Please select your gender.",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    } else if (finalAge == 0) {
                        Toast.makeText(RegisterActivity.this,"Please enter your D.O.B.",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    } else if (finalAge < 13) {
                        Toast.makeText(RegisterActivity.this,"You must be over 13 years old to create an account.",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    } else {
                        String str_gender = radioButton.getText().toString();
                        register(str_username,str_fullname,str_email,str_password,str_gender);
                    } */

                firebaseDatabase = FirebaseDatabase.getInstance();
                reference = firebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Users");

                reference.orderByChild("username")
                        .equalTo(str_username)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    username.setError("Username Exists.");
                                    pd.dismiss();
                                } else {
                                    username.setError(null);
                                    username.setErrorEnabled(false);


                                    if(!validateEmail() | !validateName() | !validatePassword() | !validateUsername()){
                                        return;
                                    } else if (radioGender.getCheckedRadioButtonId() == -1) {
                                        Toast.makeText(RegisterActivity.this,"Please select your gender.",Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                    } else if (finalAge == 0) {
                                        Toast.makeText(RegisterActivity.this,"Please enter your D.O.B.",Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                    } else if (finalAge < 13) {
                                        Toast.makeText(RegisterActivity.this,"You must be over 13 years old to create an account.",Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                    } else {
                                        checkEmail();
                                     /*   if(emailB == true){
                                                String str_gender = radioButton.getText().toString();
                                                register(str_username, str_fullname, str_email, str_password, str_gender);

                                        } */
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            }
        });
    }
   /* private void checkUsername() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Users");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {

                    users_from_database = (String) ds.child("username").getValue();

                    username_list.add(users_from_database);
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String s : username_list) {
                        stringBuilder.append(s + "\n");
                    }
                       Log.d("ZI", stringBuilder.toString());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ZI", "Failed");
            }
        });
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
    public void checkEmail(){
        String val = email.getEditText().getText().toString();
        auth.fetchSignInMethodsForEmail(val).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                boolean check = !task.getResult().getSignInMethods().isEmpty();

                if(!check){
                    emailB = true;
                    String str_gender = radioButton.getText().toString();
                    String str_username = username.getEditText().getText().toString();
                    String str_fullname = fullname.getEditText().getText().toString();
                    String str_email = email.getEditText().getText().toString();
                    String str_password = password.getEditText().getText().toString();
                    register(str_username, str_fullname, str_email, str_password, str_gender);
                } else {
                    emailB = false;
                    email.setError("Email already exist.");
                    pd.dismiss();
                }
            }
        });
    }

    private Boolean validateEmail(){
        String val = email.getEditText().getText().toString();

        if(val.isEmpty()){
            email.setError("Please enter your email");
            pd.dismiss();
            return false;
        } else if(!val.contains("@")){
            email.setError("Please enter a proper email");
            pd.dismiss();
            return false;
        }
        else if(!val.contains(".")){
            email.setError("Please enter a proper email");
            pd.dismiss();
            return false;
        }
        else{
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateName(){
        String val = fullname.getEditText().getText().toString();

        if(val.isEmpty()){
            fullname.setError("Please enter your name");
            pd.dismiss();
            return false;
        }
        else{
            fullname.setError(null);
            fullname.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername(){
        String val = username.getEditText().getText().toString();
        //String noWhiteSpace = "(?=\\s+$)";
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if(val.isEmpty()){
            username.setError("Please enter your username");
            pd.dismiss();
            return false;
        }
        else if(val.length() >=15) {
            username.setError("Username cannot exceed 15 characters.");
            pd.dismiss();
            return false;
        }
        else if(val.length() <6) {
            username.setError("Username need at least 6 characters.");
            pd.dismiss();
            return false;
        }
        else if(!val.matches(noWhiteSpace)) {
            username.setError("Username cannot contain white spaces.");
            pd.dismiss();
            return false;
        }
        else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString();
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
            password.setError("Field cannot be empty");
            pd.dismiss();
            return false;
        } else if (!val.matches(passwordVal)) {
            password.setError("Password is too weak");
            pd.dismiss();
            return false;
        } /*else if (!val.matches(passwordVal2)) {
            rPassword.setError("Password need at least 1 numeral digit and 1 upper case letter.");
            return false;
        } */ else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private void register(final String username, final String fullName, String email, String password, String gender){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userID = firebaseUser.getUid();
                            calendar = Calendar.getInstance();
                            simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            simpleYearFormat= new SimpleDateFormat("yyyy");
                            simpleMonthFormat= new SimpleDateFormat("MM");
                            date = simpleDateFormat.format(calendar.getTime());
                            year = simpleYearFormat.format(calendar.getTime());
                            month = simpleMonthFormat.format(calendar.getTime());

                            firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegisterActivity.this,"Verification Email Has Been Sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: Email not sent" + e.getMessage());
                                }
                            });


                          /*  rootNode = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app");
                            reference = rootNode.getReference("Users");


                            String name = etFName.getEditText().getText().toString();
                            String username = etUName.getEditText().getText().toString();
                            String password = etPw.getEditText().getText().toString();
                            String phoneNo = tvPhone.getText().toString();

                            UserHelperClass helperClass = new UserHelperClass(name,username,phoneNo,password);

                            reference.setValue(helperClass); */
                            DatabaseReference reference2 = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Recommend");
                            reference2.child(firebaseUser.getUid()).setValue(ingredientList);

                            reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Users").child(userID);

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("id",userID);
                            hashMap.put("username",username.toLowerCase());
                            hashMap.put("fullName",fullName);
                            hashMap.put("email",email);
                            hashMap.put("phoneNo","-");
                            hashMap.put("gender",gender);
                            hashMap.put("password",password);
                            hashMap.put("bio","");
                            hashMap.put("age",finalAge);
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
                                                Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RegisterActivity.this, "Error.", Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("ingredients", Arrays.asList(ingredientLists));

                                        fStore.collection("recommend").document(firebaseUser.getUid()).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                // Toast.makeText(OtpActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RegisterActivity.this, "Error.", Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                        Intent intent = new Intent(RegisterActivity.this,EmailLoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                        } else {
                            pd.dismiss();
                            Toast.makeText(RegisterActivity.this,"Register Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void readDefault() {
        DatabaseReference reference2 = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Default");

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ingredientList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Ingredients ingredients = snapshot.getValue(Ingredients.class);
                    ingredientList.add(ingredients);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}