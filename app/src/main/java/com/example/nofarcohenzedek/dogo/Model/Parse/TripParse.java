package com.example.nofarcohenzedek.dogo.Model.Parse;

import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.Trip;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class TripParse {
    final static String TRIPS_TABLE = "TRIPS";
    final static String TRIP_ID = "tripId";
    final static String DOG_OWNER_ID = "dogOwnerId";
    final static String DOG_WALKER_ID = "dogWalkerId";
    final static String START_OF_WALKING = "startOfWalking";
    final static String END_OF_WALKING = "endOfWalking";
    final static String IS_PAID = "isPaid";

    public static void startTrip(long dogOwnerId, long dogWalkerId, final Model.GetIdListener listener) {
        final long newTripId = getNextId();
        ParseObject newTripParseObject = new ParseObject(TRIPS_TABLE);

        newTripParseObject.put(TRIP_ID, newTripId);
        newTripParseObject.put(DOG_OWNER_ID, dogOwnerId);
        newTripParseObject.put(DOG_WALKER_ID, dogWalkerId);
        newTripParseObject.put(START_OF_WALKING, Calendar.getInstance(TimeZone.getTimeZone("GTM+2")).getTime());
        newTripParseObject.put(IS_PAID, false);

        newTripParseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    listener.onResult(newTripId, true);
                }else {
                    e.printStackTrace();
                    listener.onResult(-1, false);
                }
            }
        });
    }

    public static void endTrip(long tripId, final Model.IsSucceedListener listener) {
        ParseQuery<ParseObject> query = new ParseQuery<>(TRIPS_TABLE);
        query.whereEqualTo(TRIP_ID, tripId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    parseObject.put(END_OF_WALKING, Calendar.getInstance(TimeZone.getTimeZone("GTM+2")).getTime());

                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                listener.onResult(true);
                            }else {
                                e.printStackTrace();
                                listener.onResult(false);
                            }
                        }
                    });
                } else {
                    e.printStackTrace();
                    listener.onResult(false);
                }
            }
        });
    }

    public static void changeTripToPaid(long tripId, final Model.IsSucceedListener listener) {
        ParseQuery<ParseObject> query = new ParseQuery<>(TRIPS_TABLE);
        query.whereEqualTo(TRIP_ID, tripId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    parseObject.put(IS_PAID, true);

                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                listener.onResult(true);
                            }
                            else {
                                e.printStackTrace();
                                listener.onResult(false);
                            }
                        }
                    });
                } else {
                    e.printStackTrace();
                    listener.onResult(false);
                }
            }
        });
    }

    public static void getTripsDetailsByDogOwnerId(final long dogOwnerId, final ModelParse.GetTripsDetailsListener listener) {
        ParseQuery<ParseObject> query = new ParseQuery<>(TRIPS_TABLE);
        query.whereEqualTo(DOG_OWNER_ID, dogOwnerId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                List<Trip> trips = new LinkedList<>();

                if (e == null) {
                    for (ParseObject po : list) {
                        long tripId = po.getLong(TRIP_ID);
                        long dogWalkerId = po.getLong(DOG_WALKER_ID);
                        Date startOfWalking = po.getDate(START_OF_WALKING);
                        Date endOfWalking = po.getDate(END_OF_WALKING);
                        Boolean isPaid = po.getBoolean(IS_PAID);

                        trips.add(new Trip(tripId, dogOwnerId, dogWalkerId, startOfWalking, endOfWalking, isPaid));
                    }
                    listener.onResult(trips);

                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void getTripsDetailsByDogWalkerId(final long dogWalkerId, final ModelParse.GetTripsDetailsListener listener){
        ParseQuery<ParseObject> query = new ParseQuery<>(TRIPS_TABLE);
        query.whereEqualTo(DOG_WALKER_ID, dogWalkerId);
        query.whereExists(END_OF_WALKING);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                List<Trip> trips = new LinkedList<>();

                if (e == null) {
                    for (ParseObject po : list) {
                        long tripId = po.getLong(TRIP_ID);
                        long dogOwnerId = po.getLong(DOG_OWNER_ID);
                        Date startOfWalking = po.getDate(START_OF_WALKING);
                        Date endOfWalking = po.getDate(END_OF_WALKING);
                        Boolean isPaid = po.getBoolean(IS_PAID);

                        trips.add(new Trip(tripId, dogOwnerId, dogWalkerId, startOfWalking, endOfWalking, isPaid));
                    }
                    listener.onResult(trips);

                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private static long getNextId(){
        ParseQuery<ParseObject> query = new ParseQuery<>(TRIPS_TABLE);
        ParseObject parseObject;

        try {
            parseObject = query.addDescendingOrder(TRIP_ID).getFirst();
            return (parseObject.getLong(TRIP_ID) + 1);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return 1;
    }
}
