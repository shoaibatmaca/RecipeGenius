package com.example.recipegenius.AdapterClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipegenius.PresentationClasses.Recipe;
import com.example.recipegenius.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdatper extends RecyclerView.Adapter<RecipeAdatper.RecipeViewHolder> {

    private Context context;
    private List<Recipe> recipeList;
    private OnRecipeDeleteListener onRecipeDeleteListener;

    public RecipeAdatper(Context context, List<Recipe> recipeList, OnRecipeDeleteListener onRecipeDeleteListener) {
        this.context = context;
        this.recipeList = recipeList;
        this.onRecipeDeleteListener = onRecipeDeleteListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.recipeNameTextView.setText(recipe.getName());
        holder.recipeType.setText(recipe.getType());
        Picasso.get()
                .load(recipe.getImageUrl())
                .placeholder(R.drawable.image) // Placeholder image
                .into(holder.recipe_image);


        holder.deleteButton.setOnClickListener(v -> {
            if (onRecipeDeleteListener != null) {
                onRecipeDeleteListener.onRecipeDelete(recipe.getId()); // Pass recipe ID to listener
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public interface OnRecipeDeleteListener {
        void onRecipeDelete(String recipeId);
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView recipeNameTextView, recipeType;
        Button deleteButton;
        private ImageView recipe_image;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeNameTextView = itemView.findViewById(R.id.recipe_name);
            recipeType=itemView.findViewById(R.id.recipe_type);
            deleteButton = itemView.findViewById(R.id.btn_delete);
            recipe_image=itemView.findViewById(R.id.recipe_image);
        }
    }
}
