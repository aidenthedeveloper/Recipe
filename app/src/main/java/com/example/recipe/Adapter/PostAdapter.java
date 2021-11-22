package com.example.recipe.Adapter;

import android.app.AlertDialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


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
import com.google.firebase.analytics.FirebaseAnalytics;
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

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context mContext;
    private List<Post> mPost;

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private FirebaseUser firebaseUser;

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference userRef = fStore.collection("users");
    CollectionReference postRef = fStore.collection("posts");

    String[] str_report = { "Please Select A Problem", "Explicit Content", "Violence", "Harrassment", "Suicide or Self Injury", "False Information",
            "Hate Speech", "Terrorism", "Spam"};
    int reportType = 0;
    String reportTypeName = null;


    public PostAdapter(Context context, List<Post> mPost) {
        this.mContext = context;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, viewGroup, false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAnalytics firebaseAnalytics;
        firebaseAnalytics = FirebaseAnalytics.getInstance(mContext.getApplicationContext());
        final Post post = mPost.get(i);

        //new implement
        //viewHolder.tvIngredient.setText(post.getIngredients());

        // Create layout manager with initial prefetch item count
   /*     LinearLayoutManager layoutManager = new LinearLayoutManager(
                viewHolder.rvIngredient.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(post.getIngList().size());

        // Create sub item view adapter
        IngredientAdapter subItemAdapter = new IngredientAdapter(post.getIngList());

        viewHolder.rvIngredient.setLayoutManager(layoutManager);
        viewHolder.rvIngredient.setAdapter(subItemAdapter);
        viewHolder.rvIngredient.setRecycledViewPool(viewPool); */

        viewHolder.title.setText(post.getTitle());

        Glide.with(mContext).load(post.getPostimage())
                .apply(new RequestOptions().placeholder(R.drawable.ic_image))
                .into(viewHolder.post_image);

    /*    if (post.getCaption().equals("")) {
            viewHolder.caption.setVisibility(View.GONE);
        } else {
            viewHolder.caption.setVisibility(View.VISIBLE);
            viewHolder.caption.setText(post.getCaption());
        }

        if (post.getCookTime().equals("")) {
            viewHolder.cooktime.setVisibility(View.GONE);
        } else {
            viewHolder.cooktime.setVisibility(View.VISIBLE);
            viewHolder.cooktime.setText(post.getCookTime());
        }
        if (post.getServing().equals("")) {
            viewHolder.serving.setVisibility(View.GONE);
        } else {
            viewHolder.serving.setVisibility(View.VISIBLE);
            viewHolder.serving.setText(post.getServing());
        }

 /*  if (post.getTitle().equals("")) {
            viewHolder.title.setVisibility(View.GONE);
        } else {
            viewHolder.title.setVisibility(View.VISIBLE);
            viewHolder.title.setText(post.getTitle());
        } */



        viewHolder.image_profile.setVisibility(View.GONE);
        //viewHolder.fullName.setVisibility(View.GONE);
        //viewHolder.steps.setVisibility(View.GONE);
        //viewHolder.cooktime.setVisibility(View.GONE);
        //viewHolder.ingredients.setVisibility(View.GONE);
        //viewHolder.serving.setVisibility(View.GONE);
        //viewHolder.comments.setVisibility(View.GONE);
        //viewHolder.comment.setVisibility(View.GONE);
        //viewHolder.publisher.setVisibility(View.GONE);
        //viewHolder.caption.setVisibility(View.GONE);


        publisherInfo(viewHolder.image_profile, viewHolder.username, viewHolder.publisher, post.getPublisher());
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
                editor.putString("HomeFragment", null);
                editor.apply();


                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();


            }
        });



   /*     viewHolder.layout_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RecipeActivity.class);
                intent.putExtra("postid", post.getPostid());
                intent.putExtra("title",post.getCaption());
                intent.putExtra("caption",post.getTitle());
                intent.putExtra("ingredients",post.getIngredients());
                intent.putExtra("steps",post.getSteps());
                Glide.with(mContext).load(post.getPostimage()).apply(new RequestOptions().override(55,55))
                        .into(viewHolder.post_image);
                mContext.startActivity(intent);
            }
        }); */

        viewHolder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", post.getPublisher());
                editor.apply();

                Intent intent = new Intent(mContext, RecipeActivity.class);
                mContext.startActivity(intent);

             //   ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
             //           new ProfileFragment()).commit();
            }
        });

        /*viewHolder.publisher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", post.getPublisher());
                editor.apply();

                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
            }
        }); */

        viewHolder.post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("postid", post.getPostid());
              editor.apply();
            //    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                //       new PostDetailFragment()).commit();
                Bundle bundle = new Bundle();
                bundle.putString("postid", post.getPostid());
                firebaseAnalytics.logEvent("view_post",bundle);

                Intent intent = new Intent(mContext, RecipeActivity.class);
                mContext.startActivity(intent);

                //viewHolder.steps.setVisibility(View.VISIBLE);
                //viewHolder.cooktime.setVisibility(View.VISIBLE);
                //viewHolder.serving.setVisibility(View.VISIBLE);
               // viewHolder.ingredients.setVisibility(View.VISIBLE);

             /*   String str_username = viewHolder.username.getText().toString();
                String str_fullName = viewHolder.fullName.getText().toString();
                String str_caption = viewHolder.caption.getText().toString();
                String str_title = viewHolder.title.getText().toString();
                String str_comments = viewHolder.comments.getText().toString();
                String str_likes = viewHolder.likes.getText().toString();
            //    Intent intent = new Intent("custom-message");
                Intent intent = new Intent(mContext, RecipeActivity.class);
                intent.putExtra("postid", post.getPostid());
                intent.putExtra("title",post.getTitle());
                intent.putExtra("caption",post.getCaption());
                intent.putExtra("ingredients",post.getIngredients());
                intent.putExtra("steps",post.getSteps());
                intent.putExtra("post_image",post.getPostimage());
                intent.putExtra("username",post.getPublisher());
                intent.putExtra("fullName",str_fullName);
                intent.putExtra("comments",str_comments);
                intent.putExtra("likes",str_likes);
              //  Glide.with(mContext).load(post.getPostimage()).apply(new RequestOptions().override(55,55))
               //         .into(viewHolder.post_image);
              //  LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                mContext.startActivity(intent); */

            }
        });
     /*   viewHolder.serving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("serving",post.getServing());
                editor.apply();

               // ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
               //         new PostDetailFragment()).commit();
            }
        }); */

       /* viewHolder.cooktime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("cooktime",post.getCookTime());
                editor.apply();

                // ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                //         new PostDetailFragment()).commit();
            }
        }); */

       /* viewHolder.caption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("caption", post.getCaption());
                editor.apply();

                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PostDetailFragment()).commit();
            }
        }); */

        viewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("title",post.getTitle());
                editor.apply();

                Intent intent = new Intent(mContext, RecipeActivity.class);
                mContext.startActivity(intent);
             //   ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
             //           new PostDetailFragment()).commit();
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

/*        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postid", post.getPostid());
                intent.putExtra("publisherid", post.getPublisher());
                mContext.startActivity(intent);

            }
        }); */

    /*    viewHolder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postid", post.getPostid());
                intent.putExtra("publisherid", post.getPublisher());
                mContext.startActivity(intent);

            }
        }); */


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
                                //Toast.makeText(mContext, "Reported clicked!", Toast.LENGTH_SHORT).show();
                                reportPost(post.getPostid());
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
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_profile, post_image, like, comment, save, more;

        public Button layout_image;

        public TextView username, likes, publisher, caption, comments, fullName, title, cooktime, serving, steps, ingredients;


        private TextView tvIngredient;
        private RecyclerView rvIngredient;



        public ViewHolder(@NonNull View itemView) {
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
            //layout_image = itemView.findViewById(R.id.layout_image);
           // rvIngredient = itemView.findViewById(R.id.recycler_ingredients);
            //tvIngredient = itemView.findViewById(R.id.tv_item_title);


        }
    }

   private void reportPost(final String postid) {
       Context context = mContext.getApplicationContext();
       LinearLayout layout = new LinearLayout(context);
       layout.setOrientation(LinearLayout.VERTICAL);

       final Spinner spinner = new Spinner(context);
       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view,
                                      int position, long id) {
               switch (position) {
                   case 0:
                       reportType = 0;
                       reportTypeName = "Please Select A Problem";
                       break;
                   case 1:
                       reportType = 1;
                       reportTypeName = "Explicit Content";
                       break;
                   case 2:
                       reportType = 2;
                       reportTypeName = "Violence";
                       break;
                   case 3:
                       reportType = 3;
                       reportTypeName = "Harrassment";
                       break;
                   case 4:
                       reportType = 4;
                       reportTypeName = "Suicide or Self Injury";
                       break;
                   case 5:
                       reportType = 5;
                       reportTypeName = "False Information";
                       break;
                   case 6:
                       reportType = 6;
                       reportTypeName = "Hate Speech";
                       break;
                   case 7:
                       reportType = 7;
                       reportTypeName = "Terrorism";
                       break;
                   case 8:
                       reportType = 8;
                       reportTypeName = "Spam";
                       break;
                   case 9:
                       reportType = 9;
                       reportTypeName = "Spam";
                       break;
               }
           }
           @Override
           public void onNothingSelected(AdapterView<?> parent) {
               // TODO Auto-generated method stub
           }
       });
       final ArrayAdapter<String> arrayReport = new ArrayAdapter<String>(mContext,
               android.R.layout.simple_spinner_item, str_report);

       spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
       spinner.setAdapter(arrayReport);
       layout.addView(spinner);

       AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
       alertDialog.setTitle("Report Recipe");
       alertDialog.setView(layout);

       final EditText reportBox = new EditText(context);
       reportBox.setHint("Additional Comments (Optional)");
       layout.addView(reportBox);

       alertDialog.setPositiveButton("Report",
               new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       //String finalReport = reportTypeName;
                       String reportComment = reportBox.getText().toString();
                       if(reportType == 0){
                           Toast.makeText(context.getApplicationContext(), "Please select a problem", Toast.LENGTH_SHORT).show();
                       }else {
                           HashMap<String, Object> hashMap = new HashMap<>();
                           hashMap.put("postid", postid);
                           hashMap.put("type",reportTypeName);
                           if(reportComment == ""){
                               hashMap.put("comments","-");
                           } else {
                               hashMap.put("comments",reportComment);
                           }

                           // DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Report");
                           // reference.child(postid).setValue(hashMap);

                           FirebaseFirestore fStore = FirebaseFirestore.getInstance();

                           fStore.collection("report").document().set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void unused) {
                                   Toast.makeText(context.getApplicationContext(), "Report Successfully", Toast.LENGTH_SHORT).show();
                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Toast.makeText(context.getApplicationContext(), "Error.", Toast.LENGTH_SHORT).show();

                               }
                           });


                       }
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


    private void publisherInfo(final ImageView image_profile, final TextView username, final TextView publisher, final String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                Glide.with(mContext).load(user.getImageurl())
                        .apply(new RequestOptions().placeholder(R.drawable.ic_person))
                        .into(image_profile);
                username.setText(user.getUsername());
                //fullName.setText(user.getFullName());

                //publisher.setText(user.getUsername());

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
