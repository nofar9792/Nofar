package com.example.nofarcohenzedek.dogo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nofarcohenzedek.dogo.Model.Model;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MapPathActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ProgressBar progressBar;
    private long userId;
    private boolean listFull;

    private List<String> addrToMarkPath;
    private HashMap<Long, String> idsAndNames;
    private List<String> stringIds;

    private PathResponse pathRes;
    private Calendar startTime;
    private SimpleDateFormat format;
    private String homeLocation;
    HashMap<String, String> data;

    // for list
    ListView listView;
    LinkedHashMap<String,String> actionStrings;

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
        listFull = false;

        listView = (ListView) findViewById(R.id.pathActionsList);

        Intent intent = getIntent();
        stringIds = intent.getStringArrayListExtra("ownerIds");
        ArrayList<String> times = intent.getStringArrayListExtra("walkTimes");
        userId = intent.getLongExtra("userId", 0);


        progressBar = (ProgressBar) findViewById(R.id.mapsProgressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
        format = new SimpleDateFormat("HH:mm");

        data = new HashMap<>();
        actionStrings = new LinkedHashMap<>();

        // create data to send algorithm
        data.put("OwnerId", Long.toString(userId));
        for (int i = 0; i < stringIds.size(); i++) {
            data.put("DogWalks[" + i + "].Duration", times.get(i));
            data.put("DogWalks[" + i + "].UserId", stringIds.get(i));
        }

        String res = null;

        try {
            res = new HttpAsyncTask().execute("").get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (res != null) {
            Gson gson = new Gson();
            pathRes = gson.fromJson(res, PathResponse.class);

            // decide which addresses to mark in path
            addrToMarkPath = new ArrayList<String>() {
            };

            for (PathMilestone currMil : pathRes.getPath()) {
                if ((currMil.getAction() == PathAction.Start || currMil.getAction() == PathAction.Walk) && !addrToMarkPath.contains(currMil.getAddress())) {
                    addrToMarkPath.add(currMil.getAddress());
                }

                if (currMil.getAction() == PathAction.Start) {
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
    }

    private void buildActionStringsHashMap() {

        Calendar currentTime = Calendar.getInstance();
        currentTime.setTime(startTime.getTime());


        for (int i=0; i< pathRes.getPath() .size(); i++){
            PathMilestone currMil = pathRes.getPath().get(i);
            String lineResult = "";
            currentTime.add(Calendar.SECOND, currMil.getDuration());

            if (currMil.getAction() != PathAction.Walk && currMil.getAction() != PathAction.Wait) {
                lineResult += format.format(currentTime.getTime()) + ": ";
            }

            switch (currMil.getAction()) {
                case Pickup: {
                    lineResult += "איספי את " + idsAndNames.get(currMil.getOwnerId());
                    actionStrings.put(lineResult,currMil.getAddress());
                    break;
                }
                case Return: {
                    lineResult += "החזירי את " + idsAndNames.get(currMil.getOwnerId());
                    actionStrings.put(lineResult,currMil.getAddress());
                    break;
                }
                case Wait: {
                    Calendar temp = Calendar.getInstance();
                    temp.setTime(currentTime.getTime());
                    temp.add(Calendar.SECOND, -1 * currMil.getDuration());
                    lineResult += format.format(temp.getTime()) + ": " + " טיילי באזור במשך " + currMil.getDuration() / 60 + " דקות" ;
                    actionStrings.put(lineResult,currMil.getAddress());
                    break;
                }
                case Start: {
                    lineResult += "התחילי טיול" ;
                    actionStrings.put(lineResult,currMil.getAddress());
                    break;
                }

            }

            lineResult = "";

            if (i != pathRes.getPath().size() - 1 && pathRes.getPath().get(i + 1).getAction() == PathAction.Walk) {
                lineResult += format.format(currentTime.getTime()) + ": " + "לכי ל" + pathRes.getPath().get(i + 1).getAddress();
                actionStrings.put(lineResult, pathRes.getPath().get(i + 1).getAddress());
            } else if (i == pathRes.getPath().size() - 1) {
                lineResult += format.format(currentTime.getTime()) + ": " + "סיימי טיול";
                actionStrings.put(lineResult,currMil.getAddress());
            }

            lineResult = "";
        }
    }

    public String performPostCall() {
        String requestURL = "http://db.cs.colman.ac.il/DogoServer/api/Paths";
        HashMap<String, String> postDataParams = data;

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
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
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

        try {
            LatLng center = Utilities.getLocationFromAddress(homeLocation, getApplicationContext());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(center));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        final Route route = new Route();

        Model.getInstance().getOwnersIdsHashMapWithDogNames(stringIds, new Model.GetIdsAndDogNamesHashMapListener() {
            @Override
            public void OnResult(HashMap<Long, String> list) {
                if (list != null) {
                    idsAndNames = list;


                    for (int i = 0; i < addrToMarkPath.size(); i++) {
                        String title = buildTitleForMarker(addrToMarkPath.get(i));
                        String snippet = buildSnippetForMarker(addrToMarkPath.get(i));

                        ;
                        if (addrToMarkPath.get(i).equals(homeLocation)) {
                            mMap.addMarker(new MarkerOptions()
                                    .position(Utilities.getLocationFromAddress(addrToMarkPath.get(i), getApplicationContext()))
                                    .title(title).snippet(snippet)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        } else {
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

                    buildActionStringsHashMap();

                    listView.setVisibility(View.VISIBLE);
                    CustomAdapter adapter = new CustomAdapter();
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
            }
        });

    }

    private String buildTitleForMarker(String address) {
        return "מיקום נוכחי: " + address;
    }

    private String buildSnippetForMarker(String address) {
        String newLine = "\n";
        String result = newLine;
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTime(startTime.getTime());

        for (int i = 0; i < pathRes.getPath().size(); i++) {
            PathMilestone currMil = pathRes.getPath().get(i);
            currentTime.add(Calendar.SECOND, currMil.getDuration());

          //  String lineToAdd = "";

            if (currMil.getAddress().equals(address)) {
                if (currMil.getAction() != PathAction.Walk && currMil.getAction() != PathAction.Wait) {
                    result += format.format(currentTime.getTime()) + ": ";
                }

                switch (currMil.getAction()) {
                    case Pickup: {
                        result += "איספי את " + idsAndNames.get(currMil.getOwnerId()) + newLine;
                        break;
                    }
                    case Return: {
                        result += "החזירי את " + idsAndNames.get(currMil.getOwnerId()) + newLine;
                        break;
                    }
                    case Wait: {
                        Calendar temp = Calendar.getInstance();
                        temp.setTime(currentTime.getTime());
                        temp.add(Calendar.SECOND, -1 * currMil.getDuration());
                        result += format.format(temp.getTime()) + ": " + " טיילי באזור במשך " + currMil.getDuration() / 60 + " דקות" + newLine;
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

               // actionStrings.put(lineToAdd,currMil.getAddress());
            }
        }

        return result;
    }




    private class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return actionStrings.size();
        }

        @Override
        public Object getItem(int position) {
            return actionStrings.keySet().toArray()[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {

                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.action_string_row_layout, null);
            }

            final String currAction = actionStrings.keySet().toArray(new String[actionStrings.size()])[position];

            ((TextView)convertView.findViewById(R.id.actionStringText)).setText(currAction);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        LatLng center = Utilities.getLocationFromAddress(actionStrings.get(currAction), getApplicationContext());
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(center));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });

            return convertView;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        client.connect();
        Action viewAction = Action.newAction(Action.TYPE_VIEW, "MapPath Page", Uri.parse("http://host/path"),
                Uri.parse("android-app://com.example.nofarcohenzedek.dogo/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        finish();
    }

    @Override
    public void onStop() {
        super.onStop();

        Action viewAction = Action.newAction(Action.TYPE_VIEW, "MapPath Page", Uri.parse("http://host/path"), Uri.parse("android-app://com.example.nofarcohenzedek.dogo/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return performPostCall();
        }
    }
}