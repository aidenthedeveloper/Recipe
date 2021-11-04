package com.example.recipe.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.recipe.FollowersActivity;
import com.example.recipe.Fragment.ProfileFragment;
import com.example.recipe.HomeActivity;
import com.example.recipe.Model.Post;
import com.example.recipe.Model.User;
import com.example.recipe.R;
import com.example.recipe.RecipeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class Post2Adapter extends RecyclerView.Adapter<Post2Adapter.ViewHolder> {

    private Context mContext;
    private List<Post> mPost;

    private FirebaseUser firebaseUser;

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference userRef = fStore.collection("users");
    CollectionReference postRef = fStore.collection("posts");

    public Post2Adapter(Context mContext,List<Post> mPost){
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public Post2Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_search, viewGroup, false);
        return new Post2Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final Post post = mPost.get(i);

        viewHolder.title.setText(post.getTitle());

        Glide.with(mContext).load(post.getPostimage())
                .apply(new RequestOptions().placeholder(R.drawable.ic_image))
                .into(viewHolder.post_image);


      /*  if (post.getTitle().equals("")) {
            viewHolder.title.setVisibility(View.GONE);
        } else {
            viewHolder.title.setVisibility(View.VISIBLE);
            viewHolder.title.setText(post.getTitle());
        } */

        viewHolder.image_profile.setVisibility(View.GONE);

        publisherInfo(viewHolder.image_profile, viewHolder.username, viewHolder.fullName, viewHolder.publisher, post.getPublisher());
        isLiked(post.getPostid(), viewHolder.like);
        nrLikes(viewHolder.likes, post.getPostid());
        //getComments(post.getPostid(), viewHolder.comments);
        isSaved(post.getPostid(),viewHolder.save);

        viewHolder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.save.getTag().equals("save")){
                    FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Saves").child(firebaseUser.getUid())
                            .child(post.getPostid()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Saves").child(firebaseUser.getUid())
                            .child(post.getPostid()).removeValue();
                }
            }
        });

        viewHolder.image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", post.getPublisher());
                editor.apply();

                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();


            }
        });

        viewHolder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", post.getPublisher());
                editor.apply();

                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
            }
        });

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

        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.like.getTag().equals("like")) {
                    FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Likes").child(post.getPostid())
                            .child(firebaseUser.getUid()).setValue(true);
                    addNotification(post.getPublisher(),post.getPostid());
                } else {
                    FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Likes").child(post.getPostid())
                            .child(firebaseUser.getUid()).removeValue();
                }
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

        viewHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mContext, view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.edit:
                                editPost(post.getPostid());
                                return true;
                            case R.id.delete:
                                final String id = post.getPostid();
                                FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Posts")
                                        .child(post.getPostid()).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                      postRef.document(post.getPostid()).delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                }
                                                            });
                                                    Toast.makeText(mContext, "Deleted!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(mContext, HomeActivity.class);
                                                    mContext.startActivity(intent);
                                                }
                                            }
                                        });
                                return true;
                            case R.id.report:
                                Toast.makeText(mContext, "Reported clicked!", Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.inflate(R.menu.post_menu);
                if (!post.getPublisher().equals(firebaseUser.getUid())) {
                    popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
                }
                popupMenu.show();
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


        public ImageView image_profile, post_image, like, comment, save, more;

        public TextView username, likes, publisher, caption, comments, fullName, title, cooktime, serving, steps, ingredients;


        public ViewHolder(@NonNull View itemView){
            super(itemView);

            cooktime = itemView.findViewById(R.id.cooktime);
            serving = itemView.findViewById(R.id.serving);
            ingredients = itemView.findViewById(R.id.ingredients);
            steps = itemView.findViewById(R.id.steps);


            image_profile = itemView.findViewById(R.id.image_profile);
            post_image = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            save = itemView.findViewById(R.id.save);
            username = itemView.findViewById(R.id.username);
            likes = itemView.findViewById(R.id.likes);
            publisher = itemView.findViewById(R.id.publisher);
            caption = itemView.findViewById(R.id.caption);
            comments = itemView.findViewById(R.id.comments);
            more = itemView.findViewById(R.id.more);
            fullName = itemView.findViewById(R.id.fullname);
            title = itemView.findViewById(R.id.title);

        }

        private void isLiked(String postid, final ImageView imageView) {

            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference()
                    .child("Likes").child(postid);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                        imageView.setImageResource(R.drawable.ic_heart);
                        imageView.setTag("liked");
                    } else {
                        imageView.setImageResource(R.drawable.ic_heart_border);
                        imageView.setTag("like");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void addNotification(String userid, String postid){
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Notifications").child(userid);

        String notifid = reference.push().getKey();


        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("txtComment", "liked your post");
        hashMap.put("postid", postid);
        hashMap.put("ispost",true);

        hashMap.put("notifid",notifid);

        // reference.push().setValue(hashMap);
        reference.child(notifid).setValue(hashMap);
    }

    private void publisherInfo(final ImageView image_profile, final TextView username, final TextView fullName, final TextView publisher, final String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                Glide.with(mContext).load(user.getImageurl())
                        .apply(new RequestOptions().placeholder(R.drawable.ic_person))
                        .into(image_profile);
                username.setText(user.getUsername());
                fullName.setText(user.getFullName());
                //publisher.setText(user.getUsername());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void isLiked(String postid, final ImageView imageView) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference()
                .child("Likes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.ic_heart);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.ic_heart_border);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    private void editPost(final String postid) {
        Context context = mContext.getApplicationContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Add a TextView here for the "Title" label, as noted in the comments
        final TextView titleView = new TextView(context);
        titleView.setText("Edit Title");
        layout.addView(titleView);

        final EditText titleBox = new EditText(context);
        titleBox.setHint("Title");
        layout.addView(titleBox); // Notice this is an add method

        final TextView captionView = new TextView(context);
        captionView.setText("Edit Caption");
        layout.addView(captionView);

        // Add another TextView here for the "Description" label
        final EditText captionBox = new EditText(context);
        captionBox.setHint("Caption");
        layout.addView(captionBox); // Another add method




        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Edit Recipe");

        alertDialog.setView(layout); // Again this is a set method, not add

        //final EditText editText2 = new EditText(mContext);
        //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
        //        LinearLayout.LayoutParams.MATCH_PARENT,
        //        LinearLayout.LayoutParams.MATCH_PARENT);
        //editText2.setLayoutParams(lp);
        //alertDialog.setView(editText2);

        getText(postid, titleBox,postid,captionBox);

        alertDialog.setPositiveButton("Edit",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("title", titleBox.getText().toString());
                        hashMap.put("lowerTitle", titleBox.getText().toString().toLowerCase());
                        hashMap.put("caption", captionBox.getText().toString());

                        FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Posts")
                                .child(postid).updateChildren(hashMap);

                        postRef.document(postid).update("title",titleBox.getText().toString());
                        postRef.document(postid).update("lowerTitle",titleBox.getText().toString().toLowerCase());
                        postRef.document(postid).update("caption",captionBox.getText().toString());

                        //Intent intent = new Intent(mContext, RecipeActivity.class);
                        //mContext.startActivity(intent);

                        Intent intent = new Intent(mContext, RecipeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);


                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        alertDialog.show();
    }

    private void getText(String s, EditText titleBox, String postid, final EditText captionBox) {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Posts")
                .child(postid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                titleBox.setText(dataSnapshot.getValue(Post.class).getTitle());
                captionBox.setText(dataSnapshot.getValue(Post.class).getCaption());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void isSaved(String postid, ImageView imageView){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Saves")
                .child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postid).exists()){
                    imageView.setImageResource(R.drawable.ic_bookmarkadded);
                    imageView.setTag("saved");
                } else {
                    imageView.setImageResource(R.drawable.ic_bookmark);
                    imageView.setTag("save");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
