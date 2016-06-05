package com.example.nofarcohenzedek.dogo.Model.Parse;

import com.example.nofarcohenzedek.dogo.Model.Common.RequestConsts;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.Request;
import com.example.nofarcohenzedek.dogo.Model.RequestStatus;
import com.parse.CountCallback;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.LinkedList;
import java.util.List;

public class RequestParse {
    public static void addToRequestTable(long dogOwnerId, long dogWalkerId, RequestStatus requestStatus, final Model.IsSucceedListener listener){
        ParseObject newDogOwnerConnectDogWalkerParseObject = new ParseObject(RequestConsts.REQUESTS_TABLE);

        newDogOwnerConnectDogWalkerParseObject.put(RequestConsts.DOG_OWNER_ID, dogOwnerId);
        newDogOwnerConnectDogWalkerParseObject.put(RequestConsts.DOG_WALKER_ID, dogWalkerId);
        newDogOwnerConnectDogWalkerParseObject.put(RequestConsts.REQUEST_STATUS, requestStatus.name());

        newDogOwnerConnectDogWalkerParseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    listener.onResult(true);
                } else {
                    e.printStackTrace();
                    listener.onResult(false);
                }
            }
        });
    }

    public static void getOwnersIdsConnectedToWalker(long dogWalkerId, final ModelParse.GetIdsListener listener){
        ParseQuery<ParseObject> query = new ParseQuery<>(RequestConsts.REQUESTS_TABLE);
        query.whereEqualTo(RequestConsts.DOG_WALKER_ID, dogWalkerId).whereEqualTo(RequestConsts.REQUEST_STATUS, RequestStatus.Accepted.name());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    List<Long> dogOwnersIds = new LinkedList<>();
                    for (ParseObject po : list) {
                        dogOwnersIds.add(po.getLong(RequestConsts.DOG_OWNER_ID));
                    }
                    listener.onResult(dogOwnersIds);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void getWalkersIdsConnectedToOwner(long dogOwnerId, final ModelParse.GetIdsListener listener){
        ParseQuery<ParseObject> query = new ParseQuery<>(RequestConsts.REQUESTS_TABLE);
        query.whereEqualTo(RequestConsts.DOG_OWNER_ID, dogOwnerId).whereEqualTo(RequestConsts.REQUEST_STATUS, RequestStatus.Accepted.name());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    List<Long> dogWalkersIds = new LinkedList<>();
                    for (ParseObject po : list) {
                        dogWalkersIds.add(po.getLong(RequestConsts.DOG_WALKER_ID));
                    }
                    listener.onResult(dogWalkersIds);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void getRequestsByDogWalkerId(final long dogWalkerId, final Model.GetRequestsListener listener){
        ParseQuery<ParseObject> query = new ParseQuery<>(RequestConsts.REQUESTS_TABLE);
        query.whereEqualTo(RequestConsts.DOG_WALKER_ID, dogWalkerId).whereEqualTo(RequestConsts.REQUEST_STATUS, RequestStatus.Waiting.name());;

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    List<Request> requests = new LinkedList<>();

                    for (ParseObject po : list) {
                        Long dogOwnerId = po.getLong(RequestConsts.DOG_OWNER_ID);
                        Boolean isOwnerAskedWalker = po.getBoolean(RequestConsts.IS_OWNER_ASKED_WALKER);
                        RequestStatus requestStatus = RequestStatus.valueOf(po.getString(RequestConsts.REQUEST_STATUS));
                        requests.add(new Request(dogOwnerId, dogWalkerId,requestStatus, isOwnerAskedWalker));
                    }
                    listener.onResult(requests);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void getRequestsByDogOwnerId(final long dogOwnerId, final Model.GetRequestsListener listener){
        ParseQuery<ParseObject> query = new ParseQuery<>(RequestConsts.REQUESTS_TABLE);
        query.whereEqualTo(RequestConsts.DOG_OWNER_ID, dogOwnerId).whereEqualTo(RequestConsts.REQUEST_STATUS, RequestStatus.Waiting.name());;

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    List<Request> requests = new LinkedList<>();

                    for (ParseObject po : list) {
                        Long dogWalkerId = po.getLong(RequestConsts.DOG_WALKER_ID);
                        Boolean isOwnerAskedWalker = po.getBoolean(RequestConsts.IS_OWNER_ASKED_WALKER);
                        RequestStatus requestStatus = RequestStatus.valueOf(po.getString(RequestConsts.REQUEST_STATUS));
                        requests.add(new Request(dogOwnerId, dogWalkerId, requestStatus, isOwnerAskedWalker));
                    }
                    listener.onResult(requests);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void checkRequestExist(long dogOwnerId, long dogWalkerId, final Model.IsSucceedListener listener) {
        ParseQuery<ParseObject> query = new ParseQuery<>(RequestConsts.REQUESTS_TABLE);
        query.whereEqualTo(RequestConsts.DOG_OWNER_ID, dogOwnerId).whereEqualTo(RequestConsts.DOG_WALKER_ID, dogWalkerId);
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int i, ParseException e) {
                if (e == null) {
                    if(i == 0){
                        listener.onResult(false);
                    }else {
                        listener.onResult(true);
                    }
                } else {
                    e.printStackTrace();
                    listener.onResult(false);
                }
            }
        });
    }

    public static void updateRequest(long dogOwnerId, long dogWalkerId,final RequestStatus requestStatus, final Model.IsSucceedListener listener) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(RequestConsts.REQUESTS_TABLE);
        query.whereEqualTo(RequestConsts.DOG_OWNER_ID, dogOwnerId).whereEqualTo(RequestConsts.DOG_WALKER_ID, dogWalkerId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    parseObject.put(RequestConsts.REQUEST_STATUS, requestStatus.name());

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

    public static void deleteRequest(long dogOwnerId, long dogWalkerId, final Model.IsSucceedListener listener) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(RequestConsts.REQUESTS_TABLE);
        query.whereEqualTo(RequestConsts.DOG_OWNER_ID, dogOwnerId).whereEqualTo(RequestConsts.DOG_WALKER_ID, dogWalkerId);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    parseObject.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                listener.onResult(true);
                            } else {
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
}
