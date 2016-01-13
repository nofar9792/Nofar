package com.example.nofarcohenzedek.dogo.Model;

import android.content.Context;

import com.example.nofarcohenzedek.dogo.Model.Parse.ModelParse;
import com.example.nofarcohenzedek.dogo.Model.Sql.ModelSql;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class Model {
    //region Private Members
    private ModelSql modelSql;
    private ModelParse modelParse;
    private static final Model instance = new Model();
    //endregion

    //region Singletone Methods
    private Model() {
    }

    public void init(Context context) {
        modelSql = new ModelSql(context);
        modelParse = new ModelParse(context);
    }

    public static Model getInstance() {
        return instance;
    }
    //endregion

    //region User Methods
    public void logIn(String userName, String password, GetUserListener listener) {
        modelParse.logIn(userName, password, listener);
    }

    public void getCurrentUser(GetUserListener listener) {
        modelParse.getCurrentUser(listener);
    }

    public void logOut() {
        modelParse.logOut();
    }

    //endregion

    //region Dog Walker Methods
    public void getDogWalkerById(long userId, GetDogWalkerListener listener) {
        modelParse.getDogWalkerById(userId, listener);
    }

    public void getAllDogWalkers(final Model.GetDogWalkersListener listener) {
        final List<DogWalker> dogWalkersResult = modelSql.getAllDogWalkers();
        String lastUpdateDate = modelSql.getDogWalkersLastUpdateDate();

        modelParse.getAllDogWalkers(lastUpdateDate, new GetDogWalkersListener() {
            @Override
            public void onResult(List<DogWalker> dogWalkers) {
                if (dogWalkers.size() > 0) {
                    for (DogWalker dogWalker : dogWalkers) {
                        modelSql.addDogWalker(dogWalker);
                    }

                    dogWalkersResult.removeAll(dogWalkersResult);
                    dogWalkersResult.addAll(modelSql.getAllDogWalkers());
                }
                modelSql.setDogWalkersLastUpdateDate(Calendar.getInstance().getTime());
                listener.onResult(dogWalkersResult);
            }
        });
    }

    public long addDogWalker(String userName, String password, String firstName, String lastName, String phoneNumber,
                             String address, String city, long age, int priceForHour, boolean isComfortableOnMorning, boolean isComfortableOnAfternoon, boolean isComfortableOnEvening) throws Exception {
        return modelParse.addDogWalker(userName, password, firstName, lastName, phoneNumber, address, city, age, priceForHour, isComfortableOnMorning, isComfortableOnAfternoon, isComfortableOnEvening);
    }

    public void updateDogWalker(DogWalker dogWalker){
        modelParse.updateDogWalker(dogWalker);
    }

    //endregion

    //region Dog Owner Methods
    public void getDogOwnerById(long userId, GetDogOwnerListener listener) {
        //modelParse.getDogWalkerById(userId, listener);
        modelParse.getDogOwnerById(userId, listener);
    }

    public long addDogOwner(String userName, String password, String firstName, String lastName, String phoneNumber,
                            String address, String city, Dog dog) throws Exception {
        return modelParse.addDogOwner(userName, password, firstName, lastName, phoneNumber, address, city, dog);
    }

    public void updateDogOwner(DogOwner dogOwner){
        modelParse.updateDogOwner(dogOwner);
    }
    //endregion

    //region Comment Methods
    public void addCommentToDogWalker(long userId, String text, long rating) {
        modelParse.addCommentToDogWalker(userId, text, rating);
    }
    //endregion

    //region Trip Methods
    public void getTripsByDogOwnerId(long dogOwnerId, final Model.GetTripsListener listener) {
        modelParse.getTripsByDogOwnerId(dogOwnerId, listener);
    }

    public void getTripsByDogWalkerId(long dogWalkerId, final Model.GetTripsListener listener) {
        modelParse.getTripsByDogWalkerId(dogWalkerId, listener);
    }

//    public void addTrip(long dogOwnerId, long dogId, long dogWalkerId, Date startOfWalking, Date endOfWalking, Boolean isPaid) {
//        modelParse.addTrip(dogOwnerId, dogId, dogWalkerId, startOfWalking, endOfWalking, isPaid);
//    }

    public long startTrip(long dogOwnerId, long dogWalkerId) {
        return modelParse.startTrip(dogOwnerId, dogWalkerId);
    }

    public void endTrip(long tripId) {
        modelParse.endTrip(tripId);
    }

    public void payTrip(long tripId) {
        modelParse.payTrip(tripId);
    }

    //endregion

    //region Request Methods
    public void addRequest(long dogOwnerId, long dogWalkerId) {
        modelParse.addRequest(dogOwnerId, dogWalkerId);
    }

    public void acceptRequest(long dogOwnerId, long dogWalkerId) {
        modelParse.acceptRequest(dogOwnerId, dogWalkerId);
    }

    public void declineRequest(long dogOwnerId, long dogWalkerId) {
        modelParse.declineRequest(dogOwnerId, dogWalkerId);
    }

    public void getWalkersConnectToOwner(long dogOwnerId, GetDogWalkersListener listener) {
        modelParse.getWalkersConnectToOwner(dogOwnerId, listener);
    }

    public void getOwnersConnectToWalker(long dogWalkerId, GetDogOwnersListener listener) {
        modelParse.getOwnersConnectToWalker(dogWalkerId, listener);
    }

    // Messages for dog walker
    public void getRequestForDogWalker(final long dogWalkerId, final GetDogOwnersListener listener) {
        final List<DogOwner> dogOwnersResult = modelSql.getRequestForDogWalker(dogWalkerId);
        final String lastUpdateDate = modelSql.getRequestsLastUpdateDate();

        modelParse.getRequestByDogWalker(dogWalkerId, lastUpdateDate, new GetRequestsListener() {
            @Override
            public void onResult(List<Request> requests) {
                if (requests.size() > 0) {
                    for (Request request : requests) {
                        modelSql.addDogOwner(request.getDogOwner());
                        modelSql.addRequest(request);
                    }

                    dogOwnersResult.removeAll(dogOwnersResult);
                    dogOwnersResult.addAll(modelSql.getRequestForDogWalker(dogWalkerId));
                }
                modelSql.setRequestsLastUpdateDate(Calendar.getInstance().getTime());
                listener.onResult(dogOwnersResult);
            }
        });
    }

    // Messages of dog owner
    public void getRequestOfDogOwner(long dogOwnerId, GetDogWalkersListener listener) {
        modelParse.getRequestOfDogOwner(dogOwnerId, listener);
    }
    //endregion

    //region Interfaces
    public interface GetUserListener {
        void onResult(User user);
    }

    public interface GetDogListener {
        void onResult(Dog dog);
    }

//    public interface GetDogsListener {
//        void onResult(List<Dog> dogs);
//    }

    public interface GetDogWalkerListener {
        void onResult(DogWalker dogWalker);
    }

    public interface GetDogWalkersListener {
        void onResult(List<DogWalker> dogWalkers);
    }

    public interface GetDogOwnerListener {
        void onResult(DogOwner dogOwner);
    }

    public interface GetDogOwnersListener {
        void onResult(List<DogOwner> dogOwners);
    }

    public interface GetCommentsListener {
        void onResult(List<Comment> comments);
    }

    public interface GetTripsListener {
        void onResult(List<Trip> trips);
    }

    public interface GetRequestsListener {
        void onResult(List<Request> requests);
    }
    //endregion
}

