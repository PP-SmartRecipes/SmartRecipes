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

public class MyAdapter extends RecyclerView.Adapter<FoodViewHolder>{

    private Context mContext;
    private List<Recipe> myFoodList;

    public MyAdapter(Context mContext, List<Recipe> myFoodList) {
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
        Picasso.get().load(myFoodList.get(myFoodList.size() - 1).getImageUrl()).into(foodViewHolder.imageView);
        foodViewHolder.mTitle.setText(myFoodList.get(myFoodList.size() - 1).getTitle());
        foodViewHolder.mCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (mContext, Detail.class);
                intent.putExtra("Image", myFoodList.get(myFoodList.size() - 1).getImageUrl());
                intent.putExtra("Description", myFoodList.get(myFoodList.size() - 1).getDescription());
                intent.putExtra("Title", myFoodList.get(myFoodList.size() - 1).getTitle());
                intent.putExtra("Category", myFoodList.get(myFoodList.size() - 1).getCategory());
                intent.putExtra("Ingredients", myFoodList.get(myFoodList.size() - 1).getIngredients());
                intent.putExtra("Rating", myFoodList.get(myFoodList.size() - 1).getRating());
                intent.putExtra("numberOfRatings", myFoodList.get(myFoodList.size() - 1).getNumberOfRatings());
                intent.putStringArrayListExtra("usersRated", myFoodList.get(myFoodList.size() - 1).getUsersRated());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() { return 1; }
}

class FoodViewHolder extends RecyclerView.ViewHolder{

    ImageView imageView;
    TextView mTitle, mDescription, mCategory;
    CardView mCardview;

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.ivImage);
        mTitle = itemView.findViewById(R.id.tvTitle);
        mCardview = itemView.findViewById(R.id.myCardView);
    }
}