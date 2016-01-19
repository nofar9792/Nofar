package com.example.nofarcohenzedek.dogo.Model.Parse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.nofarcohenzedek.dogo.Model.Model;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

/**
 * Created by gila on 16/01/2016.
 */
public class ImageParse {
    final static String IMAGES_TABLE = "IMAGES";
    final static String IMAGE_NAME = "imageName";
    final static String FILE = "file";

    public static void addToImagesTable(String imageName, Bitmap picture, final Model.IsSucceedListener listener) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        picture.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] picBytes = stream.toByteArray();

        ParseFile imageParse = new ParseFile(picBytes, imageName);

        try {
            imageParse.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ParseObject parseObject = new ParseObject(IMAGES_TABLE);
        parseObject.put(IMAGE_NAME, imageName);
        parseObject.put(FILE, imageParse);

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
    }

    public static void getImage(String imageName, final Model.GetBitmapListener listener) {
        ParseQuery<ParseObject> query = new ParseQuery<>(IMAGES_TABLE);

        query.whereEqualTo(IMAGE_NAME, imageName);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                // Check if this picture exist on DB
                if (parseObject != null) {
                    try {
                        ParseFile imageParse = parseObject.getParseFile(FILE);
                        byte[] picBytes = imageParse.getData();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(picBytes, 0, picBytes.length);

                        listener.onResult(bitmap);
                    }catch (ParseException ex) {
                        ex.printStackTrace();
                        listener.onResult(null);
                    }
                }
                else {
                    listener.onResult(null);
                }
            }
        });
    }
}