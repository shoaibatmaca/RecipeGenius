package com.example.recipegenius.AdapterClasses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipegenius.Customer.PaymentActivity;
import com.example.recipegenius.Customer.customerfulldetailrecipe;
import com.example.recipegenius.PresentationClasses.Recipe;
import com.example.recipegenius.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShowRecipeAdapter extends RecyclerView.Adapter<ShowRecipeAdapter.ViewHolder> {

    private List<Recipe> showRecipeList;
    private Context context;

    public ShowRecipeAdapter(List<Recipe> showRecipeList, Context context) {
        this.showRecipeList = showRecipeList;
        this.context = context;
    }

    public void updateRecipes(List<Recipe> newRecipeList) {
        this.showRecipeList = newRecipeList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_popular_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = showRecipeList.get(position);
        holder.recipeCustomerRecipeName.setText(recipe.getName());
        holder.recipeCustomerRecipeDesc.setText(recipe.getDescription());
        Picasso.get()
                .load(recipe.getImageUrl())
                .placeholder(R.drawable.image) // Placeholder image
                .into(holder.recipeCustomerRecipeImage);

        holder.itemView.setOnClickListener(v -> {
            if (recipe.getType().equals("Premium")) {
                showPaymentBottomSheet(holder.itemView, recipe.getId(), recipe.getName(), recipe.getImageUrl());
            } else {
                // Open details activity
                Intent intent = new Intent(context, customerfulldetailrecipe.class);
                intent.putExtra("name", recipe.getName());
                intent.putExtra("type", recipe.getType());
                intent.putExtra("cuisine", recipe.getCuisine());
                intent.putExtra("ingredient", recipe.getIngredients());
                intent.putExtra("method", recipe.getMethod());
                intent.putExtra("description", recipe.getDescription());
                intent.putExtra("imageUrl", recipe.getImageUrl()); // Pass image URL
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return showRecipeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout recipeCustomerLayout;
        ImageView recipeCustomerRecipeImage;
        TextView recipeCustomerRecipeName, recipeCustomerRecipeDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recipeCustomerLayout = itemView.findViewById(R.id.popularrecipe_customer_layout);
            recipeCustomerRecipeImage = itemView.findViewById(R.id.popularrecipe_customer_recipeimage);
            recipeCustomerRecipeName = itemView.findViewById(R.id.popularrecipe_customer_recipeName);
            recipeCustomerRecipeDesc = itemView.findViewById(R.id.popularrecipe_customer_recipeDesc);
        }
    }

    private void showPaymentBottomSheet(View view, String recipeId, String recipeName, String recipeImageUrl) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext());
        View bottomSheetView = LayoutInflater.from(view.getContext()).inflate(R.layout.recipe_premium_bottomsheet, null);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        Button paymentButton = bottomSheetView.findViewById(R.id.payment_button);
        paymentButton.setOnClickListener(v -> {
            // Dismiss the BottomSheetDialog
            bottomSheetDialog.dismiss();

            // Create an Intent to start PaymentActivity
            Intent intent = new Intent(view.getContext(), PaymentActivity.class);
            intent.putExtra("RECIPE_ID", recipeId);
            intent.putExtra("RECIPE_NAME", recipeName);
            intent.putExtra("RECIPE_IMAGE_URL", recipeImageUrl);
            view.getContext().startActivity(intent);
        });
    }
}
