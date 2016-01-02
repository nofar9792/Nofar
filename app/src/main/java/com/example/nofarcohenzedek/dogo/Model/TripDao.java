package com.example.nofarcohenzedek.dogo.Model;

import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.List;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class TripDao
{
    private static final String TABLE = "TRIP";
    private static final String ID = "ID";
    private static final String OWNER_ID = "OWNER_ID";
    private static final String DOG_ID = "DOG_ID";
    private static final String WALKER_ID = "WALKER_ID";
    private static final String START_OF_WALKING = "START_OF_WALKING";
    private static final String DATE_OF_WALKING = "DATE_OF_WALKING";
    private static final String END_OF_WALKING = "END_OF_WALKING";

    public static void addTrip (Trip trip)
    {

    }

    public static void deleteTrip(Trip trip)
    {

    }

    public static void updateTrip(long id, long ownerId, long dogId, long walkerId, TimePicker startOfWalking,
                                  TimePicker endOfWalking, DatePicker dateOfWalking)
    {

    }

    public static Trip getTripByOwnerId (long ownerId)
    {
        return null;
    }

    public static User getTripByWalkerId(long walkerId)
    {
        return null;
    }

    public static User getTripById(long id)
    {
        return null;
    }

    public static List<Trip> getAllTrips()
    {
        return null;
    }
}
