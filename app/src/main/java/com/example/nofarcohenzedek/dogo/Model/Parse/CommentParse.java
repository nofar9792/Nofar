package com.example.nofarcohenzedek.dogo.Model.Parse;

import com.example.nofarcohenzedek.dogo.Model.Comment;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class CommentParse {
    final static String COMMENTS_TABLE = "COMMENTS";
    final static String USER_ID = "userId";
    final static String TEXT = "text";
    final static String RATING = "rating";

    public static void addToCommentsTable(long userId, String text, long rating){
        ParseObject newCommentParseObject = new ParseObject(COMMENTS_TABLE);

        newCommentParseObject.put(USER_ID, userId);
        newCommentParseObject.put(TEXT, text);
        newCommentParseObject.put(RATING, rating);

        newCommentParseObject.saveInBackground();
    }

    public static void addCommentsToDogWalker(final DogWalker dogWalker) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(COMMENTS_TABLE);
        query.whereEqualTo(USER_ID, dogWalker.getId());

        query.findInBackground(new FindCallback<ParseObject>() {
           @Override
           public void done(List<ParseObject> list, ParseException e) {
               if (e == null) {
                   List<Comment> comments = new LinkedList<Comment>();

                   for (ParseObject parseObject : list) {
                       String text = parseObject.getString(TEXT);
                       long rating = parseObject.getLong(RATING);
                       comments.add(new Comment(text, rating));
                   }

                   dogWalker.setComments(comments);
               } else {
                   e.printStackTrace();
               }
           }
       }
        );
    }

    // TODO: delete this func
    public static void getCommentsOfDogWalker(long userId, final ModelParse.GetCommentsListener listener) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(COMMENTS_TABLE);
        query.whereEqualTo(USER_ID, userId);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                List<Comment> comments = new LinkedList<Comment>();

                for (ParseObject po : list) {
                    if (e == null) {
                        String text = po.getString(TEXT);
                        long rating = po.getLong(RATING);
                        comments.add(new Comment(text, rating));
                    } else {
                        e.printStackTrace();
                    }
                }

                listener.onResult(comments);
            }
        });
    }
}
