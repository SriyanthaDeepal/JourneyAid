package com.example.journeyaid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Declare a Button variable called btnWelcome
    Button btnWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set the content view of the activity to the layout defined in activity_main.xml
        ActionBar actionBar = getSupportActionBar();// Get the ActionBar instance for the activity
        // Hide the ActionBar if it is not null
        if (actionBar != null) {
            actionBar.hide();
        }

        // Display a short toast message indicating that the Firebase connection was successful
        Toast.makeText(MainActivity.this,"Firebase Connection Success",Toast.LENGTH_SHORT).show();

        // Get an instance of the FirebaseFirestore class
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Initialize the btnWelcome variable with the Button object with the ID "btnStart"
        btnWelcome = findViewById(R.id.btnStart);

        // Set a click listener for the btnWelcome Button
        btnWelcome.setOnClickListener(new View.OnClickListener() {

            // Define the behavior when the Button is clicked
            @Override
            public void onClick(View view) {

                // Create a new Intent that navigates to the SignInActivity class
                Intent intent = new Intent(MainActivity.this,com.example.journeyaid.SignInActivity.class);
                startActivity(intent);
                // Finish the current activity (MainActivity)
                MainActivity.this.finish();
            }
        });

    }
}

/*
        // Compute the GeoHash for a lat/lng point
        double lat = 7.21624;
        double lng = 79.84032;
        String hash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(lat, lng));

        // Add the hash and the lat/lng to the document. We will use the hash
        // for queries and the lat/lng for distance comparisons.
        Map<String, Object> updates = new HashMap<>();
        updates.put("geohash", hash);
        updates.put("lat", lat);
        updates.put("lng", lng);

        DocumentReference londonRef = db.collection("Places").document("Sri Singama Kali Amman Kovil");
        londonRef.update(updates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        */