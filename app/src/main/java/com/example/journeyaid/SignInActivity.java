package com.example.journeyaid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    Button btnSingUpWindow;
    Button btnSignInExit,btnSignIn;
    EditText etSignInUserName,etSignInPassword;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        db = FirebaseFirestore.getInstance();
        btnSingUpWindow = findViewById(R.id.btnSingUpWindow);
        btnSignInExit = findViewById(R.id.btnSignInExit);
        btnSignIn = findViewById(R.id.btnSignIn);
        etSignInUserName = findViewById(R.id.etSignInUserName);
        etSignInPassword = findViewById(R.id.etSignInPassword);

        btnSingUpWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignInActivity.this, com.example.journeyaid.SignUpActivity.class);
                startActivity(intent);
                SignInActivity.this.finish();
            }
        });

        btnSignInExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignInActivity.this, com.example.journeyaid.MainActivity.class);
                startActivity(intent);
                SignInActivity.this.finish();
            }
        });

        // User want to sing in
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Extract the user filled data in fields
                String userName = etSignInUserName.getText().toString().trim();
                String password = etSignInPassword.getText().toString().trim();

                // Refer the user collection of firebase db
                DocumentReference documentReference = db.collection("Users").document(userName);
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        // When connection is successful following code is running
                        if(task.isSuccessful()){

                            // It retrieve the data of provided username
                            DocumentSnapshot document = task.getResult();

                            // Check whether document is exist or not
                            if(document.exists()){

                                // Get the user entered password
                                String rPassword = document.getString("password");

                                // If user entered password and firebase document password is correct redirect to home layout
                                if(rPassword.equals(password)){
                                    Intent intent = new Intent(SignInActivity
                                            .this,com.example.journeyaid.UserGoogleMapActivity.class);
                                    startActivity(intent);
                                    SignInActivity.this.finish();
                                }

                                // If user entered password and firebase document's password is wrong show a message
                                else {
                                    Toast.makeText(SignInActivity.this,"Password is Invalid!",Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }

                            // If document is not exist show a message
                            else {
                                Toast.makeText(SignInActivity.this,"No Such Record!",Toast.LENGTH_SHORT).show();
                            }
                        }

                        // If data access not happened show message
                        else {
                            Toast.makeText(SignInActivity.this,"Failed to Access to Record!",Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

            }

        });
    }
}