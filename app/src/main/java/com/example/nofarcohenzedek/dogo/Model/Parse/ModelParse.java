package com.example.nofarcohenzedek.dogo.Model.Parse;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.nofarcohenzedek.dogo.Model.Dog;
import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.Request;
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
                    addDetailsByInstance(user, listener);
                }
            }
        });
    }

    public void getUserById(long userId, final Model.GetUserListener listener) {
        UserParse.getUserById(userId, new Model.GetUserListener() {
            @Override
            public void onResult(final User user) {
                addDetailsByInstance(user, listener);
            }
        });
    }

    private void addDetailsByInstance(final User user, final Model.GetUserListener listener) {
        if (user instanceof DogWalker) {
            DogWalkerParse.addDogWalkerDetails((DogWalker) user, listener);
        } else {
            DogParse.getDogByUserId(user.getId(), new Model.GetDogListener() {
                @Override
                public void onResult(Dog dog) {
                    ((DogOwner) user).setDog(dog);
                    listener.onResult(user);
                }
            });
        }
    }

    public void logOut() {
        UserParse.logOut();
    }

    //endregion

    //region Dog Walker Methods

    public void getAllDogWalkers(String fromDate, final Model.GetDogWalkersListener listener) {
        UserParse.getDogWalkerUsers(fromDate, new Model.GetDogWalkersListener() {
            @Override
            public void onResult(final List<DogWalker> dogWalkers) {
                final List<DogWalker> resultDogWalkers = new LinkedList<>();
                if(dogWalkers.size() != 0){
                    for(DogWalker dogWalker : dogWalkers) {
                        getUserById(dogWalker.getId(), new Model.GetUserListener() {
                            @Override
                            public void onResult(User user) {
                                resultDogWalkers.add((DogWalker) user);
                                if (resultDogWalkers.size() == dogWalkers.size()) {
                                    listener.onResult(resultDogWalkers);
                                }
                            }
                        });
                    }
                }else{
                    listener.onResult(resultDogWalkers);
                }
            }
        });
    }

    public void addDogWalker(String userName, String password, String firstName, String lastName, String phoneNumber,
                             String address, String city, final long age, final int priceForHour, boolean isComfortable6To8, boolean isComfortable8To10, boolean isComfortable10To12,
                             boolean isComfortable12To14, boolean isComfortable14To16, boolean isComfortable16To18, boolean isComfortable18To20,
                             boolean isComfortable20To22, final Model.GetIdListener listener, Model.ExceptionListener exceptionListener){
        UserParse.addToUsersTable(userName, password, firstName, lastName, phoneNumber, address, city, isComfortable6To8,  isComfortable8To10,
                            isComfortable10To12, isComfortable12To14,  isComfortable14To16,  isComfortable16To18,  isComfortable18To20,
                         isComfortable20To22, true, new Model.GetIdListener() {
            @Override
            public void onResult(final long id, boolean isSucceed) {
                if (isSucceed) {
                    DogWalkerParse.addToDogWalkersTable(id, age, priceForHour, new Model.IsSucceedListener() {
                        @Override
                        public void onResult(boolean isSucceed) {
                            if (isSucceed) {
                                listener.onResult(id, true);
                            } else {
                                listener.onResult(-1, false);
                            }
                        }
                    });
                }else{
                    listener.onResult(-1, false);
                }
            }
        }, exceptionListener);
    }

    public void updateDogWalker(final DogWalker dogWalker, final Model.IsSucceedListener listener){
        UserParse.updateUser(dogWalker, new Model.IsSucceedListener() {
            @Override
            public void onResult(boolean isSucceed) {
                if(isSucceed){
                    DogWalkerParse.updateDogWalker(dogWalker, listener);
                }else{
                    listener.onResult(false);
                }
            }
        });
    }
    //endregion

    //region Dog Owner Methods

    public void addDogOwner(String userName, String password, String firstName, String lastName, String phoneNumber,
                            String address, String city, final Dog dog, boolean isComfortable6To8, boolean isComfortable8To10, boolean isComfortable10To12,
                            boolean isComfortable12To14, boolean isComfortable14To16, boolean isComfortable16To18, boolean isComfortable18To20,
                            boolean isComfortable20To22, final Model.GetIdListener listener, Model.ExceptionListener exceptionListener) {
        UserParse.addToUsersTable(userName, password, firstName, lastName, phoneNumber, address, city, isComfortable6To8,  isComfortable8To10,
                isComfortable10To12, isComfortable12To14,  isComfortable14To16,  isComfortable16To18,  isComfortable18To20,
                isComfortable20To22, false, new Model.GetIdListener() {
            @Override
            public void onResult(final long id, boolean isSucceed) {
                if (isSucceed) {
                    DogParse.addToDogsTable(id, dog, new Model.IsSucceedListener() {
                        @Override
                        public void onResult(boolean isSucceed) {
                            if (isSucceed) {
                                listener.onResult(id, true);
                            } else {
                                listener.onResult(-1, false);
                            }
                        }
                    });
                } else {
                    listener.onResult(-1, false);
                }
            }
        }, exceptionListener);
    }

    public void updateDogOwner(final DogOwner dogOwner , final Model.IsSucceedListener listener){
        UserParse.updateUser(dogOwner, new Model.IsSucceedListener() {
            @Override
            public void onResult(boolean isSucceed) {
                if (isSucceed) {
                    DogParse.updateDog(dogOwner.getId(), dogOwner.getDog(), listener);
                } else {
                    listener.onResult(false);
                }
            }
        });
    }

    //endregion

    //region Trip Methods
    public void getTripsByDogOwnerId(long dogOwnerId, final Model.GetTripsListener listener){
        TripParse.getTripsDetailsByDogOwnerId(dogOwnerId, new GetTripsDetailsListener() {
            @Override
            public void onResult(final List<Trip> trips) {
                final List<Trip> resultTrips = new LinkedList<Trip>();

                if(trips.size() != 0) {
                    for (final Trip trip : trips) {
                        getUserById(trip.getDogOwnerId(), new Model.GetUserListener() {
                            @Override
                            public void onResult(User dogOwner) {
                                trip.setDogOwner((DogOwner) dogOwner);
                                getUserById(trip.getDogWalkerId(), new Model.GetUserListener() {
                                    @Override
                                    public void onResult(User dogWalker) {
                                        trip.setDogWalker((DogWalker) dogWalker);
                                        resultTrips.add(trip);
                                        if (trips.size() == resultTrips.size()) {
                                            listener.onResult(resultTrips);
                                        }
                                    }
                                });
                            }
                        });
                    }
                }else{
                    listener.onResult(resultTrips);
                }
            }
        });
    }

    public void getTripsByDogWalkerId(long dogWalkerId, final Model.GetTripsListener listener) {
        TripParse.getTripsDetailsByDogWalkerId(dogWalkerId, new GetTripsDetailsListener() {
            @Override
            public void onResult(final List<Trip> trips) {
                final List<Trip> resultTrips = new LinkedList<>();

                if(trips.size() != 0){
                    for (final Trip trip : trips) {
                        getUserById(trip.getDogOwnerId(), new Model.GetUserListener() {
                            @Override
                            public void onResult(User dogOwner) {
                                trip.setDogOwner((DogOwner) dogOwner);
                                getUserById(trip.getDogWalkerId(), new Model.GetUserListener() {
                                    @Override
                                    public void onResult(User dogWalker) {
                                        trip.setDogWalker((DogWalker) dogWalker);
                                        resultTrips.add(trip);
                                        if (trips.size() == resultTrips.size()) {
                                            listener.onResult(resultTrips);
                                        }
                                    }
                                });
                            }
                        });
                    }
                }else {
                    listener.onResult(resultTrips);
                }
            }
        });
    }

            public void startTrip(long dogOwnerId, long dogWalkerId, Model.GetIdListener listener) {
                TripParse.startTrip(dogOwnerId, dogWalkerId, listener);
            }

            public void endTrip(long tripId, Model.IsSucceedListener listener) {
                TripParse.endTrip(tripId, listener);
            }

            public void payTrip(long tripId, Model.IsSucceedListener listener) {
                TripParse.changeTripToPaid(tripId, listener);
            }

            // endregion

    //region Request Methods
    public void addRequest(long dogOwnerId, long dogWalkerId, Model.IsSucceedListener listener) {
        RequestParse.addToRequestTable(dogOwnerId, dogWalkerId, RequestStatus.Waiting, listener);
    }

    public void acceptRequest(long dogOwnerId, long dogWalkerId, Model.IsSucceedListener listener) {
        RequestParse.updateRequest(dogOwnerId, dogWalkerId, RequestStatus.Accepted, listener);
    }

    public void declineRequest(long dogOwnerId, long dogWalkerId, Model.IsSucceedListener listener) {
        RequestParse.updateRequest(dogOwnerId, dogWalkerId, RequestStatus.Declined, listener);
    }

    public void getOwnersConnectToWalker(long dogWalkerId, final Model.GetDogOwnersListener listener) {
        RequestParse.getOwnersIdsConnectedToWalker(dogWalkerId, new GetIdsListener() {
            @Override
            public void onResult(final List<Long> ids) {
                final List<DogOwner> dogOwners = new LinkedList<>();
                if (ids.size() != 0) {
                    for (long dogOwnerId : ids) {
                        getUserById(dogOwnerId, new Model.GetUserListener() {
                            @Override
                            public void onResult(User user) {
                                dogOwners.add((DogOwner) user);
                                if (ids.size() == dogOwners.size()) {
                                    listener.onResult(dogOwners);
                                }
                            }
                        });
                    }
                } else {
                    listener.onResult(dogOwners);
                }
            }
        });
    }

    /*
     * get waiting requests for dog walker
     */
    public void getRequestForDogWalker(long dogWalkerId, final Model.GetDogOwnersListener listener) {
        RequestParse.getRequestForDogWalker(dogWalkerId, new GetIdsListener() {
            @Override
            public void onResult(final List<Long> ids) {
                final List<DogOwner> dogOwners = new LinkedList<>();
                if (ids.size() != 0) {
                    for (long dogOwnerId : ids) {
                        getUserById(dogOwnerId, new Model.GetUserListener() {
                            @Override
                            public void onResult(User user) {
                                dogOwners.add((DogOwner) user);
                                if (ids.size() == dogOwners.size()) {
                                    listener.onResult(dogOwners);
                                }
                            }
                        });
                    }
                } else {
                    listener.onResult(dogOwners);
                }
            }
        });
    }

    /*
     * get waiting requests of dog owner
     */
    public void getRequestOfDogOwner(long dogOwnerId, final Model.GetDogWalkersListener listener) {
        RequestParse.getRequestOfDogOwner(dogOwnerId, new GetIdsListener() {
            @Override
            public void onResult(final List<Long> ids) {
                final List<DogWalker> dogWalkers = new LinkedList<>();
                if (ids.size() != 0) {
                    for (long dogWalkerId : ids) {
                        getUserById(dogWalkerId, new Model.GetUserListener() {
                            @Override
                            public void onResult(User user) {
                                dogWalkers.add((DogWalker) user);
                                if (ids.size() == dogWalkers.size()) {
                                    listener.onResult(dogWalkers);
                                }
                            }
                        });
                    }
                } else {
                    listener.onResult(dogWalkers);
                }
            }
        });
    }

    /*
     * get all status requests of dog walker
     */
    public void getRequestByDogWalker(long dogWalkerId, String fromDate, final Model.GetRequestsListener listener) {
        RequestParse.getRequestByDogWalker(dogWalkerId, fromDate, new Model.GetRequestsListener() {
            @Override
            public void onResult(final List<Request> requests) {
                final List<Request> resultRequests = new LinkedList<>();
                if (requests.size() != 0) {
                    for (final Request request : requests) {
                        getUserById(request.getDogOwnerId(), new Model.GetUserListener() {
                            @Override
                            public void onResult(User user) {
                                request.setDogOwner((DogOwner) user);
                                resultRequests.add(request);
                                if (requests.size() == resultRequests.size()) {
                                    listener.onResult(resultRequests);
                                }
                            }
                        });
                    }
                } else {
                    listener.onResult(resultRequests);
                }
            }
        });
    }

    /*
     * get all status requests of dog owner
     */
    public void getRequestByDogOwner(long dogOwnerId, String fromDate, final Model.GetRequestsListener listener) {
        RequestParse.getRequestByDogOwner(dogOwnerId, fromDate, new Model.GetRequestsListener() {
            @Override
            public void onResult(final List<Request> requests) {
                final List<Request> resultRequests = new LinkedList<>();
                if (requests.size() != 0) {
                    for (final Request request : requests) {
                        getUserById(request.getDogWalkerId(), new Model.GetUserListener() {
                            @Override
                            public void onResult(User user) {
                                request.setDogWalker((DogWalker) user);
                                resultRequests.add(request);
                                if (requests.size() == resultRequests.size()) {
                                    listener.onResult(resultRequests);
                                }
                            }
                        });
                    }
                } else {
                    listener.onResult(resultRequests);
                }
            }
        });
    }

    //endregion

    //region Image Methods

    public void saveImage(String imageName, Bitmap picture, Model.IsSucceedListener listener) {
        ImageParse.addToImagesTable(imageName, picture, listener);
    }

    public void getImage(String imageName, Model.GetBitmapListener listener) {
        ImageParse.getImage(imageName, listener);
    }

    //endregion

    //region Interfaces
    public interface GetIdsListener {
        void onResult(List<Long> ids);
    }

    public interface GetTripsDetailsListener {
        void onResult(List<Trip> trips);
    }
    //endregion
}
