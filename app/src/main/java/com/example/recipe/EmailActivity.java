package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class EmailActivity extends AppCompatActivity {

    Button btnVerCont;
    FirebaseUser firebaseUser;
    TextView tvBack;
    FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        btnVerCont = findViewById(R.id.btnVerifyContinue);
        tvBack = findViewById(R.id.tvBack);
       // firebaseUser = FirebaseAuth.getInstance(FirebaseApp.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app")).getCurrentUser();

        //firebaseUser = FirebaseAuth.getInstance(FirebaseApp.getInstance()).getCurrentUser();

    /*    do{
            firebaseUser = FirebaseAuth.getInstance(FirebaseApp.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app")).getCurrentUser();

            auth.getCurrentUser().reload();
            firebaseUser.reload();

        } while (!firebaseUser.isEmailVerified()); */

       /* Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                firebaseUser = FirebaseAuth.getInstance(FirebaseApp.getInstance()).getCurrentUser();
                auth.getCurrentUser().reload();
                firebaseUser.reload();
                if (!firebaseUser.isEmailVerified()) {

                    //do nothing
                    Toast.makeText(EmailActivity.this, "Please verify your email and login.", Toast.LENGTH_SHORT).show();

                } else {
                    Intent i = new Intent(EmailActivity.this,HomeActivity.class);
                    //auth.signOut();
                    finish();
                    //overridePendingTransition(0,0);
                    startActivity(i);
                }

            }
        }, 1000); */

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EmailActivity.this, EmailLoginActivity.class));
            }
        });

        btnVerCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!firebaseUser.isEmailVerified()) {
        /*       AlertDialog.Builder emailAlert = new AlertDialog.Builder(EmailActivity.this);
                emailAlert.setTitle("Verify you email");
                emailAlert.setMessage("The email address is not verified.");
                emailAlert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        auth.signOut();
                        return;
                    }
                });
                emailAlert.setNegativeButton(R.string.send_email, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        user.sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(LoginActivity.this, "Email Sent", Toast.LENGTH_SHORT).show();
                                        }
                                        firebaseAuth.signOut();
                                        return;
                                    }
                                });
                    }
                });
                emailAlert.show();
            startActivity(new Intent(getApplicationContext(), HomeActivity.class)); */

                    Toast.makeText(EmailActivity.this, "Please verify your email and login.", Toast.LENGTH_SHORT).show();
                  //  Intent i = new Intent(EmailActivity.this,HomeActivity.class);
                    //auth.signOut();
                  //  finish();
                    //overridePendingTransition(0,0);
                  //  startActivity(i);
                    //overridePendingTransition(0,0);
                } else {
                    startActivity(new Intent(EmailActivity.this, HomeActivity.class));
                }
            }
        });
    }
}

