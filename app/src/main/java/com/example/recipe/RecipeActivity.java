package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipe.Adapter.CommentAdapter;
import com.example.recipe.Adapter.IngredientsAdapter;
import com.example.recipe.Adapter.PostAdapter;
import com.example.recipe.Adapter.Recipe2Adapter;
import com.example.recipe.Adapter.RecipeAdapter;
import com.example.recipe.Adapter.RecommendAdapter;
import com.example.recipe.Adapter.StepAdapter;
import com.example.recipe.Fragment.HomeFragment;
import com.example.recipe.Model.CombineData;
import com.example.recipe.Model.Comment;
import com.example.recipe.Model.Ingredients;
import com.example.recipe.Model.Post;
import com.example.recipe.Model.Steps;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeActivity extends AppCompatActivity {

    String postid;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private RecyclerView recyclerView_detailed;
    private RecipeAdapter recipeAdapter;
    private List<Post> postList;
    private IngredientsAdapter recommendAdapter;
    LayoutInflater inflater;

    Ingredients ingredients;
    ArrayList<String> ingredientsList = new ArrayList<>();
    ArrayList<String> stepsList = new ArrayList<>();
    ArrayList<Integer> stepsPosList = new ArrayList<>();

    Fragment selectedFragment = null;

    ArrayAdapter <String> ingredientsAdapter;
    private int countIngredients;
    //NEW METHOD
    ListView listView;


    private RecyclerView recyclerView2,recyclerView3;
    private StepAdapter stepAdapterr;
    private IngredientsAdapter ingredientAdapterr;
    private List<Steps> stepListt;
    private List<Ingredients> ingredientsListt;
    private List<Ingredients> recommendList;
    String recoIngList;
    String tag;
    String[] ingredientLists;
    ArrayList<String> ingredientList = new ArrayList<>();


    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference ingreRef = fStore.collection("posts");

    FirebaseAnalytics firebaseAnalytics;


    Button btnStartCook;
    TextView tvSteps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Recipe");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, 0);
                overridePendingTransition(0, 0);
            }
        });

        Intent intent = getIntent();
        postid = intent.getStringExtra("postid");

       /* Intent intent = getIntent();
        postid = intent.getStringExtra("postid");
        String str_title = intent.getStringExtra("title");
        String str_caption = intent.getStringExtra("caption");
        String str_steps = intent.getStringExtra("steps");
        String str_ingredients = intent.getStringExtra("ingredients");
        String str_username = intent.getStringExtra("username");
        String str_fullName = intent.getStringExtra("fullName");
        String str_likes = intent.getStringExtra("likes");
        String str_comments = intent.getStringExtra("comments");

        image_profile = findViewById(R.id.image_profile);
        post_image = findViewById(R.id.post_image);
        like = findViewById(R.id.like);
        comment = findViewById(R.id.comment);
        save = findViewById(R.id.save);
        username = findViewById(R.id.username);
        likes = findViewById(R.id.likes);
        caption = findViewById(R.id.caption);
        comments = findViewById(R.id.comments);
        more = findViewById(R.id.more);
        fullName = findViewById(R.id.fullname);
        title = findViewById(R.id.title);

//        username.setText(str_username);
        title.setText(str_title);
        caption.setText(str_caption);
//        ingredients.setText(str_ingredients);
 //       steps.setText(str_steps);
 //       fullName.setText(str_fullName);
        likes.setText(str_likes);
        comments.setText(str_comments); */

        SharedPreferences prefs = getSharedPreferences("PREFS", MODE_PRIVATE);
        postid = prefs.getString("postid", "none");

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(this, postList);
        //stepsAdapter2 = new StepsAdapter(this, stepList);
        recyclerView.setAdapter(postAdapter);

        recyclerView_detailed = findViewById(R.id.recycler_view_detailed);
        recyclerView_detailed.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager_detailed = new LinearLayoutManager(this);
        recyclerView_detailed.setLayoutManager(mLayoutManager_detailed);

        recipeAdapter = new RecipeAdapter(this, postList);
        recyclerView_detailed.setAdapter(recipeAdapter);

        recyclerView.setVisibility(View.GONE);
        recyclerView_detailed.setVisibility(View.VISIBLE);

       // listView = findViewById(R.id.listView);
        //stepsView = findViewById(R.id.stepView);

        //recyclerView_details = findViewById(R.id.recycler_view_details);
       // recyclerView_details.setHasFixedSize(true);
      //  LinearLayoutManager mLayoutManager_details = new LinearLayoutManager(this);
       // recyclerView_details.setLayoutManager(mLayoutManager_details);

        //recipeAdapter2 = new Recipe2Adapter(this, postList);
        //recyclerView_details.setAdapter(recipeAdapter2);

        //recyclerView_details.setVisibility(View.VISIBLE);

        recyclerView2 = findViewById(R.id.recycler_view_steps);
        recyclerView2.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(this);
        recyclerView2.setLayoutManager(mLayoutManager2);
        stepListt = new ArrayList<>();
        stepAdapterr = new StepAdapter(this, stepListt);
        recyclerView2.setAdapter(stepAdapterr);

        recyclerView3 = findViewById(R.id.recycler_view_ingredients);
        recyclerView3.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager3 = new LinearLayoutManager(this);
        recyclerView3.setLayoutManager(mLayoutManager3);
        ingredientsListt = new ArrayList<>();
        ingredientAdapterr = new IngredientsAdapter(this, ingredientsListt);
        recyclerView3.setAdapter(ingredientAdapterr);

        recommendList = new ArrayList<>();
        recommendAdapter = new IngredientsAdapter(this, recommendList);

        recyclerView2.setVisibility(View.GONE);

        fStore = FirebaseFirestore.getInstance();

        //ingList = new ArrayList<>();
        stepsList = new ArrayList<>();
        stepsPosList = new ArrayList<>();


        btnStartCook = findViewById(R.id.startCook);
        tvSteps = findViewById(R.id.tvStep);

        tvSteps.setVisibility(View.GONE);


        btnStartCook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvSteps.setVisibility(View.VISIBLE);
                recyclerView2.setVisibility(View.VISIBLE);

                DatabaseReference reference2 = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Recommend");
                reference2.child(firebaseUser.getUid()).setValue(recommendList);

                Bundle bundle = new Bundle();
                bundle.putStringArray("ingredients", ingredientLists);
                firebaseAnalytics.logEvent("start_cooking",bundle);

                //loadIngredients();

                HashMap<String, Object> hashMap = new HashMap<>();
                //hashMap.put("ingredients", Arrays.asList(recoIngList));
                hashMap.put("ingredients", Arrays.asList(ingredientLists));

                fStore.collection("recommend").document(firebaseUser.getUid()).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(RecipeActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RecipeActivity.this, "Error.", Toast.LENGTH_SHORT).show();

                    }
                });



            /*    DocumentReference documentReference = fStore.collection("recommend").document(firebaseUser.getUid());
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }) */
            }
        });


        //rvIngredients = findViewById(R.id.recycler_ingredients);
        //rvIngredients.setHasFixedSize(true);
       // rvIngredients = findViewById(R.id.recycler_ingredients);
     //   LinearLayoutManager ingLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
     //   PostAdapter postAdapter = new PostAdapter(buildIngList());
//        rvIngredients.setAdapter(postAdapter);
//        rvIngredients.setLayoutManager(ingLayoutManager);


     /*   listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                listView.requestDisallowInterceptTouchEvent(true);
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        listView.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        }); */

        //buildIngList();

        readRecommend();
        readRecipe();
        readIngredientss();
        readStepp();
        readTest();
       // readSteps();
       // getImage();

    }

    private void loadIngredients() {
        ingreRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";

                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Post post = documentSnapshot.toObject(Post.class);
                            post.setPostid(documentSnapshot.getId());

                            String postid = post.getPostid();

                            for (String ingredients : post.getIngredients()){
                                data += ingredients;
                                ingredientList.add(ingredients);
                            }
                            ingredientList.trimToSize();
                            ingredientList.toArray(new String[0]);
                            ingredientLists = ingredientList.toArray(new String[ingredientList.size()]);
                        }
                    }
                });

    }


   /* private List<Ingredients> buildIngList() {
        List<Ingredients> ingredientsList = new ArrayList<>();
        for (int i=0; i<ingredientsList.size(); i++) {
            Ingredients ingredients = new Ingredients("Ingredients " + ": "+ ingredientsList, "Amount "+i, "");
            //ingredientsList.add(new Ingredients("Ingredients " + ": ", "Amount "+, ""));
        }
        return ingredientsList;
    } */

    private void readStepp() {
        DatabaseReference reference2 = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Posts").child(postid).child("Steps");

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                stepListt.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Steps steps = snapshot.getValue(Steps.class);
                    stepListt.add(steps);
                    Collections.sort(stepListt,Steps.StepsPosComparator);
                }

                stepAdapterr.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void readIngredientss() {
        DatabaseReference reference2 = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Posts").child(postid).child("Ingredients");

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String data = "";
                ingredientsListt.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Ingredients ingredients = snapshot.getValue(Ingredients.class);
                    ingredientsListt.add(ingredients);
                    //data = ingredients.getIngredients();
                    recoIngList = ingredients.getIngredients().toString().toLowerCase();
                    //Toast.makeText(RecipeActivity.this, data, Toast.LENGTH_SHORT).show();
                }
                ingredientAdapterr.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
  /*  private void readSteps(){
        DatabaseReference reference2 = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Recipe").child(postid).child("Steps");
        stepsList = new ArrayList<>();
        stepsPosList = new ArrayList<>();
        //stepsFullList = new ArrayList<>();
        //stepsAdapter4 = new ArrayAdapter<CombineData>(this,R.layout.steps_item);

        //stepsAdapter = new ArrayAdapter<String>(this,R.layout.steps_item,R.id.etStepsDetails,stepsList);
        stepsAdapter2 = new ArrayAdapter<Integer>(this,R.layout.steps_item,R.id.tvStepsPosition,stepsPosList);
        //stepsAdapter3 = new ArrayAdapter<Steps>(this,R.layout.steps_item,R.id.etStepsDetails,R.id.tvStepsPosition,stepsList);

        stepsView.setAdapter(stepsAdapter);
        stepsView2.setAdapter(stepsAdapter2);

        DatabaseReference reference3 = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Recipe").child(postid);
        reference3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                countSteps = (int) snapshot.getChildrenCount();

                ViewGroup.LayoutParams listViewParams = (ViewGroup.LayoutParams)stepsView.getLayoutParams();
                listViewParams.height = 500 * countSteps;
                stepsView.requestLayout();
                //stepsView2.requestLayout();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String value = snapshot.getValue(Steps.class).toString();
                String value2 = snapshot.getValue(Steps.class).toString3();
                stepsList.add(value);
                //stepsPosList.add(value2);
                Collections.sort(stepsPosList);
                stepsAdapter.notifyDataSetChanged();
                //stepsAdapter2.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    } */
  /*  private void readIngredients(){
        DatabaseReference reference2 = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Recipe").child(postid).child("Ingredients");
        ingredientsList = new ArrayList<>();
        ingredientsAdapter = new ArrayAdapter<String>(this,R.layout.ingredients_info,R.id.ingredientsInfo,ingredientsList);
        listView.setAdapter(ingredientsAdapter);


        //displayList = new ArrayList<>();
        //Utility.setListViewHeightBasedOnChildren(listView);
        DatabaseReference reference3 = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Recipe").child(postid);
        reference3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                countIngredients = (int) snapshot.getChildrenCount();
                //Toast.makeText(getApplicationContext(), "Total number of Items are:" + countIngredients , Toast.LENGTH_LONG).show();

                ViewGroup.LayoutParams listViewParams = (ViewGroup.LayoutParams)listView.getLayoutParams();
                listViewParams.height = 120 * countIngredients;
                listView.requestLayout();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               // countIngredients = (int) snapshot.getChildrenCount();
                String value = snapshot.getValue(Ingredients.class).toString();
                ingredientsList.add(value);
                ingredientsAdapter.notifyDataSetChanged();
              //  Toast.makeText(getApplicationContext(), "Total number of Items are:" + countIngredients , Toast.LENGTH_LONG).show();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }); */
     /*           (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             //   if(dataSnapshot.exists()) {
                 //   displayList.clear();
                    for (DataSnapshot dss : dataSnapshot.getChildren()) {
                     //   Ingredients ingredients = dss.getValue(Ingredients.class);
                     //   displayList.add(ingredients);
                        ingredients = dss.getValue(Ingredients.class);
                        ingredientsList.add(ingredients.getTotalIng());
                    }
                    listView.setAdapter(ingredientsAdapter);
               //     StringBuilder stringBuilder = new StringBuilder();

              //      for (int i = 0; i < displayList.size(); i++)
              //      {
              //          stringBuilder.append(displayList.get(i)+ ",");
             //       }

            //    }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        }); */
    private void readRecipe(){
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Posts").child(postid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                postList.clear();
                Post post = dataSnapshot.getValue(Post.class);
                postList.add(post);

                recipeAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void readRecommend() {
        DatabaseReference reference2 = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Posts").child(postid).child("PlainIngredients");

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String data = "";
                recommendList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Ingredients ingredients = snapshot.getValue(Ingredients.class);
                    recommendList.add(ingredients);
                }
                recommendAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void readTest() {
        DatabaseReference reference3 = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Posts").child(postid).child("Ingredients");
        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String data = "";
                ingredientsListt.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Ingredients ingredients = snapshot.getValue(Ingredients.class);
                    ingredientsListt.add(ingredients);
                    data = ingredients.getIngredients();
                    ingredientList.add(data);
                    //Toast.makeText(RecipeActivity.this, data, Toast.LENGTH_SHORT).show();

                }
                ingredientList.trimToSize();
                ingredientList.toArray(new String[0]);
            ingredientLists = ingredientList.toArray(new String[ingredientList.size()]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
