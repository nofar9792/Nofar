package com.example.nofarcohenzedek.dogo.Model.Parse;

import com.example.nofarcohenzedek.dogo.Model.Dog;
import com.example.nofarcohenzedek.dogo.Model.DogSize;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class UserParse {
    final static String USERS_TABLE = "User";
    final static String USER_ID = "userId";
    final static String USER_NAME = "userName";
    final static String PASSWORD = "password";
    final static String FIRST_NAME = "firstName";
    final static String LAST_NAME = "lastName";
    final static String PHONE_NUMBER = "phoneNumber";
    final static String ADDRESS = "address";
    final static String CITY = "city";

    public static void addToUsersTable(long id, String userName, String password, String firstName, String lastName, String phoneNumber,
                                       String address, String city) {
        ParseUser user = new ParseUser();
        user.setUsername(userName);
        user.setPassword(password);
        user.put(USER_ID, id);
        user.put(FIRST_NAME, firstName);
        user.put(LAST_NAME, lastName);
        user.put(PHONE_NUMBER, phoneNumber);
        user.put(ADDRESS, address);
        user.put(CITY, city);

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void getUserById(long id, final ModelParse.GetUserListener listener) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(USERS_TABLE);
        query.whereEqualTo(USER_ID, id);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {

                if (e == null) {
                    long userId = parseObject.getLong(USER_ID);
                    String userName = parseObject.getString(USER_NAME);
                    String password = parseObject.getString(PASSWORD);
                    String firstName = parseObject.getString(FIRST_NAME);
                    String lastName = parseObject.getString(LAST_NAME);
                    String phoneNumber = parseObject.getString(PHONE_NUMBER);
                    String address = parseObject.getString(ADDRESS);
                    String city = parseObject.getString(CITY);
                    listener.onResult(userId, userName, password, firstName, lastName, phoneNumber, address, city);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
