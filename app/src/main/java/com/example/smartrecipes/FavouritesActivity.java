package com.example.smartrecipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends AppCompatActivity {

    RecyclerView mRecyclerView = null;

    List<Recipe> recipesList = null;
    List<Recipe> favouritesRecipes = null;
    List<String> favouritesStrings = null;
    DatabaseReference dbref = null;
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
                if(mAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(this, UserSettings.class);
                    this.startActivity(intent);
                    break;
                }
                else{
                    Intent intent = new Intent(this, SignInActivity.class);
                    startActivity(intent);
                    break;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        mRecyclerView = (RecyclerView)findViewById(R.id.RecyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(FavouritesActivity.this, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        /*
        recipesList = MainActivity.getRecipeList();
        favouritesRecipes = new ArrayList<>();
        favouritesStrings = new ArrayList<>();
        */

        mAuth = FirebaseAuth.getInstance();

        /*
        dbref = FirebaseDatabase.getInstance().getReference().child("Favourites").child(mAuth.getCurrentUser().getUid());
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipesList = MainActivity.getRecipeList();
                favouritesStrings.clear();
                favouritesRecipes.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String s = ds.getValue(String.class);
                    favouritesStrings.add(s);
                }
                for(String s : favouritesStrings)
                    for(Recipe r : recipesList){
                        if(s.equals(r.getTitle())) {
                            favouritesRecipes.add(r);
                        }
                    }
                update();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */

        update();

        //BOTTOM NAVIAGTION

        //Inizialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.favouritesButton);

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
                        return true;
                }
                return false;
            }
        });
    }

    private void update() {
        SearchAdapter mySearchAdapter = new SearchAdapter(FavouritesActivity.this, MainActivity.getFavouritesList());
        mRecyclerView.setAdapter(mySearchAdapter);
    }
}
