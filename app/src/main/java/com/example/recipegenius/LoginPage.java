package com.example.recipegenius;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipegenius.AdminUI.AdminDashboard;
import com.example.recipegenius.ChefUI.ChefDashboard;
import com.example.recipegenius.Customer.CustomerDashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginPage extends AppCompatActivity {

    FirebaseAuth auth;
    EditText email, password;
    Button btn_sign_in;
    TextView registernow,userResetPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_page);

        auth= FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);

//        initializing the widget:
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        btn_sign_in=findViewById(R.id.btn_sign_in);
        registernow=findViewById(R.id.registerNow);
        userResetPassword=findViewById(R.id.userResetPassword);

        userResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ResetPassword.class));
            }
        });

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_signin();
            }
        });



        registernow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpPage.class));

            }
        });





    }

    private void btn_signin() {
        FirebaseAuth loginauth=FirebaseAuth.getInstance();
        String em, pas;
        em=email.getText().toString();
        pas=password.getText().toString();
        loginauth.signInWithEmailAndPassword(em, pas).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user=loginauth.getCurrentUser();
                    if(user!=null){
                        userloginCreditional(user.getUid());
                    }



                }
            }
        });


    }

    private void userloginCreditional(String uid) {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference userlogin=database.getReference("User").child(uid).child("userType");
        userlogin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userType=snapshot.getValue(String.class);

                if(userType!=null){
                    if(userType.equals("Admin")){
                        startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
                    }
                    else if(userType.equals("Chef")){
                        startActivity(new Intent(getApplicationContext(), ChefDashboard.class));
                    }
                    else if(userType.equals("Customer")){
                        startActivity(new Intent(getApplicationContext(), CustomerDashboard.class));
                    }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}