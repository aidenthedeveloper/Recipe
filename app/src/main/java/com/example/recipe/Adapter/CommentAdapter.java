package com.example.recipe.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.recipe.HomeActivity;
import com.example.recipe.Model.Comment;
import com.example.recipe.Model.User;
import com.example.recipe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
private Context mContext;
private List<Comment> mComment;
private String postid,commentId;

private FirebaseUser firebaseUser;

public CommentAdapter(Context mContext, List<Comment> mComment,String postid) {
        this.mContext = mContext;
        this.mComment = mComment;
        this.postid = postid;
        }

@NonNull
@Override
public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item, viewGroup, false);
    return new CommentAdapter.ViewHolder(view);

}


    @Override
public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Comment comment = mComment.get(i);


        viewHolder.comment.setText(comment.getComment());
        getUserInfo(viewHolder.image_profile, viewHolder.username, comment.getPublisher());
        viewHolder.username.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!comment.getPublisher().equals(firebaseUser.getUid())){
        Intent intent = new Intent(mContext, HomeActivity.class);
        intent.putExtra("publisherid", comment.getPublisher());
        mContext.startActivity(intent);
        }}
        });

        viewHolder.image_profile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!comment.getPublisher().equals(firebaseUser.getUid())){
        Intent intent = new Intent(mContext, HomeActivity.class);
        intent.putExtra("publisherid", comment.getPublisher());
        mContext.startActivity(intent);
        }}
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
        if (comment.getPublisher().equals(firebaseUser.getUid())) {

        String userId = firebaseUser.getUid();
        //Toast.makeText(mContext.getApplicationContext(), comment.getCommentId(), Toast.LENGTH_SHORT).show();

        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("Do you want to delete?");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Comments")
                                .child(postid)
                                .child(comment.getCommentId())
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(mContext, "Deleted!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
    return true;
}
        });

    }

    @Override
public int getItemCount() {
        return mComment.size();
        }


public class ViewHolder extends RecyclerView.ViewHolder{

    public ImageView image_profile;
    public TextView username, comment;

    public ViewHolder(@NonNull View itemView){
        super(itemView);

        image_profile = itemView.findViewById(R.id.image_profile);
        username = itemView.findViewById(R.id.username);
        comment = itemView.findViewById(R.id.comment);
    }
}

    private void getUserInfo (final ImageView image_profile, final TextView username, String publisherid) {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference()
                .child("Users").child(publisherid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(mContext).load(user.getImageurl())
                        .apply(new RequestOptions().placeholder(R.drawable.ic_person_white))
                        .into(image_profile);
                username.setText(user.getUsername());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}