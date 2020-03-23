package com.example.smartrecipes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<FoodViewHolder1>{

    private Context mContext;
    private List<String> myPref;

    public HistoryAdapter(Context mContext, List<String> myPref) {
        this.mContext = mContext;
        this.myPref = myPref;
    }

    @NonNull
    @Override
    public FoodViewHolder1 onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item_history, viewGroup,false);

        return new FoodViewHolder1(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodViewHolder1 foodViewHolder, int i) {
        //foodViewHolder.imageView.setImageResource(myFoodList.get(i).getItemImage());
        foodViewHolder.mTitle.setText(myPref.get(i));
        //foodViewHolder.mDescription.setText(myFoodList.get(i).getDescription());
        //foodViewHolder.mCategory.setText(myFoodList.get(i).getCategory());
    }

    @Override
    public int getItemCount() { return myPref.size(); }
}

class FoodViewHolder1 extends RecyclerView.ViewHolder{

    ImageView imageView;
    TextView mTitle, mDescription, mCategory;
    CardView mCardview;

    public FoodViewHolder1(@NonNull View itemView) {
        super(itemView);
        mTitle = itemView.findViewById(R.id.tvvTitle);
        //mDescription = itemView.findViewById(R.id.txtDescription);
        //mCategory = itemView.findViewById(R.id.tvCategory);
        mCardview = itemView.findViewById(R.id.myCardView2);
    }
}
