package com.example.smartrecipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference dbref = null;
    RecyclerView mRecyclerView = null;
    static List<Recipe> recipeList = null;

    EditText editSearch;
    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editSearch= (EditText) findViewById(R.id.edit_search);
        editSearch.setOnClickListener(this);
        searchButton= (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);

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

        recipeList = new ArrayList<>();

        //Database connection
        dbref = FirebaseDatabase.getInstance().getReference().child("Recipes");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    Recipe r = s.getValue(Recipe.class);
                    recipeList.add(r);
                    refresh();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void refresh() {
        MyAdapter myAdapter = new MyAdapter(MainActivity.this, recipeList);
        mRecyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        i = new Intent(this, Recipes_list.class);
        i.putExtra("titleString", editSearch.getText().toString());
        startActivity(i);
        editSearch.clearFocus();;
        editSearch.setText("");
    }

    public static List<Recipe> getRecipeList(){
        return recipeList;
    }
}
