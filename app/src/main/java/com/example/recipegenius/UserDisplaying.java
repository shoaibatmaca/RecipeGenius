package com.example.recipegenius;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipegenius.AdapterClasses.UserDisplayingAdapter;
import com.example.recipegenius.PresentationClasses.UserRegistrationModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserDisplaying extends AppCompatActivity {

    private RecyclerView userdisplayingrecycler;
    private UserDisplayingAdapter userDisplayingAdapter;
    private List<UserRegistrationModel> userList = new ArrayList<>();
    private List<UserRegistrationModel> filteredUserList = new ArrayList<>();
    private String currentUserId;
    private ImageView profileImage, backButton, chatSearch;
    private SearchView searchView;
    private TextView adminStatusText;

    private DatabaseReference databaseReference;
    private DatabaseReference blockedUsersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_displaying);

        initializeView();

        userdisplayingrecycler = findViewById(R.id.UserDisplayingr);
        userdisplayingrecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        userDisplayingAdapter = new UserDisplayingAdapter(this, filteredUserList);
        userdisplayingrecycler.setAdapter(userDisplayingAdapter);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        blockedUsersRef = FirebaseDatabase.getInstance().getReference("BlockedUsers").child(currentUserId);

        loadUserDisplayingContent();

        chatSearch.setOnClickListener(v -> {
            searchView.setVisibility(View.VISIBLE);
            searchView.setIconified(false);
        });

        backButton.setOnClickListener(view -> finish());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterUsers(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterUsers(newText);
                return false;
            }
        });

        loadUserData();
    }

    private void loadUserData() {
        databaseReference.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserRegistrationModel user = dataSnapshot.getValue(UserRegistrationModel.class);
                    if (user != null) {
                        if (user.getProfileImage() != null && !user.getProfileImage().isEmpty()) {
                            Picasso.get().load(user.getProfileImage()).into(profileImage);
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

    private void initializeView() {
        backButton = findViewById(R.id.backtouristDashboard);  // Adjust ID as needed
        chatSearch = findViewById(R.id.chatSearch);           // Adjust ID as needed
        profileImage = findViewById(R.id.userPic);            // Adjust ID as needed
        searchView = findViewById(R.id.searchView);           // Adjust ID as needed
        adminStatusText = findViewById(R.id.adminStatusText); // Adjust ID as needed
    }


    private void loadUserDisplayingContent() {
        blockedUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot blockedSnapshot) {
                final List<String> blockedUserIds = new ArrayList<>();
                for (DataSnapshot blockedSnapshot1 : blockedSnapshot.getChildren()) {
                    String blockedUserId = blockedSnapshot1.getKey();
                    if (blockedUserId != null) {
                        blockedUserIds.add(blockedUserId);
                    }
                }

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userList.clear();
                        filteredUserList.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            UserRegistrationModel user = snapshot1.getValue(UserRegistrationModel.class);
                            if (user != null) {
                                if (user.getUserId().equals(currentUserId)) {
                                    if (user.getUserType() != null && user.getUserType().equals("admin")) {
                                        adminStatusText.setText("You are an admin");
                                        adminStatusText.setVisibility(View.VISIBLE);
                                    } else {
                                        adminStatusText.setVisibility(View.GONE);
                                    }
                                } else if (!blockedUserIds.contains(user.getUserId())) {
                                    userList.add(user);
                                    filteredUserList.add(user);
                                }
                            }
                        }
                        userDisplayingAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Failed to load users", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to load blocked users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterUsers(String query) {
        filteredUserList.clear();
        for (UserRegistrationModel user : userList) {
            if (user.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredUserList.add(user);
            }
        }
        userDisplayingAdapter.notifyDataSetChanged();
    }
}
