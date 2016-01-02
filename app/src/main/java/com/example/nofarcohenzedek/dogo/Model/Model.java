package com.example.nofarcohenzedek.dogo.Model;

import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.List;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class Model {
    public interface GetDogListener{
        public void onResult(Dog dog);
    }

    interface ModelInterface {
        // Dog Methods
        public void addDog(long userId, Dog dog);

        public void getDogById(long id, GetDogListener listener);

        public List<Dog> getAllDogsOfOwner(long userId);

        public void updateDog(long Id, String Name, String Size, long Age, String PicRef, long OwnerId);

        public void deleteDog(Dog dog);

        // Person methods
//        public void addPerson(Person person);
//
//        public void deletePerson(Person person);
//
//        public void updatePerson(long Id, String FirstName, String LastName, String PhoneNumber,
//                                 String Address, String City, long Age);
//
//        public Person getPersonById(long userId);
//
//        public List<Person> getAllPerson();

        // User methods
        public void addUser(User user);

        public void deleteUser(User user);

        public void updateUser(long Id, long userId, List<Long> MyDogsId, String UserName, String Password, long PriceForHour,
                               String Comments, boolean IsOwner, boolean IsComfortableOnMorning, boolean IsComfortableOnAfternoon,
                               boolean IsComfortableOnEvening, List<String> Reviews, List<Long> Rating);

        public User getUserByUserNameAndPassword(String UserName, String password);

        public User getUserById(long userId);

        public List<User> getAllUsers();

        // Trip methods
        public void addTrip(Trip trip);

        public void deleteTrip(Trip trip);

        public void updateTrip(long Id, long OwnerId, long DogId, long WalkerId, TimePicker StartOfWalking,
                               TimePicker EndOfWalking, DatePicker DateOfWalking);

        public Trip getTripByOwnerId(long ownerId);

        public User getTripByWalkerId(long walkerId);

        public User getTripById(long id);

        public List<Trip> getAllTrips();
    }

    private static final Model instance = new Model();
    private ModelInterface modelImpl;

    private Model() {

    }

    public void init(Context applicationContext) {
        modelImpl = new ModelSql(applicationContext);
    }

    public static Model instance() {
        return instance;
    }

    // Dog Methods
    public void addDog(long userId, Dog dog) {
        modelImpl.addDog(userId, dog);
    }

    public void getDogById(long id, GetDogListener listener) {
        //  return modelImpl.getDogById(id, listener);
    }

    public List<Dog> getAllDogsOfOwner(long userId)
    {
        return modelImpl.getAllDogsOfOwner(userId);
    }
    public void updateDog(long Id, String Name, String Size, long Age, String PicRef, long OwnerId)
    {
        modelImpl.updateDog(Id, Name, Size, Age, PicRef, OwnerId);
    }
    public void deleteDog(Dog dog)
    {
        modelImpl.deleteDog(dog);
    }

//    // Person methods
//    public  void addPerson (Person person)
//    {
//        modelImpl.addPerson(person);
//    }
//    public  void deletePerson (Person person)
//    {
//        modelImpl.deletePerson(person);
//    }
//    public  void updatePerson(long Id, String FirstName, String LastName, String PhoneNumber,
//                              String Address, String City, long Age)
//    {
//        modelImpl.updatePerson(Id, FirstName, LastName, PhoneNumber, Address, City, Age);
//    }
//    public  Person getPersonById (long userId)
//    {
//        return modelImpl.getPersonById(userId);
//    }
//    public  List<Person> getAllPerson()
//    {
//        return modelImpl.getAllPerson();
//    }

    // User methods
    public  void addUser (User user)
    {
        modelImpl.addUser(user);
    }
    public  void deleteUser(User user)
    {
        modelImpl.deleteUser(user);
    }
    public  void updateUser(long Id, long userId, List<Long> MyDogsId, String UserName, String Password, long PriceForHour,
                            String Comments, boolean IsOwner, boolean IsComfortableOnMorning, boolean IsComfortableOnAfternoon,
                            boolean IsComfortableOnEvening, List<String> Reviews, List<Long> Rating)
    {
        modelImpl.updateUser(Id, userId, MyDogsId, UserName, Password, PriceForHour, Comments, IsOwner,
                IsComfortableOnMorning, IsComfortableOnAfternoon, IsComfortableOnEvening, Reviews, Rating);
    }
    public  User getUserByUserNameAndPassword (String UserName, String password)
    {
        return modelImpl.getUserByUserNameAndPassword(UserName, password);
    }
    public  User getUserById(long userId)
    {
        return modelImpl.getUserById(userId);
    }
    public  List<User> getAllUsers()
    {
        return modelImpl.getAllUsers();
    }

    // Trip methods
    public  void addTrip (Trip trip)
    {
        modelImpl.addTrip(trip);
    }
    public  void deleteTrip(Trip trip)
    {
        modelImpl.deleteTrip(trip);
    }
    public  void updateTrip(long Id, long OwnerId, long DogId, long WalkerId, TimePicker StartOfWalking,
                            TimePicker EndOfWalking, DatePicker DateOfWalking)
    {
        modelImpl.updateTrip(Id,OwnerId,DogId,WalkerId,StartOfWalking,EndOfWalking,DateOfWalking);
    }
    public  Trip getTripByOwnerId (long ownerId)
    {
        return modelImpl.getTripByOwnerId(ownerId);
    }
    public  User getTripByWalkerId(long walkerId)
    {
        return modelImpl.getTripByWalkerId(walkerId);
    }
    public  User getTripById(long id)
    {
        return modelImpl.getTripById(id);
    }
    public  List<Trip> getAllTrips()
    {
        return modelImpl.getAllTrips();
    }
}

