package com.example.nofarcohenzedek.dogo;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.Utilities;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Long userId;
    private String address;
    private ProgressBar progressBar;

    private View rootView;

    public MapsFragment()
    {}

    public MapsFragment(Long id, String addr)
    {
        userId = id;
        address = addr;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (rootView != null)
        {
            ViewGroup parent = (ViewGroup)rootView.getParent();

            if (parent != null)
            {
                parent.removeView(rootView);
            }
        }

        try {
            rootView = inflater.inflate(R.layout.activity_maps, container, false);
        }
        catch (InflateException e){
        }

        super.onCreateView(inflater, container, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        progressBar = (ProgressBar) rootView.findViewById(R.id.mapsProgressBar);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
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

                    Activity currentActivity = getActivity();
                    if(currentActivity != null) {
                        LatLng location = Utilities.getLocationFromAddress(finalAddress, currentActivity.getApplicationContext());

                        mMap.addMarker(new MarkerOptions().position(location).title(String.valueOf(currentDogWalker.getId())));
                    }
                    progressBar.setVisibility(View.GONE);

                }
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DogWalkerDetailsActivity.class);
                intent.putExtra("walkerId", Long.valueOf(marker.getTitle()));
                intent.putExtra("ownerId", userId);
                startActivity(intent);

                return true;
            }
        });
    }
}
