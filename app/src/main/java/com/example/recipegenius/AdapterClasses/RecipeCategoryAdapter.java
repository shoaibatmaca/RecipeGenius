package com.example.recipegenius.AdapterClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipegenius.PresentationClasses.Recipe;
import com.example.recipegenius.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//
//public class RecipeCategoryAdapter extends RecyclerView.Adapter<RecipeCategoryAdapter.customercategoryviewholder> {
//
//    List<Recipe> categorylist;
//    Context context;
//    private int selectedPosition = -1;// Variable to track the selected position
//    private Map<String, Integer> categoryImageMap;
//    public RecipeCategoryAdapter(List<Recipe> categorylist, Context context) {
//        this.categorylist = categorylist;
//        this.context = context;
////        "Burgers", "Fast Food", "Desserts", "Pakistani", "Biryani", "Bread", "Karahi & Handi", "American", "Middle Eastern", "Western
//        // Initialize the category to image resource map
//        categoryImageMap = new HashMap<>();
//        categoryImageMap.put("Burgers", R.drawable.b);
//        categoryImageMap.put("Fast Food", R.drawable.p);
//        categoryImageMap.put("Biryani", R.drawable.b);
//        categoryImageMap.put("Dessert", R.drawable.dessert);
//        categoryImageMap.put("Pakistan", R.drawable.af);
//        categoryImageMap.put("Karahi & Handi", R.drawable.karahi);
//        categoryImageMap.put("American", R.drawable.p);
//        categoryImageMap.put("Middle Eastern", R.drawable.af);
//        categoryImageMap.put("Western", R.drawable.west);
//
//
//    }
//
//    @NonNull
//    @Override
//    public RecipeCategoryAdapter.customercategoryviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_recipecategory_layout, parent, false);
//        return new customercategoryviewholder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecipeCategoryAdapter.customercategoryviewholder holder, int position) {
//        Recipe recipeCategory = categorylist.get(position);
//        holder.popularcategory_customer_recipecategory.setText(recipeCategory.getCuisine());
//
//        // Change background color based on the selected position
//        if (selectedPosition == position) {
//            holder.customer_recipecategory_cardview.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.purple_500));
//        } else {
//            holder.customer_recipecategory_cardview.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
//        }
//
//        holder.itemView.setOnClickListener(v -> {
//            // Update the selected position
//            int previousSelectedPosition = selectedPosition;
//            selectedPosition = position;
//
//            // Notify adapter to refresh the item views
//            notifyItemChanged(previousSelectedPosition);
//            notifyItemChanged(selectedPosition);
//        });
//
//        // Set the image based on the category
//        Integer imageResId = categoryImageMap.get(recipeCategory.getCuisine());
//        if (imageResId != null) {
//            holder.popularcategory_recipeImage.setImageResource(imageResId);}
////        } else {
////            holder.popularcategory_recipeImage.setImageResource(R.drawable.image); // Set a default image if category not found
////        }
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return categorylist.size();
//    }
//
//    public class customercategoryviewholder extends RecyclerView.ViewHolder {
//        LinearLayout c;
//        ImageView popularcategory_recipeImage;
//        TextView popularcategory_customer_recipecategory;
//        CardView customer_recipecategory_cardview;
//
//        public customercategoryviewholder(@NonNull View itemView) {
//            super(itemView);
//            c = itemView.findViewById(R.id.customer_recipecategory_layout);
//            popularcategory_recipeImage = itemView.findViewById(R.id.popularcategory_recipeImage);
//            popularcategory_customer_recipecategory = itemView.findViewById(R.id.popularcategory_customer_recipecategory);
//            customer_recipecategory_cardview = itemView.findViewById(R.id.customer_recipecategory_cardview);
//
//
//        }
//    }
//}



public class RecipeCategoryAdapter extends RecyclerView.Adapter<RecipeCategoryAdapter.customercategoryviewholder> {

    List<Recipe> categorylist;
    Context context;
    private int selectedPosition = -1;
    private Map<String, Integer> categoryImageMap;
    private OnCategoryClickListener onCategoryClickListener;

    public RecipeCategoryAdapter(List<Recipe> categorylist, Context context, OnCategoryClickListener onCategoryClickListener) {
        this.categorylist = categorylist;
        this.context = context;
        this.onCategoryClickListener = onCategoryClickListener;

        // Initialize the category to image resource map
        categoryImageMap = new HashMap<>();
        categoryImageMap.put("Burgers", R.drawable.b);
        categoryImageMap.put("Fast Food", R.drawable.p);
        categoryImageMap.put("Biryani", R.drawable.b);
        categoryImageMap.put("Dessert", R.drawable.dessert);
        categoryImageMap.put("Pakistan", R.drawable.af);
        categoryImageMap.put("Karahi & Handi", R.drawable.karahi);
        categoryImageMap.put("American", R.drawable.p);
        categoryImageMap.put("Middle Eastern", R.drawable.af);
        categoryImageMap.put("Western", R.drawable.west);
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(String category);
    }

    @NonNull
    @Override
    public customercategoryviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_recipecategory_layout, parent, false);
        return new customercategoryviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull customercategoryviewholder holder, int position) {
        Recipe recipeCategory = categorylist.get(position);
        holder.popularcategory_customer_recipecategory.setText(recipeCategory.getCuisine());

        // Change background color based on the selected position
        if (selectedPosition == position) {
            holder.customer_recipecategory_cardview.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.purple_500));
        } else {
            holder.customer_recipecategory_cardview.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
        }

        holder.itemView.setOnClickListener(v -> {
            // Update the selected position
            int previousSelectedPosition = selectedPosition;
            selectedPosition = position;

            // Notify adapter to refresh the item views
            notifyItemChanged(previousSelectedPosition);
            notifyItemChanged(selectedPosition);

            // Notify the listener about the category selection
            if (onCategoryClickListener != null) {
                onCategoryClickListener.onCategoryClick(recipeCategory.getCuisine());
            }
        });

        // Set the image based on the category
        Integer imageResId = categoryImageMap.get(recipeCategory.getCuisine());
        if (imageResId != null) {
            holder.popularcategory_recipeImage.setImageResource(imageResId);
        }
    }

    @Override
    public int getItemCount() {
        return categorylist.size();
    }

    public class customercategoryviewholder extends RecyclerView.ViewHolder {
        LinearLayout c;
        ImageView popularcategory_recipeImage;
        TextView popularcategory_customer_recipecategory;
        CardView customer_recipecategory_cardview;

        public customercategoryviewholder(@NonNull View itemView) {
            super(itemView);
            c = itemView.findViewById(R.id.customer_recipecategory_layout);
            popularcategory_recipeImage = itemView.findViewById(R.id.popularcategory_recipeImage);
            popularcategory_customer_recipecategory = itemView.findViewById(R.id.popularcategory_customer_recipecategory);
            customer_recipecategory_cardview = itemView.findViewById(R.id.customer_recipecategory_cardview);
        }
    }
}

