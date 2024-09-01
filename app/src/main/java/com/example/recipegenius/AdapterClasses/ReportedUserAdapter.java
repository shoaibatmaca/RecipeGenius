package com.example.recipegenius.AdapterClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipegenius.PresentationClasses.Report;
import com.example.recipegenius.PresentationClasses.UserRegistrationModel;
import com.example.recipegenius.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportedUserAdapter extends RecyclerView.Adapter<ReportedUserAdapter.ReportedUserViewHolder> {

    private Context context;
    private List<Report> reportedUserList;
    private List<UserRegistrationModel> userModelList; // List for user details
    private OnUserActionListener onUserActionListener;

    // Constructor accepting both reports and user models
    public ReportedUserAdapter(Context context, List<Report> reportedUserList, List<UserRegistrationModel> userModelList, OnUserActionListener listener) {
        this.context = context;
        this.reportedUserList = reportedUserList;
        this.userModelList = userModelList;
        this.onUserActionListener = listener;
    }

    @NonNull
    @Override
    public ReportedUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reported_user, parent, false);
        return new ReportedUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportedUserViewHolder holder, int position) {
        Report report = reportedUserList.get(position);

        // Check if the userModelList has a corresponding user model for the report
        if (position < userModelList.size()) {
            UserRegistrationModel userModel = userModelList.get(position);

            // Set the reported user's name and profile image from UserRegistrationModel
            holder.reportedUserName.setText(userModel.getName());
            Glide.with(context).load(userModel.getProfileImage()).into(holder.reportedUserImage);
        } else {
            // Fallback in case user details are not available
            holder.reportedUserName.setText("User details not available");
            holder.reportedUserImage.setImageResource(R.drawable.users);  // A placeholder image
        }

        // Set the reason for the report and the timestamp from the Report model
        holder.reportReason.setText("Reason: " + report.getReportReason());

        // Convert the report timestamp into a readable format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
        String formattedDate = dateFormat.format(new Date(report.getReportTimestamp()));
        holder.reportTimestamp.setText("Reported on: " + formattedDate);

        // Set up block user button
        holder.btnBlockUser.setOnClickListener(v -> {
            if (onUserActionListener != null && report.getReportedUserId() != null) {
                onUserActionListener.onBlockUser(report.getReportedUserId());
            } else {
                Toast.makeText(context, "User ID is null", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up delete user button
        holder.btnDeleteUser.setOnClickListener(v -> {
            if (onUserActionListener != null && report.getReportedUserId() != null) {
                onUserActionListener.onDeleteUser(report.getReportedUserId());  // Pass the reportedUserId
            } else {
                Toast.makeText(context, "User ID is null", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return reportedUserList.size();  // Return the size of the reported user list
    }

    public static class ReportedUserViewHolder extends RecyclerView.ViewHolder {
        ImageView reportedUserImage;
        TextView reportedUserName, reportReason, reportTimestamp;
        Button btnBlockUser, btnDeleteUser;

        public ReportedUserViewHolder(@NonNull View itemView) {
            super(itemView);
            reportedUserImage = itemView.findViewById(R.id.reportedUserImage);
            reportedUserName = itemView.findViewById(R.id.reportedUserName);
            reportReason = itemView.findViewById(R.id.reportReason);
            reportTimestamp = itemView.findViewById(R.id.reportTimestamp);
            btnBlockUser = itemView.findViewById(R.id.btnBlockUser);
            btnDeleteUser = itemView.findViewById(R.id.btnDeleteUser);
        }
    }

    // Interface for handling user actions like block or delete
    public interface OnUserActionListener {
        void onBlockUser(String reportedUserId);
        void onDeleteUser(String reportedUserId);
    }
}
