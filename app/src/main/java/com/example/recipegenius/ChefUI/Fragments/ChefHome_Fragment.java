//package com.example.recipegenius.ChefUI.Fragments;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.recipegenius.AdapterClasses.RecipeAdatper;
//import com.example.recipegenius.ChefUI.ChefProfile;
//import com.example.recipegenius.PresentationClasses.Recipe;
//import com.example.recipegenius.R;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ChefHome_Fragment extends Fragment {
//
//    private TextView chefNameTextView;
//    private EditText searchEditText;
//    private RadioGroup filterOptions;
//    private RecyclerView recipesRecyclerView;
//
//    private DatabaseReference recipesRef;
//    private List<Recipe> recipesList;
//    private RecipeAdatper recipesAdapter;
//
//    private ImageView chefProfileImage;
//    private String currentFilter = "all"; // Default filter
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_chef_home_, container, false);
//
//        // Initialize views
//        chefNameTextView = v.findViewById(R.id.chef_name_dashboard);
//        searchEditText = v.findViewById(R.id.search_view);
//        filterOptions = v.findViewById(R.id.filter_options);
//        recipesRecyclerView = v.findViewById(R.id.recipes_recycler_view);
//        chefProfileImage = v.findViewById(R.id.chef_profile_image);
//
//        // Initialize Firebase database reference
//        recipesRef = FirebaseDatabase.getInstance().getReference().child("Recipes");
//
//        // Initialize recipes list and adapter
//        recipesList = new ArrayList<>();
//        recipesAdapter = new RecipeAdatper(requireContext(), recipesList, this::onRecipeDelete); // Pass delete listener
//        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//        recipesRecyclerView.setAdapter(recipesAdapter);
//
//        // Set profile image click listener
//        chefProfileImage.setOnClickListener(view -> {
//            startActivity(new Intent(getContext(), ChefProfile.class));
//        });
//
//        // Load recipes with the current filter
//        loadRecipes();
//
//        // Set up EditText listener for search
//        searchEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // No action needed
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.toString().isEmpty()) {
//                    loadRecipes(); // Reload recipes with the current filter
//                } else {
//                    searchRecipes(s.toString());
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // No action needed
//            }
//        });
//
//        // Set up RadioGroup listener for filtering
//        filterOptions.setOnCheckedChangeListener((group, checkedId) -> {
//            if (checkedId == R.id.rb_all) {
//                currentFilter = "all";
//            } else if (checkedId == R.id.rb_free) {
//                currentFilter = "free";
//            } else if (checkedId == R.id.rb_premium) {
//                currentFilter = "premium";
//            }
//            // Reload recipes with the current filter
//            loadRecipes();
//        });
//
//        return v;
//    }
//
//    private void loadRecipes() {
//        Query query;
//        if ("all".equals(currentFilter)) {
//            query = recipesRef; // Load all recipes
//        } else {
//            query = recipesRef.orderByChild("type").equalTo(currentFilter);
//        }
//
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                recipesList.clear();
//                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
//                    Recipe recipe = recipeSnapshot.getValue(Recipe.class);
//                    if (recipe != null) {
//                        recipesList.add(recipe);
//                    }
//                }
//
//                if (recipesList.isEmpty()) {
//                    Toast.makeText(requireContext(), "No recipes available for the selected filter", Toast.LENGTH_SHORT).show();
//                }
//
//                recipesAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(requireContext(), "Failed to load recipes: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void searchRecipes(String query) {
//        Query searchQuery;
//        if ("all".equals(currentFilter)) {
//            searchQuery = recipesRef.orderByChild("name").startAt(query).endAt(query + "\uf8ff");
//        } else {
//            searchQuery = recipesRef.orderByChild("name").startAt(query).endAt(query + "\uf8ff")
//                    .orderByChild("type").equalTo(currentFilter);
//        }
//
//        searchQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                recipesList.clear();
//                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
//                    Recipe recipe = recipeSnapshot.getValue(Recipe.class);
//                    if (recipe != null) {
//                        recipesList.add(recipe);
//                    }
//                }
//
//                if (recipesList.isEmpty()) {
//                    Toast.makeText(requireContext(), "No recipes found for the search query", Toast.LENGTH_SHORT).show();
//                }
//
//                recipesAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(requireContext(), "Failed to search recipes: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//
//    private void onRecipeDelete(String recipeId) {
//        recipesRef.child(recipeId).removeValue().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Toast.makeText(requireContext(), "Recipe deleted successfully", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(requireContext(), "Failed to delete recipe", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}

package com.example.recipegenius.ChefUI.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipegenius.AdapterClasses.RecipeAdatper;
import com.example.recipegenius.PresentationClasses.Recipe;
import com.example.recipegenius.PresentationClasses.UserRegistrationModel;
import com.example.recipegenius.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChefHome_Fragment extends Fragment {

    private TextView chefNameTextView;
    private EditText searchEditText;
    private RecyclerView recipesRecyclerView;

    private DatabaseReference recipesRef;
    private List<Recipe> recipesList;
    private RecipeAdatper recipesAdapter;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseUser currentUser;
    private ImageView chefProfileImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chef_home_, container, false);

        // Initialize views
        chefNameTextView = v.findViewById(R.id.chef_name_dashboard);
        searchEditText = v.findViewById(R.id.search_view);
        recipesRecyclerView = v.findViewById(R.id.recipes_recycler_view);
        chefProfileImage = v.findViewById(R.id.chef_profile_image);


        // Initialize Firebase database reference
        recipesRef = FirebaseDatabase.getInstance().getReference().child("Recipes");
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            loadUserData();
        } else {
            Toast.makeText(getContext(), "No user logged in", Toast.LENGTH_SHORT).show();
        }


        // Initialize recipes list and adapter
        recipesList = new ArrayList<>();
        recipesAdapter = new RecipeAdatper(requireContext(), recipesList, this::onRecipeDelete); // Pass delete listener
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recipesRecyclerView.setAdapter(recipesAdapter);


        // Load recipes with the default filter
        loadRecipes();

        // Set up EditText listener for search
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    loadRecipes(); // Reload recipes with the default filter
                } else {
                    searchRecipes(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed
            }
        });

        return v;
    }

    private void loadRecipes() {
        // Default filter set to "all"
        Query query = recipesRef; // Load all recipes

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipesList.clear();
                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                    if (recipe != null) {
                        recipesList.add(recipe);
                    }
                }

                if (recipesList.isEmpty()) {
                    Toast.makeText(requireContext(), "No recipes available", Toast.LENGTH_SHORT).show();
                }

                recipesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Failed to load recipes: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchRecipes(String query) {
        Query searchQuery = recipesRef.orderByChild("name").startAt(query).endAt(query + "\uf8ff");

        searchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipesList.clear();
                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                    if (recipe != null) {
                        recipesList.add(recipe);
                    }
                }

                if (recipesList.isEmpty()) {
                    Toast.makeText(requireContext(), "No recipes found for the search query", Toast.LENGTH_SHORT).show();
                }

                recipesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Failed to search recipes: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onRecipeDelete(String recipeId) {
        recipesRef.child(recipeId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(requireContext(), "Recipe deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Failed to delete recipe", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void loadUserData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserRegistrationModel user = dataSnapshot.getValue(UserRegistrationModel.class);
                    if (user != null) {
                        chefNameTextView.setText(user.getName());

                        if (user.getProfileImage() != null && !user.getProfileImage().isEmpty()) {
                            Picasso.get().load(user.getProfileImage()).into(chefProfileImage);
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
