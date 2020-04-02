package com.example.smartrecipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Detail extends AppCompatActivity implements View.OnClickListener {

    private static final int STORAGE_CODE = 1000;
    private Button btn;

    TextView foodTitle;
    TextView foodDescription;
    TextView foodIngredients;
    ImageView foodImage;
    TextView foodCategory;
    Button shoppingCardButton;
    Button favouritesButton;

    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    List<String> ingredientsList;
    boolean ifadded = false;

    DatabaseReference dbref = null;
    FirebaseAuth mAuth = null;

    List<Recipe> favouritesRecipes = null;

    boolean favourite = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
        setContentView(R.layout.activity_detail);

        mAuth = FirebaseAuth.getInstance();

        favouritesRecipes = MainActivity.getFavouritesList();

        favouritesButton = (Button) findViewById(R.id.favouritesButton);

        foodTitle = (TextView) findViewById(R.id.txtTitle);

        btn = (Button) findViewById(R.id.pdfButton);

        foodDescription = (TextView) findViewById(R.id.txtDescription);
        foodIngredients = (TextView) findViewById(R.id.txtIngredients);
        foodImage = (ImageView) findViewById(R.id.ivImage);
        foodCategory = (TextView) findViewById(R.id.txtCategory);
        shoppingCardButton = (Button) findViewById(R.id.shoppingCardButton);

        listDataHeader = ShoppingList.getListDataHeader();
        listDataChild = ShoppingList.getListDataChild();
        try {
            if (listDataHeader.isEmpty()) {
            }
        } catch (NullPointerException e) {
            listDataHeader = new ArrayList<>();
            listDataChild = new HashMap<>();
        }

        Bundle mBundle = getIntent().getExtras();

        if (mBundle != null) {
            foodTitle.setText(mBundle.getString("Title"));
            foodDescription.setText(mBundle.getString("Description"));
            foodCategory.setText(mBundle.getString("Category"));
            Picasso.get().load(mBundle.getString("Image")).into(foodImage);
        }

        if (lookIn())
            favouritesButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_red));
        else
            favouritesButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_gray));

        Intent intent = getIntent();
        HashMap<String, String> ingredients = (HashMap<String, String>) intent.getSerializableExtra("Ingredients");
        String ingredientsString = "";
        Iterator it = ingredients.entrySet().iterator();

        ingredientsList = new ArrayList<>();
        String ingredientsString2 = "";
        while (it.hasNext()) {
            ingredientsString2 = "";
            HashMap.Entry pair = (HashMap.Entry) it.next();
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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, STORAGE_CODE);
                    } else {
                        try {
                            createPdf();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    try {
                        createPdf();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        favouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favourite = false;
                if (mAuth.getCurrentUser() == null) {
                    Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(i);
                } else {
                    dbref = FirebaseDatabase.getInstance().getReference().child("Favourites").child(mAuth.getCurrentUser().getUid());
                    for (Recipe r : favouritesRecipes) {
                        if (foodTitle.getText().equals(r.getTitle())) {
                            favourite = true;
                            MainActivity.removeFavouriteRecipe(r);
                            favouritesRecipes.remove(r);
                            break;
                        }
                    }
                    if (!favourite) {
                        String s = foodTitle.getText().toString();
                        dbref.push().setValue(s);
                    }
                }
            }
        });

        if (!listDataHeader.contains(foodTitle.getText())) {
            listDataHeader.add(foodTitle.getText().toString());
            listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), ingredientsList);
            ifadded = true;
        } else {
            ifadded = false;
        }

        lookIn();
    }

    @Override
    public void onClick(View v) {
        ShoppingList.setListDataHeader(listDataHeader);
        ShoppingList.setListDataChild(listDataChild);
        if (ifadded) {
            Toast.makeText(
                    getApplicationContext(),
                    "Dodano składniki do listy zakupów", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void createPdf() throws IOException, DocumentException {
        Document document = new Document();
        BaseFont font = BaseFont.createFont("assets/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        BaseFont font2 = BaseFont.createFont("assets/arialbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        String nazwaa = foodTitle.getText().toString();
        // write the document content
        String targetPdf = "/sdcard/Download/" + nazwaa + ".pdf";
        try {
            PdfWriter.getInstance(document, new FileOutputStream(targetPdf));
            document.open();
            document.add(new Paragraph("Tytuł: " + foodTitle.getText().toString() + "\n", new Font(font2, 25)));
            document.add(new Chunk("Kategoria: ", new Font(font2, 25)));
            document.add(new Chunk(foodCategory.getText().toString(), new Font(font, 22)));
            document.add(new Paragraph("Składniki:", new Font(font2, 25)));
            document.add(new Paragraph(foodIngredients.getText().toString() + "\n", new Font(font, 22)));
            document.add(new Paragraph("Opis:", new Font(font2, 25)));
            document.add(new Paragraph(foodDescription.getText().toString(), new Font(font, 22)));

            document.close();

        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
            Toast.makeText(this, "Coś poszło nie tak...: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
        Toast.makeText(this, "PDF utworzony w " + targetPdf + "!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        createPdf();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "Brak pozwolenia", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public boolean lookIn() {
        if(mAuth.getCurrentUser() != null) {
            dbref = FirebaseDatabase.getInstance().getReference().child("Favourites").child(mAuth.getCurrentUser().getUid());
            for (Recipe r : favouritesRecipes) {
                Log.e("recipe", r.getTitle());
                if (foodTitle.getText().toString().equals(r.getTitle()))
                    return true;
            }
        }
        return false;
    }
}