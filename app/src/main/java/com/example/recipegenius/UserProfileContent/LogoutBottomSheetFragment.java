package com.example.recipegenius.UserProfileContent;// LogoutBottomSheetFragment.java
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.recipegenius.LoginPage;
import com.example.recipegenius.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;

public class LogoutBottomSheetFragment extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_logout_confirm, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View confirmButton = view.findViewById(R.id.confirm_logout);
        View cancelButton = view.findViewById(R.id.cancel_logout);

        confirmButton.setOnClickListener(v -> {
            // Handle logout action here
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginPage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();  // Close the current activity
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());
    }
}
