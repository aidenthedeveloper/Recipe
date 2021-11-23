package com.example.recipe;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;

import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.recipe.Model.Ingredients;
import com.example.recipe.Model.Steps;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class PostActivity extends AppCompatActivity {

    Calendar calendar;
    SimpleDateFormat simpleDateFormat,simpleYearFormat,simpleMonthFormat;
    String date,year,month,type;

    private Uri mImageUri, mStepUri;
    private Uri filePath;

    String miUrlOk = "", myUrl = "";
    private StorageTask uploadTask, uploadStepTask;
    StorageReference storageRef;

    //String[] numberWord = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"};

    ImageView close, image_added, delete,try_image;
    TextView post;

    EditText caption, title, serving, cooktime, ingredients, steps;
    private Button btnAdd;
    private EditText etInsert;

    LinearLayout layoutList;
    LinearLayout layoutStepList;
    Button btnIngredients;
    Button btnSteps;

    String[] ingredientLists,stepLists;

    Spinner dropdownRecipe;
    int foodNo = 0;
    int ing = 0;



    private final int PICK_IMAGE_REQUEST = 22;

    private VideoView videoView;
    private FloatingActionButton btnAddVid;

    public int stepPost;


    ArrayList<String> ingredientList = new ArrayList<>();
    ArrayList<String> stepList = new ArrayList<>();
    ArrayList<Ingredients> ingredientsArrayList = new ArrayList<>();
    ArrayList<Ingredients> ingredientsOnlyList = new ArrayList<>();
    ArrayList<Steps> stepsArrayList = new ArrayList<>();


    private ArrayList<Steps> mStepsArrayList;
    private RecyclerView mRecyclerView;
   // private StepsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        //ivStepsImage = findViewById(R.id.ivStepsImage);
        close = findViewById(R.id.close);
        image_added = findViewById(R.id.image_added);
        post = findViewById(R.id.post);
        caption = findViewById(R.id.caption);
        title = findViewById(R.id.title);
        //steps = findViewById(R.id.steps);
        serving = findViewById(R.id.serving);
        cooktime = findViewById(R.id.cooktime);
        //btnAdd = findViewById(R.id.btnAdd);
        // etInsert = findViewById(R.id.editTextInsert);
        btnIngredients = findViewById(R.id.btnAddIngredients);
        btnSteps = findViewById(R.id.btnAddSteps);
        layoutList = findViewById(R.id.layout_list);
        layoutStepList = findViewById(R.id.step_list);
        dropdownRecipe = findViewById(R.id.spinnerRecipeType);

        String[] foodType = new String[]{"Food Type: ", "Vegetables", "Fruits","Grains, Legumes, Nuts & Seeds","Meat & Poultry","Fish and Seafood","Dairy Foods","Eggs"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, foodType);
        dropdownRecipe.setAdapter(adapter);

        videoView = findViewById(R.id.videoView);
        btnAddVid = findViewById(R.id.addVideo);


        View ingredientView = getLayoutInflater().inflate(R.layout.add_recipe, null, false);
        View stepView = getLayoutInflater().inflate(R.layout.item_steps, null, false);


        EditText etStepsDetails = (EditText) stepView.findViewById(R.id.etStepsDetails);
        ImageView ivStepsImage = (ImageView) stepView.findViewById(R.id.ivStepsImage);
        ImageView imageDelete = (ImageView) stepView.findViewById(R.id.ivDelete);
        EditText etStepsPos = (EditText) stepView.findViewById(R.id.etStepsPosition);

        layoutList.addView(ingredientView);
        layoutStepList.addView(stepView);

        dropdownRecipe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position) {
                    case 0:
                        foodNo = 0;
                        break;
                    case 1:
                        foodNo = 1;
                        type = "vegetables";
                        break;
                    case 2:
                        foodNo = 2;
                        type = "fruits";
                        break;
                    case 3:
                        foodNo = 3;
                        type = "grains";
                        break;
                    case 4:
                        foodNo = 4;
                        type = "meat";
                        break;
                    case 5:
                        foodNo = 5;
                        type = "seafood";
                        break;
                    case 6:
                        foodNo = 6;
                        type = "dairy";
                        break;
                    case 7:
                        foodNo = 7;
                        type = "eggs";
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        btnIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ing <9) {
                    ing++;
                    addView();
                }
                else {
                    Toast.makeText(PostActivity.this, "Only 10 Ingredients Can Be Added.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addStepsView();
            }
        });



        storageRef = FirebaseStorage.getInstance().getReference("posts");


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostActivity.this, MainActivity.class));
                finish();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_cooktime = cooktime.getText().toString().toLowerCase();
                String str_serving = serving.getText().toString().toLowerCase();
                String str_title = title.getText().toString();
                String str_caption = caption.getText().toString();



                if (TextUtils.isEmpty(str_title)) {
                    Toast.makeText(PostActivity.this, "Title is required.", Toast.LENGTH_SHORT).show();
                    title.setError("Title cannot be empty.");
                    //pd.dismiss();
                } else if (TextUtils.isEmpty(str_serving)) {
                    Toast.makeText(PostActivity.this, "Serving portion is required.", Toast.LENGTH_SHORT).show();
                    serving.setError("Title cannot be empty.");
                    //pd.dismiss();
                } else if (TextUtils.isEmpty(str_cooktime)) {
                    Toast.makeText(PostActivity.this, "Cooktime is required.", Toast.LENGTH_SHORT).show();
                    cooktime.setError("Title cannot be empty.");
                    //pd.dismiss();
                } else if (foodNo == 0) {
                    Toast.makeText(PostActivity.this, "Food Type is required.", Toast.LENGTH_SHORT).show();
                    //pd.dismiss();
                } else {
                    if (checkValid()) {
                        //uploadImageToFirebaseStorage();
                        if (checkStepsValid()) {
                            uploadImage_10();
                        }
                    }
                }
            }
        });

        image_added.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code = RC_MAIN;
                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .start(PostActivity.this);
            }
        });
    }



    private boolean checkValid() {
        ingredientsArrayList.clear();
        ingredientsOnlyList.clear();
        boolean result = true;

        for (int i = 0; i < layoutList.getChildCount(); i++) {

            View ingredientView = layoutList.getChildAt(i);

            EditText editTextIng = (EditText) ingredientView.findViewById(R.id.etIngredients);
            EditText editTextAmo = (EditText) ingredientView.findViewById(R.id.etAmount);

            Ingredients ingredients = new Ingredients();
            Ingredients ingredients2 = new Ingredients();

            if (!editTextIng.getText().toString().equals("") && !editTextAmo.getText().toString().equals("")) {
                String ingredientss = editTextIng.getText().toString().toLowerCase();
                  ingredients.setIngredients(editTextIng.getText().toString().toLowerCase());
                  ingredients.setAmount(editTextAmo.getText().toString());
                ingredientList.add(ingredientss);
                ingredients2.setIngredients(editTextIng.getText().toString().toLowerCase());
                ingredients.setTotalIng(editTextAmo.getText().toString() + " of " + editTextIng.getText().toString());
            } else if(editTextIng.getText().toString().equals("")) {
                editTextIng.setError("Ingredients cannot be empty");
                Toast.makeText(this, "Add Ingredients", Toast.LENGTH_SHORT).show();
                result = false;
                break;
            } else if(editTextAmo.getText().toString().equals("")) {
                editTextAmo.setError("Amount cannot be empty.");
                Toast.makeText(this, "Add Ingredients", Toast.LENGTH_SHORT).show();
                result = false;
                break;
            } else {
                editTextIng.setError("Ingredients cannot be empty");
                editTextAmo.setError("Amount cannot be empty.");
                Toast.makeText(this, "Add Ingredients", Toast.LENGTH_SHORT).show();
                result = false;
                break;
            }
            //ingredientList[i] = ingredients.toString().toLowerCase().split("\\s*,\\s*");
            ingredientsArrayList.add(ingredients);
            ingredientsOnlyList.add(ingredients2);
        }
        ingredientList.trimToSize();
        ingredientList.toArray(new String[0]);
        ingredientLists = ingredientList.toArray(new String[ingredientList.size()]);

        
        if (ingredientsArrayList.size() == 0) {
            result = false;
            Toast.makeText(this, "Add Ingredients", Toast.LENGTH_SHORT).show();
        } else if (!result) {
            Toast.makeText(this, "Enter All Details Correctly", Toast.LENGTH_SHORT).show();
        }


        return result;
    }

    private boolean checkStepsValid() {
        stepsArrayList.clear();
        boolean result = true;

        for (int i = 0; i < layoutStepList.getChildCount(); i++) {

            View stepsView = layoutStepList.getChildAt(i);

            EditText etStepsDetails = (EditText) stepsView.findViewById(R.id.etStepsDetails);
            ImageView ivStepsImage = (ImageView) stepsView.findViewById(R.id.ivStepsImage);
            EditText etStepsPos = (EditText) stepsView.findViewById(R.id.etStepsPosition);

            Steps steps = new Steps();

            if (!etStepsDetails.getText().toString().equals("") && !etStepsPos.getText().toString().equals("")) {
                String position = etStepsPos.getText().toString();
                String stepsDetails = etStepsDetails.getText().toString();
                int finalPosition = Integer.parseInt(position);
                stepList.add(stepsDetails);
                steps.setSteps(etStepsDetails.getText().toString());
                steps.setStepsPosition(etStepsPos.getText().toString());
            } else if(etStepsDetails.getText().toString().equals("")) {
                etStepsDetails.setError("Steps cannot be empty.");
                result = false;
                break;
            } else if(etStepsPos.getText().toString().equals("")) {
                etStepsPos.setError("Steps Number cannot be empty.");
                result = false;
                break;
            } else {
                etStepsDetails.setError("Steps cannot be empty.");
                etStepsPos.setError("Steps Number cannot be empty.");
                result = false;
                break;
            }

            stepsArrayList.add(steps);
        }
        stepList.trimToSize();
        stepList.toArray(new String[0]);
        stepLists = stepList.toArray(new String[stepList.size()]);

        if (stepsArrayList.size() == 0) {
            result = false;
            Toast.makeText(this, "Add Steps and Number", Toast.LENGTH_SHORT).show();
        } else if (!result) {
            Toast.makeText(this, "Enter All Details Correctly", Toast.LENGTH_SHORT).show();

        }


        return result;
    }


    private void addView() {

        View ingredientView = getLayoutInflater().inflate(R.layout.add_recipe, null, false);
        stepPost++;
        EditText etIngredients = (EditText) ingredientView.findViewById(R.id.etIngredients);
        ImageView imageClose = (ImageView) ingredientView.findViewById(R.id.ivClose);

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeView(ingredientView);
            }
        });


        layoutList.addView(ingredientView);
    }

    private void removeView(View view) {
        ing--;
        layoutList.removeView(view);
    }

    private void addStepsView() {

        View stepView = getLayoutInflater().inflate(R.layout.item_steps, null, false);

        EditText etStepsDetails = (EditText) stepView.findViewById(R.id.etStepsDetails);
        ImageView ivStepsImage = (ImageView) stepView.findViewById(R.id.ivStepsImage);
        ImageView imageDelete = (ImageView) stepView.findViewById(R.id.ivDelete);
        EditText etStepsPos = (EditText) stepView.findViewById(R.id.etStepsPosition);


        ivStepsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
                ArrayList<Integer> images = new ArrayList<Integer>();

            }
        });

        imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeStepsView(stepView);
            }
        });

        layoutStepList.addView(stepView);
    }

    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    private void removeStepsView(View view) {

        layoutStepList.removeView(view);
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImage_10() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Posting");
        pd.show();
        if (mImageUri != null) {
            final StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            uploadTask = fileReference.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        miUrlOk = downloadUri.toString();

                        calendar = Calendar.getInstance();
                        simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        simpleYearFormat= new SimpleDateFormat("yyyy");
                        simpleMonthFormat= new SimpleDateFormat("MM");
                        date = simpleDateFormat.format(calendar.getTime());
                        year = simpleYearFormat.format(calendar.getTime());
                        month = simpleMonthFormat.format(calendar.getTime());

                        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Posts");

                        String postid = reference.push().getKey();

                        String str_title = title.getText().toString();
                        String str_uTitle = str_title.substring(0, 1).toUpperCase() + str_title.substring(1).toLowerCase();
                        String str_lTitle = title.getText().toString().toLowerCase();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("postid", postid);
                        hashMap.put("postimage", miUrlOk);
                        hashMap.put("title", str_uTitle.toString());
                        hashMap.put("caption", caption.getText().toString());
                        //hashMap.put("ingredients", ingredients.getText().toString());
                        hashMap.put("lowerTitle", str_lTitle.toString());
                        hashMap.put("cooktime", cooktime.getText().toString().toLowerCase());
                        hashMap.put("serving", serving.getText().toString().toLowerCase());
                        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("date",date);
                        hashMap.put("year",year);
                        hashMap.put("month",month);
                        hashMap.put("type",type);


                        reference.child(postid).setValue(hashMap);
                        reference.child(postid).child("Ingredients").setValue(ingredientsArrayList);
                        reference.child(postid).child("PlainIngredients").setValue(ingredientsOnlyList);
                        reference.child(postid).child("Steps").setValue(stepsArrayList);

                        hashMap.put("ingredients", Arrays.asList(ingredientLists));
                        hashMap.put("steps", Arrays.asList(stepLists));

                        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

                        fStore.collection("posts").document(postid).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(PostActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PostActivity.this, "Error.", Toast.LENGTH_SHORT).show();

                            }
                        });

                        pd.dismiss();

                        startActivity(new Intent(PostActivity.this, HomeActivity.class));
                        finish();

                    } else {
                        Toast.makeText(PostActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(PostActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
            pd.dismiss();

        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();

            image_added.setImageURI(mImageUri);
        } else {
            //Toast.makeText(this, "Something gone wrong!", Toast.LENGTH_SHORT).show();
            // startActivity(new Intent(PostActivity.this, HomeActivity.class));
            // finish();
        }


    }
}