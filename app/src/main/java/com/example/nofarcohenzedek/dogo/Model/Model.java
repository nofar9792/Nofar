package com.example.nofarcohenzedek.dogo.Model;

import android.content.Context;

import com.example.nofarcohenzedek.dogo.Model.Parse.ModelParse;
import com.example.nofarcohenzedek.dogo.Model.Sql.ModelSql;

import java.util.Date;
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
    public void logIn(String userName, String password, ModelParse.GetUserListener2 listener){
        modelParse.logIn(userName, password, listener);
    }

    public User getCurrentUser(){
        return modelParse.getCurrentUser();
    }

    public void logOut()
    {
        modelParse.logOut();
    }
    //endregion

    //region Dog Methods
    public void getDogById(long id, GetDogListener listener) {
        modelParse.getDogById(id, listener);
    }

    public void addDog(long userId, String name, final DogSize size, final long age, final String picRef) {
        modelParse.addDog(userId, name, size, age, picRef);
    }

    public void updateDog(long dogId, final String name, final DogSize size, final long age, final String picRef){
        modelParse.updateDog(dogId, name, size, age, picRef);
    }

    public void deleteDog(long id) {
        modelParse.deleteDog(id);
    }

    //endregion

    //region Dog Walker Methods
    public void getDogWalkerById(long userId, GetDogWalkerListener listener){
        //modelParse.getDogWalkerById(userId, listener);
        modelParse.getDogWalkerById2(userId, listener);
    }

    public void getAllDogWalkers(final Model.GetDogWalkersListener listener)
    {
        modelParse.getAllDogWalkers(listener);
    }

    public void addDogWalker(String userName, String password, String firstName, String lastName, String phoneNumber,
                             String address, String city, Boolean isDogWalker, long age, int priceForHour, boolean isComfortableOnMorning, boolean isComfortableOnAfternoon, boolean isComfortableOnEvening) {
        modelParse.addDogWalker(userName, password, firstName, lastName, phoneNumber, address, city, age, priceForHour, isComfortableOnMorning, isComfortableOnAfternoon, isComfortableOnEvening);
    }
    //endregion

    //region Dog Owner Methods
    public void getDogOwnerById(long userId, GetDogOwnerListener listener){
        //modelParse.getDogWalkerById(userId, listener);
        modelParse.getDogOwnerById2(userId, listener);
    }

    public void addDogOwner(String userName, String password, String firstName, String lastName, String phoneNumber,
                            String address, String city, List<Dog> dogs) {
        modelParse.addDogOwner(userName, password, firstName, lastName, phoneNumber, address, city, dogs);
    }
    //endregion

    //region Comment Methods
    public void addCommentToDogWalker(long userId, String text, long rating) {
        modelParse.addCommentToDogWalker(userId, text, rating);
    }
    //endregion

    //region Trip Methods
    public void addTrip(long dogOwnerId, long dogId, long dogWalkerId, Date startOfWalking, Date endOfWalking, Boolean isPaid) {
        modelParse.addTrip(dogOwnerId, dogId, dogWalkerId, startOfWalking, endOfWalking, isPaid);
    }

    public void getTripsByDogOwnerId(long dogOwnerId, final Model.GetTripsListener listener){
        modelParse.getTripsByDogOwnerId(dogOwnerId,listener);
    }

    public void getTripsByDogWalkerId(long dogWalkerId, final Model.GetTripsListener listener) {
        modelParse.getTripsByDogWalkerId(dogWalkerId, listener);
    }
    //endregion

    //region Interfaces
    public interface GetDogListener {
        public void onResult(Dog dog);
    }

    public interface GetDogsListener {
        public void onResult(List<Dog> dogs);
    }

    public interface GetDogWalkerListener {
        void onResult(DogWalker dogWalker);
    }

    public interface GetDogWalkersListener{
        void onResult(List<DogWalker> allDogWalkers);
    }

    public interface GetDogOwnerListener {
        void onResult(DogOwner dogOwner);
    }

    public interface GetCommentsListener {
        void onResult(List<Comment> comments);
    }

    public interface GetTripsListener {
        void onResult(List<Trip> trips);
    }
    //endregion
}

