package com.example.ea.bondera2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

import android.util.Log;


import com.google.android.gms.common.GoogleApiAvailability;

import java.util.Locale;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener, View.OnClickListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    Marker mCurrDestinationMarker;
    LocationRequest mLocationRequest;
    int PROXIMITY_RADIUS = 10000;
    double latitude, longitude;
    double end_latitude=0.0, end_longitude=0.0;
    private Button startTripBtn;
    private Button endTripBtn;
    private DatabaseReference tripDatabaseReference;
    private FirebaseAuth firebaseAuth;
    private Button historyBtn;
    private String origin;
    private String destination;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        firebaseAuth = FirebaseAuth.getInstance();
        tripDatabaseReference = FirebaseDatabase.getInstance().getReference();
        if (firebaseAuth.getCurrentUser() == null) {

            finish(); //close the running activity

            startActivity(new Intent(this, SignIn.class));//start the login activity
        }
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        //String user

        startTripBtn = (Button) findViewById(R.id.startTripBtn);
        endTripBtn = (Button) findViewById(R.id.endTripBtn);
       historyBtn = (Button) findViewById(R.id.historyBtn);
        historyBtn.setOnClickListener(this);
        endTripBtn.setOnClickListener(this);
        startTripBtn.setOnClickListener(this);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        } else {
            Log.d("onCreate", "Google Play Services available.");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        // TODO: Get info about the selected place.
                        LatLng destinationLatlng = place.getLatLng();
                        CharSequence destinationChar = place.getName();
                        destination = destinationChar.toString();
                        end_latitude = destinationLatlng.latitude;
                        end_longitude = destinationLatlng.longitude;

                        MarkerOptions markerOptions1 = new MarkerOptions();
                        markerOptions1.position(destinationLatlng);
                        markerOptions1.title(destination);
                        markerOptions1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        mCurrDestinationMarker = mMap.addMarker(markerOptions1);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destinationLatlng,15.2f));
                       // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destinationLatlng,15.2f));


                    }

                    @Override
                    public void onError(Status status) {
                        // TODO: Handle the error.

                    }
                });


    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);

    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public void onClick(View v) {
        Object dataTransfer[] = new Object[2];
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


       switch(v.getId()) {

           case R.id.startTripBtn:
               if(null==destination){
                   if(end_latitude==0.0 || end_longitude==0.0){
                   Toast.makeText(this, "Please enter a destination or drag the pin to it", Toast.LENGTH_SHORT).show();}
                   else{
                       destination = getAddress(end_latitude,end_longitude);
                   }
               }
               while(null!=destination){
                   startTripBtn.setVisibility(View.GONE);
                   endTripBtn.setVisibility(View.VISIBLE);
                   //saving trip information on start
                   origin = getAddress(latitude, longitude);
                   LatLng latlng = new LatLng(latitude,longitude);
                   mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,10.2f));
               int startHour = Calendar.getInstance().getTime().getHours();
               int startMin = Calendar.getInstance().getTime().getMinutes();
               int startSec = Calendar.getInstance().getTime().getSeconds();
               java.sql.Time sTime = new java.sql.Time(startHour, startMin, startSec);
               Log.d("timeNow", sTime.toString());
               String startTime = sTime.toString();
               final double distance = calculateTripDistance();
               String totalDistance = Double.toString(distance);
               final Trip trip = new Trip(origin, destination, startTime, totalDistance);
               final String tripID = saveTripOnStart(firebaseUser, trip);
                trip.setTripID(tripID);
               //get directions data here:
               dataTransfer = new Object[3];
               String url = getDirectionsUrl();
               GetDirectionsData getDirectionsData = new GetDirectionsData();
               dataTransfer[0] = mMap;
               dataTransfer[1] = url;
               dataTransfer[2] = new LatLng(end_latitude, end_longitude);
               getDirectionsData.execute(dataTransfer);


               endTripBtn.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       endTripBtn.setVisibility(View.GONE);
                       startTripBtn.setVisibility(View.VISIBLE);

                       int endHour = Calendar.getInstance().getTime().getHours();
                       int endMin = Calendar.getInstance().getTime().getMinutes();
                       int endSec = Calendar.getInstance().getTime().getSeconds();
                       java.sql.Time eTime = new java.sql.Time(endHour, endMin, endSec);
                       String endTime = eTime.toString();
                       trip.setEndTime(endTime);
                       String tripDuration = calculateTripDuration(trip.getStartTime(), endTime);
                       String tripFare = calculateTripFare(tripDuration,distance,firebaseUser);
                       trip.setTripDuration(tripDuration);
                       trip.setEndTime(endTime);
                       trip.setTripFare(tripFare);
                       saveTripOnFinish(firebaseUser, trip,tripID);
                       /*Intent intent = new Intent(getApplicationContext(), cal.class);
                       startActivity(intent);*/

                   }
               });
               break;}
               break;


           case R.id.historyBtn:

               Intent intent1 = new Intent(this, TripHistory.class);
               startActivity(intent1);
       }}





    private String getDirectionsUrl() {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin=" + latitude + "," + longitude);
        googleDirectionsUrl.append("&destination=" + end_latitude + "," + end_longitude);
        googleDirectionsUrl.append("&key=" + "AIzaSyCAcfy-02UHSu2F6WeQ1rhQhkCr51eBL9g");

        return googleDirectionsUrl.toString();
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace){
    //private String getUrl(double latitude, double longitude) {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyBj-cnmMUY21M0vnIKz0k3tD3bRdyZea-Y");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }


    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        latitude = location.getLatitude();
        longitude = location.getLongitude();

            LatLng latLng = new LatLng(latitude, longitude);

                            //setting the current location marker
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.draggable(true);
                            markerOptions.title(getAddress(latitude,longitude));
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            mCurrLocationMarker = mMap.addMarker(markerOptions);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15.2f));

        //Toast.makeText(MapsActivity.this,"Your Current Location", Toast.LENGTH_LONG).show();
        //Toast.makeText(this, "Please enter a destination or drag the pin to it", Toast.LENGTH_LONG).show();


        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }

    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            if(obj!=null){
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getCountryName();
            //add = add + "\n" + obj.getCountryCode();
            //add = add + "\n" + obj.getAdminArea();
           // add = add + "\n" + obj.getPostalCode();
            //add = add + "\n" + obj.getSubAdminArea();
            //add = add + "\n" + obj.getSubThoroughfare();

            return add;}
         }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return "";
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.setDraggable(true);
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        end_latitude = marker.getPosition().latitude;
        end_longitude =  marker.getPosition().longitude;
        LatLng latlng = new LatLng(end_latitude,end_longitude);
        String destination = getAddress(end_latitude,end_longitude);
        MarkerOptions markeroptions = new MarkerOptions();
        markeroptions.position(latlng);
        markeroptions.title(destination);
        mCurrDestinationMarker = mMap.addMarker(markeroptions);

    }



   public String calculateTripDuration(String startTime,String endTime){
       try {
           SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
           Date date1 = format.parse(startTime);
           Date date2 = format.parse(endTime);
           long mills = date2.getTime() - date1.getTime();
           Log.v("Data1", "" + date1.getTime());
           Log.v("Data2", "" + date2.getTime());
           int hours = (int) (mills / (1000 * 60 * 60));
           int mins = (int) (mills / (1000 * 60)) % 60;

           String diff = hours + ":" + mins; // updated value every1 second

           return diff;
       }catch (Exception e){
           e.printStackTrace();
       }

      return "Error calculating Time";
    }


   public double calculateTripDistance(){
        double distance;
        float results[] =new float[10];
        Location.distanceBetween(latitude,longitude,end_latitude,end_longitude,results);
        distance = results[0];


        return distance;
    }

    public String calculateTripFare(String tripDuration,double tripDistance,FirebaseUser firebaseUser){
        //no basefare added
        tripDistance = tripDistance / 1000;
         tripDistance = (Math.round(tripDistance));
        double costDueToCoveredDistance;
        double costDueToTimeTaken;
        double totalCost;

        if(tripDistance<=3){
            costDueToCoveredDistance = 10.4;

        } else{
            tripDistance = tripDistance - 3;
            costDueToCoveredDistance= tripDistance * 1.8;
            costDueToCoveredDistance = costDueToCoveredDistance + 10.4;

        }
        String[] duration = tripDuration.split(":");
        String hrsString = duration[0];
        String minsString = duration[1];
        int hrs = Integer.parseInt(hrsString);
        int mins= Integer.parseInt(minsString);
        if(hrs>0){
            hrs = hrs *60;
            double timeTakenInMins = hrs + mins;
            costDueToTimeTaken = timeTakenInMins * 0.5 ;
        }else {
            costDueToTimeTaken = mins * 0.5;

        }
        totalCost = Math.round(costDueToCoveredDistance +costDueToTimeTaken);



        return Double.toString(totalCost);
    }


   private void saveTripOnFinish(FirebaseUser firebaseUser, Trip trip,String tripID){
     trip =new Trip(tripID,trip.getDestination(),trip.getOrigin(),trip.getTotalDistance(),trip.getStartTime(),trip.getEndTime(),trip.getTripDuration(),trip.getTripFare());

        tripDatabaseReference.child("Users").child(firebaseUser.getUid()).child("Trips").child(tripID).setValue(trip).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MapsActivity.this, "End  Trip Added", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(MapsActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


   private String saveTripOnStart(FirebaseUser firebaseUser, Trip trip){
        //we push to concatenate
        DatabaseReference myRef = tripDatabaseReference.getRef();
        String key =  myRef.push().getKey();
        trip.setTripID(key);
        tripDatabaseReference.child("Users").child(firebaseUser.getUid()).child("Trips").child(trip.getTripID()).setValue(trip).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MapsActivity.this, "Start Trip Added", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(MapsActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return trip.getTripID();

    }
}