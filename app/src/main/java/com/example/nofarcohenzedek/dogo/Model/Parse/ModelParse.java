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
    public void logIn(String userName, String password, final Model.GetUserListener2 listener){
        UserParse.logIn(userName, password, new Model.GetUserListener2() {
            @Override
            public void onResult(User user) {
                addDetailsByInstance(user, new Model.GetUserListener2() {
                    @Override
                    public void onResult(User user) {
                        listener.onResult(user);
                    }
                });
            }
        });
    }

    public void getCurrentUser(final Model.GetUserListener2 listener){
        User user = UserParse.getCurrentUser();
        addDetailsByInstance(user, new Model.GetUserListener2() {
            @Override
            public void onResult(User user) {
                listener.onResult(user);
            }
        });
    }

    public void addDetailsByInstance(final User user,final Model.GetUserListener2 listener) {
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
            getAllDogsOfOwner(user.getId(), new Model.GetDogsListener() {
                @Override
                public void onResult(List<Dog> dogs) {
                    final DogOwner dogOwner = (DogOwner) user;
                    ((DogOwner) user).setDogs(dogs);

                    listener.onResult(dogOwner);
                }
            });
        }
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
        DogOwnerConnectDogParse.getDogIdsOfOwner(userId, new GetIds() {
            @Override
            public void onResult(List<Long> dogIds) {
                DogParse.getDogsByIds(dogIds, listener);
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
        UserParse.getUserById2(userId, new Model.GetUserListener2() {
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
        UserParse.getUserById2(userId, new Model.GetUserListener2() {
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

//    public void addTrip(long dogOwnerId, long dogId, long dogWalkerId, Date startOfWalking, Date endOfWalking, Boolean isPaid) {
//        TripParse.addToTripsTable(dogOwnerId, dogId, dogWalkerId, startOfWalking, endOfWalking, isPaid);
//    }

    public long startTrip(long dogOwnerId, long dogId, long dogWalkerId) {
        return TripParse.startTrip(dogOwnerId, dogId, dogWalkerId);
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

    public void getWalkersConnectToOwner(long dogOwnerId, final Model.GetDogWalkersListener listener) {
        RequestParse.getDogWalkersIdsOfOwner(dogOwnerId, new GetIds() {
            @Override
            public void onResult(List<Long> ids) {
                final List<DogWalker> dogWalkers = new LinkedList<>();
                for (long dogWalkerId : ids) {
                    getDogWalkerById2(dogWalkerId, new Model.GetDogWalkerListener() {
                        @Override
                        public void onResult(DogWalker dogWalker) {
                            dogWalkers.add(dogWalker);
                            listener.notify();
                        }
                    });
                }
                try {
                    listener.wait();
                    listener.onResult(dogWalkers);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getOwnersConnectToWalker(long dogWalkerId, final Model.GetDogOwnersListener listener) {
        RequestParse.getDogOwnersIdsOfWalker(dogWalkerId, new GetIds() {
            @Override
            public void onResult(List<Long> ids) {
                final List<DogOwner> dogOwners = new LinkedList<>();
                for (long dogOwnerId : ids) {
                    getDogOwnerById2(dogOwnerId, new Model.GetDogOwnerListener() {
                        @Override
                        public void onResult(DogOwner dogOwner) {
                            dogOwners.add(dogOwner);
                            listener.notify();
                        }
                    });
                }
                try {
                    listener.wait();
                    listener.onResult(dogOwners);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //endregion

    //region Interfaces
    public interface GetUsersListener {
        void onResult(List<User> users);
    }

    public interface GetIds {
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
