package com.example.journeyaid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ThankYou extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        // Compute the GeoHash for a lat/lng point
        double lat = 6.9266;
        double lng = 79.8435;
        String hash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(lat, lng));

        // Add the hash and the lat/lng to the document. We will use the hash
        // for queries and the lat/lng for distance comparisons.
        Map<String, Object> updates = new HashMap<>();
        updates.put("geohash", hash);
        updates.put("lat", lat);
        updates.put("lng", lng);

        DocumentReference londonRef = db.collection("Places").document("Gall Face Beach");
        londonRef.update(updates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });

    }
}