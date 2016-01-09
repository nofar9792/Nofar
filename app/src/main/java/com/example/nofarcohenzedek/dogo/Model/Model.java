package com.example.nofarcohenzedek.dogo.Model;

import android.content.Context;

import com.example.nofarcohenzedek.dogo.Model.Parse.ModelParse;
import com.example.nofarcohenzedek.dogo.Model.Sql.ModelSql;

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

    public void updateUser(long id,  String firstName,  String lastName,  String phoneNumber,  String address,  String city) {
        modelParse.updateUser(id, firstName, lastName, phoneNumber, address, city);
    }
    //endregion

    //region Dog Methods
//    public void getDogById(long id, GetDogListener listener) {
//        modelParse.getDogById(id, listener);
//    }

    public void getDogByUserId(long userId, final Model.GetDogListener listener) {
        modelParse.getDogByUserId(userId, listener);
    }

    public void addDog(long userId, Dog dog) {
        modelParse.addDog(userId, dog);
    }

    public void addDog(long userId, String name, final DogSize size, final long age, final String picRef) {
        modelParse.addDog(userId, name, size, age, picRef);
    }

    public void updateDog(long dogId, final String name, final DogSize size, final long age, final String picRef) {
        modelParse.updateDog(dogId, name, size, age, picRef);
    }

//    public void deleteDog(long id) {
//        modelParse.deleteDog(id);
//    }

    //endregion

    //region Dog Walker Methods
    public void getDogWalkerById(long userId, GetDogWalkerListener listener) {
        //modelParse.getDogWalkerById(userId, listener);
        modelParse.getDogWalkerById(userId, listener);
    }

    public void getAllDogWalkers(final Model.GetDogWalkersListener listener) {
        modelParse.getAllDogWalkers(listener);
    }

    public long addDogWalker(String userName, String password, String firstName, String lastName, String phoneNumber,
                             String address, String city, long age, int priceForHour, boolean isComfortableOnMorning, boolean isComfortableOnAfternoon, boolean isComfortableOnEvening) {
        return modelParse.addDogWalker(userName, password, firstName, lastName, phoneNumber, address, city, age, priceForHour, isComfortableOnMorning, isComfortableOnAfternoon, isComfortableOnEvening);
    }
    //endregion

    //region Dog Owner Methods
    public void getDogOwnerById(long userId, GetDogOwnerListener listener) {
        //modelParse.getDogWalkerById(userId, listener);
        modelParse.getDogOwnerById(userId, listener);
    }

    public long addDogOwner(String userName, String password, String firstName, String lastName, String phoneNumber,
                            String address, String city, Dog dog) {
        return modelParse.addDogOwner(userName, password, firstName, lastName, phoneNumber, address, city, dog);
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
    public void getRequestForDogWalker(long dogWalkerId, GetDogOwnersListener listener) {
        modelParse.getRequestForDogWalker(dogWalkerId, listener);
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

//    public interface GetRequestsListener {
//        void onResult(List<Request> requests);
//    }
    //endregion
}

