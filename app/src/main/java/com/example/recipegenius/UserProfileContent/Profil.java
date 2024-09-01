package com.example.recipegenius.UserProfileContent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.recipegenius.PresentationClasses.UserRegistrationModel;
import com.example.recipegenius.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Profil extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView profileImageView;
    private TextView nameTextView, emailTextView;
    private EditText editDob, editAge, editGender, editAddress, editPhone;
    private Button upload; // Renamed to updateButton for clarity
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private String currentUserType; // Variable to store the current user type

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        // Initialize views
        profileImageView = findViewById(R.id.profile_image);
        nameTextView = findViewById(R.id.text_name);
        emailTextView = findViewById(R.id.text_email);
        editDob = findViewById(R.id.edit_dob);
        editAge = findViewById(R.id.edit_age);
        editGender = findViewById(R.id.edit_gender);
        editAddress = findViewById(R.id.edit_address);
        editPhone = findViewById(R.id.edit_phone);
        upload = findViewById(R.id.button_upload);


        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        fetchUserData();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfileWithImage();
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });



    }




    private void fetchUserData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserRegistrationModel user = dataSnapshot.getValue(UserRegistrationModel.class);
                    if (user != null) {
                        nameTextView.setText(user.getName());
                        emailTextView.setText(user.getEmail());

                    }

                    // Save userType for use in updateProfileWithImage
                    currentUserType = user.getUserType();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Toast.makeText(Profil.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
        }
    }


    private void updateProfileWithImage() {
        String dob = editDob.getText().toString().trim();
        String age = editAge.getText().toString().trim();
        String gender = editGender.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();

        // Check if required fields are filled
        if (TextUtils.isEmpty(dob) || TextUtils.isEmpty(age) || TextUtils.isEmpty(gender) ||
                TextUtils.isEmpty(address) || TextUtils.isEmpty(phone) || imageUri == null) {
            Toast.makeText(Profil.this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Upload image to Firebase Storage
        StorageReference fileReference = storageReference.child("profileImages/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            String profileImageUrl = uri.toString();
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


                            // Create a User object to update
                            UserRegistrationModel updatedUser = new UserRegistrationModel(

                                    userId,
                                    nameTextView.getText().toString(),
                                    emailTextView.getText().toString(),
                                    currentUserType, // Use the retrieved userType
                                    profileImageUrl,
                                    dob,
                                    age,
                                    gender,
                                    address,
                                    phone

                            );

                            // Update user profile in Firebase Realtime Database
                            databaseReference.setValue(updatedUser)
                                    .addOnSuccessListener(aVoid -> Toast.makeText(Profil.this, "Profile updated", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(Profil.this, "Failed to update profile", Toast.LENGTH_SHORT).show());
                        }))
                .addOnFailureListener(e -> Toast.makeText(Profil.this, "Failed to upload image", Toast.LENGTH_SHORT).show());
    }
}