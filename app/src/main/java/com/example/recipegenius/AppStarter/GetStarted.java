package com.example.recipegenius.AppStarter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipegenius.AdminUI.AdminDashboard;
import com.example.recipegenius.ChefUI.ChefDashboard;
import com.example.recipegenius.Customer.CustomerDashboard;
import com.example.recipegenius.LoginPage;
import com.example.recipegenius.PresentationClasses.UserRegistrationModel;
import com.example.recipegenius.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GetStarted extends AppCompatActivity {

    private TextView prmo1_skip;
    private Button prmo1_continue;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        prmo1_skip = findViewById(R.id.prmo1_skip);
        prmo1_continue = findViewById(R.id.prmo1_continue);
        firebaseAuth = FirebaseAuth.getInstance();

        prmo1_continue.setOnClickListener(view -> CheckUserCredentials());

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToDashboard(currentUser.getUid());
        }

        prmo1_skip.setOnClickListener(view -> {
            startActivity(new Intent(GetStarted.this, LoginPage.class));
            finish();
        });
    }

    private void CheckUserCredentials() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToDashboard(currentUser.getUid());
        } else {
            startActivity(new Intent(GetStarted.this, LoginPage.class));
            finish();
        }
    }

    private void navigateToDashboard(String userId) {
        userRef = FirebaseDatabase.getInstance().getReference("User").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserRegistrationModel user = dataSnapshot.getValue(UserRegistrationModel.class);
                    if (user != null && user.getUserType() != null) {
                        String userType = user.getUserType();
                        if (userType.equals("Admin")) {
                            startActivity(new Intent(GetStarted.this, AdminDashboard.class));
                        } else if (userType.equals("Chef")) {
                            startActivity(new Intent(GetStarted.this, ChefDashboard.class));
                        } else if (userType.equals("Customer")) {
                            startActivity(new Intent(GetStarted.this, CustomerDashboard.class));
                        } else {
                            Toast.makeText(GetStarted.this, "Unknown user type", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(GetStarted.this, LoginPage.class));
                        }
                    } else {
                        Toast.makeText(GetStarted.this, "User type not found", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(GetStarted.this, LoginPage.class));
                    }
                } else {
                    startActivity(new Intent(GetStarted.this, LoginPage.class));
                }
                finish(); // Ensure the user cannot navigate back to the GetStarted activity.
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(GetStarted.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(GetStarted.this, LoginPage.class));
                finish();
            }
        });
    }
}
