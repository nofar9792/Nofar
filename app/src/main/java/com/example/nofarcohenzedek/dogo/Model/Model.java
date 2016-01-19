package com.example.nofarcohenzedek.dogo.Model;

import android.content.Context;
import android.graphics.Bitmap;

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
        if(modelSql == null){
            modelSql = new ModelSql(context);
        }
        if(modelParse == null){
            modelParse = new ModelParse(context);
        }
    }

    public static Model getInstance() {
        return instance;
    }
    //endregion

    //region User Methods
    public void logIn(String userName, String password, GetUserListener listener) {
        modelParse.logIn(userName, password, listener);
    }

    public void logOut() {
        modelParse.logOut();
    }

    public void getUserById(long userId, final Model.GetUserListener listener) {
        modelParse.getUserById(userId, listener);
    }
    //endregion

    //region Dog Walker Methods
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

    public void addDogWalker(String userName, String password, String firstName, String lastName, String phoneNumber,
                             String address, String city, long age, int priceForHour, boolean isComfortableOnMorning, boolean isComfortableOnAfternoon, boolean isComfortableOnEvening, GetIdListener listener) throws Exception {
        modelParse.addDogWalker(userName, password, firstName, lastName, phoneNumber, address, city, age, priceForHour, isComfortableOnMorning, isComfortableOnAfternoon, isComfortableOnEvening, listener);
    }

    public void updateDogWalker(DogWalker dogWalker, IsSucceedListener listener){
        modelParse.updateDogWalker(dogWalker, listener);
    }
    //endregion

    //region Dog Owner Methods
    public void addDogOwner(String userName, String password, String firstName, String lastName, String phoneNumber,
                            String address, String city, Dog dog, GetIdListener listener) throws Exception {
        modelParse.addDogOwner(userName, password, firstName, lastName, phoneNumber, address, city, dog, listener);
    }

    public void updateDogOwner(DogOwner dogOwner, IsSucceedListener listener){
        modelParse.updateDogOwner(dogOwner, listener);
    }
    //endregion

    //region Trip Methods
    public void getTripsByDogOwnerId(long dogOwnerId, final GetTripsListener listener) {
        modelParse.getTripsByDogOwnerId(dogOwnerId, listener);
    }

    public void getTripsByDogWalkerId(long dogWalkerId, final GetTripsListener listener) {
        modelParse.getTripsByDogWalkerId(dogWalkerId, listener);
    }

    public void startTrip(long dogOwnerId, long dogWalkerId, GetIdListener listener) {
        modelParse.startTrip(dogOwnerId, dogWalkerId, listener);
    }

    public void endTrip(long tripId, IsSucceedListener listener) {
        modelParse.endTrip(tripId, listener);
    }

    public void payTrip(long tripId, IsSucceedListener listener) {
        modelParse.payTrip(tripId, listener);
    }
    //endregion

    //region Request Methods
    public void addRequest(long dogOwnerId, long dogWalkerId, IsSucceedListener listener) {
        modelParse.addRequest(dogOwnerId, dogWalkerId, listener);
    }

    public void acceptRequest(long dogOwnerId, long dogWalkerId, IsSucceedListener listener) {
        modelParse.acceptRequest(dogOwnerId, dogWalkerId, listener);
    }

    public void declineRequest(long dogOwnerId, long dogWalkerId, IsSucceedListener listener) {
        modelParse.declineRequest(dogOwnerId, dogWalkerId, listener);
    }

    // Connections between walker to some owners
    public void getOwnersConnectToWalker(final long dogWalkerId, final GetDogOwnersListener listener) {
        final List<DogOwner> dogOwnersResult = modelSql.getOwnersConnectToWalker(dogWalkerId);
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
                    dogOwnersResult.addAll(modelSql.getOwnersConnectToWalker(dogWalkerId));
                }
                modelSql.setRequestsLastUpdateDate(Calendar.getInstance().getTime());
                listener.onResult(dogOwnersResult);
            }
        });
    }

    // Messages for dog walker
    public void getRequestForDogWalker(final long dogWalkerId, final GetDogOwnersListener listener) {
        final List<DogOwner> dogOwnersResult = modelSql.getRequestForDogWalker(dogWalkerId);
        final String lastUpdateDate = modelSql.getRequestsLastUpdateDate();

        modelParse.getRequestByDogWalker(dogWalkerId, null, new GetRequestsListener() {
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
    public void getRequestOfDogOwner(final long dogOwnerId, final GetDogWalkersListener listener) {
        final List<DogWalker> dogWalkersResult = modelSql.getRequestForDogOwner(dogOwnerId);
        final String lastUpdateDate = modelSql.getRequestsLastUpdateDate();

        modelParse.getRequestByDogOwner(dogOwnerId, null, new GetRequestsListener() {
            @Override
            public void onResult(List<Request> requests) {
                if (requests.size() > 0) {
                    for (Request request : requests) {
                        modelSql.addDogWalker(request.getDogWalker());
                        modelSql.addRequest(request);
                    }

                    dogWalkersResult.removeAll(dogWalkersResult);
                    dogWalkersResult.addAll(modelSql.getRequestForDogOwner(dogOwnerId));
                }
                modelSql.setRequestsLastUpdateDate(Calendar.getInstance().getTime());
                listener.onResult(dogWalkersResult);
            }
        });
    }
    //endregion

    //region Image Methods
    public void saveImage(String imageName, Bitmap picture, IsSucceedListener listener){
        modelParse.saveImage(imageName, picture, listener);
    }

    public void getImage(String imageName, Model.GetBitmapListener listener){
        modelParse.getImage(imageName, listener);
    }
    //endregion

    //region Interfaces
    public interface GetUserListener {
        void onResult(User user);
    }

    public interface GetDogListener {
        void onResult(Dog dog);
    }
    public interface GetDogWalkersListener {
        void onResult(List<DogWalker> dogWalkers);
    }

    public interface GetDogOwnersListener {
        void onResult(List<DogOwner> dogOwners);
    }

    public interface GetTripsListener {
        void onResult(List<Trip> trips);
    }

    public interface GetRequestsListener {
        void onResult(List<Request> requests);
    }

    public interface GetBitmapListener {
        void onResult(Bitmap picture);
    }

    public interface IsSucceedListener {
        void onResult(boolean isSucceed);
    }

    public interface GetIdListener {
        void onResult(long id, boolean isSucceed);
    }

    //endregion
}

