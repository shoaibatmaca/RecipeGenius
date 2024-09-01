package com.example.recipegenius.AdapterClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.recipegenius.PresentationClasses.Notification;
import com.example.recipegenius.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notificationList;
    private Map<String, Integer> notificationCountMap;
    private Context context;
    private DatabaseReference notificationsRef;

    public NotificationAdapter(Context context, List<Notification> notificationList, Map<String, Integer> notificationCountMap) {
        this.context = context;
        this.notificationList = notificationList;
        this.notificationCountMap = notificationCountMap;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifications, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        // Fetch user details (name and profile image) from Firebase
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User").child(notification.getSenderId());
        userRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String userName = snapshot.child("name").getValue(String.class);
                    String profileImageUrl = snapshot.child("profileImage").getValue(String.class);

                    holder.senderTextView.setText(userName);

                    // Load the profile image
                    Glide.with(holder.itemView.getContext())
                            .load(profileImageUrl)
                            .placeholder(R.drawable.user)
                            .into(holder.userImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error) {
                // Handle error
            }
        });

        holder.messageTextView.setText(notification.getMessage());

        // Set notification count
        Integer notificationCount = notificationCountMap.get(notification.getSenderId());
        if (notificationCount != null && notificationCount > 1) {
            holder.notificationCountTextView.setText(String.valueOf(notificationCount));
            holder.notificationCountTextView.setVisibility(View.VISIBLE);
        } else {
            holder.notificationCountTextView.setVisibility(View.GONE);
        }

        // Handle delete button click
        holder.deleteButton.setOnClickListener(v -> {
            deleteNotification(position, notification.getNotificationId());
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImageView;
        TextView senderTextView, messageTextView, notificationCountTextView;
        ImageView deleteButton;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.userImageView);
            senderTextView = itemView.findViewById(R.id.senderTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            notificationCountTextView = itemView.findViewById(R.id.notificationCountTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    private void deleteNotification(int position, String notificationId) {
        // Remove the notification from the list
        notificationList.remove(position);
        notifyItemRemoved(position);

        // Remove the notification from the database
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        notificationsRef = FirebaseDatabase.getInstance().getReference("Notifications").child(currentUserId).child(notificationId);
        notificationsRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Notification deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to delete notification", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
