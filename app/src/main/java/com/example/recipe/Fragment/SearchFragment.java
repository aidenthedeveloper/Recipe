package com.example.recipe.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.recipe.Adapter.IngredientsAdapter;
import com.example.recipe.Adapter.PostAdapter;
import com.example.recipe.Adapter.Recipe2Adapter;
import com.example.recipe.Adapter.RecipeAdapter;
import com.example.recipe.Adapter.UserAdapter;
import com.example.recipe.Model.Ingredients;
import com.example.recipe.Model.Post;
import com.example.recipe.Model.Recipe;
import com.example.recipe.Model.User;
import com.example.recipe.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView,recyclerView2,recyclerView3;
    private UserAdapter userAdapter;
    private List<User> mUsers;

    private Recipe2Adapter recipe2Adapter;
    private List<Post> postList;

    private IngredientsAdapter ingredientsAdapter;
    private List<Ingredients> ingredientsList;

    EditText search_bar;
    String postid;

    ImageView ivIngre;
    ImageButton ibUser,ibRecipe;

    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference postRef = fStore.collection("posts");

    FirebaseUser firebaseUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        ibRecipe = view.findViewById(R.id.search_recipe);
        ibUser = view.findViewById(R.id.search_user);
       // ivIngre = view.findViewById(R.id.search_ingredients);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView2 = view.findViewById(R.id.recycler_view_recipe);
        recyclerView2.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(getContext());
        recyclerView2.setLayoutManager(mLayoutManager2);

       // recyclerView3 = view.findViewById(R.id.recycler_view_ingre);
       // recyclerView3.setHasFixedSize(true);
      //  LinearLayoutManager mLayoutManager3 = new LinearLayoutManager(getContext());
       // recyclerView3.setLayoutManager(mLayoutManager3);


        search_bar = view.findViewById(R.id.search_bar);

        mUsers = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(), mUsers, true);
        recyclerView.setAdapter(userAdapter);

        postList = new ArrayList<>();
        recipe2Adapter = new Recipe2Adapter(getContext(), postList,true);
        recyclerView2.setAdapter(recipe2Adapter);




        SharedPreferences prefs = this.getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        postid = prefs.getString("postid", "none");

        recyclerView.setVisibility(View.GONE);
        recyclerView2.setVisibility(View.VISIBLE);

        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchRecipe(s.toString().toLowerCase());
                //searchRecipe(s.toString().substring(0, 1).toUpperCase() + s.toString().substring(1).toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        ibRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ibRecipe.setColorFilter(getResources().getColor(R.color.black));
                ibUser.setColorFilter(getResources().getColor(R.color.white));
                recyclerView.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.VISIBLE);
                //Toast.makeText(getContext(),"",Toast.LENGTH_SHORT).show();
                readRecipe();
                search_bar.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        searchRecipe(s.toString().toLowerCase());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

            }
        });

        ibUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ibRecipe.setColorFilter(getResources().getColor(R.color.white));
                ibUser.setColorFilter(getResources().getColor(R.color.black));
                recyclerView2.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                readUsers();
                search_bar.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        searchUsers(s.toString().toLowerCase());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

            }
        });

        readRecipe();
        return view;
    }

    private void searchUsers(String s){
        Query query = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users").orderByChild("username")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    mUsers.add(user);
                }
                userAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void readUsers(){
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(search_bar.getText().toString().equals("")){
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        User user = snapshot.getValue(User.class);
                        mUsers.add(user);
                    }
                    userAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void searchRecipe(String s){
        Query query = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Posts").orderByChild("lowerTitle")
                .startAt(s)
                .endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                postList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    postList.add(post);
                }

                recipe2Adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readRecipe(){
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(search_bar.getText().toString().equals("")){
                    postList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Post post = snapshot.getValue(Post.class);
                        postList.add(post);
                    }
                    recipe2Adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    }
