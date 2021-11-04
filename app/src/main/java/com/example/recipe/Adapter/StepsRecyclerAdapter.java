package com.example.recipe.Adapter;

import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe.Model.Steps;
import com.example.recipe.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class StepsRecyclerAdapter  extends RecyclerView.Adapter<StepsRecyclerAdapter.PostViewHolder> {
    private static String TAG = StepsRecyclerAdapter.class.getSimpleName();
    public static List<Steps> stepsList;
    public Context mcontext;
    View view;

    public StepsRecyclerAdapter(Context context, List<Steps> uploads) {
        stepsList = uploads;
        mcontext = context;
    }

    @NonNull
    @Override
    public StepsRecyclerAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mcontext).inflate(R.layout.item_steps, parent, false);
        return new StepsRecyclerAdapter.PostViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull StepsRecyclerAdapter.PostViewHolder holder, int position) {
        int pos = position+1;
        holder.tvStepsPosition.setText(""+pos);
    }


    @Override
    public int getItemCount() {
        return stepsList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {


        private TextView tvStepsPosition;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            tvStepsPosition = itemView.findViewById(R.id.tvStepsPosition);

        }

    }
}

/*public class StepsRecyclerAdapter extends RecyclerView.Adapter<StepsRecyclerAdapter.PostViewHolder> {
    private ArrayList<Steps> mStepsArrayList;
    private static String TAG = StepsRecyclerAdapter.class.getSimpleName();
    public static List<Steps> stepsList;
    public Context mcontext;
    private OnItemClickListener mListener;
    View view;



    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {


        private TextView tvStepsPosition, tvStepsDetails;
        private EditText etStepsDetails;
        private ImageView ivStepsImage;
        public ImageView ivDelete;


        public PostViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            etStepsDetails = itemView.findViewById(R.id.etStepsDetails);
            tvStepsPosition = itemView.findViewById(R.id.tvStepsPosition);
            ivStepsImage = itemView.findViewById(R.id.ivStepsImage);
            ivDelete = itemView.findViewById(R.id.ivDelete);


            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public StepsRecyclerAdapter(ArrayList<Steps> stepsArrayList){
        mStepsArrayList = stepsArrayList;
    }



    public StepsRecyclerAdapter(Context context, List<Steps> uploads) {
        stepsList = uploads;
        mcontext = context;
    }

    @NonNull
    @Override
    public StepsRecyclerAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mcontext).inflate(R.layout.item_steps, parent, false);
        return new StepsRecyclerAdapter.PostViewHolder(view, mListener);


    }

    @Override
    public void onBindViewHolder(@NonNull StepsRecyclerAdapter.PostViewHolder holder, int position) {
        int pos = position+1;
        holder.tvStepsPosition.setText(""+pos);
        //holder.etStepsDetails.setText(stepsList.get(position).getStepsDetails());


    }


    @Override
    public int getItemCount() {
        return stepsList.size();
    }


} */
