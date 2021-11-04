package com.example.recipe.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.recipe.Model.Ingredients;
import com.example.recipe.Model.Steps;
import com.example.recipe.PostActivity;
import com.example.recipe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {

    private Context nContext;

    private List<Steps> stepList;

    private FirebaseUser firebaseUser;

    private String[] numberWord;

    private LayoutInflater inflater;

    private StepsAdapter.OnItemClickListener mListener;

    private final int PICK_IMAGE_REQUEST = 22;

    public List<Uri> images = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onDeleteClick(int position);
    }


    public StepsAdapter(Context context, List<Steps> stepList, String[] numberWord,List<Uri> images) {
        this.nContext = context;
        this.numberWord = numberWord;
        this.stepList = stepList;
        this.images = images;
    }

    public void setOnItemClickListener(StepsAdapter.OnItemClickListener listener) {
        mListener = listener;
    }


    @NonNull
    @Override
    public StepsAdapter.StepsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_item, viewGroup, false);
        View view = LayoutInflater.from(nContext).inflate(R.layout.recipe_item, viewGroup, false);

        return new StepsAdapter.StepsViewHolder(view, mListener);

    }

    @Override
    public void onBindViewHolder(@NonNull StepsAdapter.StepsViewHolder holder, int i) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Steps currentItem = stepList.get(i);
        int pos = i + 1;
        holder.tvStepsPosition.setText("" + pos);


        Steps steps = stepList.get(i);
        holder.tvStepsList.setText(steps.getSteps());
        //holder.ivStepsImage.setImageResource();

        Glide.with(nContext).load(steps.getStepsImage())
                .apply(new RequestOptions().placeholder(R.drawable.ic_image))
                .into(holder.ivStepsImage);


    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    public Steps getSteps(int position) {
        return stepList.get(position);
    }


 /*   @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (inflater == null) {
            inflater = (LayoutInflater) nContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.steps_item,null);
        }

        TextView tvStepsPosition = convertView.findViewById(R.id.tvStepsPosition);

        tvStepsPosition.setText(numberWord[position]);
        return convertView;
    } */


    class StepsViewHolder extends RecyclerView.ViewHolder {
        ImageView ivStepsImage;
        TextView tvStepsPosition;
        TextView tvStepsList;
        ImageView ivDelete;


        StepsViewHolder(View itemView, final StepsAdapter.OnItemClickListener listener) {
            super(itemView);
            tvStepsList = itemView.findViewById(R.id.etStepsDetails);
            ivStepsImage = itemView.findViewById(R.id.ivStepsImage);
            ivDelete = itemView.findViewById(R.id.ivDelete);

            tvStepsPosition = itemView.findViewById(R.id.tvStepsPosition);

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }

    }
}
