package com.example.smartrecipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference dbref = null;
    RecyclerView mRecyclerView = null;
    static List<Recipe> recipeList = null;
    static List<Recipe> recipeSearchList = null;
    static List<String> SearchList = new ArrayList<>();

    AutoCompleteTextView editSearch;
    Button searchButton;
    Button newRecipeButton = null;
    String [] suggestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();

        editSearch= (AutoCompleteTextView) findViewById(R.id.edit_search);
        editSearch.setOnClickListener(this);
        searchButton= (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);
        newRecipeButton = (Button)findViewById(R.id.button_newrecipe);

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
                recipeList.clear();
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    Recipe r = s.getValue(Recipe.class);
                    recipeList.add(r);
                    refresh();
                }
                suggestions = new String[recipeList.size()];
                for(int i = 0; i < recipeList.size(); i++){
                    suggestions[i] = recipeList.get(i).getTitle();
                    Log.e("test",suggestions[i]);
                }
                editSearch.setAdapter(new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1,suggestions));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        newRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(v.getContext(),RecipeAdd.class);
                startActivity(i);
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
        SearchList.add(editSearch.getText().toString());
        recipeSearchList = new ArrayList<Recipe>();
        for(int x = 0; x < recipeList.size(); x++){
            if(recipeList.get(x).getTitle().toLowerCase().contains(editSearch.getText().toString().toLowerCase())){
                recipeSearchList.add(recipeList.get(x));
            }
        }
        if(!recipeSearchList.isEmpty()){
            startActivity(i);
            editSearch.clearFocus();;
            editSearch.setText("");
        }
        else{
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("Uwaga!");
            alert.setMessage("Nie znaleziono przepisów zawierających podaną frazę. Podaj inną frazę.");
            alert.setPositiveButton("OK",null);
            alert.show();
        }


        saveData();

    }

    private void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(SearchList);
        editor.putString("task list", json);
        editor.apply();

    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        SearchList = gson.fromJson(json, type);

        if (SearchList == null){
            SearchList = new ArrayList<>();
        }

    }

    public static List<String> getSearchList(){ return  SearchList;}
    public static List<Recipe> getRecipeList(){
        return recipeList;
    }
    public static List<Recipe> getRecipeSearchList(){
        return recipeSearchList;
    }
}
