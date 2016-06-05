package com.example.nofarcohenzedek.dogo;

import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nofarcohenzedek.dogo.Model.PathAction;
import com.example.nofarcohenzedek.dogo.Model.PathMilestone;
import com.example.nofarcohenzedek.dogo.Model.PathResponse;
import com.example.nofarcohenzedek.dogo.Model.Route;
import com.example.nofarcohenzedek.dogo.Model.Utilities;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MapPathActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ProgressBar progressBar;

    private List<String> addrToMarkPath;

    private List<String> tempAddrList;
    private PathResponse pathRes;
    private Calendar startTime;
    private SimpleDateFormat format;
    private String homeLocation;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_path);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        progressBar = (ProgressBar) findViewById(R.id.mapsProgressBar);
        format = new SimpleDateFormat("HH:mm");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HashMap<String,String> data = new HashMap<>();
        data.put("OwnerId", "0");
        data.put("DogWalks[0].Duration", "45");
        data.put("DogWalks[0].UserId", "1");
        data.put("DogWalks[1].Duration", "45");
        data.put("DogWalks[1].UserId", "2");
        String res = performPostCall("http://db.cs.colman.ac.il/DogoServer/api/Paths", data);
        Gson gson = new Gson();
        pathRes = gson.fromJson(res,PathResponse.class);

        // decide which addresses to mark in path
        addrToMarkPath = new ArrayList<String>() {
        };
        for (PathMilestone currMil : pathRes.getPath()) {
            if ((currMil.getAction() == PathAction.Start || currMil.getAction() == PathAction.Walk) && !addrToMarkPath.contains(currMil.getAddress())) {
                addrToMarkPath.add(currMil.getAddress());
            }

            if(currMil.getAction() == PathAction.Start)
            {
                homeLocation = currMil.getAddress();
            }

        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            Date date = sdf.parse(pathRes.getStartTime());
            startTime = GregorianCalendar.getInstance(); // creates a new calendar instance
            startTime.setTime(date);   // assigns calendar to given date
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    public String  performPostCall(String requestURL,
                                   HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
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

        LatLng center = Utilities.getLocationFromAddress(pathRes.getPath().get(0).getAddress(), getApplicationContext());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(center));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        Route route = new Route();

        for (int i = 0; i < addrToMarkPath.size(); i++) {
            String title = buildTitleForMarker(addrToMarkPath.get(i));
            String snippet = buildSnippetForMarker(addrToMarkPath.get(i));

            ;
            if (addrToMarkPath.get(i).equals(homeLocation)){
                mMap.addMarker(new MarkerOptions()
                        .position(Utilities.getLocationFromAddress(addrToMarkPath.get(i), getApplicationContext()))
                        .title(title).snippet(snippet)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            }
            else
            {
                mMap.addMarker(new MarkerOptions()
                        .position(Utilities.getLocationFromAddress(addrToMarkPath.get(i), getApplicationContext()))
                        .title(title).snippet(snippet));
            }


            if (i != addrToMarkPath.size() - 1) {
                route.drawRoute(mMap, getApplicationContext(),
                        Utilities.getLocationFromAddress(addrToMarkPath.get(i), getApplicationContext()),
                        Utilities.getLocationFromAddress(addrToMarkPath.get(i + 1), getApplicationContext()),
                        false, "en");
            }
        }
    }

    private String buildTitleForMarker(String address) {
        return "מיקום נוכחי: " + address;
    }

    private String buildSnippetForMarker(String address) {
        // TODO see what about the last milestone - if i have my home again or not (although if the first milestone is my home)

        String newLine = "\n";
        String result = newLine;
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTime(startTime.getTime());

        for (int i = 0; i < pathRes.getPath().size(); i++) {
            PathMilestone currMil = pathRes.getPath().get(i);
            currentTime.add(Calendar.SECOND, currMil.getDuration());

            if (currMil.getAddress().equals(address)) {
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

                if (i != pathRes.getPath().size() - 1 && pathRes.getPath().get(i + 1).getAction() == PathAction.Walk) {
                    result += format.format(currentTime.getTime()) + ": " + "לכי ל" + pathRes.getPath().get(i + 1).getAddress() + newLine;
                } else if (i == pathRes.getPath().size() - 1) {
                    result += format.format(currentTime.getTime()) + ": " + "סיימי טיול";
                }
            }
        }

        return result;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MapPath Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.nofarcohenzedek.dogo/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MapPath Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.nofarcohenzedek.dogo/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}