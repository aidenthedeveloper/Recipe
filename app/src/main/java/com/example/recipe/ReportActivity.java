package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.recipe.Model.Post;
import com.example.recipe.Model.User;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {

    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference userRef = fStore.collection("users");
    CollectionReference postRef = fStore.collection("posts");

    PieChart pieChart;
    PieDataSet pieDataSet;
    ArrayList<PieEntry> test = new ArrayList<>();
    ArrayList<PieEntry> genders = new ArrayList<>();
    ArrayList<PieEntry> ages = new ArrayList<>();
    ArrayList<PieEntry> foodType = new ArrayList<>();

    ArrayList<String> gender = new ArrayList<>();
    ArrayList<String> year = new ArrayList<>();
    ArrayList<String> month = new ArrayList<>();
    ArrayList<Integer> age = new ArrayList<>();
    ArrayList<String> typeRecipe = new ArrayList<>();

    int type = 0, typeMonth = 0;
    int totalMale = 0, totalFemale = 0, totalGender = 0;
    int totalVegetables = 0, totalFruits = 0, totalGrains = 0, totalMeat = 0, totalSeafood = 0, totalDairy = 0, totalEggs = 0;
    int tabDefault = 0;
    int tabChange = 0, tabYear = 0, tabMonth = 0;
    int ageTeen = 0, ageTwenties = 0, ageThirties = 0, ageFourties = 0, ageFifties = 0, ageOver = 0;


    Spinner dropdown, dropdownType, dropdownYear, dropdownMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        pieChart = findViewById(R.id.pieChart);
        dropdown = findViewById(R.id.spinner);
        dropdownType = findViewById(R.id.spinnerType);
        dropdownYear = findViewById(R.id.spinnerYear);
        dropdownMonth = findViewById(R.id.spinnerMonth);

        gender.add("test");
        age.add(0);
        pieDataSet = new PieDataSet(test, "Test");
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        String[] items = new String[]{"User Gender", "User Age", "Recipe Type"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        String[] items2 = new String[]{"All Year", "2021", "2020", "2019"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        dropdownYear.setAdapter(adapter2);

        String[] items3 = new String[]{"All Month", "January", "February", "March", "April", "May", "June", "July", "August", "September",
                "October", "November", "December"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items3);
        dropdownMonth.setAdapter(adapter3);

        String[] items4 = new String[]{"Number", "Percentage"};
        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items4);
        dropdownType.setAdapter(adapter4);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Data Chart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, 0);
                overridePendingTransition(0, 0);
            }
        });

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position) {
                    case 0:
                        // Whatever you want to happen when the first item gets selected
                        tabChange = 0;
                        clearChart();
                        if (tabYear == 0) {
                            loadAllYear();
                        } else if (tabYear == 1) {
                            loadYear2021();
                        } else if (tabYear == 2) {
                            loadYear2020();
                        } else if (tabYear == 3) {
                            loadYear2019();
                        }

                        //loadGender();
                        break;
                    case 1:
                        // Whatever you want to happen when the second item gets selected
                        tabChange = 1;
                        clearChart();
                        if (tabYear == 0) {
                            loadAllYear();
                        } else if (tabYear == 1) {
                            loadYear2021();
                        } else if (tabYear == 2) {
                            loadYear2020();
                        } else if (tabYear == 3) {
                            loadYear2019();
                        }
                        //loadAge();
                        break;
                    case 2:
                        // Whatever you want to happen when the second item gets selected
                        tabChange = 2;
                        clearChart();
                        if (tabYear == 0) {
                            loadAllYear();
                        } else if (tabYear == 1) {
                            loadYear2021();
                        } else if (tabYear == 2) {
                            loadYear2020();
                        } else if (tabYear == 3) {
                            loadYear2019();
                        }
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        dropdownType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position) {
                    case 0:
                        type = 0;
                        // Whatever you want to happen when the first item gets selected
                        if (tabDefault == 0) {
                        } else {
                            clearChart();
                            if (tabYear == 0) {
                                loadAllYear();
                            } else if (tabYear == 1) {
                                loadYear2021();
                            } else if (tabYear == 2) {
                                loadYear2020();
                            } else if (tabYear == 3) {
                                loadYear2019();
                            }
                        }
                        break;
                    case 1:
                        type = 1;
                        tabDefault = 1;
                        // Whatever you want to happen when the second item gets selected
                        clearChart();
                        if (tabYear == 0) {
                            loadAllYear();
                        } else if (tabYear == 1) {
                            loadYear2021();
                        } else if (tabYear == 2) {
                            loadYear2020();
                        } else if (tabYear == 3) {
                            loadYear2019();
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        dropdownYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position) {
                    case 0:
                        // Whatever you want to happen when the first item gets selected
                        if (tabDefault == 0) {
                        } else {
                            clearChart();
                            loadAllYear();
                        }
                        break;
                    case 1:
                        // Whatever you want to happen when the second item gets selected
                        tabDefault = 1;
                        clearChart();
                        loadYear2021();
                        break;
                    case 2:
                        // Whatever you want to happen when the second item gets selected
                        tabDefault = 1;
                        clearChart();
                        loadYear2020();
                        break;
                    case 3:
                        // Whatever you want to happen when the second item gets selected
                        tabDefault = 1;
                        clearChart();
                        loadYear2019();
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        dropdownMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position) {
                    case 0:
                        typeMonth = 0;
                        // Whatever you want to happen when the first item gets selected
                        if (tabDefault == 0) {
                        } else {
                            clearChart();
                            if (tabYear == 0) {
                                loadAllYear();
                            } else if (tabYear == 1) {
                                loadYear2021();
                            } else if (tabYear == 2) {
                                loadYear2020();
                            } else if (tabYear == 3) {
                                loadYear2019();
                            }
                        }
                        break;
                    case 1:
                        typeMonth = 1;
                        tabDefault = 1;
                        // Whatever you want to happen when the second item gets selected
                        clearChart();
                        if (tabYear == 0) {
                            loadAllYear();
                        } else if (tabYear == 1) {
                            loadYear2021();
                        } else if (tabYear == 2) {
                            loadYear2020();
                        } else if (tabYear == 3) {
                            loadYear2019();
                        }
                        break;
                    case 2:
                        typeMonth = 2;
                        tabDefault = 1;
                        // Whatever you want to happen when the second item gets selected
                        clearChart();
                        if (tabYear == 0) {
                            loadAllYear();
                        } else if (tabYear == 1) {
                            loadYear2021();
                        } else if (tabYear == 2) {
                            loadYear2020();
                        } else if (tabYear == 3) {
                            loadYear2019();
                        }
                        break;
                    case 3:
                        typeMonth = 3;
                        tabDefault = 1;
                        // Whatever you want to happen when the second item gets selected
                        clearChart();
                        if (tabYear == 0) {
                            loadAllYear();
                        } else if (tabYear == 1) {
                            loadYear2021();
                        } else if (tabYear == 2) {
                            loadYear2020();
                        } else if (tabYear == 3) {
                            loadYear2019();
                        }
                        break;
                    case 4:
                        typeMonth = 4;
                        tabDefault = 1;
                        // Whatever you want to happen when the second item gets selected
                        clearChart();
                        if (tabYear == 0) {
                            loadAllYear();
                        } else if (tabYear == 1) {
                            loadYear2021();
                        } else if (tabYear == 2) {
                            loadYear2020();
                        } else if (tabYear == 3) {
                            loadYear2019();
                        }
                        break;
                    case 5:
                        typeMonth = 5;
                        tabDefault = 1;
                        // Whatever you want to happen when the second item gets selected
                        clearChart();
                        if (tabYear == 0) {
                            loadAllYear();
                        } else if (tabYear == 1) {
                            loadYear2021();
                        } else if (tabYear == 2) {
                            loadYear2020();
                        } else if (tabYear == 3) {
                            loadYear2019();
                        }
                        break;
                    case 6:
                        typeMonth = 6;
                        tabDefault = 1;
                        // Whatever you want to happen when the second item gets selected
                        clearChart();
                        if (tabYear == 0) {
                            loadAllYear();
                        } else if (tabYear == 1) {
                            loadYear2021();
                        } else if (tabYear == 2) {
                            loadYear2020();
                        } else if (tabYear == 3) {
                            loadYear2019();
                        }
                        break;
                    case 7:
                        typeMonth = 7;
                        tabDefault = 1;
                        // Whatever you want to happen when the second item gets selected
                        clearChart();
                        if (tabYear == 0) {
                            loadAllYear();
                        } else if (tabYear == 1) {
                            loadYear2021();
                        } else if (tabYear == 2) {
                            loadYear2020();
                        } else if (tabYear == 3) {
                            loadYear2019();
                        }
                        break;
                    case 8:
                        typeMonth = 8;
                        tabDefault = 1;
                        // Whatever you want to happen when the second item gets selected
                        clearChart();
                        if (tabYear == 0) {
                            loadAllYear();
                        } else if (tabYear == 1) {
                            loadYear2021();
                        } else if (tabYear == 2) {
                            loadYear2020();
                        } else if (tabYear == 3) {
                            loadYear2019();
                        }
                        break;
                    case 9:
                        typeMonth = 9;
                        tabDefault = 1;
                        // Whatever you want to happen when the second item gets selected
                        clearChart();
                        if (tabYear == 0) {
                            loadAllYear();
                        } else if (tabYear == 1) {
                            loadYear2021();
                        } else if (tabYear == 2) {
                            loadYear2020();
                        } else if (tabYear == 3) {
                            loadYear2019();
                        }
                        break;
                    case 10:
                        typeMonth = 10;
                        tabDefault = 1;
                        // Whatever you want to happen when the second item gets selected
                        clearChart();
                        if (tabYear == 0) {
                            loadAllYear();
                        } else if (tabYear == 1) {
                            loadYear2021();
                        } else if (tabYear == 2) {
                            loadYear2020();
                        } else if (tabYear == 3) {
                            loadYear2019();
                        }
                        break;
                    case 11:
                        typeMonth = 11;
                        tabDefault = 1;
                        // Whatever you want to happen when the second item gets selected
                        clearChart();
                        if (tabYear == 0) {
                            loadAllYear();
                        } else if (tabYear == 1) {
                            loadYear2021();
                        } else if (tabYear == 2) {
                            loadYear2020();
                        } else if (tabYear == 3) {
                            loadYear2019();
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        //loadPieChartData();
    }

    private void clearChart() {
        genders.clear();
        ages.clear();
        gender.clear();
        age.clear();
        foodType.clear();
        typeRecipe.clear();
        pieChart.notifyDataSetChanged();
        pieChart.clear();
        pieChart.invalidate();
        totalMale = 0;
        totalFemale = 0;
        ageTeen = 0;
        ageTwenties = 0;
        ageThirties = 0;
        ageFourties = 0;
        ageFifties = 0;
        ageOver = 0;
        totalFruits = 0;
        totalVegetables = 0;
        totalMeat = 0;
        totalSeafood = 0;
        totalGrains = 0;
        totalEggs = 0;
        totalDairy = 0;
    }

    public void loadAge() {
        age = new ArrayList<>();
        userRef//.whereEqualTo("gender", "male")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                long str_age = (Long) document.get("age");
                                int age = Integer.valueOf((int) str_age);
                                if (age >= 13 && age < 20) {
                                    ageTeen++;
                                } else if (age >= 20 && age < 30) {
                                    ageTwenties++;
                                } else if (age >= 30 && age < 40) {
                                    ageThirties++;
                                } else if (age >= 40 && age < 50) {
                                    ageFourties++;
                                } else if (age >= 40 && age < 50) {
                                    ageFifties++;
                                } else if (age >= 50) {
                                    ageOver++;
                                }
                            }

                            if (ageTeen > 0) {
                                ages.add(new PieEntry(ageTeen, "Age 13-19"));
                            }
                            if (ageTwenties > 0) {
                                ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                            }
                            if (ageThirties > 0) {
                                ages.add(new PieEntry(ageThirties, "Age 30-39"));
                            }
                            if (ageFourties > 0) {
                                ages.add(new PieEntry(ageFourties, "Age 40-49"));
                            }
                            if (ageFifties > 0) {
                                ages.add(new PieEntry(ageFifties, "Age 50+"));
                            }

                            pieChart.notifyDataSetChanged();
                            pieChart.invalidate();

                        } else {
                            Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        pieDataSet = new PieDataSet(ages, "Age");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(25f);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Age");
        pieChart.setCenterTextSize(35f);
        pieChart.animate();
        Legend legend = pieChart.getLegend();
        legend.setTextSize(15f);
    }

    public void loadGender() {
        gender = new ArrayList<>();
        String a = "Male";
        String b = "Female";

        userRef//.whereEqualTo("gender", "male")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String gender = document.getString("gender");
                                if (gender.equals("male")) {
                                    totalMale++;
                                } else if (gender.equals("female")) {
                                    totalFemale++;
                                } else {
                                    Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                }
                            }

                            genders.add(new PieEntry(totalMale, "male"));
                            genders.add(new PieEntry(totalFemale, "female"));
                            pieChart.notifyDataSetChanged();
                            pieChart.invalidate();

                        } else {
                            Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(25f);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Gender");
        pieChart.setCenterTextSize(35f);
        pieChart.animate();
        Legend legend = pieChart.getLegend();
        legend.setTextSize(20f);
    }

    public void loadAllYear() {
        year = new ArrayList<>();
        tabYear = 0;
        if (tabChange == 0) {
            if (typeMonth == 0) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef//.whereEqualTo("gender", "male")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 1) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "01")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 2) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "02")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 3) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "03")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 4) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "04")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 5) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "05")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 6) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "06")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 7) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "07")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 8) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "08")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 9) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "09")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 10) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "10")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 11) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "11")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 12) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "12")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }

        } else if (tabChange == 1) {
            if (typeMonth == 0) {
                age = new ArrayList<>();
                userRef//.whereEqualTo("gender", "male")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            }
            else if (typeMonth == 1) {
                age = new ArrayList<>();
                userRef.whereEqualTo("month", "01")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            }
            else if (typeMonth == 2) {
                age = new ArrayList<>();
                userRef.whereEqualTo("month", "02")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            }
            else if (typeMonth == 3) {
                age = new ArrayList<>();
                userRef.whereEqualTo("month", "03")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            }
            else if (typeMonth == 4) {
                age = new ArrayList<>();
                userRef.whereEqualTo("month", "04")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            }
            else if (typeMonth == 5) {
                age = new ArrayList<>();
                userRef.whereEqualTo("month", "05")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            }
            else if (typeMonth == 6) {
                age = new ArrayList<>();
                userRef.whereEqualTo("month", "06")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            }
            else if (typeMonth == 7) {
                age = new ArrayList<>();
                userRef.whereEqualTo("month", "07")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            }
            else if (typeMonth == 8) {
                age = new ArrayList<>();
                userRef.whereEqualTo("month", "08")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            }
            else if (typeMonth == 9) {
                age = new ArrayList<>();
                userRef.whereEqualTo("month", "09")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            }
            else if (typeMonth == 10) {
                age = new ArrayList<>();
                userRef.whereEqualTo("month", "10")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            }
            else if (typeMonth == 11) {
                age = new ArrayList<>();
                userRef.whereEqualTo("month", "11")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            }
            else if (typeMonth == 12) {
                age = new ArrayList<>();
                userRef.whereEqualTo("month", "12")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            }
        }
        else if (tabChange == 2) {
            if (typeMonth == 0) {

                typeRecipe = new ArrayList<>();

                postRef//.whereEqualTo("gender", "male")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 1) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("month", "01")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 2) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("month", "02")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 3) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("month", "03")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 4) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("month", "04")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 5) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("month", "05")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 6) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("month", "06")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 7) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("month", "07")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 8) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("month", "08")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 9) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("month", "09")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 10) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("month", "10")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 11) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("month", "11")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 12) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("month", "12")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }



        }

    }

    public void loadYear2021() {
        year = new ArrayList<>();
        tabYear = 1;
        tabDefault = 1;
        if (tabChange == 0) {
            if (typeMonth == 0) {
                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("year", "2021")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";

                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 1) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "01")
                        .whereEqualTo("year", "2021")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 2) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "02")
                        .whereEqualTo("year", "2021")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 3) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "03")
                        .whereEqualTo("year", "2021")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 4) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "04")
                        .whereEqualTo("year", "2021")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 5) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "05")
                        .whereEqualTo("year", "2021")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 6) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "06")
                        .whereEqualTo("year", "2021")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 7) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "07")
                        .whereEqualTo("year", "2021")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 8) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "08")
                        .whereEqualTo("year", "2021")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 9) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "09")
                        .whereEqualTo("year", "2021")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 10) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "10")
                        .whereEqualTo("year", "2021")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 11) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "11")
                        .whereEqualTo("year", "2021")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 12) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "12")
                        .whereEqualTo("year", "2021")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }

        } else if (tabChange == 1) {
            if (typeMonth == 0) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2021")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 1) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2021")
                        .whereEqualTo("month", "01")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 2) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2021")
                        .whereEqualTo("month", "02")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 3) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2021")
                        .whereEqualTo("month", "03")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 4) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2021")
                        .whereEqualTo("month", "04")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 5) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2021")
                        .whereEqualTo("month", "05")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 6) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2021")
                        .whereEqualTo("month", "06")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 7) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2021")
                        .whereEqualTo("month", "07")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 8) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2021")
                        .whereEqualTo("month", "08")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 9) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2021")
                        .whereEqualTo("month", "09")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 10) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2021")
                        .whereEqualTo("month", "10")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 11) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2021")
                        .whereEqualTo("month", "11")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 12) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2021")
                        .whereEqualTo("month", "12")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            }
        }  else if (tabChange == 2) {
            if (typeMonth == 0) {
                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2021")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 1) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2021")
                        .whereEqualTo("month", "01")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 2) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2021")
                        .whereEqualTo("month", "02")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 3) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2021")
                        .whereEqualTo("month", "03")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 4) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2021")
                        .whereEqualTo("month", "04")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 5) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2021")
                        .whereEqualTo("month", "05")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 6) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2021")
                        .whereEqualTo("month", "06")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 7) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2021")
                        .whereEqualTo("month", "07")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 8) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2021")
                        .whereEqualTo("month", "08")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 9) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2021")
                        .whereEqualTo("month", "09")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 10) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2021")
                        .whereEqualTo("month", "10")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 11) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2021")
                        .whereEqualTo("month", "11")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 12) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2021")
                        .whereEqualTo("month", "12")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }

        }
    }

    public void loadYear2020() {
        year = new ArrayList<>();
        tabYear = 2;
        tabDefault = 1;
        if (tabChange == 0) {
            if (typeMonth == 0) {
                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("year", "2020")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";

                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 1) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "01")
                        .whereEqualTo("year", "2020")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 2) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "02")
                        .whereEqualTo("year", "2020")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 3) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "03")
                        .whereEqualTo("year", "2020")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 4) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "04")
                        .whereEqualTo("year", "2020")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 5) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "05")
                        .whereEqualTo("year", "2020")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 6) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "06")
                        .whereEqualTo("year", "2020")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 7) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "07")
                        .whereEqualTo("year", "2020")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 8) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "08")
                        .whereEqualTo("year", "2020")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 9) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "09")
                        .whereEqualTo("year", "2020")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 10) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "10")
                        .whereEqualTo("year", "2020")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 11) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "11")
                        .whereEqualTo("year", "2020")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 12) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "12")
                        .whereEqualTo("year", "2020")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }

        } else if (tabChange == 1) {
            if (typeMonth == 0) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2020")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 1) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2020")
                        .whereEqualTo("month", "01")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 2) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2020")
                        .whereEqualTo("month", "02")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 3) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2020")
                        .whereEqualTo("month", "03")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 4) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2020")
                        .whereEqualTo("month", "04")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 5) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2020")
                        .whereEqualTo("month", "05")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 6) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2020")
                        .whereEqualTo("month", "06")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 7) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2020")
                        .whereEqualTo("month", "07")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 8) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2020")
                        .whereEqualTo("month", "08")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 9) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2020")
                        .whereEqualTo("month", "09")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 10) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2020")
                        .whereEqualTo("month", "10")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 11) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2020")
                        .whereEqualTo("month", "11")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 12) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2020")
                        .whereEqualTo("month", "12")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            }
        }  else if (tabChange == 2) {
            if (typeMonth == 0) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2020")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 1) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2020")
                        .whereEqualTo("month", "01")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 2) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2020")
                        .whereEqualTo("month", "02")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 3) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2020")
                        .whereEqualTo("month", "03")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 4) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2020")
                        .whereEqualTo("month", "04")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 5) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2020")
                        .whereEqualTo("month", "05")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 6) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2020")
                        .whereEqualTo("month", "06")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 7) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2020")
                        .whereEqualTo("month", "07")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 8) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2020")
                        .whereEqualTo("month", "08")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 9) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2020")
                        .whereEqualTo("month", "09")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 10) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2020")
                        .whereEqualTo("month", "10")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 11) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2020")
                        .whereEqualTo("month", "11")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 12) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2020")
                        .whereEqualTo("month", "12")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }



        }
    }

    public void loadYear2019() {
        year = new ArrayList<>();
        tabYear = 3;
        tabDefault = 1;
        if (tabChange == 0) {
            if (typeMonth == 0) {
                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("year", "2019")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";

                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 1) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "01")
                        .whereEqualTo("year", "2019")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 2) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "02")
                        .whereEqualTo("year", "2019")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 3) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "03")
                        .whereEqualTo("year", "2019")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 4) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "04")
                        .whereEqualTo("year", "2019")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 5) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "05")
                        .whereEqualTo("year", "2019")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 6) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "06")
                        .whereEqualTo("year", "2019")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 7) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "07")
                        .whereEqualTo("year", "2019")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 8) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "08")
                        .whereEqualTo("year", "2019")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 9) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "09")
                        .whereEqualTo("year", "2019")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 10) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "10")
                        .whereEqualTo("year", "2019")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 11) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "11")
                        .whereEqualTo("year", "2019")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            } else if (typeMonth == 12) {

                gender = new ArrayList<>();
                String a = "Male";
                String b = "Female";

                userRef.whereEqualTo("month", "12")
                        .whereEqualTo("year", "2019")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String gender = document.getString("gender");
                                        if (gender.equals("male")) {
                                            totalMale++;
                                        } else if (gender.equals("female")) {
                                            totalFemale++;
                                        } else {
                                            Toast.makeText(ReportActivity.this, "NOOO", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalGender = totalMale + totalFemale;
                                    //malePercentage = Integer.valueOf(malePercent) + "%";
                                    //femalePercentage = Integer.valueOf(femalePercent) + "%";


                                    if (totalMale > 0) {
                                        genders.add(new PieEntry(totalMale, "male"));
                                    }
                                    if (totalFemale > 0) {
                                        genders.add(new PieEntry(totalFemale, "female"));
                                    }


                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(genders, "Gender");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Gender");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }

        } else if (tabChange == 1) {
            if (typeMonth == 0) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2019")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 1) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2019")
                        .whereEqualTo("month", "01")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 2) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2019")
                        .whereEqualTo("month", "02")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 3) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2019")
                        .whereEqualTo("month", "03")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 4) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2019")
                        .whereEqualTo("month", "04")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 5) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2019")
                        .whereEqualTo("month", "05")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 6) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2019")
                        .whereEqualTo("month", "06")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 7) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2019")
                        .whereEqualTo("month", "07")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 8) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2019")
                        .whereEqualTo("month", "08")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 9) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2019")
                        .whereEqualTo("month", "09")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 10) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2019")
                        .whereEqualTo("month", "10")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 11) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2019")
                        .whereEqualTo("month", "11")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            } else if (typeMonth == 12) {
                age = new ArrayList<>();
                userRef.whereEqualTo("year", "2019")
                        .whereEqualTo("month", "12")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        long str_age = (Long) document.get("age");
                                        int age = Integer.valueOf((int) str_age);
                                        if (age >= 13 && age < 20) {
                                            ageTeen++;
                                        } else if (age >= 20 && age < 30) {
                                            ageTwenties++;
                                        } else if (age >= 30 && age < 40) {
                                            ageThirties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFourties++;
                                        } else if (age >= 40 && age < 50) {
                                            ageFifties++;
                                        } else if (age >= 50) {
                                            ageOver++;
                                        }
                                    }

                                    if (ageTeen > 0) {
                                        ages.add(new PieEntry(ageTeen, "Age 13-19"));
                                    }
                                    if (ageTwenties > 0) {
                                        ages.add(new PieEntry(ageTwenties, "Age 20-29"));
                                    }
                                    if (ageThirties > 0) {
                                        ages.add(new PieEntry(ageThirties, "Age 30-39"));
                                    }
                                    if (ageFourties > 0) {
                                        ages.add(new PieEntry(ageFourties, "Age 40-49"));
                                    }
                                    if (ageFifties > 0) {
                                        ages.add(new PieEntry(ageFifties, "Age 50+"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                pieDataSet = new PieDataSet(ages, "Age");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);
                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Age");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
            }
        }  else if (tabChange == 2) {
            if (typeMonth == 0) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2019")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 1) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2019")
                        .whereEqualTo("month", "01")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 2) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2019")
                        .whereEqualTo("month", "02")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 3) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2019")
                        .whereEqualTo("month", "03")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 4) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2019")
                        .whereEqualTo("month", "04")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 5) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2019")
                        .whereEqualTo("month", "05")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 6) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2019")
                        .whereEqualTo("month", "06")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 7) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2019")
                        .whereEqualTo("month", "07")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 8) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2019")
                        .whereEqualTo("month", "08")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 9) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2019")
                        .whereEqualTo("month", "09")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 10) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2019")
                        .whereEqualTo("month", "10")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 11) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2019")
                        .whereEqualTo("month", "11")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }
            else if (typeMonth == 12) {

                typeRecipe = new ArrayList<>();

                postRef.whereEqualTo("year", "2019")
                        .whereEqualTo("month", "12")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String recipeType = document.getString("type");
                                        if (recipeType.equals("vegetables")) {
                                            totalVegetables++;
                                        } else if (recipeType.equals("fruits")) {
                                            totalFruits++;
                                        } else if (recipeType.equals("grains")) {
                                            totalGrains++;
                                        } else if (recipeType.equals("meat")) {
                                            totalMeat++;
                                        } else if (recipeType.equals("seafood")) {
                                            totalSeafood++;
                                        } else if (recipeType.equals("dairy")) {
                                            totalDairy++;
                                        } else if (recipeType.equals("eggs")) {
                                            totalEggs++;
                                        }
                                    }

                                    if (totalVegetables > 0) {
                                        foodType.add(new PieEntry(totalVegetables, "Vegetables"));
                                    }
                                    if (totalFruits > 0) {
                                        foodType.add(new PieEntry(totalFruits, "Fruits"));
                                    }
                                    if (totalGrains > 0) {
                                        foodType.add(new PieEntry(totalGrains, "Grains,Legumes,Nuts & Seeds"));
                                    }
                                    if (totalMeat > 0) {
                                        foodType.add(new PieEntry(totalMeat, "Meat & Poultry"));
                                    }
                                    if (totalSeafood > 0) {
                                        foodType.add(new PieEntry(totalSeafood, "Fish and Seafood"));
                                    }
                                    if (totalDairy > 0) {
                                        foodType.add(new PieEntry(totalDairy, "Dairy Foods"));
                                    }
                                    if (totalEggs > 0) {
                                        foodType.add(new PieEntry(totalEggs, "Eggs"));
                                    }

                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                } else {
                                    Toast.makeText(ReportActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                PieDataSet pieDataSet = new PieDataSet(foodType, "Types of Recipe");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(25f);

                PieData pieData = new PieData(pieDataSet);

                if (type == 1) {
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    initPieChart();
                } else if (type == 0) {
                    initValueChart();
                }
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Types of Recipe");
                pieChart.setCenterTextSize(35f);
                pieChart.animate();
                Legend legend = pieChart.getLegend();
                legend.setTextSize(20f);
            }



        }
    }

    private void initPieChart() {
        //using percentage as values instead of amount
        pieChart.setUsePercentValues(true);

    }

    private void initValueChart() {
        //using percentage as values instead of amount
        pieChart.setUsePercentValues(false);


    }
}