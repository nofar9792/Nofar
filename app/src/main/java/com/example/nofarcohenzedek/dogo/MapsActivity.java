package com.example.nofarcohenzedek.dogo;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

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

        // Add a marker in Sydney and move the camera
        LatLng karkom = new LatLng(31.907013, 35.01363900000001);
        mMap.addMarker(new MarkerOptions().position(karkom).title("CarmelsHome"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(karkom));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

        // add all of the dog walkers markers by their address

        // set every one of them a listener and check how can i see which one is pressed

        // when marker is pressed, open details activity
    }

    public LatLng getLocationFromAddress(String address)
    {
        Geocoder coder = new Geocoder(this);
        List<Address> addresses = null;

        try
        {
            addresses = coder.getFromLocationName(address, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses == null)
        {
            return null;
        }

        return new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());

    }
}
