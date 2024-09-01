package com.example.recipegenius.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipegenius.R;
import com.squareup.picasso.Picasso;

public class customerfulldetailrecipe extends AppCompatActivity {

    private ImageView recipeImage,backcustomerdashboard;
    private TextView recipeName, recipeDescription, recipeIngredients, recipeMethod, recipeType, recipeCuisine, recipeEmail;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_customerfulldetailrecipe);

            backcustomerdashboard=findViewById(R.id.backcustomerdashboard);

            // Get data from intent
            Intent intent = getIntent();
            String name = intent.getStringExtra("name");

            String type = intent.getStringExtra("type");
            String cuisine = intent.getStringExtra("cuisine");
            String ingredient = intent.getStringExtra("ingredient");
            String method = intent.getStringExtra("method");
            String description = intent.getStringExtra("description");
            String imageUrl = intent.getStringExtra("imageUrl");

//            Intent img= getIntent();
//            String email= img.getStringExtra("email");


            // Set data to views
            TextView recipeName = findViewById(R.id.recipeName);
//            TextView recipeEmail = findViewById(R.id.recipemail);
//            TextView recipeType = findViewById(R.id.recipeType);
            TextView recipeCuisine = findViewById(R.id.recipeCuisine);
            TextView recipeIngredients = findViewById(R.id.recipeIngredients);
            TextView recipeMethod = findViewById(R.id.recipeMethod);
            TextView recipeDescription = findViewById(R.id.recipeDescription);
            ImageView recipeImage = findViewById(R.id.recipeImage);

            recipeName.setText(name);
//            recipeEmail.setText(email);
//            recipeType.setText(type);
            recipeCuisine.setText(cuisine);
            recipeIngredients.setText(ingredient);
            recipeMethod.setText(method);
            recipeDescription.setText(description);
            Picasso.get().load(imageUrl).into(recipeImage);


            backcustomerdashboard.setOnClickListener(view->{
                finish();
            });



        }





    }

