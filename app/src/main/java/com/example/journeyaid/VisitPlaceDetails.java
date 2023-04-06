package com.example.journeyaid;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class VisitPlaceDetails extends AppCompatActivity {

    // Define class variables
    ImageView imageView;
    ImageView favouriteButton;
    boolean isFavourite = false;
    TextView nameText, addressText, detailsText;
    ArrayList<String> collectingVisitList;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_place_details);
        favouriteButton = findViewById(R.id.btnFavourite);
        // Get the intent and document ID passed from the previous activity
        Intent intent = getIntent();
        String doc = intent.getStringExtra("doc");

        // Initialize Firebase Storage and Firestore
        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get the list of visited places from the home fragment
        collectingVisitList = FragmentHome.visitList;

        // Get references to views in the layout
        addressText = findViewById(R.id.placeAddressText);
        imageView = findViewById(R.id.placeImage);
        nameText = findViewById(R.id.placeNameText);
        detailsText = findViewById(R.id.placeDescriptionText);


        try {
            // Retrieve the document for the current place
            db.collection("Places").document(doc).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    // Get the document snapshot and set the values for the place
                    DocumentSnapshot result = task.getResult();
                    String address = result.get("address").toString();
                    addressText.setText(address);
                    String imageUrl = result.get("image").toString();

                    // Load the image into the image view using Glide library
                    storage.getReferenceFromUrl(imageUrl).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("DDD", "onSuccess: " + uri);
                            Glide.with(getApplicationContext()).load(uri).into(imageView);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.d("DDD", "onFailure: " + exception.getMessage());
                        }
                    });

                    // Set the name and details of the place
                    name = result.getId();
                    nameText.setText(name);

                    String details = result.get("description").toString();
                    detailsText.setText(details);
                }
            });


        } catch (NullPointerException e){
            // Catch any null pointer exceptions and log the error
            Log.e(TAG, "NullPointerException caught", e);
        }

        // Set an onClickListener for the favourite button
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Toggle the favourite button image and add/remove the place from the visited places list
                if (!isFavourite) {
                    favouriteButton.setImageResource(R.drawable.faviourite_icon);
                    isFavourite = true;
                    if(!collectingVisitList.contains(name)) {
                        collectingVisitList.add(name);
                    }
                } else {
                    favouriteButton.setImageResource(R.drawable.not_faviourite_icon);
                    isFavourite = false;
                    if(collectingVisitList.contains(name)) {
                        collectingVisitList.remove(name);
                    }
                }
            }


        });

    }

}