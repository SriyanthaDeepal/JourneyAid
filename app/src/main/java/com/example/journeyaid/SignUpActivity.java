package com.example.journeyaid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    Button btnSignInAlready, btnSignUpExit, btnSingUp;
    EditText etUserName, etFirstName, etLastName, etEmail, etPassword, etConfirmPassword;
    String userName, firstName, lastName, email, password, confirmPassword;
    FirebaseFirestore db;
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        db = FirebaseFirestore.getInstance();

        btnSignInAlready = findViewById(R.id.btnSignInAlready);
        btnSignUpExit = findViewById(R.id.btnSignUpExit);
        btnSingUp = findViewById(R.id.btnSignUp);
        etUserName = findViewById(R.id.etUserName);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        //When user press sign in button it is navigate to sign in layout
        btnSignInAlready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Redirect to sign in window
                Intent intent = new Intent(SignUpActivity.this, com.example.journeyaid.SignInActivity.class);
                startActivity(intent);
                //Remove the sign up window
                SignUpActivity.this.finish();
            }
        });

        // When user want to close the window redirect to start layout
        btnSignUpExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, com.example.journeyaid.MainActivity.class);
                startActivity(intent);
                SignUpActivity.this.finish();
            }
        });

        // When new user registration ongoing
        btnSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Extract the filled data of each fields
                userName = etUserName.getText().toString().trim();
                firstName = etFirstName.getText().toString().trim();
                lastName = etLastName.getText().toString().trim();
                email = etEmail.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                confirmPassword = etConfirmPassword.getText().toString().trim();


                // Checking is there any empty fields is available
                if (userName.isEmpty() || firstName.isEmpty() ||
                        lastName.isEmpty() || email.isEmpty() ||
                        password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Fill all the Fields!", Toast.LENGTH_SHORT).show();
                }

                // New user data saved in firebase if all the fields are filled and confirm password is correct
                else if (password.equals(confirmPassword)) {

                    //Create a new user with a first and last name
                    Map<String, Object> user = new HashMap<>();
                    user.put("firstName", firstName);
                    user.put("lastName", lastName);
                    user.put("email", email);
                    user.put("password", password);

                    //Add a new document with a generated ID
                    db.collection("Users").document(userName).set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {

                        //If New user added successfully show a success message
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "DocumentSnapshot added with ID:" + userName);
                        }

                    }).addOnFailureListener(new OnFailureListener() {

                        //If new user not added show a error message
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error Adding Document", e);
                        }
                    });

                    Toast.makeText(SignUpActivity.this, "Thank You For Registration!", Toast.LENGTH_SHORT)
                            .show();

                // After successful registration redirect next layout
                Intent intent = new Intent(SignUpActivity.this, com.example.journeyaid.SignInActivity.class);
                startActivity(intent);
                SignUpActivity.this.finish();

            }
                // If confirmation password is wrong show a wrong message
                else {
                    Toast.makeText(SignUpActivity.this, "The Confirmation Password is Invalid!", Toast.LENGTH_SHORT)
                            .show();
                }
            }

        });

    }
}


