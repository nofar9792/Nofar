package com.example.nofarcohenzedek.dogo.Model.Sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.nofarcohenzedek.dogo.Model.Common.RequestConsts;
import com.example.nofarcohenzedek.dogo.Model.Request;
import com.example.nofarcohenzedek.dogo.Model.RequestStatus;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nofar Cohen Zedek on 12-Jan-16.
 */
public class RequestSql {

    public static void create(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS " + RequestConsts.REQUESTS_TABLE + " (" +
                RequestConsts.DOG_OWNER_ID + " INTEGER ," +
                RequestConsts.DOG_WALKER_ID + " INTEGER ," +
                RequestConsts.REQUEST_STATUS + " TEXT);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table IF EXISTS " +  RequestConsts.REQUESTS_TABLE + ";");
    }

    public static List<Long> getRequestForDogWalker(SQLiteDatabase db, long dogWalkerId) {
        String where = RequestConsts.DOG_WALKER_ID + " = ? AND " +
                RequestConsts.REQUEST_STATUS + " = ?";
        String[] args = { Long.toString(dogWalkerId),RequestStatus.Waiting.name()};

        Cursor cursor = db.query(RequestConsts.REQUESTS_TABLE, null,  where, args, null, null, null);
        List<Long> ids = new LinkedList<>();

        if (cursor.moveToFirst()) {
            int dogOwnerIdIndex = cursor.getColumnIndex(RequestConsts.DOG_OWNER_ID);

            do {
                ids.add(cursor.getLong(dogOwnerIdIndex));
            } while (cursor.moveToNext());
        }
        return ids;
    }

    public static List<Long> getRequestForDogOwner(SQLiteDatabase db, long dogOwnerId) {
        String where = RequestConsts.DOG_OWNER_ID + " = ? AND " +
                RequestConsts.REQUEST_STATUS + " = ?";
        String[] args = { Long.toString(dogOwnerId),RequestStatus.Waiting.name()};

        Cursor cursor = db.query(RequestConsts.REQUESTS_TABLE, null,  where, args, null, null, null);
        List<Long> ids = new LinkedList<>();

        if (cursor.moveToFirst()) {
            int dogWalkerIdIndex = cursor.getColumnIndex(RequestConsts.DOG_WALKER_ID);

            do {
                ids.add(cursor.getLong(dogWalkerIdIndex));
            } while (cursor.moveToNext());
        }
        return ids;
    }

    public static List<Long> getOwnersConnectToWalker(SQLiteDatabase db, long dogWalkerId) {
        String where = RequestConsts.DOG_WALKER_ID + " = ? AND " +
                RequestConsts.REQUEST_STATUS + " = ?";

        String[] args = { Long.toString(dogWalkerId),RequestStatus.Accepted.name()};

        Cursor cursor = db.query(RequestConsts.REQUESTS_TABLE, null, where, args, null, null, null);
        List<Long> ids = new LinkedList<>();

        if (cursor.moveToFirst()) {
            int dogOwnerIdIndex = cursor.getColumnIndex(RequestConsts.DOG_OWNER_ID);

            do {
                ids.add(cursor.getLong(dogOwnerIdIndex));
            } while (cursor.moveToNext());
        }
        return ids;
    }

    public static List<Long> getWalkersConnectToOwner(SQLiteDatabase db, long dogOwnerId) {
        String where = RequestConsts.DOG_OWNER_ID + " = ? AND " +
                       RequestConsts.REQUEST_STATUS + " = ?";
        String[] args = { Long.toString(dogOwnerId),RequestStatus.Accepted.name()};

        Cursor cursor = db.query(RequestConsts.REQUESTS_TABLE, null, where, args, null, null, null);
        List<Long> ids = new LinkedList<>();

        if (cursor.moveToFirst()) {
            int dogWalkerIdIndex = cursor.getColumnIndex(RequestConsts.DOG_WALKER_ID);

            do {
                ids.add(cursor.getLong(dogWalkerIdIndex));
            } while (cursor.moveToNext());
        }
        return ids;
    }

    public static void addToRequestTable(SQLiteDatabase db, Request request) {
        String where = RequestConsts.DOG_OWNER_ID + " = ? AND " +
                       RequestConsts.DOG_WALKER_ID + " = ?";
        String[] args = { Long.toString(request.getDogOwnerId()), Long.toString(request.getDogWalkerId())};

        ContentValues values = new ContentValues();
        values.put(RequestConsts.DOG_OWNER_ID, request.getDogOwnerId());
        values.put(RequestConsts.DOG_WALKER_ID, request.getDogWalkerId());
        values.put(RequestConsts.REQUEST_STATUS, request.getRequestStatus().name());

        long updateResult = db.update(RequestConsts.REQUESTS_TABLE, values, where, args);
        // Check in the update didnt succeed(-1) or didnt do nothing(0)
        if (updateResult == -1 || updateResult == 0) {
            if (db.insert(RequestConsts.REQUESTS_TABLE, null, values) == -1) {
                Log.e("UserSql", "Fail to write to sql");
            }
        }
    }
}
