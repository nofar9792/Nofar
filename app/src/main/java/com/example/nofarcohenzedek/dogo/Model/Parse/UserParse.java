package com.example.nofarcohenzedek.dogo.Model.Parse;

import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;
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
    final static String USER_ID = "userId";
    final static String FIRST_NAME = "firstName";
    final static String LAST_NAME = "lastName";
    final static String PHONE_NUMBER = "phoneNumber";
    final static String ADDRESS = "address";
    final static String CITY = "city";
    final static String IS_DOG_WALKER = "isDogWalker";

    public static long addToUsersTable(String userName, String password, String firstName, String lastName, String phoneNumber,
                                       String address, String city, Boolean isDogWalker) {
        long newUserId = getNextId();
        ParseUser user = new ParseUser();
        //ParseUser.logOut();

        user.setUsername(userName);
        user.setPassword(password);
        user.put(USER_ID, newUserId);
        user.put(FIRST_NAME, firstName);
        user.put(LAST_NAME, lastName);
        user.put(PHONE_NUMBER, phoneNumber);
        user.put(ADDRESS, address);
        user.put(CITY, city);
        user.put(IS_DOG_WALKER, isDogWalker);
        //ParseUser.enableRevocableSessionInBackground();
//try
//{
//    user.signUp();
//
//}
//catch (Exception e){
//e.printStackTrace();
//}
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

    public static void logIn(String userName, String password, final ModelParse.GetUserListener2 listener){
        ParseUser.logInInBackground(userName, password, new LogInCallback() {
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    User user = convertFromParseUserToUser(parseUser);

                    listener.onResult(user);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public static User getCurrentUser() {
        ParseUser parseUser = ParseUser.getCurrentUser();
        return convertFromParseUserToUser(parseUser);
    }

    public static void logOut(){
        ParseUser.logOut();
    }

//    public static void getUserById(long id, final ModelParse.GetUserListener listener) {
//        ParseQuery<ParseUser> query = ParseUser.getQuery();
//        query.whereEqualTo(USER_ID, id);
//
//        query.getFirstInBackground(new GetCallback<ParseUser>() {
//            @Override
//            public void done(ParseUser parseUser, ParseException e) {
//                if (e == null) {
//                    long userId = parseUser.getLong(USER_ID);
//                    String userName = parseUser.getUsername();
//                    String firstName = parseUser.getString(FIRST_NAME);
//                    String lastName = parseUser.getString(LAST_NAME);
//                    String phoneNumber = parseUser.getString(PHONE_NUMBER);
//                    String address = parseUser.getString(ADDRESS);
//                    String city = parseUser.getString(CITY);
//
//                    listener.onResult(userId, userName, firstName, lastName, phoneNumber, address, city);
//                } else {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    public static void getUserById2(long id, final ModelParse.GetUserListener2 listener) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo(USER_ID, id);

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

    public static void getDogWalkerUsers(final ModelParse.GetUsersListener listener) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo(IS_DOG_WALKER, true);

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                List<User> users = new LinkedList<User>();
                if (e == null) {
                    for (ParseObject po : list) {
                        long userId = po.getLong(USER_ID);
                        String userName = ((ParseUser)po).getUsername();
                        String firstName = po.getString(FIRST_NAME);
                        String lastName = po.getString(LAST_NAME);
                        String phoneNumber = po.getString(PHONE_NUMBER);
                        String address = po.getString(ADDRESS);
                        String city = po.getString(CITY);

                        users.add(new DogWalker(userId, userName, firstName, lastName, phoneNumber, address, city));
                    }
                    listener.onResult(users);
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
            parseObject = query.addDescendingOrder(USER_ID).getFirst();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return (parseObject.getLong(USER_ID) + 1);
    }

    private static User convertFromParseUserToUser(ParseUser parseUser){
        User user;

        long userId = parseUser.getLong(USER_ID);
        String userName = parseUser.getUsername();
        String firstName = parseUser.getString(FIRST_NAME);
        String lastName = parseUser.getString(LAST_NAME);
        String phoneNumber = parseUser.getString(PHONE_NUMBER);
        String address = parseUser.getString(ADDRESS);
        String city = parseUser.getString(CITY);
        Boolean isDogWalker = parseUser.getBoolean(IS_DOG_WALKER);

        if (isDogWalker) {
            user = new DogWalker(userId, userName, firstName, lastName, phoneNumber, address, city);
        } else {
            user = new DogOwner(userId, userName, firstName, lastName, phoneNumber, address, city);
        }

        return user;
    }
    //endregion
}
