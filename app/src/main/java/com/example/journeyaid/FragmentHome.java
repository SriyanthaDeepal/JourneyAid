package com.example.journeyaid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.location.Geocoder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    //Define the variables
    private MapView mapView;//Declares a MapView variable
    private GoogleMap mMap;// Declares a GoogleMap variable
    private Marker firstMarker, secondMarker; // Declares two Marker variables
    private Polyline route; // Declares a Polyline variable
    private FirebaseFirestore db; // Declares a FirebaseFirestore variable
    public static ArrayList<String> visitList = new ArrayList<>(); // Declares a static ArrayList of Strings to create visiting List
    ArrayList<LatLng> userPinnedMarkers = new ArrayList<>(); // Declares an ArrayList of LatLngs


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Calls the constructor of the parent class with a layout resource id as a parameter
    public FragmentHome() {
        super(R.layout.fragment_home);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHome.
     */


    // TODO: Rename and change types and number of parameters
    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
        // Initializes the FirebaseFirestore variable "db" with an instance of the Firestore database
        db = FirebaseFirestore.getInstance();
    }

    // Define the onCreateView method for the HomeFragment class
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment_home layout and assign it to rootView
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // Find the SupportMapFragment with the ID "map" and assign it to mapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        // Find the MapView with the ID "mapView" and assign it to mapView
        mapView = rootView.findViewById(R.id.mapView);
        // Call getMapAsync to set the onMapReady callback and pass in this as the listener
        mapView.getMapAsync(this);
        // Call onCreate on the MapView and pass in savedInstanceState
        mapView.onCreate(savedInstanceState);
        return rootView;
    }


    // Define the onMapReady callback for the GoogleMap object
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        // Assign the GoogleMap object to mMap
        mMap = googleMap;
        // Set a MarkerClickListener on mMap
        mMap.setOnMarkerClickListener(marker -> {

            // When a marker is clicked, retrieve its title (docId)
            String docId = marker.getTitle();

            // Create an intent to launch the VisitPlaceDetails activity and pass in the docId
            Intent intent = new Intent(getActivity(), VisitPlaceDetails.class);
            intent.putExtra("doc", docId);
            startActivity(intent);
            return false;
        });

        // Add a marker to the map at Colombo and move the camera to that location
        LatLng colombo = new LatLng(6.9271, 79.8612);
        mMap.addMarker(new MarkerOptions().position(colombo).title("Marker in Colombo"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(colombo));

        // Create a new CameraPosition object and set the camera to it
        CameraPosition cameraPosition = CameraPosition
                .builder().target(colombo).zoom(6).bearing(0).tilt(30).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        // Set an onMapClickListener on mMap and pass in this as the listener
        mMap.setOnMapClickListener(this);
    }

    // Find the nearest locations along the drawn route
    private void findNearbyLocations(LatLng latLng) {

        // Find suggesting places
        // Define the center of the search as the given LatLng object
        final GeoLocation center = new GeoLocation(latLng.latitude, latLng.longitude);
        // Set the radius of the search
        final double radiusInM = 25000;

        // Each item in 'bounds' represents a startAt/endAt pair.
        //Get a list of GeoQueryBounds objects that define the search
        // area around the specified center location within the specified radius
        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM);
        final List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (GeoQueryBounds b : bounds) {
            // Create a Firestore query for documents in the "Places" collection that have a "geohash" field
            // that falls within the bounds of the current GeoQueryBounds object
            Query q = db.collection("Places")
                    .orderBy("geohash")
                    .startAt(b.startHash)
                    .endAt(b.endHash);
            // Add the query to the list of tasks to be executed
            tasks.add(q.get());
        }

        // Collect all the query results together into a single list
        Tasks.whenAllComplete(tasks)
                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> t) {
                        List<DocumentSnapshot> matchingDocs = new ArrayList<>();

                        // Iterate over the list of query results and add any documents that meet the search criteria
                        for (Task<QuerySnapshot> task : tasks) {
                            QuerySnapshot snap = task.getResult();
                            for (DocumentSnapshot doc : snap.getDocuments()) {
                                double lat = doc.getDouble("lat");
                                double lng = doc.getDouble("lng");

                                // Calculate the distance between the current document and the search center
                                GeoLocation docLocation = new GeoLocation(lat, lng);
                                double distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center);
                                // If the distance is less than or equal to the search radius, add the document to the list
                                if (distanceInM <= radiusInM) {
                                    matchingDocs.add(doc);
                                }
                            }
                        }

                        // Read each document which filtered, and show them in map view
                        // Add a marker for each matching document to the map
                        for (DocumentSnapshot doc : matchingDocs) {
                            double lat = doc.getDouble("lat");
                            double lng = doc.getDouble("lng");

                            LatLng location = new LatLng(lat, lng);
                            MarkerOptions marker = new MarkerOptions();
                            marker.position(location);
                            marker.title(doc.getId());

                            mMap.addMarker(marker);
                        }
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }



    @Override
    public void onMapClick(LatLng point) {

        // Check if there are no markers already pinned by the user
        if (userPinnedMarkers.size() == 0) {
            // Clear the map and the markers list
            mMap.clear();
            markers.clear();
        }

        // Add the new marker to the list of user pinned markers
        userPinnedMarkers.add(point);

        // Create a new marker with the specified position and add it to the map
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);
        mMap.addMarker(markerOptions);

        // If the user has already pinned 2 markers, then calculate the route
        if (userPinnedMarkers.size() == 2) {
            // Find nearby locations for each of the two markers
            findNearbyLocations(userPinnedMarkers.get(0));
            findNearbyLocations(userPinnedMarkers.get(1));
            // Get the URL to fetch the route data between the two markers
            String url = getDirectionsUrl(userPinnedMarkers.get(0), userPinnedMarkers.get(1));
            Log.d("SSSS", "onMapClick: " + url);
            // Start an asynchronous task to download the route data from the URL
            new DownloadTask().execute(url);
            // Clear the list of user pinned markers
            userPinnedMarkers.clear();
        }
    }

    // Method constructs the URL to fetch the route data between two given points
    private String getDirectionsUrl(LatLng origin, LatLng destination) {

        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + destination.latitude + "," + destination.longitude;
        String sensor = "sensor=false";
        String mode = "mode=driving";

        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;
        String output = "json";
        // Append the Google Maps API key to the URL
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters +"&key=" + "AIzaSyDfGyD_APS80dgk9IyW1BOXvN4gGsVk68Q";

    }

    // Downloads the route data from the specified URL in the background
    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("SSSS", e.toString());
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Parse the route data in the background
            ParserTask parserTask = new ParserTask();
            System.out.println(result);
            parserTask.execute(result);
        }
    }

    // Method for downloads data from the specified URL
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("SSSS", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // AsyncTask for parsing the JSON data returned by the Google Directions API
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";
            if (result.size() < 1) {
                Toast.makeText(getContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = result.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    if (j == 0) {
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) {
                        duration = (String) point.get("duration");
                        continue;
                    }
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    // Calls the findNearbyLocations method to search for nearby locations
                    findNearbyLocations(position);
                    points.add(position);
                }
                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);
            }
            // Adds the polyline to the GoogleMap object
            mMap.addPolyline(lineOptions);
        }
    }

    // Get user's pinned markers into an arraylist
    private class MarkerAndID {
        private String id;
        private MarkerOptions marker;

        public MarkerAndID(String id, MarkerOptions marker) {
            this.id = id;
            this.marker = marker;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public MarkerOptions getMarker() {
            return marker;
        }

        public void setMarker(MarkerOptions marker) {
            this.marker = marker;
        }
    }
    // Create a private List of MarkerAndID objects to store the
    // markers that are added to the GoogleMap in the findNearbyLocations method
    private List<MarkerAndID> markers = new ArrayList<>();
}
