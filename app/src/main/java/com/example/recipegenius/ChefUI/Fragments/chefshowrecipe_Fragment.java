package com.example.recipegenius.ChefUI.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recipegenius.AdapterClasses.RecipeAdatper;
import com.example.recipegenius.PresentationClasses.Recipe;
import com.example.recipegenius.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class chefshowrecipe_Fragment extends Fragment {

    private RecyclerView recipesRecyclerView;
    private RecipeAdatper adapter;
    private List<Recipe> recipeList;

    private FirebaseDatabase database;


    public chefshowrecipe_Fragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_chefshowrecipe_, container, false);



return v;

    }

}