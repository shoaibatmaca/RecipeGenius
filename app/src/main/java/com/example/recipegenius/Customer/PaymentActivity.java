package com.example.recipegenius.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipegenius.R;
import com.squareup.picasso.Picasso;

public class PaymentActivity extends AppCompatActivity {

    private String recipeId;
    private String recipeName;
    private String recipeImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Retrieve data from the intent
        recipeId = getIntent().getStringExtra("RECIPE_ID");
        recipeName = getIntent().getStringExtra("RECIPE_NAME");
        recipeImageUrl = getIntent().getStringExtra("RECIPE_IMAGE_URL");

        ImageView recipeImage = findViewById(R.id.precipe_image);
        TextView recipeNameTextView = findViewById(R.id.precipe_name);

// Load the recipe image using Picasso or Glide
        Picasso.get().load(recipeImageUrl).into(recipeImage);

// Set the recipe name
        recipeNameTextView.setText(recipeName);

        Button addToCartButton = findViewById(R.id.add_to_cart_button);
        addToCartButton.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentActivity.this, PurchaseRecipeActivity.class);
            intent.putExtra("RECIPE_ID", recipeId);
            startActivity(intent);
        });
    }
}
