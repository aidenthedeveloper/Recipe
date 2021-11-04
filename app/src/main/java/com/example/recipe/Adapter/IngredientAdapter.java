package com.example.recipe.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe.Model.Ingredients;
import com.example.recipe.Model.Post;
import com.example.recipe.R;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/*public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientsViewHolder> {


    private List<Ingredients> ingreList;

    private FirebaseUser firebaseUser;


    public IngredientAdapter(List<Ingredients> ingreList) {
        this.ingreList = ingreList;
    }


    @NonNull
    @Override
    public IngredientAdapter.IngredientsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_item, viewGroup, false);
            return new IngredientAdapter.IngredientsViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.IngredientsViewHolder holder, int i) {
        Ingredients ingredients = ingreList.get(i);
        holder.tvIngredientList.setText(ingredients.getTotalIng());
    }

    @Override
    public int getItemCount() {
        return ingreList.size();
    }

    class IngredientsViewHolder extends RecyclerView.ViewHolder {
        TextView tvIngredientList;

        IngredientsViewHolder(View itemView) {
            super(itemView);
            tvIngredientList = itemView.findViewById(R.id.ingredientsInfo);
        }
    }
} */