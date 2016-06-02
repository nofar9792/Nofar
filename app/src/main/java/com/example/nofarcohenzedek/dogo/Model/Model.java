package com.example.nofarcohenzedek.dogo.Model;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.nofarcohenzedek.dogo.Model.Parse.ModelParse;

import java.util.List;

public class Model {
    //region Private Members
    private ModelParse modelParse;
    private static final Model instance = new Model();
    //endregion

    //region Singletone Methods
    private Model() {
    }

    public void init(Context context) {
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
        modelParse.getAllDogWalkers(null,listener);
    }

    public void addDogWalker(String userName, String password, String firstName, String lastName, String phoneNumber,
                             String address, String city, long age, int priceForHour, boolean isComfortable6To8, boolean isComfortable8To10, boolean isComfortable10To12,
                             boolean isComfortable12To14, boolean isComfortable14To16, boolean isComfortable16To18, boolean isComfortable18To20,
                             boolean isComfortable20To22, GetIdListener listener,  ExceptionListener exceptionListener){
        modelParse.addDogWalker(userName, password, firstName, lastName, phoneNumber, address, city, age, priceForHour,
                                isComfortable6To8,  isComfortable8To10,  isComfortable10To12, isComfortable12To14,
                                isComfortable14To16,  isComfortable16To18,  isComfortable18To20, isComfortable20To22,
                                listener, exceptionListener);
    }

    public void updateDogWalker(DogWalker dogWalker, IsSucceedListener listener){
        modelParse.updateDogWalker(dogWalker, listener);
    }
    //endregion

    //region Dog Owner Methods
    public void addDogOwner(String userName, String password, String firstName, String lastName, String phoneNumber,
                            String address, String city, Dog dog, boolean isComfortable6To8, boolean isComfortable8To10, boolean isComfortable10To12,
                            boolean isComfortable12To14, boolean isComfortable14To16, boolean isComfortable16To18, boolean isComfortable18To20,
                            boolean isComfortable20To22, GetIdListener listener, ExceptionListener exceptionListener) {
        modelParse.addDogOwner(userName, password, firstName, lastName, phoneNumber, address, city, dog,
                                isComfortable6To8,  isComfortable8To10,  isComfortable10To12, isComfortable12To14,
                                isComfortable14To16,  isComfortable16To18,  isComfortable18To20, isComfortable20To22,
                                listener, exceptionListener);
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

    public void deleteTrip(long tripId, IsSucceedListener listener){
        modelParse.deleteTrip(tripId,listener);
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
        modelParse.getOwnersConnectToWalker(dogWalkerId, listener);
    }

    // Messages for dog walker
    public void getRequestForDogWalker(final long dogWalkerId, final GetDogOwnersListener listener) {
        modelParse.getRequestForDogWalker(dogWalkerId, listener);
    }

    // Messages of dog owner
    public void getRequestOfDogOwner(final long dogOwnerId, final GetDogWalkersListener listener) {
        modelParse.getRequestOfDogOwner(dogOwnerId, listener);
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

    public interface ExceptionListener {
        void onResult(String message);
    }

    //endregion
}

