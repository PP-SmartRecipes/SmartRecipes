package com.example.smartrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;

public class Detail extends AppCompatActivity {

    TextView foodTitle;
    TextView foodDescription;
    TextView foodIngredients;
    ImageView foodImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        foodTitle = (TextView)findViewById(R.id.txtTitle);
        foodDescription = (TextView)findViewById(R.id.txtDescription);
        foodIngredients = (TextView)findViewById(R.id.txtIngredients);
        foodImage = (ImageView)findViewById((R.id.ivImage));

        Bundle mBundle = getIntent().getExtras();

        if(mBundle != null){
            foodTitle.setText(mBundle.getString("Title"));
            foodDescription.setText(mBundle.getString("Description"));
            foodImage.setImageResource(mBundle.getInt("Image"));
        }

        Intent intent = getIntent();
        HashMap<String, String> ingredients= (HashMap<String, String>) intent.getSerializableExtra("Ingredients");
        String ingredientsString = "";
        Iterator it = ingredients.entrySet().iterator();

        while(it.hasNext()){
            HashMap.Entry pair = (HashMap.Entry)it.next();
            ingredientsString += pair.getKey();
            ingredientsString += " ";
            ingredientsString += pair.getValue();
            ingredientsString += '\n';
        }

        foodIngredients.setText(ingredientsString);
    }
}