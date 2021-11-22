package com.example.recipe.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.recipe.Fragment.ProfileFragment;
import com.example.recipe.Model.Notification;
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

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context mContext;
    private List<Notification> mNotification;
    String publisher,notifid;


    private FirebaseUser firebaseUser;

    public NotificationAdapter(Context mContext, List<Notification> mNotification,String publisher) {
        this.mContext = mContext;
        this.mNotification = mNotification;
        this.publisher = publisher;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item, viewGroup, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        Notification notification =mNotification.get(i);

        Intent intent = ((Activity) mContext).getIntent();
        String publisherid = intent.getStringExtra("publisherid");


        viewHolder.txtComment.setText(notification.getTxtComment());

        getUserInfo(viewHolder.image_profile, viewHolder.username, notification.getUserid());

        if(notification.isIspost()){
            viewHolder.post_image.setVisibility(View.VISIBLE);
            getPostImage(viewHolder.post_image, notification.getPostid());
        } else {
            viewHolder.post_image.setVisibility(View.GONE);
        }
        viewHolder.post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!notification.getPublisher().equals(firebaseUser.getUid())){

                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("postid",notification.getPostid());
                editor.apply();
                    Intent intent = new Intent(mContext, RecipeActivity.class);
                    mContext.startActivity(intent);
            }}
        });

        viewHolder.image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!notification.getPublisher().equals(firebaseUser.getUid())){

                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid",notification.getUserid());
                editor.apply();

                FragmentManager fm = ((FragmentActivity) mContext).getSupportFragmentManager();
                Bundle arguments = new Bundle();
                arguments.putInt("VALUE1", 4);

                ProfileFragment profileFragment = new ProfileFragment();
                profileFragment.setArguments(arguments);


                fm.beginTransaction().replace(R.id.fragment_container, profileFragment).commit();

              //  ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
             //           new ProfileFragment()).commit();
            }}
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                    String userId = firebaseUser.getUid();
                    //Toast.makeText(mContext.getApplicationContext(), comment.getCommentId(), Toast.LENGTH_SHORT).show();

                    AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                    alertDialog.setTitle("Do you want to delete?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int i) {
                                    FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Notifications")
                                            .child(firebaseUser.getUid())
                                            .child(notification.getNotifid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(mContext, "Deleted!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                return true;
            }
        });

   /*     viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(notification.isIspost()){
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                    editor.putString("postid",notification.getPostid());
                    editor.apply();

                    Intent intent = new Intent(mContext, RecipeActivity.class);
                    mContext.startActivity(intent);

                //    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    //        new PostDetailFragment()).commit();
                } else {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("profileid",notification.getUserid());
                    editor.apply();

                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();
                }
            }
        }); */

    }

    @Override
    public int getItemCount() {
        return mNotification.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_profile, post_image;
        public TextView username, txtComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            post_image =itemView.findViewById(R.id.post_image);
            username =itemView.findViewById(R.id.username);
            txtComment =itemView.findViewById(R.id.comment);
        }
    }

    private void getUserInfo(final ImageView image_profile, final TextView username, String publisherid){
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Users").child(publisherid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(mContext).load(user.getImageurl())
                        .apply(new RequestOptions().placeholder(R.drawable.ic_person_white))
                        .into(image_profile);
                username.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getPostImage(final ImageView post_image, final String postid){
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Posts").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                Glide.with(mContext).load(post.getPostimage())
                        .apply(new RequestOptions().placeholder(R.drawable.ic_image_white))
                        .into(post_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
