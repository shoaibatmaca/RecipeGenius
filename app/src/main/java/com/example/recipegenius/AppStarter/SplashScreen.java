package com.example.recipegenius.AppStarter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.example.recipegenius.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the GetStarted activity

                startActivity(new Intent(SplashScreen.this, GetStarted.class));
                // Finish the SplashScreen activity
                finish();
            }
        }, 1000);
    }


    @Override
    public void onBackPressed() {
        // Do nothing, or optionally you can override to prevent any action
        super.onBackPressed();

    }
}


//finish(): This is called immediately after starting the GetStarted activity, which ensures the SplashScreen activity is removed from the back stack. This way, pressing the back button from GetStarted or any subsequent screen will not return the user to the splash screen.
//onBackPressed(): Overriding this method and leaving it empty ensures that the back button does nothing while the splash screen is visible