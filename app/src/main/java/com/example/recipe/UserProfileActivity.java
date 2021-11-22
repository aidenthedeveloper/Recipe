package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import com.example.recipe.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

public class UserProfileActivity extends AppCompatActivity {

    TextView tv_FName,tv_UName,tv_Phone;

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private Uri mImageUri;
    private StorageTask uploadTask;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        tv_FName = findViewById(R.id.tvFName);
        tv_UName = findViewById(R.id.tvUName);
        tv_Phone = findViewById(R.id.tvPhoneNumber);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getPhoneNumber());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                tv_FName.setText(user.getFullName());
                tv_UName.setText(user.getUsername());
                tv_Phone.setText(user.getPhoneNo());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
   /* private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    } */
}