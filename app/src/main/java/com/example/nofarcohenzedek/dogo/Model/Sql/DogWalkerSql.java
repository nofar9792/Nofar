package com.example.nofarcohenzedek.dogo.Model.Sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.nofarcohenzedek.dogo.Model.Common.WalkerConsts;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class DogWalkerSql {
    public static void create(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS " + WalkerConsts.DOG_WALKERS_TABLE + " (" +
                WalkerConsts.USER_ID + " PRIMARY KEY INTEGER ," +
                WalkerConsts.AGE + " INTEGER," +
                WalkerConsts.PRICE_FOR_HOUR + " INTEGER," +
                WalkerConsts.IS_COMFORTABLE_ON_MORNING + " BOOLEAN," +
                WalkerConsts.IS_COMFORTABLE_ON_AFTERNOON + " BOOLEAN," +
                WalkerConsts.IS_COMFORTABLE_ON_EVENING + " BOOLEAN);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table IF EXISTS " +  WalkerConsts.DOG_WALKERS_TABLE + ";");
    }

    public static void addToDogWalkersTable(SQLiteDatabase db, DogWalker dogWalker) {
        String where = WalkerConsts.USER_ID + " = ?";
        String[] args = {Long.toString(dogWalker.getId())};

        ContentValues values = new ContentValues();
        values.put(WalkerConsts.USER_ID, dogWalker.getId());
        values.put(WalkerConsts.AGE, dogWalker.getAge());
        values.put(WalkerConsts.PRICE_FOR_HOUR, dogWalker.getPriceForHour());

        if (dogWalker.isComfortableOnMorning()) {
            values.put(WalkerConsts.IS_COMFORTABLE_ON_MORNING, 1);
        } else {
            values.put(WalkerConsts.IS_COMFORTABLE_ON_MORNING, 0);
        }
        if (dogWalker.isComfortableOnAfternoon()) {
            values.put(WalkerConsts.IS_COMFORTABLE_ON_AFTERNOON, 1);
        } else {
            values.put(WalkerConsts.IS_COMFORTABLE_ON_AFTERNOON, 0);
        }
        if (dogWalker.isComfortableOnEvening()) {
            values.put(WalkerConsts.IS_COMFORTABLE_ON_EVENING, 1);
        } else {
            values.put(WalkerConsts.IS_COMFORTABLE_ON_EVENING, 0);
        }

        long updateResult = db.update(WalkerConsts.DOG_WALKERS_TABLE, values, where, args);

        // Check in the update didnt succeed(-1) or didnt do nothing(0)
        if (updateResult == -1 || updateResult == 0) {
            if (db.insert(WalkerConsts.DOG_WALKERS_TABLE, null, values) == -1) {
                Log.e("UserSql", "Fail to write to sql");
            }
        }
    }

    public static void addDogWalkerDetails(SQLiteDatabase db, DogWalker dogWalker) {
        String where = WalkerConsts.USER_ID + " = ?";
        String[] args = {Long.toString(dogWalker.getId())};
        Cursor cursor = db.query(WalkerConsts.DOG_WALKERS_TABLE, null, where, args, null, null, null);

        if (cursor.moveToFirst()) {
            int ageIndex = cursor.getColumnIndex(WalkerConsts.AGE);
            int priceForHourIndex = cursor.getColumnIndex(WalkerConsts.PRICE_FOR_HOUR);
            int isComfortableOnMorningIndex = cursor.getColumnIndex(WalkerConsts.IS_COMFORTABLE_ON_MORNING);
            int isComfortableOnAfternoonIndex = cursor.getColumnIndex(WalkerConsts.IS_COMFORTABLE_ON_AFTERNOON);
            int isComfortableOnEveningIndex = cursor.getColumnIndex(WalkerConsts.IS_COMFORTABLE_ON_EVENING);

            dogWalker.setAge(cursor.getLong(ageIndex));
            dogWalker.setPriceForHour(cursor.getInt(priceForHourIndex));
            dogWalker.setIsComfortableOnMorning(cursor.getInt(isComfortableOnMorningIndex) == 1);
            dogWalker.setIsComfortableOnAfternoon(cursor.getInt(isComfortableOnAfternoonIndex) == 1);
            dogWalker.setIsComfortableOnEvening(cursor.getInt(isComfortableOnEveningIndex) == 1);
        }
    }
}
