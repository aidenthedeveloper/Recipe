package com.example.recipe.Adapter;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipe.Fragment.ProfileFragment;
import com.example.recipe.HomeActivity;
import com.example.recipe.MainActivity;
import com.example.recipe.Model.User;
import com.example.recipe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private boolean isFragment;

    private FirebaseUser firebaseUser;

    public UserAdapter(Context mContext,List<User> mUsers, boolean isFragment){
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.isFragment = isFragment;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, viewGroup, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final User user = mUsers.get(i);

        viewHolder.btn_follow.setVisibility(View.VISIBLE);

        viewHolder.username.setText(user.getUsername());
        viewHolder.fullname.setText(user.getFullName());
        Glide.with(mContext).load(user.getImageurl()).into(viewHolder.image_profile);
        isFollowing(user.getId(), viewHolder.btn_follow);

      //  if(user.getId().equals(firebaseUser.getUid())){
        if(firebaseUser.getUid().equals(user.getId())){
            viewHolder.btn_follow.setVisibility(View.GONE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFragment) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("profileid", user.getId());
                    editor.apply();

                    FragmentManager fm = ((FragmentActivity) mContext).getSupportFragmentManager();
                    Bundle arguments = new Bundle();
                    arguments.putInt("VALUE1", 2);

                    ProfileFragment profileFragment = new ProfileFragment();
                    profileFragment.setArguments(arguments);

                    fm.beginTransaction().replace(R.id.fragment_container, profileFragment).commit();

                } else {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("profileid", user.getId());
                    editor.apply();

                    FragmentManager fm = ((FragmentActivity) mContext).getSupportFragmentManager();
                    Bundle arguments = new Bundle();
                    arguments.putInt("VALUE1", 3);

                    ProfileFragment profileFragment = new ProfileFragment();
                    profileFragment.setArguments(arguments);

                    fm.beginTransaction().replace(R.id.fragment_container, profileFragment).commit();

                  //  Intent intent = new Intent(mContext, HomeActivity.class);
                 //   intent.putExtra("publiserid",user.getId());
                 //   mContext.startActivity(intent);
                }
            }
        });
        viewHolder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.btn_follow.getText().toString().equals("Follow")){
                    FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Follow").child(firebaseUser.getUid())
                            .child("Following").child(user.getId()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                            .child("Followers").child(firebaseUser.getUid()).setValue(true);

                    addNotification(user.getId());
                } else {
                    FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Follow").child(firebaseUser.getUid())
                            .child("Following").child(user.getId()).removeValue();
                    FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Follow").child(user.getId())
                            .child("Followers").child(firebaseUser.getUid()).removeValue();

                }
            }
        });
    }

    private void addNotification(String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Notifications").child(userid);

        String notifid = reference.push().getKey();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("txtComment", "started following you.");
        hashMap.put("postid", "");
        hashMap.put("ispost",false);
        hashMap.put("notifid",notifid);


        reference.child(notifid).setValue(hashMap);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public TextView fullname;
        public ImageView image_profile;
        public Button btn_follow;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            username = itemView.findViewById(R.id.username);
            fullname = itemView.findViewById(R.id.fullname);
            image_profile = itemView.findViewById(R.id.image_profile);
            btn_follow = itemView.findViewById(R.id.btn_follow);
        }
    }

    private void isFollowing(final String userid, final Button button){
        DatabaseReference reference = FirebaseDatabase.getInstance("https://recipe-20a97-default-rtdb.asia-southeast1.firebasedatabase.app").getReference()
                .child("Follow").child(firebaseUser.getUid()).child("Following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(userid).exists()){
                    button.setText("Following");
                } else {
                    button.setText("Follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}