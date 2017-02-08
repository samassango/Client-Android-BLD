package com.androidbld.client.sibusisomassango.transportfares;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.androidbld.client.sibusisomassango.transportfares.gpslocation.GpsLocationTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GpsLocationTracker gpsLocationTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        gpsLocationTracker = new GpsLocationTracker(MapsActivity.this);
        if(gpsLocationTracker.canGetLocation()) {

            double latitude = gpsLocationTracker.getLatitude();
            double longitude = gpsLocationTracker.getLongitude();
            Toast.makeText(MapsActivity.this,latitude +" "+longitude,Toast.LENGTH_LONG).show();
            // Add a marker in Sydney and move the camera
            // LatLng sydney = new LatLng(-34, 151);
            LatLng pointlocation = new LatLng(-latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(pointlocation).title("Pickup point"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(pointlocation));
        }else{
            gpsLocationTracker.showSettingsAlert();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the

                    // contacts-related task you need to do.

                    gpsLocationTracker = new GpsLocationTracker(MapsActivity.this);

                    // Check if GPS enabled
                    if (gpsLocationTracker.canGetLocation()) {

                        double latitude = gpsLocationTracker.getLatitude();
                        double longitude = gpsLocationTracker.getLongitude();

                        // \n is for new line
                        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    } else {
                        // Can't get location.
                        // GPS or network is not enabled.
                        // Ask user to enable GPS/network in settings.
                        gpsLocationTracker.showSettingsAlert();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    Toast.makeText(this, "You need to grant permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
