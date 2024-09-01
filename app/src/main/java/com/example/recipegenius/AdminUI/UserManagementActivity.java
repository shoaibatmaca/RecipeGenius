package com.example.recipegenius.AdminUI;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipegenius.AdapterClasses.ReportedUserAdapter;
import com.example.recipegenius.PresentationClasses.Report;
import com.example.recipegenius.PresentationClasses.UserRegistrationModel;
import com.example.recipegenius.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserManagementActivity extends AppCompatActivity implements ReportedUserAdapter.OnUserActionListener {

    private RecyclerView recyclerViewReportedUsers;
    private ReportedUserAdapter reportedUserAdapter;
    private List<Report> reportedUserList = new ArrayList<>();
    private List<UserRegistrationModel> userModelList = new ArrayList<>(); // List for user models
    private DatabaseReference reportsRef;
    private DatabaseReference usersRef;  // Reference to the Users node

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        recyclerViewReportedUsers = findViewById(R.id.recyclerViewReportedUsers);
        recyclerViewReportedUsers.setLayoutManager(new LinearLayoutManager(this));

        reportedUserAdapter = new ReportedUserAdapter(this, reportedUserList, userModelList, this);  // Initialize with both lists
        recyclerViewReportedUsers.setAdapter(reportedUserAdapter);

        reportsRef = FirebaseDatabase.getInstance().getReference("Reports");
        usersRef = FirebaseDatabase.getInstance().getReference("User");  // Reference to Users

        loadReportedUsers();
    }

    private void loadReportedUsers() {
        reportsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reportedUserList.clear();
                userModelList.clear();  // Clear the user list when data is reloaded

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Report report = dataSnapshot.getValue(Report.class);
                    if (report != null && report.getReportedUserId() != null) {
                        reportedUserList.add(report);
                        loadUserDetails(report.getReportedUserId());  // Load user details only if userId is not null
                    } else {
                        // Handle null userId case (optional, log it or show a warning)
                        Toast.makeText(UserManagementActivity.this, "Invalid report with null user ID", Toast.LENGTH_SHORT).show();
                    }
                }
                reportedUserAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to load reported users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserDetails(String userId) {
        if (userId == null || userId.isEmpty()) {
            // Handle invalid userId case (optional, log it or show a warning)
            Toast.makeText(this, "User ID is null or empty", Toast.LENGTH_SHORT).show();
            return;
        }

        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserRegistrationModel userModel = snapshot.getValue(UserRegistrationModel.class);
                if (userModel != null) {
                    userModelList.add(userModel);
                    reportedUserAdapter.notifyDataSetChanged();  // Notify adapter after loading user details
                } else {
                    Toast.makeText(getApplicationContext(), "User details not found for ID: " + userId, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to load user details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBlockUser(String reportedUserId) {

    }

    // Implement the OnUserActionListener methods

    @Override
    public void onDeleteUser(String reportedUserId) {
        if (reportedUserId == null) {
            Toast.makeText(this, "Reported user ID is null", Toast.LENGTH_SHORT).show();
            return;  // Exit early if reportedUserId is null
        }

        // Proceed with deletion if ID is not null
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User").child(reportedUserId);
        userRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                    // Also remove the report entry
                    reportsRef.child(reportedUserId).removeValue()
                            .addOnSuccessListener(aVoid1 ->
                                    Toast.makeText(this, "Report deleted successfully", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "Failed to delete report", Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to delete user", Toast.LENGTH_SHORT).show());
    }

}
