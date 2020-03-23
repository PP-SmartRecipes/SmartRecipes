package com.example.smartrecipes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecipeAdd extends AppCompatActivity {

    Recipe recipe = null;
    HashMap<String, String> ingredients = null;
    Spinner spinner = null;
    Button imageButton = null;
    boolean done = false;
    String imageUrl = "";
    //List<Ingredient> ingList;
    List<String> ingredientsStrings;
    List<String> quantityStrings;
    EditText eanEditText;
    EditText brandEditText;
    EditText productNameEditText;
    EditText ingredientsEditText = null;
    EditText ingredientQuantity = null;
    ListView ingredientsList = null;
    ImageView check = null;
    AnimatedVectorDrawableCompat avd = null;
    AnimatedVectorDrawable avd2 = null;
    Button sendBtn;
    Button confirmIng;
    String json;
    ArrayList<String> autocomplete;
    String result;
    String EAN;
    ArrayList<String> reqHints;
    String[] reqHintsArr;

    DatabaseReference dbref = null;
    StorageReference stref = null;
    public Uri imguri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_add);

        eanEditText = findViewById(R.id.ean);
        brandEditText = findViewById(R.id.brand);
        //productNameEditText = findViewById(R.id.productName);
        ingredientsEditText = findViewById(R.id.ingredients);
        ingredientQuantity = findViewById(R.id.quantity);
        sendBtn = findViewById(R.id.sendBtn);
        confirmIng = findViewById(R.id.confirmIng);
        ingredientsList = findViewById(R.id.listview1);

        ingredientsStrings = new ArrayList<>();
        autocomplete = new ArrayList<>();
        reqHints = new ArrayList<>();
        reqHintsArr = new String[]{};

        imageButton = (Button)findViewById(R.id.imageButton);
        check = (ImageView)findViewById(R.id.check);
        dbref = FirebaseDatabase.getInstance().getReference().child("Recipes");
        stref = FirebaseStorage.getInstance().getReference("Images");
        recipe = new Recipe();
        ingredients = new HashMap<>();
        spinner = (Spinner)findViewById(R.id.spinner);
        String[] arraySpinner = new String[] {
                "Kategoria", "Śniadanie", "Lunch", "Obiad", "Deser", "Napój", "FastFood"
        };
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }
        };

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUploader();
            }
        });

        confirmIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredients.put(ingredientsEditText.getText().toString(), ingredientQuantity.getText().toString());
                ingredientsStrings.add(ingredientsEditText.getText().toString() + "\t\t" + ingredientQuantity.getText().toString());
                ingredientsEditText.setText("");
                ingredientQuantity.setText("");
            }
        });

        final ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ingredientsStrings);
        ingredientsList.setAdapter(adapter);

        ingredientsList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = ingredientsStrings.get(position);
                ingredientsStrings.remove(item);
                ingredients.remove(item);
                //ingList.remove(ingList.get(position));
                adapter.notifyDataSetChanged();
            }
        });

        /*
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product editedProduct = new Product();
                ArrayList<Ingredient> ingredientsList = new ArrayList<>();

                for (String item : ingStrings){
                    ingList.add(new Ingredient(item));
                    ingredientsList.add(new Ingredient(item));
                }

                if(prod!=null){
                    prod.setBrand(brandEditText.getText().toString());
                    prod.setProductName(productNameEditText.getText().toString());
                    prod.setIngredients(ingredientsList);
                } else {
                    editedProduct.setBrand(brandEditText.getText().toString());
                    editedProduct.setProductName(productNameEditText.getText().toString());
                    editedProduct.setIngredients(ingredientsList);
                }



                if(prod!=null) {
                    prod.setId(prod.getId());
                    prod.setGtin(prod.getGtin());
                } else {
                    editedProduct.setGtin(EAN);
                }

                try {
                    if(prod!=null) json = new ObjectMapper().writeValueAsString(prod);
                    else json = new ObjectMapper().writeValueAsString(editedProduct);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    json = "JsonProcessingException";
                }
                System.out.println(json);

                //System.out.println(json);

                if(prod!=null){
                    HttpPutRequest putRequest = new HttpPutRequest();
                    try {
                        putRequest.execute(json);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    HttpPostRequest postRequest = new HttpPostRequest();
                    try {
                        result = postRequest.execute(json).get();
                        //System.out.println(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                startActivity(new Intent(editProduct.this, MainActivity.class));

            }
        });
        */
    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void FileUploader(){
        StorageReference ref = stref.child(System.currentTimeMillis() + "." + getExtension(imguri));

        ref.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Get a URL to the uploaded content
                        Task<Uri> downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageUrl = uri.toString();
                                pushRecipe();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    private void pushRecipe(){
        recipe.setTitle(eanEditText.getText().toString().trim());
        recipe.setDescription(brandEditText.getText().toString().trim());
        recipe.setCategory(spinner.getSelectedItem().toString().trim());
        recipe.setImageUrl(imageUrl);
        recipe.setIngredients(ingredients);
        dbref.push().setValue(recipe);
    }

    private void FileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imguri = data.getData();
            done = true;
        }
        Drawable drawable = check.getDrawable();
        if(drawable instanceof AnimatedVectorDrawableCompat) {
            avd = (AnimatedVectorDrawableCompat) drawable;
            avd.start();
        }
        else if(drawable instanceof AnimatedVectorDrawable){
            avd2 = (AnimatedVectorDrawable) drawable;
            avd2.start();
        }
    }
}
