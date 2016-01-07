package com.example.nofarcohenzedek.dogo.Model.Parse;

import com.example.nofarcohenzedek.dogo.Model.RequestStatus;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nofar Cohen Zedek on 07-Jan-16.
 */
public class RequestParse {
    final static String REQUESTS_TABLE = "REQUESTS";
    final static String DOG_OWNER_ID = "dogOwnerId";
    final static String DOG_WALKER_ID = "dogWalkerId";
    final static String REQUEST_STATUS = "requestStatus";

    public static void addToRequestTable(long dogOwnerId, long dogWalkerId, RequestStatus requestStatus){
        ParseObject newDogOwnerConnectDogWalkerParseObject = new ParseObject(REQUESTS_TABLE);

        newDogOwnerConnectDogWalkerParseObject.put(DOG_OWNER_ID, dogOwnerId);
        newDogOwnerConnectDogWalkerParseObject.put(DOG_WALKER_ID, dogWalkerId);
        newDogOwnerConnectDogWalkerParseObject.put(REQUEST_STATUS, requestStatus.name());

        newDogOwnerConnectDogWalkerParseObject.saveInBackground();
    }

    public static void getDogWalkersIdsOfOwner(long dogOwnerId, final ModelParse.GetIds listener){
        ParseQuery<ParseObject> query = new ParseQuery<>(REQUESTS_TABLE);
        query.whereEqualTo(DOG_OWNER_ID, dogOwnerId).whereEqualTo(REQUEST_STATUS, RequestStatus.Accepted.name());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    List<Long> dogWalkersIds = new LinkedList<>();
                    for (ParseObject po : list) {
                        dogWalkersIds.add(po.getLong(DOG_WALKER_ID));
                    }
                    listener.onResult(dogWalkersIds);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void getDogOwnersIdsOfWalker(long dogWalkerId, final ModelParse.GetIds listener){
        ParseQuery<ParseObject> query = new ParseQuery<>(REQUESTS_TABLE);
        query.whereEqualTo(DOG_WALKER_ID, dogWalkerId).whereEqualTo(REQUEST_STATUS, RequestStatus.Accepted.name());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    List<Long> dogOwnersIds = new LinkedList<>();
                    for (ParseObject po : list) {
                        dogOwnersIds.add(po.getLong(DOG_OWNER_ID));
                    }
                    listener.onResult(dogOwnersIds);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void updateRequest(long dogOwnerId, long dogWalkerId,final RequestStatus requestStatus) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(REQUESTS_TABLE);
        query.whereEqualTo(DOG_OWNER_ID, dogOwnerId).whereEqualTo(DOG_WALKER_ID,dogWalkerId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    parseObject.put(REQUEST_STATUS, requestStatus.name());

                    parseObject.saveInBackground();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
