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
import java.util.Map;

public class Detail extends AppCompatActivity implements View.OnClickListener {

    private Button btn;

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

        btn = (Button)findViewById(R.id.pdfButton);

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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createPdf();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });

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

    private void createPdf() throws IOException, DocumentException {
        Document document=new Document();
        BaseFont font= BaseFont.createFont("assets/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        BaseFont font2= BaseFont.createFont("assets/arialbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        String nazwaa = foodTitle.getText().toString();
        // write the document content
        String targetPdf = "/sdcard/Download/" + nazwaa + ".pdf";
        try {
            PdfWriter.getInstance(document, new FileOutputStream(targetPdf));
            document.open();
            document.add(new Paragraph("Tytuł :" + foodTitle.getText().toString() + "\n", new Font(font2, 25)));
            document.add(new Paragraph("Kategoria: " + foodCategory.getText().toString(), new Font(font, 22)));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Składniki:", new Font(font2,25)));
            document.add(new Paragraph(foodIngredients.getText().toString() + "\n", new Font(font, 22)));
            document.add(new Paragraph("Opis:" , new Font(font2, 25)));
            document.add(new Paragraph(foodDescription.getText().toString(), new Font(font, 22)));

            document.close();

        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
            Toast.makeText(this, "Coś poszło nie tak...: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
        Toast.makeText(this, "PDF utworzony w "+ targetPdf +"!!!", Toast.LENGTH_SHORT).show();
    }
}