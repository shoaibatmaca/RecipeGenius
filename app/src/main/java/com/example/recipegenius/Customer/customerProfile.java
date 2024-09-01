package com.example.recipegenius.Customer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipegenius.PresentationClasses.UserProfile;
import com.example.recipegenius.PresentationClasses.UserRegistrationModel;
import com.example.recipegenius.R;

import com.example.recipegenius.UserDisplaying;
import com.example.recipegenius.UserProfileContent.AboutApp;
import com.example.recipegenius.UserProfileContent.LogoutBottomSheetFragment;
import com.example.recipegenius.UserProfileContent.Profil;
import com.example.recipegenius.UserProfileContent.UserNotification;
import com.example.recipegenius.UserProfileContent.UserSettingpage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class customerProfile extends AppCompatActivity {


    private ImageView profileImage, profilechevron;
    private TextView profileName, settings, my_profile, profileEmail, about_app, notifications, my_profile_messages;

    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    Button logoutTextView;
    private StorageReference storageReference = storageReference = FirebaseStorage.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_customer_profile);

        profileImage = findViewById(R.id.profileImage);
        profileName = findViewById(R.id.profile_name);
        profileEmail = findViewById(R.id.profile_email);
        about_app = findViewById(R.id.about_app);
        settings = findViewById(R.id.settings);
        logoutTextView = findViewById(R.id.logout);
        my_profile = findViewById(R.id.my_profile);
        profileImage = findViewById(R.id.profileImage);
        profilechevron = findViewById(R.id.profilechevron);
        my_profile_messages = findViewById(R.id.my_profile_messages);
        notifications = findViewById(R.id.notifications);

        StorageReference fileReference = storageReference.child("profileImages/");

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        notifications.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), UserNotification.class));
        });


        my_profile_messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UserDisplaying.class));
            }
        });
        profilechevron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        about_app.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), AboutApp.class));
        });


        logoutTextView.setOnClickListener(v -> {
            LogoutBottomSheetFragment bottomSheet = new LogoutBottomSheetFragment();
            bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
        });

        settings.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), UserSettingpage.class));

        });


        my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Profil.class));

            }
        });


        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userId);

            loadUserData();
        } else {
            Toast.makeText(getApplicationContext(), "No user logged in", Toast.LENGTH_SHORT).show();
        }


    }

    private void loadUserData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserRegistrationModel user = dataSnapshot.getValue(UserRegistrationModel.class);
                    if (user != null) {
                        profileName.setText(user.getName());
                        profileEmail.setText(user.getEmail());

                        if (user.getProfileImage() != null && !user.getProfileImage().isEmpty()) {
                            Picasso.get().load(user.getProfileImage()).into(profileImage);
                        }

                    }
                } else {
                    Toast.makeText(customerProfile.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    }
