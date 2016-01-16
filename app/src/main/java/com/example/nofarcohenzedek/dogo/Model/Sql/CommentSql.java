package com.example.nofarcohenzedek.dogo.Model.Sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.nofarcohenzedek.dogo.Model.Comment;
import com.example.nofarcohenzedek.dogo.Model.Common.CommentConsts;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class CommentSql {
    public static void create(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS " + CommentConsts.COMMENTS_TABLE + " (" +
                CommentConsts.USER_ID + " TEXT ," +
                CommentConsts.TEXT + " TEXT ," +
                CommentConsts.RATING + " INTEGER);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table IF EXISTS " +  CommentConsts.COMMENTS_TABLE + ";");
    }

    public static void addToCommentsTable(SQLiteDatabase db, long userId, Comment comment){
        ContentValues values = new ContentValues();
        values.put(CommentConsts.USER_ID, userId);
        values.put(CommentConsts.TEXT, comment.getText());
        values.put(CommentConsts.RATING, comment.getRating());

        if (db.insert(CommentConsts.COMMENTS_TABLE, null, values) == -1) {
            if (db.replace(CommentConsts.COMMENTS_TABLE, null, values) == -1) {
                Log.e("CommentSql", "Fail to write to sql");
            }
        }
    }

    public static void addCommentsToDogWalker(SQLiteDatabase db, DogWalker dogWalker) {
        String where = CommentConsts.USER_ID + " = ?";
        String[] args = {Long.toString(dogWalker.getId())};
        Cursor cursor = db.query(CommentConsts.COMMENTS_TABLE, null, where, args, null, null, null);

        List<Comment> comments = new LinkedList<>();

        if (cursor.moveToFirst()) {
            int textIndex = cursor.getColumnIndex(CommentConsts.TEXT);
            int ratingIndex = cursor.getColumnIndex(CommentConsts.RATING);

            do {
                comments.add(new Comment(cursor.getString(textIndex), cursor.getLong(ratingIndex)));
            } while (cursor.moveToNext());
        }

        dogWalker.setComments(comments);
    }
}
