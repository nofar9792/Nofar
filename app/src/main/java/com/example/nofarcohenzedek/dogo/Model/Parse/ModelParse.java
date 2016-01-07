package com.example.nofarcohenzedek.dogo.Model.Parse;

import android.content.Context;

import com.example.nofarcohenzedek.dogo.Model.Comment;
import com.example.nofarcohenzedek.dogo.Model.Dog;
import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.DogSize;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.Trip;
import com.example.nofarcohenzedek.dogo.Model.User;
import com.parse.Parse;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class ModelParse {
    //region Ctor
    public ModelParse(Context context) {
        Parse.initialize(context, "ClCDxIalYQPR6IrVXUHtHQW99tazxTZOAFUnanLB", "8EqrsIlUMqsfx8yxs9FjUndl0AO58cDHIJFp6wyY");
    }
    //endregion

    //region User Methods
    public void logIn(String userName, String password, GetUserListener2 listener){
        UserParse.logIn(userName, password, listener);
    }

    public User getCurrentUser(){
        return UserParse.getCurrentUser();
    }

    public void logOut()
    {
        UserParse.logOut();
    }
    //endregion

    //region Dog Methods
    public void getDogById(long id, final Model.GetDogListener listener) {
        DogParse.getDogById(id, listener);
    }

    public void getAllDogsOfOwner(long userId, final Model.GetDogsListener listener) {
        DogOwnerConnectDogParse.getDogIdsOfOwner(userId, new GetDogIds() {
            @Override
            public void onResult(List<Long> dogIds) {
                DogParse.getDogByIds(dogIds, listener);
            }
        });
    }

    public long addDog(long userId, Dog dog) {
        long newDogId = DogParse.addToDogsTable(dog);
        DogOwnerConnectDogParse.addToDogOwnersConnectDogsTable(userId, newDogId);

        return newDogId;
    }

    public long addDog(long userId, String name, final DogSize size, final long age, final String picRef) {
        long newDogId = DogParse.addToDogsTable(name, size, age, picRef);
        DogOwnerConnectDogParse.addToDogOwnersConnectDogsTable(userId, newDogId);

        return newDogId;
    }

    public void updateDog(long id, final String name, final DogSize size, final long age, final String picRef){
        DogParse.updateDog(id, name, size, age, picRef);
    }

    public void deleteDog(long id){
        DogParse.deleteDog(id);
    }
    //endregion

    //region Dog Walker Methods
    public void getDogWalkerById2(final long userId, final Model.GetDogWalkerListener listener) {
        UserParse.getUserById2(userId, new GetUserListener2() {
            @Override
            public void onResult(final User user) {
                DogWalkerParse.getDogWalkerDetailsById(userId, new GetDogWalkerDetailsListener() {
                    @Override
                    public void onResult(long userId, long age, int priceForHour, boolean isComfortableOnMorning, boolean isComfortableOnAfternoon, boolean isComfortableOnEvening) {
                        final DogWalker dogWalker = (DogWalker) user;
                        dogWalker.setAge(age);
                        dogWalker.setPriceForHour(priceForHour);
                        dogWalker.setIsComfortableOnMorning(isComfortableOnMorning);
                        dogWalker.setIsComfortableOnAfternoon(isComfortableOnAfternoon);
                        dogWalker.setIsComfortableOnEvening(isComfortableOnEvening);

                        getCommentsOfDogWalker(userId, new Model.GetCommentsListener() {
                            @Override
                            public void onResult(List<Comment> comments) {
                                dogWalker.setComments(comments);
                                listener.onResult(dogWalker);
                            }
                        });
                    }
                });
            }
        });
    }

    //    public void getDogWalkerById(final long userId, final Model.GetDogWalkerListener listener) {
//        UserParse.getUserById(userId, new GetUserListener() {
//            @Override
//            public void onResult(long id, final String userName, final String firstName, final String lastName, final String phoneNumber, final String address, final String city) {
//                DogWalkerParse.getDogWalkerDetailsById(userId, new GetDogWalkerDetailsListener() {
//                    @Override
//                    public void onResult(long userId, long age, int priceForHour, boolean isComfortableOnMorning, boolean isComfortableOnAfternoon, boolean isComfortableOnEvening) {
//                        final DogWalker dogWalker = new DogWalker(userId, userName, firstName, lastName, phoneNumber, address, city, age, priceForHour, isComfortableOnMorning, isComfortableOnAfternoon, isComfortableOnEvening);
//
//                        CommentParse.getCommentsOfDogWalker(userId, new GetCommentsListener() {
//                            @Override
//                            public void onResult(List<Comment> comments) {
//                                dogWalker.setComments(comments);
//                                listener.onResult(dogWalker);
//                            }
//                        });
//                    }
//                });
//            }
//        });
//    }

    public void getAllDogWalkers(final Model.GetDogWalkersListener listener) {
        final List<DogWalker> dogWalkers = new LinkedList<>();

        UserParse.getDogWalkerUsers(new GetUsersListener() {
            @Override
            public void onResult(List<User> users) {
                for (final User user : users) {

                    DogWalkerParse.addDogWalkerDetails((DogWalker) user);
                    CommentParse.addCommentsToDogWalker((DogWalker) user);
                    dogWalkers.add((DogWalker) user);

                }
                listener.onResult(dogWalkers);
            }
        });
    }

    public long addDogWalker(String userName, String password, String firstName, String lastName, String phoneNumber,
                             String address, String city, long age, int priceForHour, boolean isComfortableOnMorning, boolean isComfortableOnAfternoon, boolean isComfortableOnEvening) {
        long newUserId = UserParse.addToUsersTable(userName, password, firstName, lastName, phoneNumber, address, city, true);
        DogWalkerParse.addToDogWalkersTable(newUserId, age, priceForHour, isComfortableOnMorning, isComfortableOnAfternoon, isComfortableOnEvening);
        return newUserId;
    }
    //endregion

    //region Dog Owner Methods
    public void getDogOwnerById2(final long userId, final Model.GetDogOwnerListener listener) {
        UserParse.getUserById2(userId, new GetUserListener2() {
            @Override
            public void onResult(final User user) {
                getAllDogsOfOwner(userId, new Model.GetDogsListener() {
                    @Override
                    public void onResult(List<Dog> dogs) {
                        final DogOwner dogOwner = (DogOwner) user;
                        ((DogOwner) user).setDogs(dogs);

                        listener.onResult(dogOwner);
                    }
                });
            }
        });
    }

//    public void getDogOwnerById(final long userId, final Model.GetDogOwnerListener listener) {
//        UserParse.getUserById(userId, new GetUserListener() {
//            @Override
//            public void onResult(long id, final String userName, final String firstName, final String lastName, final String phoneNumber, final String address, final String city) {
//                getAllDogsOfOwner(userId, new Model.GetDogsListener() {
//                    @Override
//                    public void onResult(List<Dog> dogs) {
//                        DogOwner dogOwner = new DogOwner(userId, userName, firstName, lastName, phoneNumber, address, city, dogs);
//
//                        listener.onResult(dogOwner);
//                    }
//                });
//            }
//        });
//    }

    public long addDogOwner(String userName, String password, String firstName, String lastName, String phoneNumber,
                             String address, String city, List<Dog> dogs) {
        long newUserId = UserParse.addToUsersTable(userName, password, firstName, lastName, phoneNumber, address, city, false);
        for(Dog dog : dogs) {
            addDog(newUserId, dog);
        }

        return newUserId;
    }
    //endregion

    //region Comment Methods
    private void getCommentsOfDogWalker(long userId, Model.GetCommentsListener listener) {
        CommentParse.getCommentsOfDogWalker(userId, listener);
    }

    public void addCommentToDogWalker(long userId, String text, long rating) {
        CommentParse.addToCommentsTable(userId, text, rating);
    }
    //endregion

    //region Trip Methods
    public void getTripsByDogOwnerId(long dogOwnerId, final Model.GetTripsListener listener){
        TripParse.getTripsDetailsByDogOwnerId(dogOwnerId, new GetTripsDetailsListener() {
            @Override
            public void onResult(final List<Trip> trips) {
                for (final Trip trip : trips) {
                    getDogWalkerById2(trip.getDogWalkerId(), new Model.GetDogWalkerListener() {
                        @Override
                        public void onResult(DogWalker dogWalker) {
                            trip.setDogWalker(dogWalker);
                            getDogById(trip.getDogId(), new Model.GetDogListener() {
                                @Override
                                public void onResult(Dog dog) {
                                    trip.setDog(dog);
                                    listener.onResult(trips);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    public void getTripsByDogWalkerId(long dogWalkerId, final Model.GetTripsListener listener){
        TripParse.getTripsDetailsByDogWalkerId(dogWalkerId, new GetTripsDetailsListener() {
            @Override
            public void onResult(final List<Trip> trips) {
                for (final Trip trip : trips) {
                    getDogOwnerById2(trip.getDogOwnerId(), new Model.GetDogOwnerListener() {
                        @Override
                        public void onResult(DogOwner dogOwner) {
                            trip.setDogOwner(dogOwner);

                            for (Dog dog : dogOwner.getDogs()) {
                                if (dog.getId() == trip.getDogId()) {
                                    trip.setDog(dog);
                                    break;
                                }
                            }
                            listener.onResult(trips);
                        }
                    });
                }
            }
        });
    }

    public void addTrip(long dogOwnerId, long dogId, long dogWalkerId, Date startOfWalking, Date endOfWalking, Boolean isPaid) {
        TripParse.addToTripsTable(dogOwnerId, dogId, dogWalkerId, startOfWalking, endOfWalking, isPaid);
    }

    public void startTrip(long dogOwnerId, long dogId, long dogWalkerId) {
        TripParse.startTrip(dogOwnerId, dogId, dogWalkerId);
    }

    public void endTrip(long tripId) {
        TripParse.endTrip(tripId);
    }
    // endregion

    //region Interfaces
    public interface GetUserListener {
        void onResult(long id, String userName, String firstName, String lastName, String phoneNumber, String address, String city);
    }

    public interface GetUserListener2 {
        void onResult(User user);
    }

    public interface GetUsersListener {
        void onResult(List<User> users);
    }

    public interface GetDogIds {
        void onResult(List<Long> dogIds);
    }

    public interface GetDogWalkerDetailsListener {
        void onResult(long userId, long age, int priceForHour, boolean isComfortableOnMorning, boolean isComfortableOnAfternoon, boolean isComfortableOnEvening);
    }

    public interface GetTripsDetailsListener {
        void onResult(List<Trip> trips);
    }
    //endregion

//    public void addUser(long id, String userName, String password, String firstName, String lastName, String phoneNumber,
//                        String address, String city) {
//    }
//
//    @Override
//    public void deleteUser(User user) {
//
//    }
//
//    @Override
//    public void updateUser(long Id, long userId, List<Long> MyDogsId, String UserName, String Password, long PriceForHour, String Comments, boolean IsOwner, boolean IsComfortableOnMorning, boolean IsComfortableOnAfternoon, boolean IsComfortableOnEvening, List<String> Reviews, List<Long> Rating) {
//
//    }
//
//    @Override
//    public List<User> getAllUsers() {
//        return null;
//    }
//
//    @Override
//    public void addTrip(Trip trip) {
//
//    }
//
//    @Override
//    public void deleteTrip(Trip trip) {
//
//    }
//
//    @Override
//    public void updateTrip(long Id, long OwnerId, long DogId, long WalkerId, TimePicker StartOfWalking, TimePicker EndOfWalking, DatePicker DateOfWalking) {
//
//    }
}
