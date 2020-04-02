package com.example.smartrecipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class Category_list extends AppCompatActivity {

    RecyclerView mRecyclerView = null;
    FirebaseAuth mAuth = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.account:
                Intent intent = new Intent(this, UserSettings.class);
                this.startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        mAuth = FirebaseAuth.getInstance();

        mRecyclerView = (RecyclerView)findViewById(R.id.RecyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(Category_list.this, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        Intent intent = getIntent();
        String category = intent.getExtras().getString("categoryString");
        List<Recipe> recipeList = MainActivity.getRecipeList();
        List<Recipe> recipeSearchList = new ArrayList<>();
        for(int i = 0; i < recipeList.size(); i++){
            if(recipeList.get(i).getCategory().equals(category)){
                recipeSearchList.add(recipeList.get(i));
            }
        }

        SearchAdapter mySearchAdapter = new SearchAdapter(Category_list.this, recipeSearchList);
        mRecyclerView.setAdapter(mySearchAdapter);


        //BOTTOM NAVIAGTION

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
                    case R.id.shopping:
                        startActivity(new Intent(getApplicationContext(),ShoppingList.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.favorite:
                        if(mAuth.getCurrentUser() != null) {
                            startActivity(new Intent(getApplicationContext(), FavouritesActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        }
                        else{
                            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        }
                }
                return false;
            }
        });

    }
}
