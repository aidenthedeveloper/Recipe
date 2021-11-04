package com.example.recipe.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe.Model.Ingredients;
import com.example.recipe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder>  {
    private Context mContext;
    private List<Ingredients> mIngre;

    private FirebaseUser firebaseUser;

    public RecommendAdapter(Context context, List<Ingredients> mIngre) {
        this.mContext = context;
        this.mIngre = mIngre;
    }


    @NonNull
    @Override
    public RecommendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ingredients_info, viewGroup, false);
        return new RecommendAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendAdapter.ViewHolder holder, int i) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final Ingredients ingredients = mIngre.get(i);

        holder.tvIngredients.setText(ingredients.getIngredients());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("postid",ingredients.getIngredients());
                editor.apply();

            }
        });
    }



    @Override
    public int getItemCount() {
        return mIngre.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvIngredients;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvIngredients = itemView.findViewById(R.id.ingredientsInfo);

        }
    }
}
