package com.example.nofarcohenzedek.dogo;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.Utilities;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.zip.Inflater;

public class MapsActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Long userId;
    private String address;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_maps, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        Bundle args = getArguments();


        MapFragment mapFragment = (MapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        progressBar = (ProgressBar) view.findViewById(R.id.mapsProgressBar);
        userId = args.getLong("userId");
        address = args.getString("address");

        return view;
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
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        LatLng center = Utilities.getLocationFromAddress(address,getActivity().getApplicationContext());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(center));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        // add all of the dog walkers markers by their address
        Model.getInstance().getAllDogWalkers(new Model.GetDogWalkersListener() {
            @Override
            public void onResult(List<DogWalker> allDogWalkers) {
                for (DogWalker currentDogWalker : allDogWalkers)
                {
                    String finalAddress = currentDogWalker.getAddress() + "," + currentDogWalker.getCity();

                    if(getActivity() != null) {
                        LatLng location = Utilities.getLocationFromAddress(finalAddress, getActivity().getApplicationContext());

                        mMap.addMarker(new MarkerOptions().position(location).title(String.valueOf(currentDogWalker.getId())));
                    }
                    progressBar.setVisibility(View.GONE);

                }
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DogWalkerDetails.class);
                intent.putExtra("walkerId", marker.getTitle());
                intent.putExtra("ownerId", userId);
                startActivity(intent);

                return true;
            }
        });
    }
}
