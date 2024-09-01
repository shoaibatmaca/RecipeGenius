package com.example.recipegenius.ChefUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.recipegenius.ChefUI.Fragments.ChefHome_Fragment;
import com.example.recipegenius.ChefUI.Fragments.uploadrecipe_Fragment;
import com.example.recipegenius.Customer.customerProfile;
import com.example.recipegenius.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ChefDashboard extends AppCompatActivity {

    private BottomAppBar bottomAppBar;
    private FloatingActionButton fab;
    private FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_dashboard);

        bottomAppBar = findViewById(R.id.chef_bottomBar);
        fab = findViewById(R.id.chef_fab);
        fragmentContainer = findViewById(R.id.fragment_container);

        // Display the upload recipe fragment by default
        if (savedInstanceState == null) {
            replaceFragment(new uploadrecipe_Fragment());
        }

        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Fragment fragment = null;

                int itemId = item.getItemId();

                if (itemId == R.id.menu_Addrecipe) {
                    fragment = new uploadrecipe_Fragment();
                } else if (itemId == R.id.menu_showrecipe) {
                    fragment = new ChefHome_Fragment();
                } else if (itemId == R.id.chef_profile) {
                    startActivity(new Intent(getApplicationContext(), customerProfile.class));
                }

                if (fragment != null) {
                    replaceFragment(fragment);
                }
                return true;
            }
        });

        // Set OnClickListener for FAB to show uploadrecipe_Fragment
        fab.setOnClickListener(view -> replaceFragment(new uploadrecipe_Fragment()));
    }

    // Method to replace fragments based on menu item selection
    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
