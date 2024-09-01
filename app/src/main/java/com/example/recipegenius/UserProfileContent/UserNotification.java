package com.example.recipegenius.UserProfileContent;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipegenius.AdapterClasses.NotificationAdapter;
import com.example.recipegenius.PresentationClasses.Notification;
import com.example.recipegenius.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserNotification extends AppCompatActivity {

    private RecyclerView recyclerViewNotifications;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;
    private Map<String, Notification> latestNotificationsMap;
    private Map<String, Integer> notificationCountMap;
    private DatabaseReference notificationsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notification);

        recyclerViewNotifications = findViewById(R.id.recyclerViewNotifications);
        notificationList = new ArrayList<>();
        latestNotificationsMap = new HashMap<>();
        notificationCountMap = new HashMap<>();

        notificationAdapter = new NotificationAdapter(this, notificationList, notificationCountMap);
        recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotifications.setAdapter(notificationAdapter);

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        notificationsRef = FirebaseDatabase.getInstance().getReference("Notifications").child(currentUserId);

        loadNotifications();
    }

    private void loadNotifications() {
        notificationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notificationList.clear();
                latestNotificationsMap.clear();
                notificationCountMap.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Notification notification = dataSnapshot.getValue(Notification.class);
                    if (notification != null) {
                        String senderId = notification.getSenderId();

                        // Update the count
                        notificationCountMap.put(senderId, notificationCountMap.getOrDefault(senderId, 0) + 1);

                        // Update the latest message for the sender
                        latestNotificationsMap.put(senderId, notification);
                    }
                }

                // Add the latest notifications to the list
                notificationList.addAll(latestNotificationsMap.values());
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserNotification.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
