package com.example.nofarcohenzedek.dogo.Model;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by carmel on 1/13/2016.
 */
public class Utilities
{
    public static LatLng getLocationFromAddress(String address, Context context)
    {
        Geocoder coder = new Geocoder(context);
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
