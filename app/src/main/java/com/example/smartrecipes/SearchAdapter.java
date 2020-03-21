package com.example.smartrecipes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<FoodViewHolder>{

    private Context mContext;
    private List<Recipe> myFoodList;

    public SearchAdapter(Context mContext, List<Recipe> myFoodList) {
        this.mContext = mContext;
        this.myFoodList = myFoodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_items, viewGroup,false);

        return new FoodViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodViewHolder foodViewHolder, int i) {
        Picasso.get().load(myFoodList.get(i).getImageUrl()).into(foodViewHolder.imageView);
        //foodViewHolder.imageView.setImageResource(myFoodList.get(i).getItemImage());
        foodViewHolder.mTitle.setText(myFoodList.get(i).getTitle());
        //foodViewHolder.mDescription.setText(myFoodList.get(i).getDescription());
        //foodViewHolder.mCategory.setText(myFoodList.get(i).getCategory());
        foodViewHolder.mCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, Detail.class);
                intent.putExtra("Image", myFoodList.get(foodViewHolder.getAdapterPosition()).getImageUrl());
                intent.putExtra("Description", myFoodList.get(foodViewHolder.getAdapterPosition()).getDescription());
                intent.putExtra("Title", myFoodList.get(foodViewHolder.getAdapterPosition()).getTitle());
                intent.putExtra("Category", myFoodList.get(foodViewHolder.getAdapterPosition()).getCategory());
                intent.putExtra("Ingredients", myFoodList.get(foodViewHolder.getAdapterPosition()).getIngredients());
                mContext.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return myFoodList.size();
    }
}