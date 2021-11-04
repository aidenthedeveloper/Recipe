package com.example.recipe.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recipe.Adapter.PostAdapter;
import com.example.recipe.HomeActivity;
import com.example.recipe.Model.Post;
import com.example.recipe.PostActivity;
import com.example.recipe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PostDetailFragment extends Fragment {
    String postid;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;

    private RecyclerView recyclerView_detailed;
    private PostAdapter postAdapter_detailed;
    private List<Post> postList_detailed;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);


        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Recipe");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getContext(), ProfileFragment.class));

                startActivity(new Intent(getContext(), HomeActivity.class));
            }
        });

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        postid = prefs.getString("postid", "none");


        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);
        recyclerView.setAdapter(postAdapter);

        recyclerView_detailed = view.findViewById(R.id.recycler_view_detailed);
        recyclerView_detailed.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager_detailed = new LinearLayoutManager(getContext());
        recyclerView_detailed.setLayoutManager(mLayoutManager_detailed);


        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);
        recyclerView.setAdapter(postAdapter);

        recyclerView.setVisibility(View.GONE);
        recyclerView_detailed.setVisibility(View.VISIBLE);

        readPost();
        return view;
    }

    private void readPost(){
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Posts").child(postid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                postList.clear();
                Post post = dataSnapshot.getValue(Post.class);
                postList.add(post);

                postAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}