package com.example.journeyaid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DownlaodMap extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    ArrayList<String> downloadVisitList;
    private MapView mapView2;
    GoogleMap googleMap;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downlaod_map);
        mapView2 = findViewById(R.id.mapView2);
        mapView2.onCreate(savedInstanceState);
        mapView2.getMapAsync(this);

        downloadVisitList = FragmentHome.visitList;

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        // Add a marker in Colombe and move the camera
        LatLng colombo = new LatLng(6.9271, 79.8612);
        googleMap.addMarker(new MarkerOptions().position(colombo).title("Colombo"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(colombo));
        CameraPosition cameraPosition = CameraPosition
                .builder().target(colombo).zoom(6).bearing(0).tilt(30).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googleMap.setOnMapClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView2.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView2.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView2.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView2.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView2.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView2.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView2.onSaveInstanceState(outState);
    }


    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        // Add a marker at the clicked position
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Clicked location");
        googleMap.addMarker(markerOptions);
    }
}

