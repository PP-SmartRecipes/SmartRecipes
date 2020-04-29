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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
    List<String> ingredientsStrings;
    EditText eanEditText;
    EditText brandEditText;
    EditText ingredientsEditText = null;
    EditText ingredientQuantity = null;
    ListView ingredientsList = null;
    ImageView check = null;
    AnimatedVectorDrawableCompat avd = null;
    AnimatedVectorDrawable avd2 = null;
    Button sendBtn;
    Button confirmIng;
    ArrayList<String> autocomplete;
    ArrayList<String> reqHints;
    String[] reqHintsArr;

    DatabaseReference dbref = null;
    StorageReference stref = null;
    FirebaseAuth mAuth = null;
    public Uri imguri = null;

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
        setContentView(R.layout.activity_recipe_add);

        eanEditText = findViewById(R.id.ean);
        brandEditText = findViewById(R.id.brand);
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
        mAuth = FirebaseAuth.getInstance();
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
                if(eanEditText.getText().toString().isEmpty() || brandEditText.getText().toString().isEmpty() || spinner.getSelectedItem().toString().isEmpty() || ingredients.isEmpty())
                    ;
                else
                    FileUploader();
            }
        });

        confirmIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ingredientsEditText.getText().toString().isEmpty() || ingredientQuantity.getText().toString().isEmpty())
                    ;
                else {
                    ingredients.put(ingredientsEditText.getText().toString(), ingredientQuantity.getText().toString());
                    ingredientsStrings.add(ingredientsEditText.getText().toString() + "\t\t" + ingredientQuantity.getText().toString());
                    ingredientsEditText.setText("");
                    ingredientQuantity.setText("");
                }
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
                adapter.notifyDataSetChanged();
            }
        });

    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void FileUploader(){
        if(!done) {
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/smartrecipes-c64e7.appspot.com/o/Images%2Fbrak_zdjecia.png?alt=media&token=f1a6ebb3-392b-444e-a4ed-9bd35c18b6ba";
            pushRecipe();
        }
        else {
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
    }

    private void pushRecipe(){
            recipe.setTitle(eanEditText.getText().toString().trim());
            recipe.setDescription(brandEditText.getText().toString().trim());
            recipe.setCategory(spinner.getSelectedItem().toString().trim());
            recipe.setImageUrl(imageUrl);
            recipe.setIngredients(ingredients);
            recipe.setAuthor(mAuth.getCurrentUser().getUid());
            dbref.push().setValue(recipe);
            Toast toast = Toast.makeText(this, "Pomyślnie dodano przepis!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 500);
            toast.show();
            goBack();
    }

    private void FileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    private void goBack(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        check.setVisibility(View.INVISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imguri = data.getData();
            done = true;
            Drawable drawable = check.getDrawable();
            if(drawable instanceof AnimatedVectorDrawableCompat) {
                avd = (AnimatedVectorDrawableCompat) drawable;
                check.setVisibility(View.VISIBLE);
                avd.start();
            }
            else if(drawable instanceof AnimatedVectorDrawable){
                avd2 = (AnimatedVectorDrawable) drawable;
                check.setVisibility(View.VISIBLE);
                avd2.start();
            }
        }
    }
}
