package com.example.recipegenius;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipegenius.PresentationClasses.UserRegistrationModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpPage extends AppCompatActivity {
    EditText Email, Password, Name, ConfirmPassword;
    Button Signup;
    TextView logintxt;


RadioGroup usergroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up_page);

//        Initializing the widget:
    Email= findViewById(R.id.CreateEmail);
    Password=findViewById(R.id.CreateUserPassword);
    Name=findViewById(R.id.CreateUserName);
    ConfirmPassword=findViewById(R.id.CreateConfirmUserPassword);
    Signup=findViewById(R.id.userRegistrationBtn);
    logintxt=findViewById(R.id.logintxt);





//        For radio group
        usergroup=findViewById(R.id.userradiogroup);
        usergroup.clearCheck();





        Signup.setOnClickListener(v -> {

            SignUp();

        });

        logintxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
            }
        });


    }

    private void SignUp() {

        FirebaseAuth auth= FirebaseAuth.getInstance();
        String name,email,password;
        name=Name.getText().toString();
        email=Email.getText().toString();
        password=Password.getText().toString();
//        password validations:

        passwordcreditionals();

        int selectedid=usergroup.getCheckedRadioButtonId();
        RadioButton userRadiobtn= findViewById(selectedid);
        String userType=userRadiobtn.getText().toString();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user=auth.getCurrentUser();
                    if (user!=null){
                        String userId=user.getUid();
                        UserRegistrationModel model= new UserRegistrationModel(userId, name, email,  userType, "", "","","","","");

                        DatabaseReference userdf= FirebaseDatabase.getInstance().getReference("User").child(userId);

                        userdf.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(SignUpPage.this, "Successfully created account!", Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    Toast.makeText(SignUpPage.this, "Failed to create!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });





                    }




                }




            }
        });


    }

    private void passwordcreditionals() {

        String pas, confirmpas;
        pas=Password.getText().toString();
        confirmpas=ConfirmPassword.getText().toString();
        if (!pas.equals(confirmpas)){
            Password.setError("Password must be same");
            ConfirmPassword.setError("Password must be same");
        }
        else if (pas.length()<8){
            Password.setError("Password must be same");
        } else if (!pas.contains("@") && !pas.contains("?") && !pas.contains("&") && !pas.contains("#")) {
            Password.setError("Must contain special character");
        }


    }


}