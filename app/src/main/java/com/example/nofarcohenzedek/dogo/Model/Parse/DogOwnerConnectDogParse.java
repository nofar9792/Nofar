package com.example.nofarcohenzedek.dogo.Model.Parse;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class DogOwnerConnectDogParse {
    final static String DOG_OWNER_CONNECT_DOG_TABLE = "DOG_OWNER_CONNECT_DOG";
    final static String USER_ID = "userId";
    final static String DOG_ID = "dogId";

    public static void addToDogOwnersConnectDogsTable(long userId, long dogId){
        ParseObject newDogOwnerConnectDogParseObject = new ParseObject(DOG_OWNER_CONNECT_DOG_TABLE);

        newDogOwnerConnectDogParseObject.put(USER_ID, userId);
        newDogOwnerConnectDogParseObject.put(DOG_ID, dogId);

        newDogOwnerConnectDogParseObject.saveInBackground();
    }

    public static void getDogIdsOfOwner(long userId, final ModelParse.GetIds listener){
        ParseQuery<ParseObject> query = new ParseQuery<>(DOG_OWNER_CONNECT_DOG_TABLE);
        query.whereEqualTo(USER_ID, userId);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    List<Long> dogIds = new LinkedList<>();
                    for (ParseObject po : list) {
                        dogIds.add(po.getLong(DOG_ID));
                    }
                    listener.onResult(dogIds);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
