package com.example.recipegenius.AdapterClasses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.recipegenius.ChatActivity;
import com.example.recipegenius.PresentationClasses.UserRegistrationModel;
import com.example.recipegenius.R;


import java.util.List;

public class UserDisplayingAdapter extends RecyclerView.Adapter<UserDisplayingAdapter.UserDisplayHolder> {

    Context context;
    List<UserRegistrationModel>userList;

    public UserDisplayingAdapter(Context context, List<UserRegistrationModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserDisplayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userdisplayinglist, parent, false);
        return new UserDisplayHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDisplayHolder holder, int position) {
        UserRegistrationModel user = userList.get(position);
        holder.displayuser.setText(user.getName());
        Glide.with(context).load(user.getProfileImage()).into(holder.dispalyuserProfileImage);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("OTHER_USER_ID", user.getUserId()); // Assuming your User model has a getUserId() method
            intent.putExtra("USER_NAME", user.getName());
            intent.putExtra("USER_PROFILE_IMAGE", user.getProfileImage());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }



    public class UserDisplayHolder extends RecyclerView.ViewHolder {
        TextView displayuser;
        ImageView dispalyuserProfileImage;

        public UserDisplayHolder(@NonNull View itemView) {
            super(itemView);

            displayuser = itemView.findViewById(R.id.userdisplayinglist_username);
            dispalyuserProfileImage = itemView.findViewById(R.id.userdisplaying_userimage);
        }

        }

}
