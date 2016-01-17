package com.example.nofarcohenzedek.dogo.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Environment;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by carmel on 1/13/2016.
 */
public class Utilities
{

    private static String root = Environment.getExternalStorageDirectory().toString();

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

    public static void saveImageOnDevice(String imageName, Bitmap picture)
    {
        if (!imageName.equals("") && picture != null) {
            File myDir = new File(root + "/saved_images");
            myDir.mkdirs();
            File file = new File(myDir, imageName);
            try {

                FileOutputStream out = new FileOutputStream(file);
                picture.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap loadImageFromDevice(String imageName) {
        if (!imageName.equals("")) {
            File myDir = new File(root + "/saved_images");
            File file = new File(myDir, imageName);

            return BitmapFactory.decodeFile(file.getPath());
        }
        else {
            return null;
        }
    }

    public static boolean isFileExistInDevice(String imageName)
    {
        File myDir = new File(root + "/saved_images");
        File file = new File(myDir, imageName);

        return (!imageName.equals("") && file.exists());
    }
}
