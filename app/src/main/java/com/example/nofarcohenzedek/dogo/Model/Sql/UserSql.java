package com.example.nofarcohenzedek.dogo.Model.Sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.nofarcohenzedek.dogo.Model.Common.UserConsts;
import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.User;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class UserSql
{
    public static void create(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS " + UserConsts.USER_TABLE + " (" +
                UserConsts.USER_ID + " INTEGER PRIMARY KEY," +
                UserConsts.USERNAME + " TEXT ," +
                UserConsts.FIRST_NAME + " TEXT," +
                UserConsts.LAST_NAME + " TEXT," +
                UserConsts.PHONE_NUMBER + " TEXT," +
                UserConsts.ADDRESS + " TEXT," +
                UserConsts.CITY + " TEXT," +
                UserConsts.IS_DOG_WALKER + " BOOLEAN);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table IF EXISTS " +  UserConsts.USER_TABLE + ";");
    }

    public static List<DogWalker> getDogWalkerUsers(SQLiteDatabase db) {
        String where = UserConsts.IS_DOG_WALKER + " = ?";
        String[] args = {"1"};

        Cursor cursor = db.query(UserConsts.USER_TABLE, null,  where, args, null, null, null);
        List<DogWalker> users = new LinkedList<>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(UserConsts.USER_ID);
            int userNameIndex = cursor.getColumnIndex(UserConsts.USERNAME);
            int firstNameIndex = cursor.getColumnIndex(UserConsts.FIRST_NAME);
            int lastNameIndex = cursor.getColumnIndex(UserConsts.LAST_NAME);
            int phoneNumberIndex = cursor.getColumnIndex(UserConsts.PHONE_NUMBER);
            int addressIndex = cursor.getColumnIndex(UserConsts.ADDRESS);
            int cityIndex = cursor.getColumnIndex(UserConsts.CITY);
            do {
                long id = cursor.getLong(idIndex);
                String userName = cursor.getString(userNameIndex);
                String firstName = cursor.getString(firstNameIndex);
                String lastName = cursor.getString(lastNameIndex);
                String phoneNumber = cursor.getString(phoneNumberIndex);
                String address = cursor.getString(addressIndex);
                String city = cursor.getString(cityIndex);
                DogWalker dogWalker = new DogWalker(id,userName, firstName, lastName, phoneNumber, address, city);
                users.add(dogWalker);
            } while (cursor.moveToNext());
        }
        return users;
    }

    public static User getUserById(SQLiteDatabase db, long id) {
        String where = UserConsts.USER_ID + " = ?";
        String[] args = {Long.toString(id)};

        Cursor cursor = db.query(UserConsts.USER_TABLE, null,  where, args, null, null, null);
        User user = null;

        if (cursor.moveToFirst()) {
            int userNameIndex = cursor.getColumnIndex(UserConsts.USERNAME);
            int firstNameIndex = cursor.getColumnIndex(UserConsts.FIRST_NAME);
            int lastNameIndex = cursor.getColumnIndex(UserConsts.LAST_NAME);
            int phoneNumberIndex = cursor.getColumnIndex(UserConsts.PHONE_NUMBER);
            int addressIndex = cursor.getColumnIndex(UserConsts.ADDRESS);
            int cityIndex = cursor.getColumnIndex(UserConsts.CITY);
            int isDogWalkerIndex = cursor.getColumnIndex(UserConsts.IS_DOG_WALKER);

            String userName = cursor.getString(userNameIndex);
            String firstName = cursor.getString(firstNameIndex);
            String lastName = cursor.getString(lastNameIndex);
            String phoneNumber = cursor.getString(phoneNumberIndex);
            String address = cursor.getString(addressIndex);
            String city = cursor.getString(cityIndex);
            Boolean isDogWalker = cursor.getInt(isDogWalkerIndex) == 1;

            if(isDogWalker) {
                user = new DogWalker(id,userName, firstName, lastName, phoneNumber, address, city);
            }else {
                user = new DogOwner(id,userName, firstName, lastName, phoneNumber, address, city);
            }
        }
        return user;
    }

    public static void addToUsersTable(SQLiteDatabase db,User user) {
        String where = UserConsts.USER_ID + " = ?";
        String[] args = { Long.toString(user.getId())};

        ContentValues values = new ContentValues();
        values.put(UserConsts.USER_ID, user.getId());
        values.put(UserConsts.USERNAME, user.getUserName());
        values.put(UserConsts.FIRST_NAME, user.getFirstName());
        values.put(UserConsts.LAST_NAME, user.getLastName());
        values.put(UserConsts.PHONE_NUMBER, user.getPhoneNumber());
        values.put(UserConsts.ADDRESS, user.getAddress());
        values.put(UserConsts.CITY, user.getCity());
        if (user instanceof DogWalker) {
            values.put(UserConsts.IS_DOG_WALKER, 1);
        } else {
            values.put(UserConsts.IS_DOG_WALKER, 0);
        }

        long updateResult = db.update(UserConsts.USER_TABLE, values, where, args);

        // Check in the update didnt succeed(-1) or didnt do nothing(0)
        if (updateResult == -1 || updateResult == 0) {
            if (db.insert(UserConsts.USER_TABLE, null, values) == -1) {
                Log.e("UserSql", "Fail to write to sql");
            }
        }
    }
}
