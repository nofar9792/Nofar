package com.example.nofarcohenzedek.dogo;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends Activity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Long userId;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setActionBar((Toolbar) findViewById(R.id.mapToolBar));
        getActionBar().setDisplayShowTitleEnabled(false);
        progressBar = (ProgressBar) findViewById(R.id.mapsProgressBar);

        userId = getIntent().getLongExtra("userId",0);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_prime_dog_owner, menu);

        return true;
        //return super.onCreateOptionsMenu(menu);
    }

        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();

            Intent intent = null;

            if (id == R.id.searchDW) {
                 intent = new Intent(this, SearchActivity.class);
            } else if (id == R.id.dogsList) {
                 intent = new Intent(this, DogsListActivity.class);
            } else if (id == R.id.tripsReport) {
                 intent = new Intent(this, TripsReportActivity.class);
            } else if (id == R.id.messages) {
                 intent = new Intent(this, MessagesActivity.class);
            } else if (id == R.id.myProfile) {
                 intent = new Intent(this, MyProfileActivity.class);

            }

            intent.putExtra("isOwner", true);
            intent.putExtra("userId", userId);
            startActivity(intent);

            return super.onOptionsItemSelected(item);
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

        LatLng center = getLocationFromAddress(getIntent().getStringExtra("address"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(center));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        // add all of the dog walkers markers by their address
        Model.getInstance().getAllDogWalkers(new Model.GetDogWalkersListener() {
            @Override
            public void onResult(List<DogWalker> allDogWalkers) {
                for (DogWalker currentDogWalker : allDogWalkers)
                {
                    String finalAddress = currentDogWalker.getAddress() + "," + currentDogWalker.getCity();
                    LatLng location = getLocationFromAddress(finalAddress);

                    //mMap.addMarker(new MarkerOptions().position(location).title(String.valueOf(currentDogWalker.getId()))
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.manwithdog)));

                    mMap.addMarker(new MarkerOptions().position(location).title(String.valueOf(currentDogWalker.getId())));
                          //  .icon(BitmapDescriptorFactory.fromPath("/drawable/manwithdog.png")));

                    progressBar.setVisibility(View.GONE);

                }
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(getApplicationContext(), DogWalkerDetails.class);
                intent.putExtra("walkerId", marker.getTitle());
                intent.putExtra("ownerId", userId);
                startActivity(intent);

                return true;
            }
        });

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
