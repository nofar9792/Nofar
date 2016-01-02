package com.example.nofarcohenzedek.dogo.Model;

import java.util.List;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class UserDao
{
    private static final String TABLE = "USER";
    private static final String ID = "ID";
    private static final String USER_NAME = "USER_NAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String FIRST_NAME = "FIRST_NAME";
    private static final String LAST_NAME = "LAST_NAME";
    private static final String PHONE_NUMBER = "PHONE_NUMBER";
    private static final String ADDRESS = "ADDRESS";
    private static final String CITY = "CITY";

    public static void addUser (User user)
    {

    }

    public static void deleteUser(User user)
    {

    }

    public static void updateUser(long id, String userName, String password, String firstName, String lastName, String phoneNumber,
                                  String address, String city)
    {

    }

    public static User getUserByUserNameAndPassword(String userName, String password)
    {
        return null;
    }

    public static User getUserById(long userId)
    {
        return null;
    }

    public static List<User> getAllUsers()
    {
        return null;
    }
}
