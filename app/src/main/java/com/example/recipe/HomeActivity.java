 package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.recipe.Fragment.HomeFragment;
import com.example.recipe.Fragment.NotificationFragment;
import com.example.recipe.Fragment.ProfileFragment;
import com.example.recipe.Fragment.SearchFragment;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

 public class HomeActivity extends AppCompatActivity {
     BottomNavigationView bottomNavigationView;
     Fragment selectedFragment = null;

     public static FragmentManager fragmentManager;
     
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_home);


         bottomNavigationView = findViewById(R.id.btm_navigation);
         bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

         fragmentManager = getSupportFragmentManager();

        Bundle intent = getIntent().getExtras();
        if(intent != null){
            String publisher = intent.getString("publisherid");

            SharedPreferences.Editor editor = getSharedPreferences("PREFS",MODE_PRIVATE).edit();
            editor.putString("profileid",publisher);
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }




     }

     private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
             new BottomNavigationView.OnNavigationItemSelectedListener() {
                 @Override
                 public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                     FirebaseUser firebaseUser;

                     firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                     switch (item.getItemId()) {
                         case R.id.nav_home:
                                selectedFragment = new HomeFragment();

                             break;
                         case R.id.nav_search:
                             SharedPreferences.Editor editor2 = getSharedPreferences("PREFS_SEARCH",MODE_PRIVATE).edit();
                             editor2.putString("search","search");
                             editor2.apply();
                                selectedFragment = new SearchFragment();
                             break;
                         case R.id.nav_add:
                             selectedFragment = null;
                                   startActivity(new Intent(HomeActivity.this, PostActivity.class));
                             break;
                         case R.id.nav_notification:
                             selectedFragment = null;
                             selectedFragment = new NotificationFragment();
                             break;
                         case R.id.nav_profile:
                                SharedPreferences.Editor editor = getSharedPreferences("PREFS",MODE_PRIVATE).edit();
                             editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                             editor.apply();
                             selectedFragment = new ProfileFragment();
                             //((AppCompatActivity) selectedFragment.getActivity()).getSupportActionBar().hide();
                             break;

                     }
                         if (selectedFragment != null) {
                             getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                         }

                         return true;
                     }

             };

 }
