package com.example.smartrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Detail extends AppCompatActivity implements View.OnClickListener {

    TextView foodTitle;
    TextView foodDescription;
    TextView foodIngredients;
    ImageView foodImage;
    TextView foodCategory;
    Button shoppingCardButton;

    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    List<String> ingredientsList;
    boolean ifadded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        foodTitle = (TextView)findViewById(R.id.txtTitle);
        foodDescription = (TextView)findViewById(R.id.txtDescription);
        foodIngredients = (TextView)findViewById(R.id.txtIngredients);
        foodImage = (ImageView)findViewById(R.id.ivImage);
        foodCategory = (TextView)findViewById(R.id.txtCategory);
        shoppingCardButton = (Button)findViewById(R.id.shoppingCardButton);


        listDataHeader = ShoppingList.getListDataHeader();
        listDataChild = ShoppingList.getListDataChild();
        try
        {
            if (listDataHeader.isEmpty()){
            }
        }
        catch(NullPointerException e)
        {
            listDataHeader = new ArrayList<>();
            listDataChild = new HashMap<>();
        }


        Bundle mBundle = getIntent().getExtras();

        if(mBundle != null){
            foodTitle.setText(mBundle.getString("Title"));
            foodDescription.setText(mBundle.getString("Description"));
            foodCategory.setText(mBundle.getString("Category"));
            Picasso.get().load(mBundle.getString("Image")).into(foodImage);
        }

        Intent intent = getIntent();
        HashMap<String, String> ingredients= (HashMap<String, String>) intent.getSerializableExtra("Ingredients");
        String ingredientsString = "";
        Iterator it = ingredients.entrySet().iterator();

        ingredientsList = new ArrayList<>();
        String ingredientsString2 = "";
        while(it.hasNext()){
            ingredientsString2 = "";
            HashMap.Entry pair = (HashMap.Entry)it.next();
            ingredientsString += pair.getKey();
            ingredientsString += " ";
            ingredientsString += pair.getValue();
            ingredientsString2 += pair.getKey();
            ingredientsString2 += " ";
            ingredientsString2 += pair.getValue();
            ingredientsString += '\n';
            ingredientsList.add(ingredientsString2);

        }

        foodIngredients.setText(ingredientsString);
        if(!listDataHeader.contains(foodTitle.getText())){
            listDataHeader.add(foodTitle.getText().toString());
            listDataChild.put(listDataHeader.get(listDataHeader.size()-1),ingredientsList);
            ifadded = true;
        }
        else{
            ifadded = false;
        }

    }

    @Override
    public void onClick(View v) {
        ShoppingList.setListDataHeader(listDataHeader);
        ShoppingList.setListDataChild(listDataChild);
        if(ifadded) {
            Toast.makeText(
                    getApplicationContext(),
                    "Dodano składniki do listy zakupów", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}