package com.example.nofarcohenzedek.dogo.Model.Sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.nofarcohenzedek.dogo.Model.Common.UserConsts;
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
        db.execSQL("create table " + UserConsts.USER_TABLE + " (" +
                UserConsts.USER_ID + " TEXT PRIMARY KEY," +
                UserConsts.USER_NAME + " TEXT ," +
                UserConsts.FIRST_NAME + " TEXT," +
                UserConsts.LAST_NAME + " TEXT," +
                UserConsts.PHONE_NUMBER + " TEXT," +
                UserConsts.ADDRESS + " TEXT," +
                UserConsts.CITY + " TEXT," +
                UserConsts.IS_DOG_WALKER + " BOOLEAN);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " +  UserConsts.USER_TABLE + ";");
    }

    public static List<DogWalker> getDogWalkerUsers(SQLiteDatabase db)
    {
        String where = UserConsts.IS_DOG_WALKER + " = ?";
        String[] args = {"1"};

        Cursor cursor = db.query(UserConsts.USER_TABLE, null,  where, args, null, null, null);
        List<DogWalker> users = new LinkedList<>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(UserConsts.USER_ID);
            int userNameIndex = cursor.getColumnIndex(UserConsts.USER_NAME);
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

    public static void addToUsersTable(SQLiteDatabase db,User user) {
        ContentValues values = new ContentValues();
        values.put(UserConsts.USER_ID, user.getId());
        values.put(UserConsts.USER_NAME, user.getUserName());
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

        if (db.insert(UserConsts.USER_TABLE, null, values) == -1) {
            if (db.replace(UserConsts.USER_TABLE, null, values) == -1) {
                Log.e("UserSql", "Fail to write to sql");
            }
        }
    }
}
