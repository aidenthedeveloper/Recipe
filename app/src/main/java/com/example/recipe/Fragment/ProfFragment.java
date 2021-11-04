package com.example.recipe.Fragment;

import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.recipe.Model.User;
import com.example.recipe.R;
import com.example.recipe.SettingsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfFragment extends Fragment {

    ImageView ivProfile,ivSettings;
    TextView tvFullName, tvUsername, tvBio;

    private static final String TAG = "ProfileActivity";
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    String phoneNo;



    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        phoneNo = prefs.getString("phoneNo", "none");


        ivSettings = view.findViewById(R.id.ivSettings);
        ivProfile = view.findViewById(R.id.ivProfile);
        tvFullName = view.findViewById(R.id.tvFullName);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvBio = view.findViewById(R.id.tvBio);


        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        // postList = new ArrayList<>();
        // myPicAdapter = new MyPicAdapter(getContext(), postList);
        //  recyclerView.setAdapter(myPicAdapter);

        userInfo();


        ivSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

    private void userInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users").child(phoneNo);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (getContext() == null) {
                    return;
                }
                User user = dataSnapshot.getValue(User.class);

                Glide.with(getContext()).load(user.getImageurl()).into(ivProfile);
                tvUsername.setText(user.getUsername());
                tvFullName.setText(user.getFullName());
                tvBio.setText(user.getBio());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}