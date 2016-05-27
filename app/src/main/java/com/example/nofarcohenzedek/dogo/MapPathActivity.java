package com.example.nofarcohenzedek.dogo;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nofarcohenzedek.dogo.Model.PathAction;
import com.example.nofarcohenzedek.dogo.Model.PathMilestone;
import com.example.nofarcohenzedek.dogo.Model.Route;
import com.example.nofarcohenzedek.dogo.Model.Utilities;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class MapPathActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ProgressBar progressBar;

    private List<String> addrToMarkPath;

    private List<String> tempAddrList;
    private List<PathMilestone> milestones;
    private Calendar startTime;
    private SimpleDateFormat format;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_path);

        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        progressBar = (ProgressBar) findViewById(R.id.mapsProgressBar);
        format = new SimpleDateFormat("HH:mm");

        milestones = new ArrayList<PathMilestone>(){};
        milestones.add(new PathMilestone(PathAction.Start,0,"כרכום 3, מודיעין, ישראל"));
        milestones.add(new PathMilestone(PathAction.Walk, 300, "עמק איילון 30, מודיעין, ישראל"));
        milestones.add(new PathMilestone(PathAction.Wait, 60, "עמק איילון 30, מודיעין, ישראל"));
        milestones.add(new PathMilestone(PathAction.Pickup,0, "עמק איילון 30, מודיעין, ישראל"));
        milestones.add(new PathMilestone(PathAction.Walk, 600, "לבונה 6, מודיעין, ישראל"));
        milestones.add(new PathMilestone(PathAction.Pickup,0, "לבונה 6, מודיעין, ישראל"));
        milestones.add(new PathMilestone(PathAction.Walk, 600, "עמק איילון 30, מודיעין, ישראל"));
        milestones.add(new PathMilestone(PathAction.Return,0, "עמק איילון 30, מודיעין, ישראל"));
        milestones.add(new PathMilestone(PathAction.Walk, 600, "לבונה 6, מודיעין, ישראל"));
        milestones.add(new PathMilestone(PathAction.Return,0, "לבונה 6, מודיעין, ישראל"));
        milestones.add(new PathMilestone(PathAction.Walk, 900, "כרכום 3, מודיעין, ישראל"));

        // decide which addresses to mark in path
        addrToMarkPath = new ArrayList<String>(){};
        for (PathMilestone currMil: milestones) {
            if (currMil.getAction() == PathAction.Start || currMil.getAction() == PathAction.Walk) {
                addrToMarkPath.add(currMil.getAddress());
            }
        }

        startTime = new GregorianCalendar(Locale.ITALY);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // snippet multiline
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(getApplicationContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getApplicationContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getApplicationContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        LatLng center = Utilities.getLocationFromAddress(milestones.get(0).getAddress(), getApplicationContext());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(center));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        Route route = new Route();

        for (int i = 0; i < addrToMarkPath.size(); i++) {
            String title = buildTitleForMarker(addrToMarkPath.get(i));
            String snippet = buildSnippetForMarker(addrToMarkPath.get(i));

            mMap.addMarker(new MarkerOptions()
                    .position(Utilities.getLocationFromAddress(addrToMarkPath.get(i), getApplicationContext()))
                    .title(title).snippet(snippet));

            if (i != addrToMarkPath.size() - 1) {
                route.drawRoute(mMap, getApplicationContext(),
                        Utilities.getLocationFromAddress(addrToMarkPath.get(i), getApplicationContext()),
                        Utilities.getLocationFromAddress(addrToMarkPath.get(i + 1), getApplicationContext()),
                        false, "en");
            }
        }
    }

    private String buildTitleForMarker(String address)
    {
        return "מיקום נוכחי: " + address;
    }

    private String buildSnippetForMarker(String address)
    {
        // TODO see what about the last milestone - if i have my home again or not (although if the first milestone is my home)

        String newLine = "\n";
        String result = newLine;
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTime(startTime.getTime());

        for (int i=0; i<milestones.size();i++)
        {
            PathMilestone currMil = milestones.get(i);
            currentTime.add(Calendar.SECOND,currMil.getDuration());

            if (currMil.getAddress().equals(address))
            {
                if (currMil.getAction() != PathAction.Walk) {
                    result += format.format(currentTime.getTime()) + ": ";
                }

                switch (currMil.getAction()) {
                    case Pickup: {
                        result += "איספי את " + newLine;
                        break;
                    }
                    case Return: {
                        result += "החזירי את " + newLine;
                        break;
                    }
                    case Wait: {
                        result += "חכי במשך " + currMil.getDuration() + " שניות" + newLine;
                        break;
                    }
                    case Start: {
                        result += "התחילי טיול" + newLine;
                        break;
                    }
                }

                if (i != milestones.size() -1 && milestones.get(i+1).getAction() == PathAction.Walk)
                {
                    result += format.format(currentTime.getTime()) + ": " + "לכי ל" + milestones.get(i+1).getAddress() + newLine;
                }
                else if (i == milestones.size() - 1)
                {
                    result += format.format(currentTime.getTime()) + ": " + "סיימי טיול";
                }
            }
        }

        return result;
    }
}