package com.example.recipe.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipe.Adapter.PostAdapter;
import com.example.recipe.Model.Ingredients;
import com.example.recipe.Model.Post;
import com.example.recipe.Model.Recipe;
import com.example.recipe.Model.User;
import com.example.recipe.R;
import com.example.recipe.ReportActivity;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView, recyclerView2, recyclerView3,recyclerView4,recyclerView5,recyclerView6
            ,recyclerView7,recyclerView8,recyclerView9,recyclerView10;
    private PostAdapter postAdapter, postAdapter2, postAdapter3,postAdapter4,postAdapter5,postAdapter6
            ,postAdapter7,postAdapter8,postAdapter9,postAdapter10;
    private List<Post> postLists, postLists2, postLists3,postLists4,postLists5,postLists6,postLists7,
            postLists8,postLists9,postLists10;
    private List<String> followingList, selfList, recommendList, recommendList2;

    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference userRef = fStore.collection("users");
    CollectionReference postRef = fStore.collection("posts");
    CollectionReference recoRef = fStore.collection("recommend");

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    TextView tvReco, tvFollow, tvExp;

    String postid;

    ProgressBar progressBar;

    String[] recommendIngreList;
    List<String> ingreList;
    String[] ingredientLists;
    String[] ingredientNoLists = {"salt","sugar"};
    String[] test = {"sugar", "b"};
    ArrayList<String> ingredientList = new ArrayList<>();
    ArrayList<String> ingredientNoList = new ArrayList<>();

    Spinner dropdownFood;
    int tabChange = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        tvReco = view.findViewById(R.id.recommend_bar);
        tvFollow = view.findViewById(R.id.following_bar);
        tvExp = view.findViewById(R.id.explore_bar);

        dropdownFood = view.findViewById(R.id.spinnerFood);

        String[] foodType = new String[]{"All", "Vegetables", "Fruits", "Grains,Legumes,Nuts & Seeds", "Meat & Poultry", "Fish and Seafood", "Dairy Foods", "Eggs"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, foodType);
        dropdownFood.setAdapter(adapter);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        postLists = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postLists);
        recyclerView.setAdapter(postAdapter);

        recyclerView2 = view.findViewById(R.id.recycler_view_all);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        linearLayoutManager2.setReverseLayout(true);
        linearLayoutManager2.setStackFromEnd(true);
        recyclerView2.setLayoutManager(linearLayoutManager2);
        postLists2 = new ArrayList<>();
        postAdapter2 = new PostAdapter(getContext(), postLists2);
        recyclerView2.setAdapter(postAdapter2);

        recyclerView3 = view.findViewById(R.id.recycler_view_recommend);
        recyclerView3.setHasFixedSize(true);
        recyclerView3.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getContext());
        linearLayoutManager3.setReverseLayout(true);
        linearLayoutManager3.setStackFromEnd(true);
        recyclerView3.setLayoutManager(linearLayoutManager3);
        postLists3 = new ArrayList<>();
        postAdapter3 = new PostAdapter(getContext(), postLists3);
        recyclerView3.setAdapter(postAdapter3);

        recyclerView4 = view.findViewById(R.id.recycler_view_type1);
        recyclerView4.setHasFixedSize(true);
        recyclerView4.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(getContext());
        linearLayoutManager4.setReverseLayout(true);
        linearLayoutManager4.setStackFromEnd(true);
        recyclerView4.setLayoutManager(linearLayoutManager4);
        postLists4 = new ArrayList<>();
        postAdapter4 = new PostAdapter(getContext(), postLists4);
        recyclerView4.setAdapter(postAdapter4);

        recyclerView5 = view.findViewById(R.id.recycler_view_type2);
        recyclerView5.setHasFixedSize(true);
        recyclerView5.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager5 = new LinearLayoutManager(getContext());
        linearLayoutManager5.setReverseLayout(true);
        linearLayoutManager5.setStackFromEnd(true);
        recyclerView5.setLayoutManager(linearLayoutManager5);
        postLists5 = new ArrayList<>();
        postAdapter5 = new PostAdapter(getContext(), postLists5);
        recyclerView5.setAdapter(postAdapter5);

        recyclerView6 = view.findViewById(R.id.recycler_view_type3);
        recyclerView6.setHasFixedSize(true);
        recyclerView6.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager6 = new LinearLayoutManager(getContext());
        linearLayoutManager6.setReverseLayout(true);
        linearLayoutManager6.setStackFromEnd(true);
        recyclerView6.setLayoutManager(linearLayoutManager6);
        postLists6 = new ArrayList<>();
        postAdapter6 = new PostAdapter(getContext(), postLists6);
        recyclerView6.setAdapter(postAdapter6);

        recyclerView7 = view.findViewById(R.id.recycler_view_type4);
        recyclerView7.setHasFixedSize(true);
        recyclerView7.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager7 = new LinearLayoutManager(getContext());
        linearLayoutManager7.setReverseLayout(true);
        linearLayoutManager7.setStackFromEnd(true);
        recyclerView7.setLayoutManager(linearLayoutManager7);
        postLists7 = new ArrayList<>();
        postAdapter7 = new PostAdapter(getContext(), postLists7);
        recyclerView7.setAdapter(postAdapter7);

        recyclerView8 = view.findViewById(R.id.recycler_view_type5);
        recyclerView8.setHasFixedSize(true);
        recyclerView8.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager8 = new LinearLayoutManager(getContext());
        linearLayoutManager8.setReverseLayout(true);
        linearLayoutManager8.setStackFromEnd(true);
        recyclerView8.setLayoutManager(linearLayoutManager8);
        postLists8 = new ArrayList<>();
        postAdapter8 = new PostAdapter(getContext(), postLists8);
        recyclerView8.setAdapter(postAdapter8);

        recyclerView9 = view.findViewById(R.id.recycler_view_type6);
        recyclerView9.setHasFixedSize(true);
        recyclerView9.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager9 = new LinearLayoutManager(getContext());
        linearLayoutManager9.setReverseLayout(true);
        linearLayoutManager9.setStackFromEnd(true);
        recyclerView9.setLayoutManager(linearLayoutManager9);
        postLists9 = new ArrayList<>();
        postAdapter9 = new PostAdapter(getContext(), postLists9);
        recyclerView9.setAdapter(postAdapter9);

        recyclerView10 = view.findViewById(R.id.recycler_view_type7);
        recyclerView10.setHasFixedSize(true);
        recyclerView10.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager10 = new LinearLayoutManager(getContext());
        linearLayoutManager10.setReverseLayout(true);
        linearLayoutManager10.setStackFromEnd(true);
        recyclerView10.setLayoutManager(linearLayoutManager10);
        postLists10 = new ArrayList<>();
        postAdapter10 = new PostAdapter(getContext(), postLists10);
        recyclerView10.setAdapter(postAdapter10);



        tvReco.setTypeface(null, Typeface.BOLD);
        recyclerView.setVisibility(View.GONE);
        recyclerView2.setVisibility(View.GONE);
        recyclerView3.setVisibility(View.GONE);
        recyclerView4.setVisibility(View.GONE);
        recyclerView5.setVisibility(View.GONE);
        recyclerView6.setVisibility(View.GONE);
        recyclerView7.setVisibility(View.GONE);
        recyclerView8.setVisibility(View.GONE);
        recyclerView9.setVisibility(View.GONE);
        recyclerView10.setVisibility(View.GONE);
        dropdownFood.setVisibility(View.GONE);

        SharedPreferences prefs = getActivity().getSharedPreferences("PREFS", getActivity().MODE_PRIVATE);
        postid = prefs.getString("postid", "none");

        //loadRecommend();
        //checkTesting();
        readReco();
        checkFollowing();
       // checkRecommend();
        readRecipe();
        readType1();
        readType2();
        readType3();
        readType4();
        readType5();
        readType6();
        readType7();


        tvReco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.GONE);
                //recyclerView3.setVisibility(View.VISIBLE);
                recyclerView4.setVisibility(View.GONE);
                recyclerView5.setVisibility(View.GONE);
                recyclerView6.setVisibility(View.GONE);
                recyclerView7.setVisibility(View.GONE);
                recyclerView8.setVisibility(View.GONE);
                recyclerView9.setVisibility(View.GONE);
                recyclerView10.setVisibility(View.GONE);
                tvReco.setTypeface(null, Typeface.BOLD);
                tvFollow.setTypeface(null, Typeface.NORMAL);
                tvExp.setTypeface(null, Typeface.NORMAL);
                dropdownFood.setVisibility(View.GONE);
                readReco();
            }
        });

        tvFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView2.setVisibility(View.GONE);
                recyclerView3.setVisibility(View.GONE);
                recyclerView4.setVisibility(View.GONE);
                recyclerView5.setVisibility(View.GONE);
                recyclerView6.setVisibility(View.GONE);
                recyclerView7.setVisibility(View.GONE);
                recyclerView8.setVisibility(View.GONE);
                recyclerView9.setVisibility(View.GONE);
                recyclerView10.setVisibility(View.GONE);
                tvReco.setTypeface(null, Typeface.NORMAL);
                tvExp.setTypeface(null, Typeface.NORMAL);
                tvFollow.setTypeface(null, Typeface.BOLD);
                dropdownFood.setVisibility(View.GONE);

            }
        });

        tvExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.VISIBLE);
                recyclerView3.setVisibility(View.GONE);
                recyclerView4.setVisibility(View.GONE);
                recyclerView5.setVisibility(View.GONE);
                recyclerView6.setVisibility(View.GONE);
                recyclerView7.setVisibility(View.GONE);
                recyclerView8.setVisibility(View.GONE);
                recyclerView9.setVisibility(View.GONE);
                recyclerView10.setVisibility(View.GONE);
                tvExp.setTypeface(null, Typeface.BOLD);
                tvReco.setTypeface(null, Typeface.NORMAL);
                tvFollow.setTypeface(null, Typeface.NORMAL);
                dropdownFood.setVisibility(View.VISIBLE);
            }
        });

        dropdownFood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position) {
                    case 0:
                        tabChange = 0;
                        recyclerView.setVisibility(View.GONE);
                        recyclerView2.setVisibility(View.VISIBLE);
                        recyclerView3.setVisibility(View.GONE);
                        recyclerView4.setVisibility(View.GONE);
                        recyclerView5.setVisibility(View.GONE);
                        recyclerView6.setVisibility(View.GONE);
                        recyclerView7.setVisibility(View.GONE);
                        recyclerView8.setVisibility(View.GONE);
                        recyclerView9.setVisibility(View.GONE);
                        recyclerView10.setVisibility(View.GONE);
                        //readRecipe();
                        break;

                    case 1:
                        tabChange = 1;
                        recyclerView.setVisibility(View.GONE);
                        recyclerView2.setVisibility(View.GONE);
                        recyclerView3.setVisibility(View.GONE);
                        recyclerView4.setVisibility(View.VISIBLE);
                        recyclerView5.setVisibility(View.GONE);
                        recyclerView6.setVisibility(View.GONE);
                        recyclerView7.setVisibility(View.GONE);
                        recyclerView8.setVisibility(View.GONE);
                        recyclerView9.setVisibility(View.GONE);
                        recyclerView10.setVisibility(View.GONE);
                        //postLists2.clear();
                        //readType1();
                        break;

                    case 2:
                        tabChange = 2;
                        recyclerView.setVisibility(View.GONE);
                        recyclerView2.setVisibility(View.GONE);
                        recyclerView3.setVisibility(View.GONE);
                        recyclerView4.setVisibility(View.GONE);
                        recyclerView5.setVisibility(View.VISIBLE);
                        recyclerView6.setVisibility(View.GONE);
                        recyclerView7.setVisibility(View.GONE);
                        recyclerView8.setVisibility(View.GONE);
                        recyclerView9.setVisibility(View.GONE);
                        recyclerView10.setVisibility(View.GONE);
                        //postLists2.clear();
                       // readType2();
                        break;

                    case 3:
                        tabChange = 3;
                        recyclerView.setVisibility(View.GONE);
                        recyclerView2.setVisibility(View.GONE);
                        recyclerView3.setVisibility(View.GONE);
                        recyclerView4.setVisibility(View.GONE);
                        recyclerView5.setVisibility(View.GONE);
                        recyclerView6.setVisibility(View.VISIBLE);
                        recyclerView7.setVisibility(View.GONE);
                        recyclerView8.setVisibility(View.GONE);
                        recyclerView9.setVisibility(View.GONE);
                        recyclerView10.setVisibility(View.GONE);
                        //postLists2.clear();
                        //readType3();
                        break;

                    case 4:
                        tabChange = 4;
                        recyclerView.setVisibility(View.GONE);
                        recyclerView2.setVisibility(View.GONE);
                        recyclerView3.setVisibility(View.GONE);
                        recyclerView4.setVisibility(View.GONE);
                        recyclerView5.setVisibility(View.GONE);
                        recyclerView6.setVisibility(View.GONE);
                        recyclerView7.setVisibility(View.VISIBLE);
                        recyclerView8.setVisibility(View.GONE);
                        recyclerView9.setVisibility(View.GONE);
                        recyclerView10.setVisibility(View.GONE);
                        //postLists2.clear();
                       // readType4();
                        break;

                    case 5:
                        tabChange = 5;
                        recyclerView.setVisibility(View.GONE);
                        recyclerView2.setVisibility(View.GONE);
                        recyclerView3.setVisibility(View.GONE);
                        recyclerView4.setVisibility(View.GONE);
                        recyclerView5.setVisibility(View.GONE);
                        recyclerView6.setVisibility(View.GONE);
                        recyclerView7.setVisibility(View.GONE);
                        recyclerView8.setVisibility(View.VISIBLE);
                        recyclerView9.setVisibility(View.GONE);
                        recyclerView10.setVisibility(View.GONE);
                        //postLists2.clear();
                        //readType5();
                        break;

                    case 6:
                        tabChange = 6;
                        recyclerView.setVisibility(View.GONE);
                        recyclerView2.setVisibility(View.GONE);
                        recyclerView3.setVisibility(View.GONE);
                        recyclerView4.setVisibility(View.GONE);
                        recyclerView5.setVisibility(View.GONE);
                        recyclerView6.setVisibility(View.GONE);
                        recyclerView7.setVisibility(View.GONE);
                        recyclerView8.setVisibility(View.GONE);
                        recyclerView9.setVisibility(View.VISIBLE);
                        recyclerView10.setVisibility(View.GONE);
                        //postLists2.clear();
                       // readType6();
                        break;

                    case 7:
                        tabChange = 7;
                        recyclerView.setVisibility(View.GONE);
                        recyclerView2.setVisibility(View.GONE);
                        recyclerView3.setVisibility(View.GONE);
                        recyclerView4.setVisibility(View.GONE);
                        recyclerView5.setVisibility(View.GONE);
                        recyclerView6.setVisibility(View.GONE);
                        recyclerView7.setVisibility(View.GONE);
                        recyclerView8.setVisibility(View.GONE);
                        recyclerView9.setVisibility(View.GONE);
                        recyclerView10.setVisibility(View.VISIBLE);
                        //postLists2.clear();
                       // readType7();
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        progressBar = view.findViewById(R.id.progress_circular);
        return view;
    }

    private void loadRecommend() {
        recoRef.document(firebaseUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        ingreList = (List<String>) document.get("ingredients");
                        recommendIngreList = ingreList.toArray(new String[0]);
                        //recommendIngreList = document.get("ingredients");
                    }
                });
    }

    public void readReco() {
        DatabaseReference reference3 = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Recommend").child(firebaseUser.getUid());
        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ingredientList.clear();
                String data = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Ingredients ingredients = snapshot.getValue(Ingredients.class);
                    data = ingredients.getIngredients();
                    ingredientList.add(data);
                }
                ingredientList.trimToSize();
                ingredientList.toArray(new String[0]);
                ingredientLists = ingredientList.toArray(new String[ingredientList.size()]);

             /*   DatabaseReference reference4 = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Unrecommend").child(firebaseUser.getUid());
                reference4.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                        ingredientNoList.clear();
                        String data2 ="";
                        for (DataSnapshot snapshot2 : dataSnapshot2.getChildren()) {
                            Ingredients ingredients = snapshot2.getValue(Ingredients.class);
                            data2 = ingredients.getIngredients();
                            ingredientNoList.add(data2);
                        }
                        ingredientNoList.trimToSize();
                        ingredientNoList.toArray(new String[0]);
                        ingredientNoLists = ingredientNoList.toArray(new String[ingredientNoList.size()]);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }); */

                if(ingredientList != null && !ingredientList.isEmpty()) {
                    postRef.whereArrayContainsAny("ingredients", Arrays.asList(ingredientLists))
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    postLists3.clear();
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        Post post = documentSnapshot.toObject(Post.class);
                                        if (!firebaseUser.getUid().equals(post.getPublisher())) {
                                            postLists3.add(post);
                                        }
                                    }
                                    postAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(View.GONE);
                                    recyclerView3.setVisibility(View.VISIBLE);
                                }
                            });
                /*    postRef.whereNotIn("ingredients",Arrays.asList(ingredientNoLists))
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    postLists3.clear();
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        Post post = documentSnapshot.toObject(Post.class);
                                        if (!firebaseUser.getUid().equals(post.getPublisher())) {
                                            postLists3.add(post);
                                        }
                                    }
                                    postAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(View.GONE);
                                    recyclerView3.setVisibility(View.VISIBLE);
                                }
                            }); */
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

     /*   DatabaseReference reference3 = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Posts").child(postid).child("PlainIngredients");
        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ingredientList.clear();
                String data = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Ingredients ingredients = snapshot.getValue(Ingredients.class);
                    data = ingredients.getIngredients();
                    ingredientList.add(data);
                    //Toast.makeText(getContext(), data, Toast.LENGTH_SHORT).show();
                }
                ingredientList.trimToSize();
                ingredientList.toArray(new String[0]);
                ingredientLists = ingredientList.toArray(new String[ingredientList.size()]);
                //Toast.makeText(getContext(), data, Toast.LENGTH_SHORT).show();
                //String str_test = Arrays.toString(ingredientLists);
                //Toast.makeText(getContext(),str_test ,Toast.LENGTH_SHORT).show();

                if(ingredientList != null && !ingredientList.isEmpty()) {
                    postRef.whereArrayContainsAny("ingredients", Arrays.asList(ingredientLists))
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    postLists3.clear();
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        Post post = documentSnapshot.toObject(Post.class);
                                        if (!firebaseUser.getUid().equals(post.getPublisher())) {
                                            postLists3.add(post);
                                        }
                                    }
                                    postAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(View.GONE);
                                    recyclerView3.setVisibility(View.VISIBLE);
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); */

       /* postRef.document(postid).document(email).collection("Group").document(groupTitle).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> arrayList = (ArrayList<String>) document.get("partecipant");
                        //Do what you need to do with your ArrayList
                        for (String s : arrayList) {
                            Log.d(TAG, s);
                        }
                    }
                }
            }
        }); */


     /*   postRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ingredientList.clear();
                        if (task.isSuccessful()) {
                            String data = "";
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String ingredients = document.getString("ingredients");
                                data = ingredients;
                                ingredientList.add(data);
                            }
                            ingredientList.trimToSize();
                            ingredientList.toArray(new String[0]);
                            ingredientLists = ingredientList.toArray(new String[ingredientList.size()]);

                            if(ingredientList != null && !ingredientList.isEmpty()) {
                                postRef.whereArrayContainsAny("ingredients", Arrays.asList(ingredientLists))
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                postLists3.clear();
                                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                    Post post = documentSnapshot.toObject(Post.class);
                                                    if (!firebaseUser.getUid().equals(post.getPublisher())) {
                                                        postLists3.add(post);
                                                    }
                                                }
                                                postAdapter.notifyDataSetChanged();
                                                progressBar.setVisibility(View.GONE);
                                                recyclerView3.setVisibility(View.VISIBLE);
                                            }
                                        });
                            }
                        }
                    }
                }); */

    }

    public void checkTesting() {
        readReco();
        String sysid1String = Arrays.toString(ingredientLists);
        Toast.makeText(getContext(), sysid1String, Toast.LENGTH_SHORT).show();
        postRef.whereArrayContainsAny("ingredients", Arrays.asList(test) /*"sugar" */)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        postLists3.clear();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Post post = documentSnapshot.toObject(Post.class);
                            postLists3.add(post);
                        }
                        postAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }


    private void checkRecommend() {
        recommendList = new ArrayList<>();
        DatabaseReference reference2 = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Recommend")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = (String) ds.getKey();
                    //Toast.makeText(getContext(), "Key" + key, Toast.LENGTH_SHORT).show();
                    DatabaseReference keyReference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Recommend")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(key);
                    keyReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String ingredients = dataSnapshot.child("ingredients").getValue(String.class);
                            recommendList.add(dataSnapshot.child("ingredients").getValue(String.class));
                            //Toast.makeText(getContext(), ingredients, Toast.LENGTH_SHORT).show();
                            //checkPost();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                //readRecommend();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    private void checkPost() {
        postLists3 = new ArrayList<>();
        recommendList2 = new ArrayList<>();
        //Post post = mPost.get(i);
        DatabaseReference reference2 = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Posts")
                .child(postid)
                .child("PlainIngredients");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = (String) ds.getKey();
                    //Toast.makeText(getContext(), "Key" + key, Toast.LENGTH_SHORT).show();
                    DatabaseReference keyReference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Posts")
                            .child(postid)
                            .child("PlainIngredients")
                            .child(key);
                    keyReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String ingredients2 = dataSnapshot.child("ingredients").getValue(String.class);
                            recommendList2.add(dataSnapshot.child("ingredients").getValue(String.class));
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Post post = snapshot.getValue(Post.class);
                                for (String id : recommendList2) {
                                    if (recommendList.equals(id)) {
                                        postLists3.add(post);
                                    }
                                }
                                postAdapter.notifyDataSetChanged();

                                progressBar.setVisibility(View.GONE);

                            }
                            //Toast.makeText(getContext(), ingredients, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                //readRecommend();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void readRecommend() {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Posts");
        //.child();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postLists3.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    for (String ingre : recommendList) {
                        //Toast.makeText(getContext(), ingre, Toast.LENGTH_SHORT).show();
                     /*   if(ingredients.getIngredients().equals(ingre)){
                            postLists3.add(post);
                        } */
                        //if(recommendList.contains(ingre))

                    }
                }
                postAdapter.notifyDataSetChanged();

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void checkFollowing() {
        followingList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Follow")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                followingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    followingList.add(snapshot.getKey());

                }
                readPosts();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void readPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                postLists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    for(String id:followingList){
                        if(post.getPublisher().equals(id)){
                            postLists.add(post);
                        }
                 /*   for (String id : followingList) {
                        if (!firebaseUser.getUid().equals(post.getPublisher())) {
                            postLists.add(post);
                        } */
                    }
                }
                postAdapter.notifyDataSetChanged();

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void readRecipe() {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postLists2.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if (!firebaseUser.getUid().equals(post.getPublisher())) {
                        postLists2.add(post);
                    }
                }
                postAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void readType1() {
            DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Posts");
        reference.orderByChild("type")
                .equalTo("vegetables")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        postLists4.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Post post = snapshot.getValue(Post.class);
                            if (!firebaseUser.getUid().equals(post.getPublisher())) {
                                postLists4.add(post);
                            }
                        }
                        postAdapter4.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void readType2() {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Posts");
        reference.orderByChild("type")
                .equalTo("fruits")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        postLists5.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Post post = snapshot.getValue(Post.class);
                            if (!firebaseUser.getUid().equals(post.getPublisher())) {
                                postLists5.add(post);
                            }
                        }
                        postAdapter5.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }


    private void readType3() {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Posts");
        reference.orderByChild("type")
                .equalTo("grains")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        postLists6.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Post post = snapshot.getValue(Post.class);
                            if (!firebaseUser.getUid().equals(post.getPublisher())) {
                                postLists6.add(post);
                            }
                        }
                        postAdapter6.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void readType4() {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Posts");
        reference.orderByChild("type")
                .equalTo("meat")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        postLists7.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Post post = snapshot.getValue(Post.class);
                            if (!firebaseUser.getUid().equals(post.getPublisher())) {
                                postLists7.add(post);
                            }
                        }
                        postAdapter7.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void readType5() {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Posts");
        reference.orderByChild("type")
                .equalTo("seafood")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        postLists8.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Post post = snapshot.getValue(Post.class);
                            if (!firebaseUser.getUid().equals(post.getPublisher())) {
                                postLists8.add(post);
                            }
                        }
                        postAdapter8.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void readType6() {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Posts");
        reference.orderByChild("type")
                .equalTo("dairy")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        postLists9.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Post post = snapshot.getValue(Post.class);
                            if (!firebaseUser.getUid().equals(post.getPublisher())) {
                                postLists9.add(post);
                            }
                        }
                        postAdapter9.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void readType7() {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Posts");
        reference.orderByChild("type")
                .equalTo("eggs")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        postLists10.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Post post = snapshot.getValue(Post.class);
                            if (!firebaseUser.getUid().equals(post.getPublisher())) {
                                postLists10.add(post);
                            }
                        }
                        postAdapter10.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }
}