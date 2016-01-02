package com.example.nofarcohenzedek.dogo.Model;

import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class ModelParse implements Model.ModelInterface{
    private static final String DOG = "DOG";

    public void init(Context context) {
        Parse.initialize(context, "ClCDxIalYQPR6IrVXUHtHQW99tazxTZOAFUnanLB", "8EqrsIlUMqsfx8yxs9FjUndl0AO58cDHIJFp6wyY");
    }

    @Override
    public void addDog(long userId, Dog dog) {
        ParseObject newDogParseObject = new ParseObject(DOG);
        newDogParseObject.put("userId", userId);
        newDogParseObject.put("dogId", dog.getId());
        newDogParseObject.put("name", dog.getName());
        newDogParseObject.put("size", dog.getSize());
        newDogParseObject.put("age", dog.getAge());
        newDogParseObject.put("picRef", dog.getPicRef());

        try {
            newDogParseObject.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getDogById(long id,final Model.GetDogListener listener) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(DOG);
        query.whereEqualTo("id", id);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                Dog dog = null;
                if (e == null && list.size() > 0) {
                    ParseObject po = list.get(0);
                    long id = Long.parseLong(po.getString("id"));
                    String name = po.getString("name");
                    DogSize dogSize = DogSize.valueOf(po.getString("dogSize"));
                    long age = Long.parseLong(po.getString("age"));
                    String picRef = po.getString("picRef");
                    dog = new Dog(id, name, dogSize, age, picRef);
                }
                listener.onResult(dog);
            }
        });

        //return null;
    }

    @Override
    public List<Dog> getAllDogsOfOwner(long userId) {

        return null;
    }

    @Override
    public void updateDog(long Id, String Name, String Size, long Age, String PicRef, long OwnerId) {

    }

    @Override
    public void deleteDog(Dog dog) {

    }

    @Override
    public void addUser(User user) {

    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public void updateUser(long Id, long userId, List<Long> MyDogsId, String UserName, String Password, long PriceForHour, String Comments, boolean IsOwner, boolean IsComfortableOnMorning, boolean IsComfortableOnAfternoon, boolean IsComfortableOnEvening, List<String> Reviews, List<Long> Rating) {

    }

    @Override
    public User getUserByUserNameAndPassword(String UserName, String password) {
        return null;
    }

    @Override
    public User getUserById(long userId) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public void addTrip(Trip trip) {

    }

    @Override
    public void deleteTrip(Trip trip) {

    }

    @Override
    public void updateTrip(long Id, long OwnerId, long DogId, long WalkerId, TimePicker StartOfWalking, TimePicker EndOfWalking, DatePicker DateOfWalking) {

    }

    @Override
    public Trip getTripByOwnerId(long ownerId) {
        return null;
    }

    @Override
    public User getTripByWalkerId(long walkerId) {
        return null;
    }

    @Override
    public User getTripById(long id) {
        return null;
    }

    @Override
    public List<Trip> getAllTrips() {
        return null;
    }
}
