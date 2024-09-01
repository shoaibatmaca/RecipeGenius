
package com.example.recipegenius.AdminUI;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recipegenius.Customer.customerProfile;
import com.example.recipegenius.PresentationClasses.UserProfile;
import com.example.recipegenius.PresentationClasses.UserRegistrationModel;
import com.example.recipegenius.R;
import com.example.recipegenius.UserDisplaying;
import com.example.recipegenius.UserProfileContent.UserNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminDashboard extends AppCompatActivity {
ImageView admin_profile_image,notification_icon;
    private CardView userManagement, adminchatting, adminNotification, payeemanagement;
    private boolean isCard1Selected = false, isCard2Selected = false, isCard3Selected = false, isCard4Selected = false;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference=storageReference = FirebaseStorage.getInstance().getReference();
    private FirebaseUser currentUser;
    private TextView profileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_dashboard);

        admin_profile_image=findViewById(R.id.admin_profile_image);

        userManagement = findViewById(R.id.admin_usermanage_card);
        adminchatting = findViewById(R.id.adminchatting);
        adminNotification=findViewById(R.id.adminNotification);
        admin_profile_image=findViewById(R.id.admin_profile_image);
        notification_icon=findViewById(R.id.notification_icon);

        notification_icon.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), UserNotification.class));
        });


        adminchatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UserDisplaying.class));
            }
        });

        // Set onClickListeners for each card
        userManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCard1Selected = !isCard1Selected;
                startActivity(new Intent(getApplicationContext(), UserManagementActivity.class));
            }
        });
        adminNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UserNotification.class));
            }
        });






        databaseReference = FirebaseDatabase.getInstance().getReference();
        StorageReference fileReference = storageReference.child("profileImages/");



        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userId);

            loadUserData();
        } else {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
        }



        admin_profile_image.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), customerProfile.class));
        });







    }



    private void loadUserData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserRegistrationModel user = dataSnapshot.getValue(UserRegistrationModel.class);
                    if (user != null) {

                        if (user.getProfileImage() != null && !user.getProfileImage().isEmpty()) {
                            Picasso.get().load(user.getProfileImage()).into(admin_profile_image);
                        }
                        if (user.getProfileImage() == null && user.getProfileImage().isEmpty()){
                            admin_profile_image.setImageResource(R.drawable.profilepic);
                        }

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
