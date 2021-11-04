package com.example.recipe.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.recipe.CommentActivity;
import com.example.recipe.FollowersActivity;
import com.example.recipe.Fragment.ProfileFragment;
import com.example.recipe.HomeActivity;
import com.example.recipe.Model.Post;
import com.example.recipe.Model.User;
import com.example.recipe.R;
import com.example.recipe.RecipeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class Recipe2Adapter extends RecyclerView.Adapter<Recipe2Adapter.ViewHolder> {

    private Context mContext;
    private List<Post> mPost;
    private boolean isFragment;

    private FirebaseUser firebaseUser;

    public Recipe2Adapter(Context mContext,List<Post> mPost, boolean isFragment){
        this.mContext = mContext;
        this.mPost = mPost;
        this.isFragment = isFragment;
    }

    @NonNull
    @Override
    public Recipe2Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_search, viewGroup, false);
        return new Recipe2Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final Post post = mPost.get(i);

        viewHolder.title.setText(post.getTitle());

        Glide.with(mContext).load(post.getPostimage())
                .apply(new RequestOptions().placeholder(R.drawable.ic_image))
                .into(viewHolder.post_image);


        if (post.getTitle().equals("")) {
            viewHolder.title.setVisibility(View.GONE);
        } else {
            viewHolder.title.setVisibility(View.VISIBLE);
            viewHolder.title.setText(post.getTitle());
        }

        publisherInfo(viewHolder.image_profile, viewHolder.username, post.getPublisher());
        nrLikes(viewHolder.likes, post.getPostid());

        viewHolder.post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("postid", post.getPostid());
                editor.apply();
                //    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                //       new PostDetailFragment()).commit();

                Intent intent = new Intent(mContext, RecipeActivity.class);
                mContext.startActivity(intent);
            }
        });

        viewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("postid", post.getPostid());
                editor.apply();
                //    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                //       new PostDetailFragment()).commit();

                Intent intent = new Intent(mContext, RecipeActivity.class);
                mContext.startActivity(intent);
            }
        });


        viewHolder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FollowersActivity.class);
                intent.putExtra("id",post.getPostid());
                intent.putExtra("title","likes");
                mContext.startActivity(intent);
            }
        });

        viewHolder.image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", post.getPublisher());
                editor.apply();

                FragmentManager fm = ((FragmentActivity) mContext).getSupportFragmentManager();
                Bundle arguments = new Bundle();
                arguments.putInt("VALUE1", 2);

                ProfileFragment profileFragment = new ProfileFragment();
                profileFragment.setArguments(arguments);

                fm.beginTransaction().replace(R.id.fragment_container, profileFragment).commit();

            }
        });

        viewHolder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", post.getPublisher());
                editor.apply();

                FragmentManager fm = ((FragmentActivity) mContext).getSupportFragmentManager();
                Bundle arguments = new Bundle();
                arguments.putInt("VALUE1", 2);

                ProfileFragment profileFragment = new ProfileFragment();
                profileFragment.setArguments(arguments);

                fm.beginTransaction().replace(R.id.fragment_container, profileFragment).commit();

            //    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
            //            new ProfileFragment()).commit();
            }
        });


       /* viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFragment) {

                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("title",post.getTitle());
                    editor.apply();

                    Intent intent = new Intent(mContext, RecipeActivity.class);
                    mContext.startActivity(intent);

                } else {
                    Intent intent = new Intent(mContext, HomeActivity.class);
                    //Intent intent = new Intent(mContext, MainActivity.class);
                    //intent.putExtra("publiserid",user.getId());
                    mContext.startActivity(intent);
                }
            }
        }); */
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public ImageView post_image,image_profile;

        public TextView title;

        public TextView username,likes;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            title = itemView.findViewById(R.id.title);
            post_image = itemView.findViewById(R.id.post_image);
            likes = itemView.findViewById(R.id.likes);
            username = itemView.findViewById(R.id.username);
            image_profile = itemView.findViewById(R.id.image_profile);

        }
    }
    private void nrLikes(final TextView likes, String postid) {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Likes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                likes.setText(dataSnapshot.getChildrenCount() + " Likes");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void publisherInfo(final ImageView image_profile, final TextView username, final String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                Glide.with(mContext).load(user.getImageurl())
                        .apply(new RequestOptions().placeholder(R.drawable.ic_person))
                        .into(image_profile);
                username.setText(user.getUsername());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
