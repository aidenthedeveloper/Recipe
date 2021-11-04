package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.recipe.Model.Ingredients;
import com.example.recipe.Model.Post;
import com.example.recipe.Model.Post2;
import com.example.recipe.Model.User;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TableActivity extends AppCompatActivity {

    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference postFRef = fStore.collection("posts");

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private DatabaseReference postRef;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static int PERMISSION_ALL = 12;


    //creating a list of objects constants
    List<User> usersList;
    List<Post2> postsList;
    ArrayList<String> ingredientList = new ArrayList<>();

    private List<Ingredients> ingredientsListt;
    String[] ingredientLists;

    String data,totalSteps = null,totalIng = null;


    File file,pFile;
    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Data Table");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, 0);
                overridePendingTransition(0, 0);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference().child("Users");
        postRef = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference().child("Posts");
        pdfView = findViewById(R.id.pdf_viewer);



        Button btnUserReport = findViewById(R.id.btnUserReport);
        Button btnPostReport = findViewById(R.id.btnPostReport);
        btnUserReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previewUsersReport();
                //displayUserReport();
            }
        });
        
        btnPostReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previewPostReport();
            }
        });

        //
        usersList = new ArrayList<>();
        postsList = new ArrayList<>();

        //create files in charity care folder
        file = new File(Environment.getExternalStorageDirectory(), "UsersDataTable.pdf");
        pFile = new File(Environment.getExternalStorageDirectory(), "PostsDataTable.pdf");

        //check if they exist, if not create them(directory)
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            Toast.makeText(TableActivity.this, "File Exist", Toast.LENGTH_SHORT).show();
        }

        if (!pFile.exists()) {
            pFile.getParentFile().mkdirs();
            Toast.makeText(TableActivity.this, "File Exist", Toast.LENGTH_SHORT).show();
        }

        //fetch payment and disabled users details;
        fetchUsers();
        fetchPosts();
    }

    //function to fetch payment data from the database
    private void fetchUsers() {

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    User users = new User();
                    users.setFullName(snapshot.child("fullName").getValue().toString());
                    users.setUsername(snapshot.child("username").getValue().toString());
                    users.setEmail(snapshot.child("email").getValue().toString());
                    users.setPhoneNo(snapshot.child("phoneNo").getValue().toString());
                    users.setGender(snapshot.child("gender").getValue().toString());
                    users.setAge(Integer.parseInt(snapshot.child("age").getValue().toString()));
                    users.setId(snapshot.child("id").getValue().toString());
                    //users.setDate(snapshot.child("date").getValue().toString());


                    usersList.add(users);


                }
                //create a pdf file and catch exception beacause file may not be created
                try {
                    createUserReport(usersList);
                } catch (DocumentException | FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void fetchPosts() {

        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Post2 post2 = new Post2();
                    //Post post2 = snapshot.getValue(Post.class);
                    post2.setPostid(snapshot.child("postid").getValue().toString());
                    post2.setPublisher(snapshot.child("publisher").getValue().toString());
                    post2.setTitle(snapshot.child("title").getValue().toString());
                    post2.setCaption(snapshot.child("caption").getValue().toString());
                    post2.setServing(snapshot.child("serving").getValue().toString());
                    post2.setCookTime(snapshot.child("cooktime").getValue().toString());
                    post2.setType(snapshot.child("type").getValue().toString());

                    //Toast.makeText(TableActivity.this, data, Toast.LENGTH_SHORT).show();

                    //post2.setSteps();
                    //totalIng += snapshot.child("PlainIngredients").child("ingredients").getValue();

                    //post2.setIngredients((List<String>) snapshot.child("PlainIngredients").getValue());
                    totalIng += (List<String>) snapshot.child("PlainIngredients").getValue();
                    //Toast.makeText(TableActivity.this, totalIng.substring(4), Toast.LENGTH_SHORT).show();
                    post2.setIngredients(totalIng.substring(4));

                    //totalSteps += (List<String>) snapshot.child("Steps").getValue();
                    //Toast.makeText(TableActivity.this, totalSteps.substring(4), Toast.LENGTH_SHORT).show();
                    //post2.setIngredients(totalSteps.substring(4));
                    postsList.add(post2);


                }
                //create a pdf file and catch exception beacause file may not be created
                try {
                    createPostReport(postsList);
                } catch (DocumentException | FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void previewUsersReport() {
        if (hasPermissions(this, PERMISSIONS)) {
            displayUserReport();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    public void previewPostReport() {
        if (hasPermissions(this, PERMISSIONS)) {
            displayPostReport();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    private void createUserReport(List<User> usersList) throws DocumentException, FileNotFoundException {
        BaseColor colorWhite = BaseColor.WHITE;
        BaseColor grayColor = BaseColor.GRAY;

        Font white = new Font(Font.FontFamily.HELVETICA, 13.0f, Font.BOLD, colorWhite);
        FileOutputStream output = new FileOutputStream(file);
        Document document = new Document(PageSize.A4);
        PdfPTable table = new PdfPTable(new float[]{6, 18, 18, 35, 25, 10, 8});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(100);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(110);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        Chunk noText = new Chunk("No.", white);
        PdfPCell noCell = new PdfPCell(new Phrase(noText));
        noCell.setFixedHeight(30);
        noCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        noCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk nameText = new Chunk("Full Name", white);
        PdfPCell nameCell = new PdfPCell(new Phrase(nameText));
        nameCell.setFixedHeight(30);
        nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        nameCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk userText = new Chunk("Username", white);
        PdfPCell userCell = new PdfPCell(new Phrase(userText));
        userCell.setFixedHeight(30);
        userCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        userCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk emailphoneText = new Chunk("Email / Phone", white);
        PdfPCell emailphoneCell = new PdfPCell(new Phrase(emailphoneText));
        emailphoneCell.setFixedHeight(30);
        emailphoneCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        emailphoneCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk idText = new Chunk("User ID", white);
        PdfPCell idCell = new PdfPCell(new Phrase(idText));
        idCell.setFixedHeight(30);
        idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        idCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk genderText = new Chunk("Gender", white);
        PdfPCell genderCell = new PdfPCell(new Phrase(genderText));
        genderCell.setFixedHeight(30);
        genderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        genderCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk ageText = new Chunk("Age", white);
        PdfPCell ageCell = new PdfPCell(new Phrase(ageText));
        ageCell.setFixedHeight(30);
        ageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        ageCell.setVerticalAlignment(Element.ALIGN_CENTER);


        //Chunk footerText = new Chunk("Moses Njoroge - Copyright @ 2020");
        //PdfPCell footCell = new PdfPCell(new Phrase(footerText));
        //footCell.setFixedHeight(70);
        //footCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //footCell.setVerticalAlignment(Element.ALIGN_CENTER);
        //footCell.setColspan(4);


        table.addCell(noCell);
        table.addCell(nameCell);
        table.addCell(userCell);
        table.addCell(emailphoneCell);
        table.addCell(idCell);
        table.addCell(genderCell);
        table.addCell(ageCell);
        table.setHeaderRows(1);

        PdfPCell[] cells = table.getRow(0).getCells();

        for (PdfPCell cell : cells) {
            cell.setBackgroundColor(grayColor);
        }
        for (int i = 0; i < usersList.size(); i++) {
            User user = usersList.get(i);

            String id = String.valueOf(i + 1);
            String fullName = user.getFullName();
            String username = user.getUsername();
            String email = user.getEmail();
            String phoneNo = user.getPhoneNo();
            String userid = user.getId();
            String gender = user.getGender();
            int age = user.getAge();


            table.addCell(id + ". ");
            table.addCell(fullName);
            table.addCell(username);
            table.addCell(email + "\n" + phoneNo);
            table.addCell(userid);
            table.addCell(gender);
            table.addCell(String.valueOf(age));

        }


        PdfWriter.getInstance(document, output);
        document.open();
        document.add(table);

        document.close();
    }

    private void createPostReport(List<Post2> postsList) throws DocumentException, FileNotFoundException {
        BaseColor colorWhite = BaseColor.WHITE;
        BaseColor grayColor = BaseColor.GRAY;

        Font white = new Font(Font.FontFamily.HELVETICA, 13.0f, Font.BOLD, colorWhite);
        FileOutputStream output = new FileOutputStream(pFile);
        Document document = new Document(PageSize.A4);
        PdfPTable table = new PdfPTable(new float[]{6, 9, 12, 10, 12, 12, 15, 7, 25});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(200);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(110);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        Chunk noText = new Chunk("No.", white);
        PdfPCell noCell = new PdfPCell(new Phrase(noText));
        noCell.setFixedHeight(50);
        noCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        noCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk titleText = new Chunk("Title", white);
        PdfPCell titleCell = new PdfPCell(new Phrase(titleText));
        titleCell.setFixedHeight(50);
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk captionText = new Chunk("Caption", white);
        PdfPCell captionCell = new PdfPCell(new Phrase(captionText));
        captionCell.setFixedHeight(50);
        captionCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        captionCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk postText = new Chunk("Post ID", white);
        PdfPCell postCell = new PdfPCell(new Phrase(postText));
        postCell.setFixedHeight(50);
        postCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        postCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk publisherText = new Chunk("Publisher ID", white);
        PdfPCell publisherCell = new PdfPCell(new Phrase(publisherText));
        publisherCell.setFixedHeight(50);
        publisherCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        publisherCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk servingText = new Chunk("Serving", white);
        PdfPCell servingCell = new PdfPCell(new Phrase(servingText));
        servingCell.setFixedHeight(50);
        servingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        servingCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk cooktimeText = new Chunk("CookTime", white);
        PdfPCell cooktimeCell = new PdfPCell(new Phrase(cooktimeText));
        cooktimeCell.setFixedHeight(50);
        cooktimeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cooktimeCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk typeText = new Chunk("Food Type", white);
        PdfPCell typeCell = new PdfPCell(new Phrase(typeText));
        typeCell.setFixedHeight(50);
        typeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        typeCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk ingText = new Chunk("Ingredients", white);
        PdfPCell ingCell = new PdfPCell(new Phrase(ingText));
        ingCell.setFixedHeight(50);
        ingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        ingCell.setVerticalAlignment(Element.ALIGN_CENTER);




        //Chunk footerText = new Chunk("Moses Njoroge - Copyright @ 2020");
        //PdfPCell footCell = new PdfPCell(new Phrase(footerText));
        //footCell.setFixedHeight(70);
        //footCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //footCell.setVerticalAlignment(Element.ALIGN_CENTER);
        //footCell.setColspan(4);


        table.addCell(noCell);
        table.addCell(titleCell);
        table.addCell(captionCell);
        table.addCell(postCell);
        table.addCell(publisherCell);
        table.addCell(servingCell);
        table.addCell(cooktimeCell);
        table.addCell(typeCell);
        table.addCell(ingCell);
        table.setHeaderRows(1);

        PdfPCell[] cells = table.getRow(0).getCells();


        for (PdfPCell cell : cells) {
            cell.setBackgroundColor(grayColor);
        }

        for (int i = 0; i < postsList.size(); i++) {
            Post2 post2 = postsList.get(i);

            String id = String.valueOf(i + 1);
            String title = post2.getTitle();
            String caption = post2.getCaption();
            String postid = post2.getPostid();
            String publisher = post2.getPublisher();
            String serving = post2.getServing();
            String cookTime = post2.getCookTime();
            String foodType = post2.getType();
            String ingredients = post2.getIngredients().replaceAll("ingredients", "").replaceAll("\\{|\\}|=|\\[|\\]", " ");



        /*    for(int j=0;j<ingredientsListt.size();j++){

                String temp1 = ingredientsListt.get(i).getTotalIng();
                //String temp2 = ingredientsListt.get(i).getIngredients();


                if(temp1.equalsIgnoreCase("")){
                    temp1="*"; // this fills the cell with * if the String is empty otherwise cell won't be created
                }
                table.addCell(temp1); // rows for first column

            } */

            table.addCell(id + ". ");
            table.addCell(title);
            table.addCell(caption);
            table.addCell(postid);
            table.addCell(publisher);
            table.addCell(serving);
            table.addCell(cookTime);
            table.addCell(foodType);
            table.addCell(ingredients);
          /*  for(int j=0;j<ingredients.size();j++){

                String temp1 = ingredients.get(i);
                //String temp2 = ingredientsListt.get(i).getIngredients();


                if(temp1.equalsIgnoreCase("")){
                    temp1="*"; // this fills the cell with * if the String is empty otherwise cell won't be created
                }
                table.addCell(temp1); // rows for first column

            } */


        }


        PdfWriter.getInstance(document, output);
        document.open();
        document.add(table);

        document.close();
    }

    private void displayUserReport() {
        pdfView.fromFile(file)
                .pages(0, 1, 2, 3, 3, 3)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .load();


    }

    private void displayPostReport() {
        pdfView.fromFile(pFile)
                .pages(0, 1, 2, 3, 3, 3)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .load();


    }
    
    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}