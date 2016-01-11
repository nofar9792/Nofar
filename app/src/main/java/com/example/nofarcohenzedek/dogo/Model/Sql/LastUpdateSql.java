package com.example.nofarcohenzedek.dogo.Model.Sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nofar Cohen Zedek on 11-Jan-16.
 */
public class LastUpdateSql {
    final static String LAST_UPDATE_TABLE = "lastUpdate";
    final static String TABLE_NAME = "tableName";
    final static String LAST_UPDATE_DATE = "lastUpdateDate";

    public static void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + LAST_UPDATE_TABLE + " (" +
                TABLE_NAME + " TEXT PRIMARY KEY," +
                LAST_UPDATE_DATE + " TEXT);");
    }

    public static String getLastUpdateDate(SQLiteDatabase db, String tableName) {
        String where = TABLE_NAME + " = ?";
        String[] args = {tableName};
        Cursor cursor = db.query(LAST_UPDATE_TABLE, null, where, args, null, null, null);

        if (cursor.moveToFirst()) {
            int lastUpdateDateIndex = cursor.getColumnIndex(LAST_UPDATE_DATE);
            return cursor.getString(lastUpdateDateIndex);
        }
        return null;
    }

    public static void setLastUpdateDate(SQLiteDatabase db, String tableName, Date newDate) {
        ContentValues values = new ContentValues();
        values.put(TABLE_NAME, tableName);
        values.put(LAST_UPDATE_DATE, convertToParseFormat(newDate));

        if (db.insert(LAST_UPDATE_TABLE, null, values) == -1) {
            if (db.replace(LAST_UPDATE_TABLE, null, values) == -1) {
                Log.e("LastUpdateDate", "Fail to write to sql");
            }
        }
    }

    private static String convertToParseFormat(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return formatter.format(date).replace(' ', 'T') + ".000Z";
    }
}
