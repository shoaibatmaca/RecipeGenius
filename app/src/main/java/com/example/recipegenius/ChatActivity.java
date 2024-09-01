package com.example.recipegenius;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipegenius.AdapterClasses.MessageAdapter;
import com.example.recipegenius.PresentationClasses.Message;
import com.example.recipegenius.PresentationClasses.Notification;
import com.example.recipegenius.PresentationClasses.Report;
import com.example.recipegenius.PresentationClasses.UserRegistrationModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMessages;
    private EditText messageEditText;
    private ImageView sendButton, backArrow, menuIcon;
    private CircleImageView chatUserImage;
    private TextView chatUserName;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;

    private DatabaseReference messagesRef;
    private String chatId;
    private String currentUserId;
    private String otherUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_chat);

        // Initialize UI components
        initializeUIComponents();

        // Set up Firebase references
        setupFirebase();

        // Load user data
        loadUserData();

        // Set up RecyclerView and Adapter
        setupRecyclerView();

        // Create notification channel
        createNotificationChannel();

        // Load messages from Firebase
        loadMessages();

        // Set up click listeners
        setupClickListeners();
    }

    private void initializeUIComponents() {
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        backArrow = findViewById(R.id.back_arrow);
        menuIcon = findViewById(R.id.menu_icon);
        chatUserImage = findViewById(R.id.chat_user_image);
        chatUserName = findViewById(R.id.chat_user_name);
    }

    private void setupFirebase() {
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        otherUserId = getIntent().getStringExtra("OTHER_USER_ID");
        chatId = getChatId(currentUserId, otherUserId);
        messagesRef = FirebaseDatabase.getInstance().getReference("Chats").child(chatId);
    }

    private void setupRecyclerView() {
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, currentUserId);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMessages.setAdapter(messageAdapter);
    }

    private void setupClickListeners() {
        sendButton.setOnClickListener(v -> sendMessage());
        backArrow.setOnClickListener(v -> finish());
        menuIcon.setOnClickListener(v -> showPopupMenu(v));
    }

    private void loadMessages() {
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    if (message != null) {
                        messageList.add(message);
                    }
                }
                messageAdapter.notifyDataSetChanged();
                recyclerViewMessages.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, "Failed to load messages", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();
        if (!messageText.isEmpty()) {
            String messageId = messagesRef.push().getKey();
            Message message = new Message(messageId, currentUserId, otherUserId, messageText);

            messagesRef.child(messageId).setValue(message)
                    .addOnSuccessListener(aVoid -> {
                        messageEditText.setText("");
                        storeNotificationInDatabase(messageText);
                        sendNotificationToUser(messageText);
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(ChatActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show());
        }
    }

    private void storeNotificationInDatabase(String messageText) {
        DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference("Notifications").child(otherUserId);
        String notificationId = notificationsRef.push().getKey();
        Notification notification = new Notification(notificationId, currentUserId, otherUserId, messageText);

        notificationsRef.child(notificationId).setValue(notification)
                .addOnFailureListener(e ->
                        Toast.makeText(ChatActivity.this, "Failed to store notification", Toast.LENGTH_SHORT).show());
    }

    private String getChatId(String userId1, String userId2) {
        return userId1.compareTo(userId2) > 0 ? userId1 + "_" + userId2 : userId2 + "_" + userId1;
    }

    private void sendNotificationToUser(String messageText) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("OTHER_USER_ID", otherUserId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User").child(currentUserId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currentUserName = snapshot.child("name").getValue(String.class);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ChatActivity.this, "chat_notifications")
                        .setSmallIcon(R.drawable.bell)
                        .setContentTitle("New Message from " + currentUserName)
                        .setContentText(messageText)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ChatActivity.this);
                if (ActivityCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
                    return;
                }
                notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, "Failed to send notification", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Chat Notifications";
            String description = "Notifications for new chat messages";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("chat_notifications", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void loadUserData() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User").child(otherUserId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserRegistrationModel user = dataSnapshot.getValue(UserRegistrationModel.class);
                    if (user != null) {
                        chatUserName.setText(user.getName());
                        Glide.with(ChatActivity.this).load(user.getProfileImage()).into(chatUserImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.chat_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this::onPopupMenuItemClick);
        popupMenu.show();
    }


    private boolean onPopupMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.clear_all_chat) {
                clearChatHistory();
                return true;
            } else if (item.getItemId() == R.id.block_user) {
                blockUser();
                return true;
            } else if (item.getItemId() == R.id.report_user) {

                showReportDialog();
                return true;

            }
            return false;
        }




    private void showReportDialog() {
        // Inflate the report dialog layout
        View view = getLayoutInflater().inflate(R.layout.report_dialog, null);
        EditText editTextReportReason = view.findViewById(R.id.editTextReportReason);

        // Create a dialog to display the report reason input field
        new AlertDialog.Builder(this)
                .setTitle("Report User")
                .setView(view)
                .setPositiveButton("Submit", (dialog, which) -> {
                    // Get the report reason entered by the user
                    String reportReason = editTextReportReason.getText().toString().trim();

                    // Check if reason is not empty
                    if (!reportReason.isEmpty()) {
                        // Call the reportUser method with the reason
                        reportUser(reportReason);
                    } else {
                        Toast.makeText(ChatActivity.this, "Please provide a reason", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void clearChatHistory() {
        messagesRef.removeValue().addOnSuccessListener(aVoid ->
                        Toast.makeText(ChatActivity.this, "Chat history cleared", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(ChatActivity.this, "Failed to clear chat history", Toast.LENGTH_SHORT).show());
    }

    private void blockUser() {
        DatabaseReference blockedUsersRef = FirebaseDatabase.getInstance().getReference("BlockedUsers").child(currentUserId);
        String blockedUserId = otherUserId;

        blockedUsersRef.child(blockedUserId).setValue(true)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ChatActivity.this, "User blocked successfully", Toast.LENGTH_SHORT).show();
                    // Close the ChatActivity
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(ChatActivity.this, "Failed to block user", Toast.LENGTH_SHORT).show());
    }



//    private void reportUser() {
//        DatabaseReference reportsRef = FirebaseDatabase.getInstance().getReference("Reports").child(currentUserId);
//        String reportId = reportsRef.push().getKey();
//
//        DatabaseReference reportedUserRef = FirebaseDatabase.getInstance().getReference("User").child(otherUserId);
//        reportedUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    UserRegistrationModel reportedUser = snapshot.getValue(UserRegistrationModel.class);
//                    if (reportedUser != null) {
//                        Report report = new Report(reportId, otherUserId, reportedUser.getName(), reportedUser.getProfileImage(), currentUserId);
//
//                        reportsRef.child(reportId).setValue(report)
//                                .addOnSuccessListener(aVoid ->
//                                        Toast.makeText(ChatActivity.this, "User reported successfully", Toast.LENGTH_SHORT).show())
//                                .addOnFailureListener(e ->
//                                        Toast.makeText(ChatActivity.this, "Failed to report user", Toast.LENGTH_SHORT).show());
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(ChatActivity.this, "Failed to report user", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
private void reportUser(String reportReason) {
    // Ensure that currentUserId and otherUserId are not null
    if (currentUserId == null || otherUserId == null) {
        Toast.makeText(ChatActivity.this, "User information is missing", Toast.LENGTH_SHORT).show();
        return;
    }

    // Get reference to Reports node in Firebase
    DatabaseReference reportsRef = FirebaseDatabase.getInstance().getReference("Reports").child(currentUserId);
    String reportId = reportsRef.push().getKey();

    // Ensure the reportId is generated correctly
    if (reportId == null) {
        Toast.makeText(ChatActivity.this, "Failed to generate report ID", Toast.LENGTH_SHORT).show();
        return;
    }

    // Get reference to the reported user
    DatabaseReference reportedUserRef = FirebaseDatabase.getInstance().getReference("User").child(otherUserId);
    reportedUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                UserRegistrationModel reportedUser = snapshot.getValue(UserRegistrationModel.class);
                if (reportedUser != null) {
                    // Get the current timestamp
                    long currentTime = System.currentTimeMillis();

                    // Create the report object
                    Report report = new Report(
                            reportId,
                            otherUserId,
                            reportedUser.getName(),
                            reportedUser.getProfileImage(),
                            currentUserId,
                            reportReason,
                            currentTime
                    );

                    // Save the report to Firebase
                    reportsRef.child(reportId).setValue(report)
                            .addOnSuccessListener(aVoid ->
                                    Toast.makeText(ChatActivity.this, "User reported successfully", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e ->
                                    Toast.makeText(ChatActivity.this, "Failed to report user", Toast.LENGTH_SHORT).show());
                } else {
                    Toast.makeText(ChatActivity.this, "Reported user not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ChatActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(ChatActivity.this, "Failed to report user", Toast.LENGTH_SHORT).show();
        }
    });
}





}
