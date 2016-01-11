package com.example.nofarcohenzedek.dogo.Model.Sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nofarcohenzedek.dogo.Model.Comment;
import com.example.nofarcohenzedek.dogo.Model.Common.WalkerConsts;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;

import java.util.Date;
import java.util.List;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class ModelSql
{
    Helper sqlDb;
    SQLiteDatabase db;

    public ModelSql(Context context) {
        if (sqlDb == null){
            sqlDb = new Helper(context);
            db = sqlDb.getReadableDatabase();
        }
    }

    public List<DogWalker> getAllDogWalkers(){
        List<DogWalker> dogWalkers = UserSql.getDogWalkerUsers(db);
        for(DogWalker dogWalker : dogWalkers){
            DogWalkerSql.addDogWalkerDetails(db, dogWalker);
            CommentSql.addCommentsToDogWalker(db , dogWalker);
        }
        return dogWalkers;
    }

    public void addDogWalker(DogWalker dogWalker) {
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        UserSql.addToUsersTable(db, dogWalker);
        DogWalkerSql.addToDogWalkersTable(db,dogWalker);

        for(Comment comment : dogWalker.getComments()){
            CommentSql.addToCommentsTable(db, dogWalker.getId(), comment);
        }
    }

    public String getDogWalkersLastUpdateDate() {
        return LastUpdateSql.getLastUpdateDate(db, WalkerConsts.DOG_WALKERS_TABLE);
    }

    public void setDogWalkersLastUpdateDate(Date newDate){
        LastUpdateSql.setLastUpdateDate(db, WalkerConsts.DOG_WALKERS_TABLE, newDate);
    }

//    @Override
//    public void addDog(long userId, Dog dog) {
//        DogDao.addDog(userId, dog);
//    }
//
//    @Override
//    public void getDogById(long id,final Model.GetDogListener listener) {
//        //return DogDao.getDogById(id);
//    }
//
//    @Override
//    public List<Dog> getAllDogsOfOwner(long userId) {
//        return DogDao.getAllDogsOfOwner(userId);
//    }
//
//    @Override
//    public void updateDog(long Id, String Name, String Size, long Age, String PicRef, long OwnerId) {
////        DogDao.updateDog(Id,Name,Size,Age,PicRef,OwnerId);
//    }
//
//    @Override
//    public void deleteDog(Dog dog) {
//        DogDao.deleteDog(dog);
//    }
//
//    //   @Override
////    public void addPerson(Person person) {
////        PersonDao.addPerson(person);
////    }
////
////    @Override
////    public void deletePerson(Person person) {
////        PersonDao.deletePerson(person);
////    }
////
////    @Override
////    public void updatePerson(long Id, String FirstName, String LastName, String PhoneNumber, String Address, String City, long Age) {
////        PersonDao.updatePerson(Id,FirstName,LastName,PhoneNumber,Address,City,Age);
////    }
////
////    @Override
////    public Person getPersonById(long userId) {
////        return PersonDao.getPersonById(userId);
////    }
////
////    @Override
////    public List<Person> getAllPerson() {
////        return PersonDao.getAllPerson();
////    }
//
//    @Override
//    public void addUser(User user) {
//        UserDao.addUser(user);
//    }
//
//    @Override
//    public void deleteUser(User user) {
//        UserDao.deleteUser(user);
//    }
//
//    @Override
//    public void updateUser(long Id, long userId, List<Long> MyDogsId, String UserName, String Password, long PriceForHour, String Comments, boolean IsOwner, boolean IsComfortableOnMorning, boolean IsComfortableOnAfternoon, boolean IsComfortableOnEvening, List<String> Reviews, List<Long> Rating) {
////        UserDao.updateUser(Id,userId,MyDogsId,UserName,Password,PriceForHour,Comments,IsOwner,IsComfortableOnMorning,
////                            IsComfortableOnAfternoon,IsComfortableOnEvening,Reviews,Rating);
//    }
//
//    @Override
//    public User getUserByUserNameAndPassword(String UserName, String password) {
//        return UserDao.getUserByUserNameAndPassword(UserName,password);
//    }
//
//    @Override
//    public User getUserById(long userId) {
//        return UserDao.getUserById(userId);
//    }
//
//    @Override
//    public List<User> getAllUsers() {
//        return UserDao.getAllUsers();
//    }
//
//    @Override
//    public void addTrip(Trip trip) {
//        TripDao.addTrip(trip);
//    }
//
//    @Override
//    public void deleteTrip(Trip trip) {
//        TripDao.deleteTrip(trip);
//    }
//
//    @Override
//    public void updateTrip(long Id, long OwnerId, long DogId, long WalkerId, TimePicker StartOfWalking, TimePicker EndOfWalking, DatePicker DateOfWalking) {
//        TripDao.updateTrip(Id,OwnerId,DogId,WalkerId,StartOfWalking,EndOfWalking,DateOfWalking);
//    }
//
//    @Override
//    public Trip getTripByOwnerId(long ownerId) {
//        return TripDao.getTripByOwnerId(ownerId);
//    }
//
//    @Override
//    public User getTripByWalkerId(long walkerId) {
//        return TripDao.getTripByWalkerId(walkerId);
//    }
//
//    @Override
//    public User getTripById(long id) {
//        return TripDao.getTripById(id);
//    }
//
//    @Override
//    public List<Trip> getAllTrips() {
//        return TripDao.getAllTrips();
//    }

    class Helper extends SQLiteOpenHelper {
        public Helper(Context context) {
            super(context, "database.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            LastUpdateSql.create(db);
            UserSql.create(db);
            DogWalkerSql.create(db);
            CommentSql.create(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            UserSql.drop(db);
            DogWalkerSql.drop(db);
            CommentSql.drop(db);
            onCreate(db);
        }
    }
}

