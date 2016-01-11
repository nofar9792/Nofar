package com.example.nofarcohenzedek.dogo.Model.Parse;

import android.content.Context;

import com.example.nofarcohenzedek.dogo.Model.Comment;
import com.example.nofarcohenzedek.dogo.Model.Dog;
import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.DogSize;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.RequestStatus;
import com.example.nofarcohenzedek.dogo.Model.Trip;
import com.example.nofarcohenzedek.dogo.Model.User;
import com.parse.Parse;

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
    public void logIn(String userName, String password, final Model.GetUserListener listener) {
        UserParse.logIn(userName, password, new Model.GetUserListener() {
            @Override
            public void onResult(User user) {
                if (user != null) {
                    addDetailsByInstance(user, new Model.GetUserListener() {
                        @Override
                        public void onResult(User user) {
                            listener.onResult(user);
                        }
                    });
                }
                listener.onResult(user);
            }
        });
    }

    public void getCurrentUser(final Model.GetUserListener listener){
        User user = UserParse.getCurrentUser();
        addDetailsByInstance(user, new Model.GetUserListener() {
            @Override
            public void onResult(User user) {
                listener.onResult(user);
            }
        });
    }

    public void addDetailsByInstance(final User user,final Model.GetUserListener listener) {
        if (user instanceof DogWalker) {
            DogWalkerParse.getDogWalkerDetailsById(user.getId(), new GetDogWalkerDetailsListener() {
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
        } else {
            getDogByUserId(user.getId(), new Model.GetDogListener() {
                @Override
                public void onResult(Dog dog) {
                    final DogOwner dogOwner = (DogOwner) user;
                    ((DogOwner) user).setDog(dog);

                    listener.onResult(dogOwner);
                }
            });
        }
    }

    public void logOut() {
        UserParse.logOut();
    }

    public static void updateUser(long id,  String firstName,  String lastName,  String phoneNumber,  String address,  String city) {
        UserParse.updateUser(id, firstName, lastName, phoneNumber, address, city);
    }
        //endregion

    //region Dog Methods
//    public void getDogById(long id, final Model.GetDogListener listener) {
//        DogParse.getDogById(id, listener);
//    }

    public void getDogByUserId(long userId, final Model.GetDogListener listener) {
        DogParse.getDogByUserId(userId, listener);
    }

    public void addDog(long userId, Dog dog) {
        DogParse.addToDogsTable(userId, dog);
    }

    public void addDog(long userId, String name, DogSize size, long age, String picRef) {
        DogParse.addToDogsTable(userId, name, size, age, picRef);
    }

    public void updateDog(long userId, final String name, final DogSize size, final long age, final String picRef){
        DogParse.updateDog(userId, name, size, age, picRef);
    }

    //endregion

    //region Dog Walker Methods
    public void getDogWalkerById(final long userId, final Model.GetDogWalkerListener listener) {
        UserParse.getUserById(userId, new Model.GetUserListener() {
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

    public DogWalker getDogWalkerByIdSync(long userId) {
        User user = UserParse.getUserByIdSync(userId);
        if(user != null){
            DogWalkerParse.addDogWalkerDetails((DogWalker) user);
            CommentParse.addCommentsToDogWalker((DogWalker) user);
            return (DogWalker)user;
        }
       return null;
    }

    public void getAllDogWalkers(String fromDate, final Model.GetDogWalkersListener listener) {
        UserParse.getDogWalkerUsers(fromDate, new Model.GetDogWalkersListener() {
            @Override
            public void onResult(List<DogWalker> dogWalkers) {
                for (final DogWalker dogWalker : dogWalkers) {
                    DogWalkerParse.addDogWalkerDetails(dogWalker);
                    CommentParse.addCommentsToDogWalker(dogWalker);
                }
                listener.onResult(dogWalkers);
            }
        });
    }

    public long addDogWalker(String userName, String password, String firstName, String lastName, String phoneNumber,
                             String address, String city, long age, int priceForHour, boolean isComfortableOnMorning, boolean isComfortableOnAfternoon, boolean isComfortableOnEvening) throws Exception {
        long newUserId = UserParse.addToUsersTable(userName, password, firstName, lastName, phoneNumber, address, city, true);
        DogWalkerParse.addToDogWalkersTable(newUserId, age, priceForHour, isComfortableOnMorning, isComfortableOnAfternoon, isComfortableOnEvening);
        return newUserId;
    }
    //endregion

    //region Dog Owner Methods
    public void getDogOwnerById(long userId, final Model.GetDogOwnerListener listener) {
        UserParse.getUserById(userId, new Model.GetUserListener() {
            @Override
            public void onResult(final User user) {
                DogParse.getDogByUserId(user.getId(), new Model.GetDogListener() {
                    @Override
                    public void onResult(Dog dog) {
                        DogOwner dogOwner = (DogOwner) user;
                        ((DogOwner) user).setDog(dog);

                        listener.onResult(dogOwner);
                    }
                });
            }
        });
    }

    public DogOwner getDogOwnerByIdSync(long userId) {
        final User user = UserParse.getUserByIdSync(userId);
        if (user != null) {
            ((DogOwner) user).setDog(DogParse.getDogByUserIdSync(userId));
            return (DogOwner) user;
        }
        return null;
    }

    public long addDogOwner(String userName, String password, String firstName, String lastName, String phoneNumber,
                             String address, String city, Dog dog) throws Exception {
        long newUserId = UserParse.addToUsersTable(userName, password, firstName, lastName, phoneNumber, address, city, false);
        addDog(newUserId, dog);

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
                    trip.setDogWalker(getDogWalkerByIdSync(trip.getDogWalkerId()));
                }
                listener.onResult(trips);
            }
        });
    }

    public void getTripsByDogWalkerId(long dogWalkerId, final Model.GetTripsListener listener) {
        TripParse.getTripsDetailsByDogWalkerId(dogWalkerId, new GetTripsDetailsListener() {
            @Override
            public void onResult(final List<Trip> trips) {
                for (final Trip trip : trips) {
                    trip.setDogOwner(getDogOwnerByIdSync(trip.getDogOwnerId()));

                }
                listener.onResult(trips);
            }
        });
    }

//    public void addTrip(long dogOwnerId, long dogId, long dogWalkerId, Date startOfWalking, Date endOfWalking, Boolean isPaid) {
//        TripParse.addToTripsTable(dogOwnerId, dogId, dogWalkerId, startOfWalking, endOfWalking, isPaid);
//    }

    public long startTrip(long dogOwnerId, long dogWalkerId) {
        return TripParse.startTrip(dogOwnerId, dogWalkerId);
    }

    public void endTrip(long tripId) {
        TripParse.endTrip(tripId);
    }

    public void payTrip(long tripId) {
        TripParse.changeTripToPaid(tripId);
    }

    // endregion

    //region Request Methods
    public void addRequest(long dogOwnerId, long dogWalkerId) {
        RequestParse.addToRequestTable(dogOwnerId, dogWalkerId, RequestStatus.Waiting);
    }

    public void acceptRequest(long dogOwnerId, long dogWalkerId) {
        RequestParse.updateRequest(dogOwnerId, dogWalkerId, RequestStatus.Accepted);
    }

    public void declineRequest(long dogOwnerId, long dogWalkerId) {
        RequestParse.updateRequest(dogOwnerId, dogWalkerId, RequestStatus.Declined);
    }

    // todo: im not sure we need this func
    public void getWalkersConnectToOwner(long dogOwnerId, final Model.GetDogWalkersListener listener) {
        RequestParse.getWalkersIdsConnectedToOwner(dogOwnerId, new GetIdsListener() {
            @Override
            public void onResult(List<Long> ids) {
                final List<DogWalker> dogWalkers = new LinkedList<>();
                for (long dogWalkerId : ids) {
                    dogWalkers.add(getDogWalkerByIdSync(dogWalkerId));
                }
                listener.onResult(dogWalkers);
            }
        });
    }

    public void getOwnersConnectToWalker(long dogWalkerId, final Model.GetDogOwnersListener listener) {
        RequestParse.getOwnersIdsConnectedToWalker(dogWalkerId, new GetIdsListener() {
            @Override
            public void onResult(List<Long> ids) {
                final List<DogOwner> dogOwners = new LinkedList<>();
                for (long dogWalkerId : ids) {
                    dogOwners.add(getDogOwnerByIdSync(dogWalkerId));
                }
                listener.onResult(dogOwners);
            }
        });
    }

    public void getRequestForDogWalker(long dogWalkerId, final Model.GetDogOwnersListener listener){
        RequestParse.getRequestForDogWalker(dogWalkerId, new GetIdsListener() {
            @Override
            public void onResult(List<Long> ids) {
                final List<DogOwner> dogOwners = new LinkedList<>();
                for (long dogWalkerId : ids) {
                    dogOwners.add(getDogOwnerByIdSync(dogWalkerId));
                }
                listener.onResult(dogOwners);
            }
        });
    }

    public void getRequestOfDogOwner(long dogOwnerId, final Model.GetDogWalkersListener listener){
        RequestParse.getRequestOfDogOwner(dogOwnerId, new GetIdsListener() {
            @Override
            public void onResult(List<Long> ids) {
                final List<DogWalker> dogWalkers = new LinkedList<>();
                for (long dogWalkerId : ids) {
                    dogWalkers.add(getDogWalkerByIdSync(dogWalkerId));
                }
                listener.onResult(dogWalkers);
            }
        });
    }
    //endregion

    //region Interfaces
    public interface GetUsersListener {
        void onResult(List<User> users);
    }

    public interface GetIdsListener {
        void onResult(List<Long> ids);
    }

    public interface GetDogWalkerDetailsListener {
        void onResult(long userId, long age, int priceForHour, boolean isComfortableOnMorning, boolean isComfortableOnAfternoon, boolean isComfortableOnEvening);
    }

    public interface GetTripsDetailsListener {
        void onResult(List<Trip> trips);
    }
    //endregion
}
