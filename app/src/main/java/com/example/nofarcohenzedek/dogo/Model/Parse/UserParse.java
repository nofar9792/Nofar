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
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class UserParse {
    public static long addToUsersTable(String userName, String password, String firstName, String lastName, String phoneNumber,
                                       String address, String city, Boolean isDogWalker) throws Exception {
        if(isUserNameAlreadyExist(userName)){
            throw new Exception("user already exist");
        }

        long newUserId = getNextId();
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
                    // Hooray! Let them use the app now.
                } else {
                    e.printStackTrace();
                }
            }
        });
        return newUserId;
    }

    public static void logIn(String userName, String password, final Model.GetUserListener listener){
        ParseUser.logInInBackground(userName, password, new LogInCallback() {
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    User user = convertFromParseUserToUser(parseUser);

                    listener.onResult(user);
                } else {
                    e.printStackTrace();
                    listener.onResult(null);
                }
            }
        });
    }

    public static void logOut(){
        ParseUser.logOut();
    }

    public static void getUserById(long id, final Model.GetUserListener listener) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo(UserConsts.USER_ID, id);

        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e == null) {
                    User user = convertFromParseUserToUser(parseUser);

                    listener.onResult(user);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public static User getUserByIdSync(long id) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo(UserConsts.USER_ID, id);

        try{
            ParseUser parseUser = query.getFirst();
           return convertFromParseUserToUser(parseUser);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
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
                    for (ParseUser parseUser : list) {;
                        dogWalkers.add((DogWalker)convertFromParseUserToUser(parseUser));
                    }
                    listener.onResult(dogWalkers);
                } else {
                    e.printStackTrace();
                }
            }
        });

    }

    public static void updateUser(final User user) {
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

                    parseUser.saveInBackground();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    //region Private Methods
    private static long getNextId(){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        ParseUser parseObject = null;

        try {
            parseObject = query.addDescendingOrder(UserConsts.USER_ID).getFirst();
            return (parseObject.getLong(UserConsts.USER_ID) + 1);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return 1;
    }

    private static boolean isUserNameAlreadyExist(String username) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo(UserConsts.USERNAME, username);

        try {
            query.getFirst();
            return true;

        } catch (ParseException e) {
        }

        return false;
    }
    private static User convertFromParseUserToUser(ParseUser parseUser){
        User user;

        long userId = parseUser.getLong(UserConsts.USER_ID);
        String userName = parseUser.getUsername();
        String firstName = parseUser.getString(UserConsts.FIRST_NAME);
        String lastName = parseUser.getString(UserConsts.LAST_NAME);
        String phoneNumber = parseUser.getString(UserConsts.PHONE_NUMBER);
        String address = parseUser.getString(UserConsts.ADDRESS);
        String city = parseUser.getString(UserConsts.CITY);
        Boolean isDogWalker = parseUser.getBoolean(UserConsts.IS_DOG_WALKER);

        if (isDogWalker) {
            user = new DogWalker(userId, userName, firstName, lastName, phoneNumber, address, city);
        } else {
            user = new DogOwner(userId, userName, firstName, lastName, phoneNumber, address, city);
        }

        return user;
    }
    //endregion
}
