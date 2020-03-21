package com.example.smartrecipes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class recipes_list extends AppCompatActivity {

    RecyclerView mRecyclerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        mRecyclerView = (RecyclerView)findViewById(R.id.RecyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(recipes_list.this, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        Intent intent = getIntent();
        String text = intent.getExtras().getString("titleString");

        List<Recipe> recipeList = MainActivity.getRecipeList();
        List<Recipe> recipeSearchList = new ArrayList<>();
        for(int i = 0; i < MainActivity.recipeList.size(); i++){
            if(MainActivity.recipeList.get(i).getTitle().contains(text)){
                recipeSearchList.add(MainActivity.recipeList.get(i));
            }
        }

        SearchAdapter mySearchAdapter = new SearchAdapter(recipes_list.this, recipeSearchList,text);
        mRecyclerView.setAdapter(mySearchAdapter);
        
    }
}
