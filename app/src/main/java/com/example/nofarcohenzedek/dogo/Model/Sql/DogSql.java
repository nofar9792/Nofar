package com.example.nofarcohenzedek.dogo.Model.Sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.nofarcohenzedek.dogo.Model.Common.DogConsts;
import com.example.nofarcohenzedek.dogo.Model.Dog;
import com.example.nofarcohenzedek.dogo.Model.DogSize;

/**
 * Created by Nofar Cohen Zedek on 03-Jan-16.
 */
public class DogSql {
    public static void create(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS " + DogConsts.DOGS_TABLE + " (" +
                DogConsts.USER_ID + " INTEGER PRIMARY KEY ," +
                DogConsts.NAME + " TEXT ," +
                DogConsts.SIZE + " TEXT," +
                DogConsts.AGE + " INTEGER," +
                DogConsts.PIC_REF + " TEXT);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table IF EXISTS " + DogConsts.DOGS_TABLE + ";");
    }

    public static void addToDogsTable(SQLiteDatabase db,long userId, Dog dog) {
        String where = DogConsts.USER_ID + " = ?";
        String[] args = {Long.toString(userId)};

        ContentValues values = new ContentValues();
        values.put(DogConsts.USER_ID, userId);
        values.put(DogConsts.NAME, dog.getName());
        values.put(DogConsts.SIZE, dog.getSize().name());
        values.put(DogConsts.AGE, dog.getAge());
        values.put(DogConsts.PIC_REF, dog.getPicRef());


        long updateResult = db.update(DogConsts.DOGS_TABLE, values, where, args);

        // Check in the update didnt succeed(-1) or didnt do nothing(0)
        if (updateResult == -1 || updateResult == 0) {
            if (db.insert(DogConsts.DOGS_TABLE, null, values) == -1) {
                Log.e("UserSql", "Fail to write to sql");
            }
        }
    }

    public static Dog getDogByUserId(SQLiteDatabase db, long id) {
        String where = DogConsts.USER_ID + " = ?";
        String[] args = {Long.toString(id)};

        Cursor cursor = db.query(DogConsts.DOGS_TABLE, null, where, args, null, null, null);
        Dog dog = null;

        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DogConsts.NAME);
            int sizeIndex = cursor.getColumnIndex(DogConsts.SIZE);
            int ageIndex = cursor.getColumnIndex(DogConsts.AGE);
            int picRefIndex = cursor.getColumnIndex(DogConsts.PIC_REF);

            String name = cursor.getString(nameIndex);
            DogSize size = DogSize.valueOf(cursor.getString(sizeIndex));
            long age = cursor.getLong(ageIndex);
            String picRef = cursor.getString(picRefIndex);

            dog = new Dog(name, size, age, picRef);
        }
        return dog;
    }
}
