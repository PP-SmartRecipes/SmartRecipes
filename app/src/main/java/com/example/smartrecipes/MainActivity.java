package com.example.smartrecipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    List<Recipe> myFoodList;
    Recipe mFoodData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inizialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        //ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.category:
                        startActivity(new Intent(getApplicationContext(),Category.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        return true;
                    case R.id.history:
                        startActivity(new Intent(getApplicationContext(),History.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        mRecyclerView = (RecyclerView)findViewById(R.id.RecyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        myFoodList = new ArrayList<>();

        //tutaj z bazy pobieram to co mnie interesuje
        mFoodData = new Recipe("Tytul1","Opis1", "Cena1");
        myFoodList.add(mFoodData);
        mFoodData = new Recipe("Tytul2","Opis2", "Cena2");
        myFoodList.add(mFoodData);
        mFoodData = new Recipe("Tytul3","Opis3", "Cena3");
        myFoodList.add(mFoodData);
        mFoodData = new Recipe("Tytul4","Opis4", "Cena4");
        myFoodList.add(mFoodData);
        mFoodData = new Recipe("Tytul5","Opis5", "Cena5");
        myFoodList.add(mFoodData);
        mFoodData = new Recipe("Tytul6","Opis6", "Cena6");
        myFoodList.add(mFoodData);
        mFoodData = new Recipe("Tytul7","Opis7", "Cena7");
        myFoodList.add(mFoodData);
        mFoodData = new Recipe("Tytul8","Opis8", "Cena8");
        myFoodList.add(mFoodData);

        MyAdapter myAdapter = new MyAdapter(MainActivity.this, myFoodList);
        mRecyclerView.setAdapter(myAdapter);

    }
}
