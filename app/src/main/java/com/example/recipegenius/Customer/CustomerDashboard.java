//package com.example.recipegenius.Customer;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//
//import com.example.recipegenius.AdapterClasses.RecipeCategoryAdapter;
//import com.example.recipegenius.AdapterClasses.ShowRecipeAdapter;
//import com.example.recipegenius.PresentationClasses.Recipe;
//import com.example.recipegenius.PresentationClasses.UserRegistrationModel;
//import com.example.recipegenius.R;
//import com.example.recipegenius.UserProfileContent.UserNotification;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class CustomerDashboard extends AppCompatActivity {
//
//    private RecyclerView recyclerViewCategories, customer_recyclerViewRecipes;
//    private DatabaseReference customer_categoryRef, customer_recipeRef;
//    private RecipeCategoryAdapter recipeCategoryAdapter;
//    private ShowRecipeAdapter showRecipeAdapter;
//    private List<Recipe> recipeCategoryList = new ArrayList<>();
//    private List<Recipe> showrecipelist = new ArrayList<>();
//    private ImageView customerProfileImage,notification_icon;
//    private DatabaseReference databaseReference;
//    private FirebaseUser currentUser;
//    private TextView customerName;
//    private StorageReference storageReference = storageReference = FirebaseStorage.getInstance().getReference();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_customer_dashboard);
//
//
//        StorageReference fileReference = storageReference.child("profileImages/");
//
//        storageReference = FirebaseStorage.getInstance().getReference();
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//
//        customerProfileImage = findViewById(R.id.customer_profile_image);
//        notification_icon=findViewById(R.id.notification_icon);
//        customerName=findViewById(R.id.customerName);
//
//        currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (currentUser != null) {
//            String userId = currentUser.getUid();
//            databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userId);
//
//            loadUserData();
//        } else {
//            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
//        }
//
//        notification_icon.setOnClickListener(view -> {
//            startActivity(new Intent(getApplicationContext(), UserNotification.class));
//        });
//
//
//        customerProfileImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),customerProfile.class));
//            }
//        });
//
//
//        // Initialize RecyclerViews and Adapters
//        recyclerViewCategories = findViewById(R.id.customer_recyclerViewCategories);
//        customer_recyclerViewRecipes = findViewById(R.id.customer_recyclerViewRecipes);
//
//
//        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        customer_recyclerViewRecipes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//
//
//        recipeCategoryAdapter = new RecipeCategoryAdapter(recipeCategoryList, this);
//        showRecipeAdapter=new ShowRecipeAdapter(showrecipelist,this);
//        recyclerViewCategories.setAdapter(recipeCategoryAdapter);
//        customer_recyclerViewRecipes.setAdapter(showRecipeAdapter);
//
//
//
//        // Initialize Firebase references
//        customer_categoryRef = FirebaseDatabase.getInstance().getReference().child("Recipes");
//        customer_recipeRef = FirebaseDatabase.getInstance().getReference().child("Recipes");
//
//        // Fetch category data
//        customer_categoryRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                recipeCategoryList.clear();
//
//                if (task.isSuccessful()) {
//                    DataSnapshot snapshot = task.getResult();
//                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                        Recipe recipeCategoryModel = childSnapshot.getValue(Recipe.class);
//                        if (recipeCategoryModel.getType() != null && recipeCategoryModel.getType().equals("Burgers")&& recipeCategoryModel.getType().equals("Fast Food") && recipeCategoryModel.getType().equals("Dessert")
//                                && recipeCategoryModel.getType().equals("Biryani")
//                                && recipeCategoryModel.getType().equals("Pakistan")
//                                && recipeCategoryModel.getType().equals("karahi & Handi")
//                                && recipeCategoryModel.getType().equals("American")
//                                && recipeCategoryModel.getType().equals("Middle Eastern")
//                                && recipeCategoryModel.getType().equals("Western")
//                        ) {
//                            Toast.makeText(CustomerDashboard.this, "exist", Toast.LENGTH_SHORT).show();
//                        }
//                        else if (recipeCategoryModel.getType() != null){
//                        recipeCategoryList.add(recipeCategoryModel);
//                    }
//                    }
//                    recipeCategoryAdapter.notifyDataSetChanged();
//                }
//            }
//        });
//
//        // Fetch recipe data
//        customer_recipeRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                showrecipelist.clear();
//
//                if (task.isSuccessful()) {
//                    DataSnapshot snapshot = task.getResult();
//                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                        Recipe recipe = childSnapshot.getValue(Recipe.class);
//                        showrecipelist.add(recipe);
//                    }
//                    showRecipeAdapter.notifyDataSetChanged();
//                }
//            }
//        });
//    }
//
//
//    private void loadUserData() {
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    UserRegistrationModel user = dataSnapshot.getValue(UserRegistrationModel.class);
//                    if (user != null) {
//                        customerName.setText(user.getName());
//
//
//                        if (user.getProfileImage() != null && !user.getProfileImage().isEmpty()) {
//                            Picasso.get().load(user.getProfileImage()).into(customerProfileImage);
//                        }
//
//                    }
//                } else {
//                    Toast.makeText(getApplicationContext(), "User data not found", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//
//}




package com.example.recipegenius.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.recipegenius.AdapterClasses.RecipeCategoryAdapter;
import com.example.recipegenius.AdapterClasses.ShowRecipeAdapter;
import com.example.recipegenius.PresentationClasses.Recipe;
import com.example.recipegenius.PresentationClasses.UserRegistrationModel;
import com.example.recipegenius.R;
import com.example.recipegenius.UserProfileContent.UserNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerDashboard extends AppCompatActivity implements RecipeCategoryAdapter.OnCategoryClickListener {

    private RecyclerView recyclerViewCategories, customer_recyclerViewRecipes;
    private DatabaseReference customer_categoryRef, customer_recipeRef;
    private RecipeCategoryAdapter recipeCategoryAdapter;
    private ShowRecipeAdapter showRecipeAdapter;
    private List<Recipe> recipeCategoryList = new ArrayList<>();
    private List<Recipe> showrecipelist = new ArrayList<>();
    private ImageView customerProfileImage, notification_icon;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private TextView customerName;
    private StorageReference storageReference;
    private EditText editTextText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        customerProfileImage = findViewById(R.id.customer_profile_image);
        notification_icon = findViewById(R.id.notification_icon);
        customerName = findViewById(R.id.customerName);
        editTextText=findViewById(R.id.editTextText);

        setupSearchFunctionality();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            loadUserData();
        } else {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
        }

        notification_icon.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), UserNotification.class)));

        customerProfileImage.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), customerProfile.class)));

        // Initialize RecyclerViews and Adapters
        recyclerViewCategories = findViewById(R.id.customer_recyclerViewCategories);
        customer_recyclerViewRecipes = findViewById(R.id.customer_recyclerViewRecipes);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        customer_recyclerViewRecipes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        recipeCategoryAdapter = new RecipeCategoryAdapter(recipeCategoryList, this, this);
        showRecipeAdapter = new ShowRecipeAdapter(showrecipelist, this);
        recyclerViewCategories.setAdapter(recipeCategoryAdapter);
        customer_recyclerViewRecipes.setAdapter(showRecipeAdapter);

        // Load data for categories and recipes
        loadCategories();
        loadAllRecipes();
    }

    private void loadUserData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserRegistrationModel user = dataSnapshot.getValue(UserRegistrationModel.class);
                    if (user != null) {
                        customerName.setText(user.getName());
                        if (user.getProfileImage() != null && !user.getProfileImage().isEmpty()) {
                            Picasso.get().load(user.getProfileImage()).into(customerProfileImage);
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCategories() {
        customer_categoryRef = FirebaseDatabase.getInstance().getReference().child("Recipes");
        customer_categoryRef.get().addOnCompleteListener(task -> {
            recipeCategoryList.clear();
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Recipe recipeCategory = childSnapshot.getValue(Recipe.class);
                    if (recipeCategory != null && recipeCategory.getCuisine() != null) {
                        recipeCategoryList.add(recipeCategory);
                    }
                }
                recipeCategoryAdapter.notifyDataSetChanged();
            }
        });
    }

    private void loadAllRecipes() {
        customer_recipeRef = FirebaseDatabase.getInstance().getReference().child("Recipes");
        customer_recipeRef.get().addOnCompleteListener(task -> {
            showrecipelist.clear();
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Recipe recipe = childSnapshot.getValue(Recipe.class);
                    if (recipe != null) {
                        showrecipelist.add(recipe);
                    }
                }
                showRecipeAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onCategoryClick(String category) {
        if (category.equals("All")) {
            showRecipeAdapter.updateRecipes(showrecipelist); // Show all recipes
        } else {
            List<Recipe> filteredList = new ArrayList<>();
            for (Recipe recipe : showrecipelist) {
                if (recipe.getCuisine().equals(category)) {
                    filteredList.add(recipe);
                }
            }
            showRecipeAdapter.updateRecipes(filteredList); // Show filtered recipes
        }
    }



    private void setupSearchFunctionality() {
        editTextText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No action needed before text changes
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterRecipes(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No action needed after text changes
            }
        });
    }

    private void filterRecipes(String query) {
        if (query.isEmpty()) {
            showRecipeAdapter.updateRecipes(showrecipelist); // If query is empty, show all recipes
        } else {
            List<Recipe> filteredList = showrecipelist.stream()
                    .filter(recipe -> recipe.getName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
            showRecipeAdapter.updateRecipes(filteredList);
        }
    }

}

