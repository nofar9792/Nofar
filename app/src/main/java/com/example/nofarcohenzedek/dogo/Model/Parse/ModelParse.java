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

    public ModelParse(Context context) {
        Parse.initialize(context, "ClCDxIalYQPR6IrVXUHtHQW99tazxTZOAFUnanLB", "8EqrsIlUMqsfx8yxs9FjUndl0AO58cDHIJFp6wyY");
    }

    public void getDogById(long id, final Model.GetDogListener listener) {
        DogParse.getDogById(id, listener);
    }

    public void getAllDogsOfOwner(long userId, final Model.GetDogsListener listener) {
        final List<Long> dogIds = new LinkedList<Long>();
        DogOwnerConnectDogParse.getDogIdsOfOwner(userId, dogIds);

        DogParse.getDogByIds(dogIds, listener);
    }

    public void addDog(long userId, long id, String name, final DogSize size, final long age, final String picRef) {
        DogOwnerConnectDogParse.addToDogOwnersConnectDogsTable(userId, id);
        DogParse.addToDogsTable(id, name, size, age, picRef);
    }

    public void updateDog(long id, final String name, final DogSize size, final long age, final String picRef){
        DogParse.updateDog(id, name, size, age, picRef);
    }

    public void deleteDog(long id){
        DogParse.deleteDog(id);
    }

    public interface GetUserListener {
        void onResult(long id, String userName, String firstName, String lastName, String phoneNumber, String address, String city);
    }

    public interface GetUserListener2 {
        void onResult(User user);
    }

    public interface GetUsersListener {
        void onResult(List<User> users);
    }
    public interface GetDogWalkerDetailsListener {
        void onResult(long userId, long age, int priceForHour, boolean isComfortableOnMorning, boolean isComfortableOnAfternoon, boolean isComfortableOnEvening);
    }

    public interface GetCommentsListener {
        void onResult(List<Comment> comments);
    }

    public void getDogWalkerById(final long userId, final Model.GetDogWalkerListener listener) {
        UserParse.getUserById(userId, new GetUserListener() {
            @Override
            public void onResult(long id, final String userName, final String firstName, final String lastName, final String phoneNumber, final String address, final String city) {
                DogWalkerParse.getDogWalkerDetailsById(userId, new GetDogWalkerDetailsListener() {
                    @Override
                    public void onResult(long userId, long age, int priceForHour, boolean isComfortableOnMorning, boolean isComfortableOnAfternoon, boolean isComfortableOnEvening) {
                        final DogWalker dogWalker = new DogWalker(userId, userName, firstName, lastName, phoneNumber, address, city, age, priceForHour, isComfortableOnMorning, isComfortableOnAfternoon, isComfortableOnEvening);

                        CommentParse.getCommentsOfDogWalker(userId, new GetCommentsListener() {
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

                        CommentParse.getCommentsOfDogWalker(userId, new GetCommentsListener() {
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

    // stub
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
        // Temp
//        List<DogWalker> list = new ArrayList<DogWalker>();
//        list.add(new DogWalker(2, "nofi","nofar", "cz","033","סביון", "מודיעין", 22,20,true,true,true));
//        list.add(new DogWalker(2, "tahat","carmel", "bt","052456374","חרצית", "מודיעין", 25,40,true,false,true));
//        listener.onResult(list);
    }

    public void addDogWalker(long id, String userName, String password, String firstName, String lastName, String phoneNumber,
                             String address, String city,Boolean isDogWalker, long age, int priceForHour, boolean isComfortableOnMorning, boolean isComfortableOnAfternoon, boolean isComfortableOnEvening) {
        UserParse.addToUsersTable(id, userName, password, firstName, lastName, phoneNumber, address, city, isDogWalker);
        DogWalkerParse.addToDogWalkersTable(id, age, priceForHour, isComfortableOnMorning, isComfortableOnAfternoon, isComfortableOnEvening);
    }

    public void getDogOwnerById(final long userId, final Model.GetDogOwnerListener listener) {
        UserParse.getUserById(userId, new GetUserListener() {
            @Override
            public void onResult(long id, final String userName, final String firstName, final String lastName, final String phoneNumber, final String address, final String city) {
                getAllDogsOfOwner(userId, new Model.GetDogsListener() {
                    @Override
                    public void onResult(List<Dog> dogs) {
                        DogOwner dogOwner = new DogOwner(userId, userName, firstName, lastName, phoneNumber, address, city, dogs);

                        listener.onResult(dogOwner);
                    }
                });
            }
        });
    }

    public void addDogOwner(long id, String userName, String password, String firstName, String lastName, String phoneNumber,
                             String address, String city, Boolean isDogWalker) {
        UserParse.addToUsersTable(id, userName, password, firstName, lastName, phoneNumber, address, city, isDogWalker);
    }

    public void addCommentToDogWalker(long userId, String text, long rating) {
        CommentParse.addToCommentsTable(userId, text, rating);
    }

    public void addTrip(long dogOwnerId, long dogId, long dogWalkerId, Date startOfWalking, Date endOfWalking, Date dateOfWalking) {
        TripParse.addToTripsTable(dogOwnerId, dogId, dogWalkerId, startOfWalking, endOfWalking, dateOfWalking);
    }

    public interface GetTripsDetailsListener {
        void onResult(List<Trip> trips);
    }

    public void getTripsByDogOwnerId(long dogOwnerId, final Model.GetTripsListener listener){
        TripParse.getTripsDetailsByDogOwnerId(dogOwnerId, new GetTripsDetailsListener() {
            @Override
            public void onResult(List<Trip> trips) {
                for (final Trip trip : trips) {
                    getDogWalkerById(trip.getDogWalkerId(), new Model.GetDogWalkerListener() {
                        @Override
                        public void onResult(DogWalker dogWalker) {
                            trip.setDogWalker(dogWalker);
                        }
                    });

                    getDogById(trip.getDogId(), new Model.GetDogListener() {
                        @Override
                        public void onResult(Dog dog) {
                            trip.setDog(dog);
                        }
                    });
                }
                listener.onResult(trips);
            }
        });
    }

    public void getTripsByDogWalkerId(long dogWalkerId, final Model.GetTripsListener listener){
        TripParse.getTripsDetailsByDogWalkerId(dogWalkerId, new GetTripsDetailsListener() {
            @Override
            public void onResult(List<Trip> trips) {
                for (final Trip trip : trips) {
                    getDogOwnerById(trip.getDogWalkerId(), new Model.GetDogOwnerListener() {
                        @Override
                        public void onResult(DogOwner dogOwner) {
                            trip.setDogOwner(dogOwner);
                        }
                    });

                    getDogById(trip.getDogId(), new Model.GetDogListener() {
                        @Override
                        public void onResult(Dog dog) {
                            trip.setDog(dog);
                        }
                    });
                }
                listener.onResult(trips);
            }
        });
    }

    public void addUser(long id, String userName, String password, String firstName, String lastName, String phoneNumber,
                        String address, String city) {

//LogIn
//        ParseUser.logInInBackground("Jerry", "showmethemoney", new LogInCallback() {
//            public void done(ParseUser user, ParseException e) {
//                if (user != null) {
//                    // Hooray! The user is logged in.
//                } else {
//                    // Signup failed. Look at the ParseException to see what happened.
//                }
//            }
//        });

// current user
//        ParseUser currentUser = ParseUser.getCurrentUser();
//        if (currentUser != null) {
//            // do stuff with the user
//        } else {
//            // show the signup or login screen
//        }

        // log out
//        ParseUser.logOut();
//        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
    }

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
//    public User getUserByUserNameAndPassword(String UserName, String password) {
//        return null;
//    }
//
//    @Override
//    public User getUserById(long userId) {
//        return null;
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
//
//    @Override
//    public Trip getTripByOwnerId(long ownerId) {
//        return null;
//    }
//
//    @Override
//    public User getTripByWalkerId(long walkerId) {
//        return null;
//    }
//
//    @Override
//    public User getTripById(long id) {
//        return null;
//    }
//
//    @Override
//    public List<Trip> getAllTrips() {
//        return null;
//    }
}
