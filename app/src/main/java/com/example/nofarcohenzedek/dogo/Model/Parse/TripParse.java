package com.example.nofarcohenzedek.dogo.Model.Parse;

import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.nofarcohenzedek.dogo.Model.Dog;
import com.example.nofarcohenzedek.dogo.Model.Trip;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class TripParse {
    final static String TRIPS_TABLE = "TRIPS";
    final static String DOG_OWNER_ID = "dogOwnerId";
    final static String DOG_ID = "dogId";
    final static String DOG_WALKER_ID = "dogWalkerId";
    final static String START_OF_WALKING = "startOfWalking";
    final static String END_OF_WALKING = "endOfWalking";
    final static String DATE_OF_WALKING = "dateOfWalking";

    public static void addToTripsTable(long dogOwnerId, long dogId, long dogWalkerId, Date startOfWalking, Date endOfWalking, Date dateOfWalking) {
        ParseObject newDogParseObject = new ParseObject(TRIPS_TABLE);

        newDogParseObject.put(DOG_OWNER_ID, dogOwnerId);
        newDogParseObject.put(DOG_ID, dogId);
        newDogParseObject.put(DOG_WALKER_ID, dogWalkerId);
        newDogParseObject.put(START_OF_WALKING, startOfWalking);
        newDogParseObject.put(END_OF_WALKING, endOfWalking);
        newDogParseObject.put(DATE_OF_WALKING, dateOfWalking);

        newDogParseObject.saveInBackground();
    }

    public static void getTripsDetailsByDogOwnerId(final long dogOwnerId, final ModelParse.GetTripsDetailsListener listener) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(TRIPS_TABLE);
        query.whereEqualTo(DOG_OWNER_ID, dogOwnerId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                List<Trip> trips = new LinkedList<Trip>();

                if (e == null) {
                    for (ParseObject po : list) {
                        Dog dog = null;

                        long dogId = po.getLong(DOG_ID);
                        long dogWalkerId = po.getLong(DOG_WALKER_ID);
                        Date startOfWalking = po.getDate(START_OF_WALKING);
                        Date endOfWalking = po.getDate(END_OF_WALKING);
                        Date dateOfWalking = po.getDate(DATE_OF_WALKING);
                        trips.add(new Trip(dogOwnerId, dogId, dogWalkerId, startOfWalking, endOfWalking, dateOfWalking));
                    }
                    listener.onResult(trips);

                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void getTripsDetailsByDogWalkerId(final long dogWalkerId, final ModelParse.GetTripsDetailsListener listener){
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(TRIPS_TABLE);
        query.whereEqualTo(DOG_WALKER_ID, dogWalkerId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                List<Trip> trips = new LinkedList<Trip>();

                if (e == null) {
                    for (ParseObject po : list) {
                        Dog dog = null;

                        long dogOwnerId = po.getLong(DOG_OWNER_ID);
                        long dogId = po.getLong(DOG_ID);
                        Date startOfWalking = po.getDate(START_OF_WALKING);
                        Date endOfWalking = po.getDate(END_OF_WALKING);
                        Date dateOfWalking = po.getDate(DATE_OF_WALKING);
                        trips.add(new Trip(dogOwnerId, dogId, dogWalkerId, startOfWalking, endOfWalking, dateOfWalking));
                    }
                    listener.onResult(trips);

                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
