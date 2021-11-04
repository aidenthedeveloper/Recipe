package com.example.recipe.Adapter;

import  android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.recipe.CommentActivity;
import com.example.recipe.FollowersActivity;
import com.example.recipe.Fragment.PostDetailFragment;
import com.example.recipe.Fragment.ProfileFragment;
import com.example.recipe.HomeActivity;
import com.example.recipe.Model.Ingredients;
import com.example.recipe.Model.Post;
import com.example.recipe.Model.Steps;
import com.example.recipe.Model.User;
import com.example.recipe.PostActivity;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>{

    private Context mContext;
    private List<Post> mPost;

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    //private List<Recipe> recipeList;

    Fragment selectedFragment = null;

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference userRef = fStore.collection("users");
    CollectionReference postRef = fStore.collection("posts");

    ArrayList<String> ingredientList = new ArrayList<>();
    String[] ingredientLists;
    ArrayList<String> stepList = new ArrayList<>();
    ArrayList<String> ingreList = new ArrayList<>();
    String[] stepLists,ingreLists;


    String[] str_report = { "Please Select A Problem", "Explicit Content", "Violence", "Harrassment", "Suicide or Self Injury", "False Information",
            "Hate Speech", "Terrorism", "Spam"};
    int reportType = 0;
    String reportTypeName = null;

    private FirebaseUser firebaseUser;

    public RecipeAdapter(Context context, List<Post> mPost/*,List<Recipe> recipeList*/) {
        this.mContext = context;
        this.mPost = mPost;
        //this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_item, viewGroup, false);
        return new RecipeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final Post post = mPost.get(i);

        //new implement
//        viewHolder.tvIngredient.setText(post.getIngredients());

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

        Glide.with(mContext).load(post.getPostimage())
                .apply(new RequestOptions().placeholder(R.drawable.ic_image))
                .into(viewHolder.post_image);

        viewHolder.image_profile.setVisibility(View.VISIBLE);

        if (post.getCaption().equals("")) {
            viewHolder.caption.setVisibility(View.GONE);
        } else {
            viewHolder.caption.setVisibility(View.VISIBLE);
            viewHolder.caption.setText(post.getCaption());
        }
        if (post.getTitle().equals("")) {
            viewHolder.title.setVisibility(View.GONE);
        } else {
            viewHolder.title.setVisibility(View.VISIBLE);
            viewHolder.title.setText(post.getTitle());
        }
        if ("".equals(post.getCookTime())) {
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

        viewHolder.ingredients.setVisibility(View.GONE);
        viewHolder.steps.setVisibility(View.GONE);



        publisherInfo(viewHolder.image_profile, viewHolder.username, viewHolder.fullName, viewHolder.publisher, post.getPublisher());
        isLiked(post.getPostid(), viewHolder.like);
        nrLikes(viewHolder.likes, post.getPostid());
        getComments(post.getPostid(), viewHolder.comments);
        isSaved(post.getPostid(),viewHolder.save);

        viewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("title",post.getTitle());
                editor.apply();

               // ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
              //          new PostDetailFragment()).commit();
            }
        });

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


        viewHolder.rlImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", post.getPublisher());
                editor.apply();


                FragmentManager fm = ((FragmentActivity) mContext).getSupportFragmentManager();
                Bundle arguments = new Bundle();
                arguments.putInt("VALUE1", 1);

                ProfileFragment profileFragment = new ProfileFragment();
                profileFragment.setArguments(arguments);


                fm.beginTransaction().replace(R.id.fragment_container, profileFragment).commit();

             //   ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
             //           new ProfileFragment()).commit();

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

            //    Intent intent = new Intent(mContext, RecipeActivity.class);
            //    mContext.startActivity(intent);

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

        viewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("title",post.getTitle());
                editor.apply();

             //  ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
             //           new PostDetailFragment()).commit();
            }
        });

        viewHolder.caption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("title",post.getTitle());
                editor.putString("caption", post.getCaption());
                editor.apply();

               // ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
               //         new PostDetailFragment()).commit();
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

        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postid", post.getPostid());
                intent.putExtra("publisherid", post.getPublisher());
                mContext.startActivity(intent);

            }
        });

        viewHolder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postid", post.getPostid());
                intent.putExtra("publisherid", post.getPublisher());
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
                                reportPost(post.getPostid());
                               // Toast.makeText(mContext, "Reported clicked!", Toast.LENGTH_SHORT).show();
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

        public RelativeLayout rlImage;

        public Button layout_image;

        public TextView username, likes, publisher, caption, comments, fullName, fullName2, title, cooktime, serving, steps, ingredients;

        private TextView tvIngredient;
        private RecyclerView rvIngredient;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cooktime = itemView.findViewById(R.id.cooktime);
            serving = itemView.findViewById(R.id.serving);
            ingredients = itemView.findViewById(R.id.ingredients);
            steps = itemView.findViewById(R.id.steps);

            rlImage = itemView.findViewById(R.id.rl_Image);
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
            fullName = itemView.findViewById(R.id.fullname2);
            title = itemView.findViewById(R.id.title);
            //layout_image = itemView.findViewById(R.id.layout_image);
            //rvIngredient = itemView.findViewById(R.id.recycler_ingredients);
            //tvIngredient = itemView.findViewById(R.id.tv_item_title);


        }
    }

    private void getComments(String postId, final TextView comments) {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Comments").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                comments.setText("View All " + dataSnapshot.getChildrenCount() + " Comments");
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
                    imageView.setImageResource(R.drawable.ic_heart_black);
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


    private void publisherInfo(final ImageView image_profile, final TextView username, final TextView fullName, final TextView publisher, final String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                fullName.setText(user.getFullName());
                Glide.with(mContext.getApplicationContext()).load(user.getImageurl())
                        .apply(new RequestOptions().placeholder(R.drawable.ic_person))
                        .into(image_profile);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

        final TextView cooktimeView = new TextView(context);
        cooktimeView.setText("Edit Cook Time");
        layout.addView(cooktimeView);

        // Add another TextView here for the "Description" label
        final EditText cooktimeBox = new EditText(context);
        cooktimeBox.setHint("Cook Time");
        layout.addView(cooktimeBox); // Another add method

        final TextView servingView = new TextView(context);
        servingView.setText("Edit Serving");
        layout.addView(servingView);

        // Add another TextView here for the "Description" label
        final EditText servingBox = new EditText(context);
        servingBox.setHint("Serving");
        layout.addView(servingBox); // Another add method

        final TextView ingreView = new TextView(context);
        ingreView.setText("Edit Ingredients:");
        layout.addView(ingreView);

        // Add another TextView here for the "Description" label
        final EditText ingreBox = new EditText(context);
        ingreBox.setHint("200 grams of sugar \n 300 ml of water \n 1 pound of flour");
        layout.addView(ingreBox); // Another add method

        final TextView stepView = new TextView(context);
        stepView.setText("Edit Steps");
        layout.addView(stepView);

        // Add another TextView here for the "Description" label
        final EditText stepBox = new EditText(context);
        stepBox.setHint("1.))Heat the oven to 400C. \n 2.))Cut the Sweet potatoes into sticks. \n " +
                "3.))Mix the spices, salt and pepper in a small bowl.");
        layout.addView(stepBox); // Another add method

        ListView stepListView = new ListView(context);
        layout.addView(stepListView);


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Edit Recipe");
        final ScrollView scrollView = new ScrollView(mContext);
        scrollView.addView(layout);

        alertDialog.setView(scrollView); // Again this is a set method, not add

        //final EditText editText2 = new EditText(mContext);
        //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
        //        LinearLayout.LayoutParams.MATCH_PARENT,
        //        LinearLayout.LayoutParams.MATCH_PARENT);
        //editText2.setLayoutParams(lp);
        //alertDialog.setView(editText2);

        getText(postid, titleBox,postid,captionBox,cooktimeBox,servingBox,ingreBox,stepBox);

        alertDialog.setPositiveButton("Edit",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String str_title = titleBox.getText().toString();
                        String str_caption = captionBox.getText().toString();
                        String str_cooktime = cooktimeBox.getText().toString();
                        String str_serving = servingBox.getText().toString();
                        String str_stepCheck = stepBox.getText().toString();
                        String str_ingredients = ingreBox.getText().toString();
                        String of = " of ";
                        String dotBracket = ".))";

                        if(TextUtils.isEmpty(str_title)){
                            Toast.makeText(context.getApplicationContext(), "Update Failed. Title cannot be empty.", Toast.LENGTH_SHORT).show();
                        }
                        else if(TextUtils.isEmpty(str_cooktime)){
                            Toast.makeText(context.getApplicationContext(), "Update Failed. Cooktime cannot be empty.", Toast.LENGTH_SHORT).show();
                        }
                        else if(TextUtils.isEmpty(str_serving)){
                            Toast.makeText(context.getApplicationContext(), "Update Failed. Serving cannot be empty.", Toast.LENGTH_SHORT).show();
                        }
                        else if(TextUtils.isEmpty(str_stepCheck)){
                            Toast.makeText(context.getApplicationContext(), "Update Failed. Step cannot be empty.", Toast.LENGTH_SHORT).show();
                        }
                        else if(TextUtils.isEmpty(str_ingredients)){
                            Toast.makeText(context.getApplicationContext(), "Update Failed. Ingredients cannot be empty.", Toast.LENGTH_SHORT).show();
                        }
                        else if (!str_ingredients.contains(of) ) {
                            Toast.makeText(context.getApplicationContext(), "Update Failed. Ingredients format is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                        else if (!str_stepCheck.contains(dotBracket)) {
                            Toast.makeText(context.getApplicationContext(), "Update Failed. Steps format is incorrect.", Toast.LENGTH_SHORT).show();
                        }

                        else {

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("title", titleBox.getText().toString());
                        hashMap.put("lowerTitle", titleBox.getText().toString().toLowerCase());
                        hashMap.put("caption", captionBox.getText().toString());
                        hashMap.put("cooktime", cooktimeBox.getText().toString());
                        hashMap.put("serving", servingBox.getText().toString());
                       // hashMap.put("steps", stepBox.getText().toString());

                        String str_step = stepBox.getText().toString();
                        String[] stepArray,stepArray2,stepArray3;
                        List<String> stepDatas;
                        stepArray2 = str_step.split("\\s*\\.\\)\\)|\n\\s*");
                        stepArray = str_step.split("\\s*\n\\s*");
                        stepDatas = Arrays.asList(stepArray);

                        int o = 1, p = 0;
                        ArrayList<Steps> stepsArrayList = new ArrayList<>();
                        for (int k = 0; k < stepArray.length; k++) {
                            Steps steps = new Steps();
                            String str_step3 = null;
                            String str_step2 = null;
                            str_step3 = stepArray2[o];
                            str_step2 = stepArray2[p];

                            steps.setSteps(str_step3);
                            //steps.setStepsPosition(String.valueOf(k));
                            steps.setStepsPosition(str_step2);
                            stepsArrayList.add(steps);
                            o++;
                            o++;
                            p++;
                            p++;
                        }

                        String str_ingre = ingreBox.getText().toString();
                        String[] ingreArray,ingreArray2;
                        List<String> ingreDatas;
                        ingreArray = str_ingre.split("\\s*\n\\s*");
                        ingreArray2 = str_ingre.split("\\s*\n|of\\s*");
                        ingreDatas = Arrays.asList(ingreArray);

                        int l = 1, m = 0;
                        ArrayList<Ingredients> ingredientsArrayList = new ArrayList<>();
                        ArrayList<Ingredients> ingredientsOnlyList = new ArrayList<>();
                        for (int k = 0; k < ingreArray.length; k++) {
                            Ingredients ingredients = new Ingredients();
                            Ingredients ingredients2 = new Ingredients();
                            String str_ingre2 = null;
                            String str_ingre3 = null;
                            String str_ingre4 = null;
                            str_ingre2 = ingreArray[k];
                            str_ingre3 = ingreArray2[l];
                            str_ingre4 = ingreArray2[m];

                            ingredients.setIngredients(str_ingre3);
                            ingredients.setAmount(str_ingre4);
                            ingredients.setTotalIng(str_ingre2);
                            ingredients2.setIngredients(str_ingre3);
                            ingredientsArrayList.add(ingredients);
                            ingredientsOnlyList.add(ingredients2);
                            l++;
                            l++;
                            m++;
                            m++;
                        }


                        //hashMap.put("steps", Arrays.asList(stepLists));
                        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Posts");
                        reference.child(postid).child("Steps").setValue(stepsArrayList);
                        reference.child(postid).child("PlainIngredients").setValue(ingredientsOnlyList);
                        reference.child(postid).child("Ingredients").setValue(ingredientsArrayList);

                        FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Posts")
                                .child(postid).updateChildren(hashMap);

                        postRef.document(postid).update("title",titleBox.getText().toString());
                        postRef.document(postid).update("lowerTitle",titleBox.getText().toString().toLowerCase());
                        postRef.document(postid).update("caption",captionBox.getText().toString());
                        postRef.document(postid).update("cooktime",cooktimeBox.getText().toString());
                        postRef.document(postid).update("serving",servingBox.getText().toString());
                        postRef.document(postid).update("steps",Arrays.asList(stepArray));
                        postRef.document(postid).update("ingredients",Arrays.asList(ingreArray));


                        //Intent intent = new Intent(mContext, RecipeActivity.class);
                        //mContext.startActivity(intent);

                        Intent intent = new Intent(mContext, RecipeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);

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

    private void getText(String s, EditText titleBox, String postid, final EditText captionBox, EditText cooktimeBox, EditText servingBox, EditText ingreBox, EditText stepBox) {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Posts")
                .child(postid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                titleBox.setText(dataSnapshot.getValue(Post.class).getTitle());
                captionBox.setText(dataSnapshot.getValue(Post.class).getCaption());
                cooktimeBox.setText(dataSnapshot.getValue(Post.class).getCookTime());
                servingBox.setText(dataSnapshot.getValue(Post.class).getServing());

                DatabaseReference reference2 = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Posts").child(postid).child("Ingredients");
                reference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ingreList.clear();
                        String data = "";
                        String data2 = "";
                        String data3 = "";
                        int i = 1;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Ingredients ingredients = snapshot.getValue(Ingredients.class);
                            data = ingredients.getIngredients();
                            data2 = ingredients.getAmount();
                            data3 = ingredients.getTotalIng();
                            ingreList.add(data3 + "\n");
                            i++;
                            //layout.addView(stepList);

                        }
                        ingreList.trimToSize();
                        ingreList.toArray(new String[0]);
                        ingreLists = ingreList.toArray(new String[ingreList.size()]);

                        ingreBox.setText(Arrays.toString(ingreLists).replaceAll("\n, ","\n").replaceAll("\\[|\\]", ""));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Posts").child(postid).child("Steps");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        stepList.clear();
                        String data = "";
                        String data2 = "";
                        int i = 1;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Steps steps = snapshot.getValue(Steps.class);
                            data = steps.getSteps();
                            data2 = steps.getStepsPosition();
                            stepList.add(i + ".))" + data + "\n");
                            i++;
                            //layout.addView(stepList);

                        }
                        stepList.trimToSize();
                        stepList.toArray(new String[0]);
                        stepLists = stepList.toArray(new String[stepList.size()]);

                        stepBox.setText(Arrays.toString(stepLists).replaceAll("\n, ","\n").replaceAll("\\[|\\]", ""));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
