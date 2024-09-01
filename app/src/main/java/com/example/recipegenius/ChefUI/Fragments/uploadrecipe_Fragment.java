package com.example.recipegenius.ChefUI.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipegenius.Customer.customerProfile;
import com.example.recipegenius.Customer.customerfulldetailrecipe;
import com.example.recipegenius.PresentationClasses.Recipe;
import com.example.recipegenius.PresentationClasses.UserRegistrationModel;
import com.example.recipegenius.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

// ?????????????????/This  is a very simple way but code ::::
//public class uploadrecipe_Fragment extends Fragment {
//
//    private EditText nameEditText, descriptionEditText, ingredientsEditText, methodEditText;
//
//
//    private CheckBox recommendedCheckBox;
//    private Button uploadButton, updateButton;
//    private ImageView recipeImageView;
//    private Uri imageUri;
//    private FirebaseAuth mAuth;
//    private DatabaseReference recipeRef;
//    private StorageReference storageRef;
//    private ProgressDialog progressDialog;
//    private Recipe currentRecipe; // To hold the recipe being updated
//    private ImageView uploadRecipeImageView;
//    private EditText recipeNameEditText, recipeIngredientEditText, recipeMethodEditText, recipeDescriptionEditText;
//    private TextView selectCuisineTextView;
//    private RadioGroup recipeTypeRadioGroup;
//    private RadioButton premiumRadioButton, freeRadioButton;
//    private Button uploadRecipeButton, updateRecipeButton;
//    private String selectedCuisine;
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_uploadrecipe_, container, false);
//
//        // Initialize UI elements
//        initializeUI(view);
//
//        mAuth = FirebaseAuth.getInstance();
//        recipeRef = FirebaseDatabase.getInstance().getReference("Recipes");
//        storageRef = FirebaseStorage.getInstance().getReference("RecipeImages");
//        progressDialog = new ProgressDialog(getContext());
//
//
//        // Set click listener for select cuisine
//        selectCuisineTextView.setOnClickListener(v -> openCuisineBottomSheet());
//
//        // Upload button listener
//        uploadButton.setOnClickListener(v -> uploadRecipe());
//
//        // Update button listener (for updating an existing recipe)
//        updateButton.setOnClickListener(v -> updateRecipe());
//
//        // Image selection listener
//        recipeImageView.setOnClickListener(v -> selectImage());
//
//        return view;
//    }
//
//    private void initializeUI(View view) {
//
//        // Initialize Views
//        uploadRecipeImageView = view.findViewById(R.id.chef_uploadRecipe);
//        recipeNameEditText = view.findViewById(R.id.chef_recipeName);
//        recipeIngredientEditText = view.findViewById(R.id.chef_recipeIngredient);
//        recipeMethodEditText = view.findViewById(R.id.chef_recipemethod);
//        recipeDescriptionEditText = view.findViewById(R.id.chef_recipeDescription);
//        selectCuisineTextView = view.findViewById(R.id.chef_selectCuisine);
//        recipeTypeRadioGroup = view.findViewById(R.id.chef_RecipeTpeRadioGroup);
//        premiumRadioButton = view.findViewById(R.id.chef_radioButtonPremium);
//        freeRadioButton = view.findViewById(R.id.chef_radioButtonFree);
//        uploadRecipeButton = view.findViewById(R.id.chef_uploadRecipebtn);
//        updateRecipeButton = view.findViewById(R.id.chef_updateRecipebtn);
//    }
//
//    private void selectImage() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        startActivityForResult(intent, 1);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
//            imageUri = data.getData();
//            recipeImageView.setImageURI(imageUri);
//        }
//    }
//
//    private void uploadRecipe() {
//        String recipeId = recipeRef.push().getKey();
//        uploadRecipeToFirebase(recipeId, false);
//    }
//
//    private void updateRecipe() {
//        if (currentRecipe != null) {
//            uploadRecipeToFirebase(currentRecipe.getId(), true);
//        }
//    }
//
//    private void uploadRecipeToFirebase(String recipeId, boolean isUpdate) {
//        progressDialog.setMessage(isUpdate ? "Updating Recipe..." : "Uploading Recipe...");
//        progressDialog.show();
//
//        String name = nameEditText.getText().toString();
//        String description = descriptionEditText.getText().toString();
//        String ingredients = ingredientsEditText.getText().toString();
//        String method = methodEditText.getText().toString();
//        String type = typeSpinner.getSelectedItem().toString();
//        boolean recommended = recommendedCheckBox.isChecked();
//
//        if (selectedCuisine == null || selectedCuisine.isEmpty()) {
//            Toast.makeText(getContext(), "Please select a cuisine", Toast.LENGTH_SHORT).show();
//            progressDialog.dismiss();
//            return;
//        }
//
//        if (imageUri != null) {
//            StorageReference fileRef = storageRef.child(recipeId + "." + getFileExtension(imageUri));
//            fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
//                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                    String imageUrl = uri.toString();
//
//                    Recipe recipe = new Recipe(recipeId, name, description, ingredients, method, selectedCuisine, type, imageUrl, 0, recommended);
//
//                    if (isUpdate) {
//                        recipe.setLikes(currentRecipe.getLikes()); // Retain the existing likes count
//                    }
//
//                    recipeRef.child(recipeId).setValue(recipe).addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(getContext(), isUpdate ? "Recipe Updated" : "Recipe Uploaded", Toast.LENGTH_SHORT).show();
//                            clearFields();
//                        } else {
//                            Toast.makeText(getContext(), "Failed to " + (isUpdate ? "update" : "upload") + " recipe", Toast.LENGTH_SHORT).show();
//                        }
//                        progressDialog.dismiss();
//                    });
//                });
//            }).addOnFailureListener(e -> {
//                progressDialog.dismiss();
//                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//            });
//        } else {
//            Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
//            progressDialog.dismiss();
//        }
//    }
//
//    private void openCuisineBottomSheet() {
//        View view = getLayoutInflater().inflate(R.layout.chef_bottom_sheet_uploadrecipe, null);
//        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
//        dialog.setContentView(view);
//
//        ListView listView = view.findViewById(R.id.chef_addrecipe_cuisine_list_view);
//        EditText searchView = view.findViewById(R.id.chef_addrecipe_search_view);
//
//        List<String> cuisines = Arrays.asList("Burgers", "Fast Food", "Desserts", "Pakistani", "Biryani", "Bread", "Karahi & Handi", "American", "Middle Eastern", "Western");
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, cuisines);
//        listView.setAdapter(adapter);
//
//        searchView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                adapter.getFilter().filter(s);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
//
//        listView.setOnItemClickListener((parent, view1, position, id) -> {
//            selectedCuisine = adapter.getItem(position);
//            selectCuisineText.setText(selectedCuisine);
//            dialog.dismiss();
//        });
//
//        dialog.show();
//    }
//
//    private void clearFields() {
//        nameEditText.setText("");
//        descriptionEditText.setText("");
//        ingredientsEditText.setText("");
//        methodEditText.setText("");
//        selectCuisineText.setText("Select Cuisine"); // Reset the cuisine selection
//        typeSpinner.setSelection(0);
//        recommendedCheckBox.setChecked(false);
//        recipeImageView.setImageResource(R.drawable.placeholder_image);
//        imageUri = null;
//        currentRecipe = null; // Clear currentRecipe after update
//        selectedCuisine = null; // Reset the selected cuisine
//    }
//
//    private String getFileExtension(Uri uri) {
//        ContentResolver contentResolver = getActivity().getContentResolver();
//        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
//    }
//
//    // Call this method to populate fields when editing a recipe
//    public void populateRecipeForEdit(Recipe recipe) {
//        currentRecipe = recipe;
//        nameEditText.setText(recipe.getName());
//        descriptionEditText.setText(recipe.getDescription());
//        ingredientsEditText.setText(recipe.getIngredients());
//        methodEditText.setText(recipe.getMethod());
//        selectCuisineText.setText(recipe.getCuisine());
//        selectedCuisine = recipe.getCuisine();
//        recommendedCheckBox.setChecked(recipe.isRecommended());
//        // Assuming the image is loaded with some image loading library (e.g., Picasso or Glide)
//        // Picasso.get().load(recipe.getImageUrl()).into(recipeImageView);
//        // Glide.with(this).load(recipe.getImageUrl()).into(recipeImageView);
//    }
//}
//
//




// Ye working ker ria -----------------------------------------------------------------------
//
//public class uploadrecipe_Fragment extends Fragment {
//
//    private ImageView recipeImageView;
//    private EditText recipeNameEditText, recipeIngredientEditText, recipeMethodEditText, recipeDescriptionEditText;
//    private TextView selectCuisineTextView;
//    private RadioGroup recipeTypeRadioGroup;
//    private Button uploadRecipeButton, updateRecipeButton;
//    private Uri imageUri;
//    private String selectedCuisine;
//    private ProgressDialog progressDialog;
//    private FirebaseAuth mAuth;
//    private DatabaseReference recipeRef;
//    private StorageReference storageRef;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_uploadrecipe_, container, false);
//
//        // Initialize UI elements
//        initializeUI(view);
//
//        // Firebase and Progress Dialog setup
//        mAuth = FirebaseAuth.getInstance();
//        recipeRef = FirebaseDatabase.getInstance().getReference("Recipes");
//        storageRef = FirebaseStorage.getInstance().getReference("RecipeImages");
//        progressDialog = new ProgressDialog(getContext());
//        // Set click listener for select cuisine
//        selectCuisineTextView.setOnClickListener(v -> openCuisineBottomSheet());
//
//        // Set listeners for buttons
//        uploadRecipeButton.setOnClickListener(v -> uploadRecipe());
//        updateRecipeButton.setOnClickListener(v -> {
//                updateRecipe();
//                }
//
//        );
//        recipeImageView.setOnClickListener(v -> openImageChooser());
//
//
//        return view;
//    }
//
//    private void initializeUI(View view) {
//        recipeImageView = view.findViewById(R.id.chef_uploadRecipeImage);
//        recipeNameEditText = view.findViewById(R.id.chef_recipeName);
//        recipeIngredientEditText = view.findViewById(R.id.chef_recipeIngredient);
//        recipeMethodEditText = view.findViewById(R.id.chef_recipemethod);
//        recipeDescriptionEditText = view.findViewById(R.id.chef_recipeDescription);
//        selectCuisineTextView = view.findViewById(R.id.chef_selectCuisine);
//        recipeTypeRadioGroup = view.findViewById(R.id.chef_RecipeTpeRadioGroup);
//        uploadRecipeButton = view.findViewById(R.id.chef_uploadRecipebtn);
//        updateRecipeButton = view.findViewById(R.id.chef_updateRecipebtn);
//
//    }
//
//    private void openImageChooser() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");
//        startActivityForResult(intent, 100);
//    }
//
//    private void openCuisineBottomSheet() {
//        View bottomSheetView = getLayoutInflater().inflate(R.layout.chef_bottom_sheet_uploadrecipe, null);
//        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
//        dialog.setContentView(bottomSheetView);
//
//        ListView listView = bottomSheetView.findViewById(R.id.chef_addrecipe_cuisine_list_view);
//        EditText searchView = bottomSheetView.findViewById(R.id.chef_addrecipe_search_view);
//
//        List<String> cuisines = Arrays.asList("Burgers", "Fast Food", "Desserts", "Pakistani", "Biryani", "Bread", "Karahi & Handi", "American", "Middle Eastern", "Western");
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, cuisines);
//        listView.setAdapter(adapter);
//
//        searchView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                adapter.getFilter().filter(s);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
//
//        listView.setOnItemClickListener((parent, view, position, id) -> {
//            selectedCuisine = adapter.getItem(position);
//            selectCuisineTextView.setText(selectedCuisine);
//            dialog.dismiss();
//        });
//
//        dialog.show();
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
//            imageUri = data.getData();
//            recipeImageView.setImageURI(imageUri);
//        }
//    }
//
//    private void uploadRecipe() {
//        final String recipeName = recipeNameEditText.getText().toString().trim();
//        final String recipeIngredients = recipeIngredientEditText.getText().toString().trim();
//        final String recipeMethod = recipeMethodEditText.getText().toString().trim();
//        final String recipeDescription = recipeDescriptionEditText.getText().toString().trim();
//        final String recipeType = ((RadioButton) getView().findViewById(recipeTypeRadioGroup.getCheckedRadioButtonId())).getText().toString();
//        final String recipeId = UUID.randomUUID().toString(); // Unique ID for recipe
//
//        if (imageUri != null) {
//            // Upload image first, then save recipe
//            StorageReference imageRef = storageRef.child("recipe_images/" + recipeId);
//            progressDialog.setMessage("Uploading Recipe...");
//            progressDialog.show();
//
//            imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                saveRecipeToDatabase(recipeId, recipeName,recipeDescription ,recipeIngredients, recipeMethod, selectedCuisine, recipeType, uri.toString());
//                progressDialog.dismiss();
//            })).addOnFailureListener(e -> {
//                progressDialog.dismiss();
//                Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
//            });
//        } else {
//            // No image, save recipe directly
//            saveRecipeToDatabase(recipeId, recipeName, recipeDescription,recipeIngredients, recipeMethod, selectedCuisine, recipeType, null);
//        }
//    }
//
//    private void saveRecipeToDatabase(String recipeId, String name, String ingredients, String method, String description, String cuisine, String type, String imageUrl) {
//        Recipe recipe = new Recipe(recipeId, name, ingredients, method, description, cuisine, type, imageUrl, 0, false);
//        recipeRef.child(recipeId).setValue(recipe).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Toast.makeText(getContext(), "Recipe uploaded successfully", Toast.LENGTH_SHORT).show();
//                resetForm();
//            } else {
//                Toast.makeText(getContext(), "Failed to upload recipe", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//    private void updateRecipe() {
//        final String name = recipeNameEditText.getText().toString().trim();
//        final String ingredients = recipeIngredientEditText.getText().toString().trim();
//        final String method = recipeMethodEditText.getText().toString().trim();
//        final String description = recipeDescriptionEditText.getText().toString().trim();
//        final String cuisine = selectCuisineTextView.getText().toString().trim();
//        final String recipeType = ((RadioButton) getView().findViewById(recipeTypeRadioGroup.getCheckedRadioButtonId())).getText().toString();
//        final String recipeId = UUID.randomUUID().toString(); // Get the recipe ID from a proper source
//
//        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(ingredients) || TextUtils.isEmpty(method) ||
//                TextUtils.isEmpty(description) || TextUtils.isEmpty(cuisine) || TextUtils.isEmpty(recipeType)) {
//            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        DatabaseReference recipeRef = FirebaseDatabase.getInstance().getReference("Recipes").child(recipeId);
//
//        if (imageUri != null) {
//            // Upload new image and get URL
//            StorageReference imageRef = FirebaseStorage.getInstance().getReference("RecipeImages").child(recipeId + ".jpg");
//            imageRef.putFile(imageUri).addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                        String imageUrl = uri.toString();
//                        updateRecipeInDatabase(recipeRef, name, ingredients, method, description, cuisine, recipeType, imageUrl);
//                    });
//                } else {
//                    Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            // No new image, keep existing image URL
//            recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        String existingImageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
//                        updateRecipeInDatabase(recipeRef, name, ingredients, method, description, cuisine, recipeType, existingImageUrl);
//                    } else {
//                        Toast.makeText(getContext(), "Recipe not found", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    Toast.makeText(getContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
//
//    private void updateRecipeInDatabase(DatabaseReference recipeRef, String name, String ingredients, String method,
//                                        String description, String cuisine, String type, String imageUrl) {
//        HashMap<String, Object> recipeMap = new HashMap<>();
//        recipeMap.put("name", name);
//        recipeMap.put("ingredients", ingredients);
//        recipeMap.put("method", method);
//        recipeMap.put("description", description);
//        recipeMap.put("cuisine", cuisine);
//        recipeMap.put("type", type);
//        recipeMap.put("imageUrl", imageUrl);
//
//        recipeRef.updateChildren(recipeMap).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Toast.makeText(getContext(), "Recipe updated successfully", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(getContext(), "Failed to update recipe", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void resetForm() {
//        recipeNameEditText.setText("");
//        recipeIngredientEditText.setText("");
//        recipeMethodEditText.setText("");
//        recipeDescriptionEditText.setText("");
//        selectCuisineTextView.setText("Select Cuisine");
//        recipeTypeRadioGroup.clearCheck();
//        recipeImageView.setImageResource(R.drawable.user); // Default placeholder image
//        imageUri = null;
//    }
//
//}
//


public class uploadrecipe_Fragment extends Fragment {

    private ImageView recipeImageView;
    private EditText recipeNameEditText, recipeIngredientEditText, recipeMethodEditText, recipeDescriptionEditText,profileEmail;
    private TextView selectCuisineTextView;
    private RadioGroup recipeTypeRadioGroup;
    private Button uploadRecipeButton, updateRecipeButton;
    private Uri imageUri;
    private String selectedCuisine;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference recipeRef;
    private StorageReference storageRef;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uploadrecipe_, container, false);

        // Initialize UI elements
        initializeUI(view);

        // Firebase and Progress Dialog setup
        mAuth = FirebaseAuth.getInstance();
        recipeRef = FirebaseDatabase.getInstance().getReference("Recipes");
        storageRef = FirebaseStorage.getInstance().getReference("RecipeImages");
        progressDialog = new ProgressDialog(getContext());

        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());




        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userId);

            loadUserData();
        } else {
            Toast.makeText(getContext(), "No user logged in", Toast.LENGTH_SHORT).show();
        }





        // Set click listener for select cuisine
        selectCuisineTextView.setOnClickListener(v -> openCuisineBottomSheet());

        // Set listeners for buttons
        uploadRecipeButton.setOnClickListener(v -> uploadRecipe());
        updateRecipeButton.setOnClickListener(v -> updateRecipe());
        recipeImageView.setOnClickListener(v -> openImageChooser());

        return view;
    }

    private void initializeUI(View view) {
        recipeImageView = view.findViewById(R.id.chef_uploadRecipeImage);
        recipeNameEditText = view.findViewById(R.id.chef_recipeName);
        recipeIngredientEditText = view.findViewById(R.id.chef_recipeIngredient);
        recipeMethodEditText = view.findViewById(R.id.chef_recipemethod);
        recipeDescriptionEditText = view.findViewById(R.id.chef_recipeDescription);
        selectCuisineTextView = view.findViewById(R.id.chef_selectCuisine);
        recipeTypeRadioGroup = view.findViewById(R.id.chef_RecipeTpeRadioGroup);
        uploadRecipeButton = view.findViewById(R.id.chef_uploadRecipebtn);
        updateRecipeButton = view.findViewById(R.id.chef_updateRecipebtn);
        profileEmail=view.findViewById(R.id.profileEmail);
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    private void openCuisineBottomSheet() {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.chef_bottom_sheet_uploadrecipe, null);
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        dialog.setContentView(bottomSheetView);

        ListView listView = bottomSheetView.findViewById(R.id.chef_addrecipe_cuisine_list_view);
        EditText searchView = bottomSheetView.findViewById(R.id.chef_addrecipe_search_view);

        List<String> cuisines = Arrays.asList("Burgers", "Fast Food", "Desserts", "Pakistani", "Biryani", "Bread", "Karahi & Handi", "American", "Middle Eastern", "Western");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, cuisines);
        listView.setAdapter(adapter);

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            selectedCuisine = adapter.getItem(position);
            selectCuisineTextView.setText(selectedCuisine);
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            recipeImageView.setImageURI(imageUri);
        }
    }

    private void uploadRecipe() {
        final String recipeName = recipeNameEditText.getText().toString().trim();
        final String recipeIngredients = recipeIngredientEditText.getText().toString().trim();
        final String recipeMethod = recipeMethodEditText.getText().toString().trim();
        final String recipeDescription = recipeDescriptionEditText.getText().toString().trim();
        final String recipeType = ((RadioButton) getView().findViewById(recipeTypeRadioGroup.getCheckedRadioButtonId())).getText().toString();
        final String recipeId = UUID.randomUUID().toString(); // Unique ID for recipe

        if (TextUtils.isEmpty(recipeName) || TextUtils.isEmpty(recipeIngredients) || TextUtils.isEmpty(recipeMethod) ||
                TextUtils.isEmpty(recipeDescription) || TextUtils.isEmpty(selectedCuisine) || TextUtils.isEmpty(recipeType)) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri != null) {
            // Upload image first, then save recipe
            StorageReference imageRef = storageRef.child("recipe_images/" + recipeId);
            progressDialog.setMessage("Uploading Recipe...");
            progressDialog.show();

            imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                saveRecipeToDatabase(recipeId, recipeName, recipeDescription, recipeIngredients, recipeMethod, selectedCuisine, recipeType, uri.toString());
                progressDialog.dismiss();
            })).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
            });
        } else {
            // No image, save recipe directly
            saveRecipeToDatabase(recipeId, recipeName, recipeDescription, recipeIngredients, recipeMethod, selectedCuisine, recipeType, null);
        }
    }

    private void saveRecipeToDatabase(String recipeId, String name, String description, String ingredients, String method, String cuisine, String type, String imageUrl) {
        Recipe recipe = new Recipe(recipeId, name, description, ingredients, method, cuisine, type, imageUrl, 0, false);

        recipeRef.child(recipeId).setValue(recipe).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Recipe uploaded successfully", Toast.LENGTH_SHORT).show();
                resetForm();
            } else {
                Toast.makeText(getContext(), "Failed to upload recipe", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateRecipe() {
        final String recipeName = recipeNameEditText.getText().toString().trim();
        final String ingredients = recipeIngredientEditText.getText().toString().trim();
        final String method = recipeMethodEditText.getText().toString().trim();
        final String description = recipeDescriptionEditText.getText().toString().trim();
        final String cuisine = selectCuisineTextView.getText().toString().trim();
        final String recipeType = ((RadioButton) getView().findViewById(recipeTypeRadioGroup.getCheckedRadioButtonId())).getText().toString();

        if (TextUtils.isEmpty(recipeName) || TextUtils.isEmpty(ingredients) || TextUtils.isEmpty(method) ||
                TextUtils.isEmpty(description) || TextUtils.isEmpty(cuisine) || TextUtils.isEmpty(recipeType)) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Updating Recipe...");
        progressDialog.show();

        Query recipeQuery = recipeRef.orderByChild("name").equalTo(recipeName);
        recipeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String recipeId = snapshot.getKey();
                        HashMap<String, Object> updateMap = new HashMap<>();
                        updateMap.put("ingredients", ingredients);
                        updateMap.put("method", method);
                        updateMap.put("description", description);
                        updateMap.put("cuisine", cuisine);
                        updateMap.put("recipeType", recipeType);

                        if (imageUri != null) {
                            StorageReference imageRef = storageRef.child("recipe_images/" + recipeId);
                            imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                updateMap.put("imageUrl", uri.toString());
                                recipeRef.child(recipeId).updateChildren(updateMap).addOnCompleteListener(task -> {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Recipe updated successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Failed to update recipe", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }));
                        } else {
                            recipeRef.child(recipeId).updateChildren(updateMap).addOnCompleteListener(task -> {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Recipe updated successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Failed to update recipe", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Recipe not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetForm() {
        recipeNameEditText.setText("");
        recipeIngredientEditText.setText("");
        recipeMethodEditText.setText("");
        recipeDescriptionEditText.setText("");
        selectCuisineTextView.setText("");
        recipeTypeRadioGroup.clearCheck();
        recipeImageView.setImageResource(R.drawable.user);
        imageUri = null;
    }



    private void loadUserData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserRegistrationModel user = dataSnapshot.getValue(UserRegistrationModel.class);
                    if (user != null) {
                        profileEmail.setText(user.getEmail());
//                        String profil= user.getEmail().toString();
//                        Intent intent= new Intent();
//                        intent.putExtra("email", profil);
//                        startActivity(new Intent(getContext(), customerfulldetailrecipe.class));
//



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