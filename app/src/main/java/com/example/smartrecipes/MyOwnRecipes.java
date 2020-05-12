package com.example.smartrecipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MyOwnRecipes extends AppCompatActivity  {

    RecyclerView mRecyclerView = null;
    FirebaseAuth mAuth = null;
    DatabaseReference dbref = null;
    MyOwnRecipesAdapter myOwnSearchRecipesAdapter;
    List<Recipe> myOwnRecipes = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_own_recipes);

        mAuth = FirebaseAuth.getInstance();
        dbref = FirebaseDatabase.getInstance().getReference().child("Recipes");

        mRecyclerView = (RecyclerView)findViewById(R.id.RecyclerView_Own_Recipes);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MyOwnRecipes.this, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        myOwnRecipes = MainActivity.getMyOwnRecipes();

        myOwnSearchRecipesAdapter = new MyOwnRecipesAdapter(this, myOwnRecipes);
        myOwnSearchRecipesAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(myOwnSearchRecipesAdapter);

        SwipeHelper swipeHelper = new SwipeHelper(this) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Usuń",
                        0,
                        Color.parseColor("#FF3C30"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(final int pos) {
                                String name = myOwnSearchRecipesAdapter.getData().get(pos).getTitle();
                                Query query = dbref.orderByChild("title").equalTo(name);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                                            ds.getRef().removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                myOwnSearchRecipesAdapter.removeItem(pos);
                                Toast toast = Toast.makeText(getApplicationContext(), "Pomyślnie usunięto przepis", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 700);
                                toast.show();
                            }
                        }
                ));
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Zmodyfikuj",
                        0,
                        Color.parseColor("#FF9502"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                String name = myOwnSearchRecipesAdapter.getData().get(pos).getTitle();
                                System.out.println(myOwnSearchRecipesAdapter.getData().get(pos).getAuthor());
                                Intent i = new Intent(MyOwnRecipes.this, RecipeAdd.class);
                                i.putExtra("Title", myOwnSearchRecipesAdapter.getData().get(pos).getTitle());
                                i.putExtra("Description", myOwnSearchRecipesAdapter.getData().get(pos).getDescription());
                                i.putExtra("Category", myOwnSearchRecipesAdapter.getData().get(pos).getCategory());
                                i.putExtra("ImageUrl", myOwnSearchRecipesAdapter.getData().get(pos).getImageUrl());
                                i.putExtra("Ingredients", myOwnSearchRecipesAdapter.getData().get(pos).getIngredients());
                                i.putExtra("Rating", myOwnSearchRecipesAdapter.getData().get(pos).getRating());
                                i.putExtra("numberOfRatings", myOwnSearchRecipesAdapter.getData().get(pos).getNumberOfRatings());
                                i.putStringArrayListExtra("usersRated", myOwnSearchRecipesAdapter.getData().get(pos).getUsersRated());
                                i.putExtra("Update", true);
                                startActivity(i);
                            }
                        }
                ));
            }
        };
        swipeHelper.attachToRecyclerView(mRecyclerView);

        //BOTTOM NAVIAGTION
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
