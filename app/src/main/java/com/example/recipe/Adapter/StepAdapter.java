package com.example.recipe.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipe.Fragment.PostDetailFragment;
import com.example.recipe.Fragment.ProfileFragment;
import com.example.recipe.Model.Ingredients;
import com.example.recipe.Model.Post;
import com.example.recipe.Model.Steps;
import com.example.recipe.Model.User;
import com.example.recipe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder>  {
    private Context mContext;
    private List<Steps> mStep;

    private FirebaseUser firebaseUser;

    public StepAdapter(Context context, List<Steps> mStep) {
        this.mContext = context;
        this.mStep = mStep;
    }


    @NonNull
    @Override
    public StepAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.steps_item, viewGroup, false);
        return new StepAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepAdapter.ViewHolder holder, int i) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final Steps steps = mStep.get(i);

        holder.tvStepsPos.setText(steps.getStepsPosition());
        holder.tvStepDetails.setText(steps.getSteps());

        if (steps.getSteps().equals("")) {
            holder.tvStepDetails.setVisibility(View.GONE);
        } else {
            holder.tvStepDetails.setVisibility(View.VISIBLE);
            holder.tvStepDetails.setText(steps.getSteps());
        }
        if (steps.getStepsPosition().equals("")) {
            holder.tvStepsPos.setVisibility(View.GONE);
        } else {
            holder.tvStepsPos.setVisibility(View.VISIBLE);
            holder.tvStepsPos.setText(steps.getStepsPosition());
        }

        //getStepInfo(holder.tvStepDetails, holder.tvStepDetails, steps.getPostid());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                    editor.putString("postid",steps.getPostid());
                    editor.apply();

                }
            });
    }



    @Override
    public int getItemCount() {
        return mStep.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivStepsImage;

        public TextView tvStepDetails,tvStepsPos;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvStepDetails = itemView.findViewById(R.id.tvStepsDetails);
            tvStepsPos = itemView.findViewById(R.id.tvStepsPosition);
        }
    }

    private void getStepInfo(final TextView tvStepDetails, final TextView tvStepsPos, String postid){
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Posts").child(postid).child("Steps");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Steps steps = dataSnapshot.getValue(Steps.class);
                //Glide.with(mContext).load(steps.getImageurl()).into(imageView);
                tvStepDetails.setText(steps.getSteps());
                tvStepsPos.setText(steps.getStepsPosition());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    }
