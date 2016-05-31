package com.example.nofarcohenzedek.dogo.Model.Parse;

import com.example.nofarcohenzedek.dogo.Model.Common.UserConsts;
import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.User;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class UserParse {
    public static void addToUsersTable(final String userName, final String password, final String firstName, final String lastName, final String phoneNumber,
                                       final String address, final String city, final Boolean isDogWalker, final Model.GetIdListener listener, final Model.ExceptionListener exceptionListener) {
        isUserNameAlreadyExist(userName, new Model.IsSucceedListener() {
            @Override
            public void onResult(boolean isExist) {
                if(isExist){
                    exceptionListener.onResult("user already exist");
                }else {
                    getNextId(new Model.GetIdListener() {
                        @Override
                        public void onResult(final long newUserId, boolean isSucceed) {
                            ParseUser user = new ParseUser();

                            user.setUsername(userName);
                            user.setPassword(password);
                            user.put(UserConsts.USER_ID, newUserId);
                            user.put(UserConsts.FIRST_NAME, firstName);
                            user.put(UserConsts.LAST_NAME, lastName);
                            user.put(UserConsts.PHONE_NUMBER, phoneNumber);
                            user.put(UserConsts.ADDRESS, address);
                            user.put(UserConsts.CITY, city);
                            user.put(UserConsts.IS_DOG_WALKER, isDogWalker);

                            user.signUpInBackground(new SignUpCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {
                                        listener.onResult(newUserId, true);
                                    } else {
                                        e.printStackTrace();
                                        listener.onResult(-1, false);
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    public static void logIn(String userName, String password, final Model.GetUserListener listener){
//        ParseUser.logInInBackground(userName, password, new LogInCallback() {
//            public void done(ParseUser parseUser, ParseException e) {
//                if (parseUser != null) {
//                    User user = convertFromParseUserToUser(parseUser);
//
//                    listener.onResult(user);
//                } else {
//                    e.printStackTrace();
//                    listener.onResult(null);
//                }
//            }
//        });
    }

    public static void logOut(){
        ParseUser.logOutInBackground();
    }

    public static void getUserById(long id, final Model.GetUserListener listener) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo(UserConsts.USER_ID, id);

//        query.getFirstInBackground(new GetCallback<ParseUser>() {
//            @Override
//            public void done(ParseUser parseUser, ParseException e) {
//                if (e == null) {
//                    User user = convertFromParseUserToUser(parseUser);
//
//                    listener.onResult(user);
//                } else {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    public static void getDogWalkerUsers(String fromDate, final Model.GetDogWalkersListener listener) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo(UserConsts.IS_DOG_WALKER, true);

        if (fromDate != null) {
            query.whereGreaterThanOrEqualTo("updatedAt", fromDate);
        }

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                List<DogWalker> dogWalkers = new LinkedList<>();
                if (e == null) {
                    for (ParseUser parseUser : list) {
//                        dogWalkers.add((DogWalker) convertFromParseUserToUser(parseUser));
                    }
                } else {
                    e.printStackTrace();
                }
                listener.onResult(dogWalkers);
            }
        });

    }

    public static void updateUser(final User user, final Model.IsSucceedListener listener) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo(UserConsts.USER_ID, user.getId());
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e == null) {
                    parseUser.put(UserConsts.FIRST_NAME, user.getFirstName());
                    parseUser.put(UserConsts.LAST_NAME, user.getLastName());
                    parseUser.put(UserConsts.PHONE_NUMBER, user.getPhoneNumber());
                    parseUser.put(UserConsts.ADDRESS, user.getAddress());
                    parseUser.put(UserConsts.CITY, user.getCity());

                    parseUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                listener.onResult(true);
                            }else {
                                e.printStackTrace();
                                listener.onResult(false);
                            }
                        }
                    });
                } else {
                    e.printStackTrace();
                    listener.onResult(false);
                }
            }
        });
    }

    //region Private Methods
    private static void getNextId(final Model.GetIdListener listener){
        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.addDescendingOrder(UserConsts.USER_ID).getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if(e == null){
                    listener.onResult(parseUser.getLong(UserConsts.USER_ID) + 1, true);
                }else {
                    listener.onResult(1, true);
                }
            }
        });
    }

    private static void isUserNameAlreadyExist(String username, final Model.IsSucceedListener listener) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo(UserConsts.USERNAME, username);

        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if(e == null) {
                    listener.onResult(true);
                }else {
                    listener.onResult(false);
                }
            }
        });
    }

//    private static User convertFromParseUserToUser(ParseUser parseUser){
//        User user;
//
//        long userId = parseUser.getLong(UserConsts.USER_ID);
//        String userName = parseUser.getUsername();
//        String firstName = parseUser.getString(UserConsts.FIRST_NAME);
//        String lastName = parseUser.getString(UserConsts.LAST_NAME);
//        String phoneNumber = parseUser.getString(UserConsts.PHONE_NUMBER);
//        String address = parseUser.getString(UserConsts.ADDRESS);
//        String city = parseUser.getString(UserConsts.CITY);
//        Boolean isDogWalker = parseUser.getBoolean(UserConsts.IS_DOG_WALKER);
//
//        if (isDogWalker) {
//            user = new DogWalker(userId, userName, firstName, lastName, phoneNumber, address, city);
//        } else {
//            user = new DogOwner(userId, userName, firstName, lastName, phoneNumber, address, city);
//        }
//
//        return user;
//    }
    //endregion
}
