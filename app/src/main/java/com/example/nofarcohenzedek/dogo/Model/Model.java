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
//    interface ModelInterface {
//        // Dog Methods
//        public void addDog(long userId, Dog dog);
//
//        //    public void getDogById(long id, GetDogListener listener);
//
//        public List<Dog> getAllDogsOfOwner(long userId);
//
//        public void updateDog(long Id, String Name, String Size, long Age, String PicRef, long OwnerId);
//
//        public void deleteDog(Dog dog);
//
//        // Person methods
////        public void addPerson(Person person);
////
////        public void deletePerson(Person person);
////
////        public void updatePerson(long Id, String FirstName, String LastName, String PhoneNumber,
////                                 String Address, String City, long Age);
////
////        public Person getPersonById(long userId);
////
////        public List<Person> getAllPerson();
//
//        // User methods
//        public void addUser(User user);
//
//        public void deleteUser(User user);
//
//        public void updateUser(long Id, long userId, List<Long> MyDogsId, String UserName, String Password, long PriceForHour,
//                               String Comments, boolean IsOwner, boolean IsComfortableOnMorning, boolean IsComfortableOnAfternoon,
//                               boolean IsComfortableOnEvening, List<String> Reviews, List<Long> Rating);
//
//        public User getUserByUserNameAndPassword(String UserName, String password);
//
//        public User getUserById(long userId);
//
//        public List<User> getAllUsers();
//
//        // Trip methods
//        public void addTrip(Trip trip);
//
//        public void deleteTrip(Trip trip);
//
//        public void updateTrip(long Id, long OwnerId, long DogId, long WalkerId, TimePicker StartOfWalking,
//                               TimePicker EndOfWalking, DatePicker DateOfWalking);
//
//        public Trip getTripByOwnerId(long ownerId);
//
//        public User getTripByWalkerId(long walkerId);
//
//        public User getTripById(long id);
//
//        public List<Trip> getAllTrips();
//    }

    private Context context;
    private ModelSql modelSql;
    private ModelParse modelParse;
    private static final Model instance = new Model();

    private Model() {
    }

    public void init(Context context) {
        modelSql = new ModelSql(context);
        modelParse = new ModelParse(context);
    }

    public static Model getInstance() {
        return instance;
    }

    public static void logIn(String userName, String password, ModelParse.GetUserListener2 listener){
        ModelParse.logIn(userName, password, listener);
    }

    public static User getCurrentUser(){
        return ModelParse.getCurrentUser();
    }

    public static void logOut()
    {
        ModelParse.logOut();
    }

    // Dog Methods

    public interface GetDogListener {
        public void onResult(Dog dog);
    }

    public void getDogById(long id, GetDogListener listener) {
        modelParse.getDogById(id, listener);
    }

    public interface GetDogsListener {
        public void onResult(List<Dog> dogs);
    }

//    public void getAllDogsOfOwner(long userId, GetDogsListener listener) {
//        modelParse.getAllDogsOfOwner(userId, listener);
//    }

    public void addDog(long userId, long id, String name, final DogSize size, final long age, final String picRef) {
        modelParse.addDog(userId, id, name, size, age, picRef);
    }

    public void updateDog(long dogId, final String name, final DogSize size, final long age, final String picRef){
        modelParse.updateDog(dogId, name, size, age, picRef);
    }

    public void deleteDog(long id) {
        modelParse.deleteDog(id);
    }

    public interface GetDogWalkerListener {
        void onResult(DogWalker dogWalker);
    }

    public void getDogWalkerById(long userId, GetDogWalkerListener listener){
        //modelParse.getDogWalkerById(userId, listener);
        modelParse.getDogWalkerById2(userId, listener);
    }

    public void addDogWalker(long id, String userName, String password, String firstName, String lastName, String phoneNumber,
                             String address, String city, Boolean isDogWalker, long age, int priceForHour, boolean isComfortableOnMorning, boolean isComfortableOnAfternoon, boolean isComfortableOnEvening) {
        modelParse.addDogWalker(id, userName, password, firstName, lastName, phoneNumber, address, city, age, priceForHour, isComfortableOnMorning, isComfortableOnAfternoon, isComfortableOnEvening);
    }

    public interface GetDogWalkersListener
    {
        void onResult(List<DogWalker> allDogWalkers);
    }

    public void getAllDogWalkers(final Model.GetDogWalkersListener listener)
    {
        modelParse.getAllDogWalkers(listener);
    }

    public interface GetDogOwnerListener {
        void onResult(DogOwner dogOwner);
    }

    public void addDogOwner(long id, String userName, String password, String firstName, String lastName, String phoneNumber,
                            String address, String city, List<Dog> dogs) {
        modelParse.addDogOwner(id, userName, password, firstName, lastName, phoneNumber, address, city, dogs);
    }

    public interface GetTripsListener {
        void onResult(List<Trip> trips);
    }

    // Trip methods
    public void addTrip(long dogOwnerId, long dogId, long dogWalkerId, Date startOfWalking, Date endOfWalking, Boolean isPaid) {
        modelParse.addTrip(dogOwnerId, dogId, dogWalkerId, startOfWalking, endOfWalking, isPaid);
    }

    public void getTripsByDogOwnerId(long dogOwnerId, final Model.GetTripsListener listener){
        modelParse.getTripsByDogOwnerId(dogOwnerId,listener);
    }

    public void getTripsByDogWalkerId(long dogWalkerId, final Model.GetTripsListener listener) {
        modelParse.getTripsByDogWalkerId(dogWalkerId, listener);
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

//    // User methods
//    public void addUser(User user) {
//       // modelImpl.addUser(user);
//    }
//
//    public void deleteUser(User user) {
//
//        //modelImpl.deleteUser(user);
//    }
//
//    public void updateUser(long Id, long userId, List<Long> MyDogsId, String UserName, String Password, long PriceForHour,
//                           String Comments, boolean IsOwner, boolean IsComfortableOnMorning, boolean IsComfortableOnAfternoon,
//                           boolean IsComfortableOnEvening, List<String> Reviews, List<Long> Rating) {
//     //   modelImpl.updateUser(Id, userId, MyDogsId, UserName, Password, PriceForHour, Comments, IsOwner,
//     //           IsComfortableOnMorning, IsComfortableOnAfternoon, IsComfortableOnEvening, Reviews, Rating);
//    }
//
//    public void getUserByUserNameAndPassword(String UserName, String password) {
//      //  return modelImpl.getUserByUserNameAndPassword(UserName, password);
//    }
//
//    public void getUserById(long userId) {
//
//        //return modelImpl.getUserById(userId);
//    }
//
//    public void getAllUsers() {
//
//        //return modelImpl.getAllUsers();
//    }
//    public void deleteTrip(Trip trip)
//    {
//       // modelImpl.deleteTrip(trip);
//    }
//
//    public void updateTrip(long Id, long OwnerId, long DogId, long WalkerId, TimePicker StartOfWalking,
//                           TimePicker EndOfWalking, DatePicker DateOfWalking) {
//       // modelImpl.updateTrip(Id, OwnerId, DogId, WalkerId, StartOfWalking, EndOfWalking, DateOfWalking);
//    }
}

