package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
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

public class EmailLoginActivity extends AppCompatActivity {

    TextInputLayout email,password;
    Button login;
    TextView tv_signup,tv_forget;

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    ProgressDialog pd;

    Boolean emailB = false;

    FirebaseAnalytics firebaseAnalytics;

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

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


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        tv_signup = findViewById(R.id.textview_SignUp);
        login = findViewById(R.id.btnSignIn);
        tv_forget = findViewById(R.id.textview_Forgot);

        auth = FirebaseAuth.getInstance();

        //FirebaseUser firebaseUser;

        //firebaseUser = FirebaseAuth.getInstance(FirebaseApp.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app")).getCurrentUser();
        //firebaseUser = FirebaseAuth.getInstance(FirebaseApp.getInstance()).getCurrentUser();


        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp = new Intent(EmailLoginActivity.this,RegisterActivity.class);
                startActivity(intSignUp);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(EmailLoginActivity.this);
                pd.setMessage("Please wait...");
                pd.show();

                String str_email = email.getEditText().getText().toString();
                String str_password = password.getEditText().getText().toString();

                //if (TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password)) {
                if (!validateEmail() | !validatePassword()) {
                    Toast.makeText(EmailLoginActivity.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                } /*else if (emailB == false) {
                    checkEmail();
                    pd.dismiss();
                } */else {
                    checkEmail();
                    /*if (emailB == true) {
                        auth.signInWithEmailAndPassword(str_email, str_password)
                                .addOnCompleteListener(EmailLoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Users")
                                                    .child(auth.getCurrentUser().getUid());

                                            FirebaseUser firebaseUser = FirebaseAuth.getInstance(FirebaseApp.getInstance()).getCurrentUser();

                                            reference.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    pd.dismiss();
                                                    if (!firebaseUser.isEmailVerified()) {
                                                        pd.dismiss();
                                                        Toast.makeText(EmailLoginActivity.this, "Please verify your email before logging in.", Toast.LENGTH_SHORT).show();
                                                        //Intent intent = new Intent(EmailLoginActivity.this, HomeActivity.class);
                                                        //startActivity(intent);
                                                    } else {
                                                        pd.dismiss();
                                                        Intent intent = new Intent(EmailLoginActivity.this, HomeActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    pd.dismiss();
                                                }
                                            });
                                        } else {
                                            pd.dismiss();
                                            Toast.makeText(EmailLoginActivity.this, "Login Failed.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                } */
                    }
                }
        });

        tv_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter your email to received reset link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract the email and send reset link

                        String mail = resetMail.getText().toString();
                        auth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EmailLoginActivity.this,"Reset Link Sent To You Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EmailLoginActivity.this,"Error! Reset Link Is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close the dialog
                    }
                });

                passwordResetDialog.create().show();
            }

        });
    }
    private void loginAcc(){
        String str_email = email.getEditText().getText().toString();
        String str_password = password.getEditText().getText().toString();

        auth.signInWithEmailAndPassword(str_email, str_password)
                .addOnCompleteListener(EmailLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Users")
                                    .child(auth.getCurrentUser().getUid());

                            FirebaseUser firebaseUser = FirebaseAuth.getInstance(FirebaseApp.getInstance()).getCurrentUser();

                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    pd.dismiss();
                                    if (!firebaseUser.isEmailVerified()) {
                                        pd.dismiss();
                                        email.setError("Email Not Yet Verified.");
                                        Toast.makeText(EmailLoginActivity.this, "Please verify your email before logging in.", Toast.LENGTH_SHORT).show();

                                        firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(EmailLoginActivity.this, "New Verification Email Has Been Sent.", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                e.getMessage();
                                            }
                                        });
                                        //Intent intent = new Intent(EmailLoginActivity.this, HomeActivity.class);
                                        //startActivity(intent);
                                    } else {
                                        pd.dismiss();
                                        email.setError(null);
                                        email.setErrorEnabled(false);

                                        Intent intent = new Intent(EmailLoginActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    pd.dismiss();
                                }
                            });
                        } else {
                            pd.dismiss();
                            Toast.makeText(EmailLoginActivity.this, "Login Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void checkEmail(){
        String val = email.getEditText().getText().toString();
        auth.fetchSignInMethodsForEmail(val).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                boolean check = !task.getResult().getSignInMethods().isEmpty();

                if(!check){
                    emailB = false;
                    email.setError("Email Not Yet Registered.");
                    pd.dismiss();
                } else {
                    emailB = true;
                    loginAcc();
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

    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString();

        if (val.isEmpty()) {
            password.setError("Please enter your password.");
            pd.dismiss();
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

}