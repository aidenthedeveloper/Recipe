package com.example.recipe.Adapter;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipe.Fragment.PostDetailFragment;
import com.example.recipe.Model.Post;
import com.example.recipe.R;
import com.example.recipe.RecipeActivity;

import org.w3c.dom.Text;

import java.util.List;

public class MyPicAdapter extends RecyclerView.Adapter<MyPicAdapter.ViewHolder>{

        private Context mContext;
        private List<Post> mPosts;

        public MyPicAdapter(Context context, List<Post> posts){
            mContext = context;
            mPosts = posts;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.pic_item, viewGroup, false);
            return new MyPicAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyPicAdapter.ViewHolder holder, final int i) {


            final Post post = mPosts.get(i);

            Glide.with(mContext).load(post.getPostimage()).into(holder.post_image);

            holder.title.setText(post.getTitle());

            holder.post_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("postid", post.getPostid());
                    editor.apply();

                    Intent intent = new Intent(mContext, RecipeActivity.class);
                    mContext.startActivity(intent);
                }
            });

            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("postid", post.getPostid());
                    editor.apply();

                    Intent intent = new Intent(mContext, RecipeActivity.class);
                    mContext.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return mPosts.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView post_image;
            public TextView title;


            public ViewHolder(View itemView) {
                super(itemView);

                post_image = itemView.findViewById(R.id.post_image);
                title = itemView.findViewById(R.id.title);

            }
        }
    }
