package com.example.smartrecipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Category extends AppCompatActivity implements View.OnClickListener{
    private CardView breakfastCard, lunchCard, dinnerCard, dessertCard, drinkCard, fastfoodCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        //defining Cards
        breakfastCard = (CardView) findViewById(R.id.breakfast_card);
        lunchCard = (CardView) findViewById(R.id.lunch_card);
        dinnerCard = (CardView) findViewById(R.id.dessert_card);
        drinkCard = (CardView) findViewById(R.id.drink_card);
        fastfoodCard = (CardView) findViewById(R.id.fastfood_card);
        //Clicklistener for Cards
        breakfastCard.setOnClickListener(this);
        lunchCard.setOnClickListener(this);
        dinnerCard.setOnClickListener(this);
        drinkCard.setOnClickListener(this);
        fastfoodCard.setOnClickListener(this);


        //Inizialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.category);

        //ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.category:
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.history:
                        startActivity(new Intent(getApplicationContext(),History.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
    //Testowo
    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.breakfast_card : i = new Intent(this, History.class);startActivity(i); break;
        }
    }
}
