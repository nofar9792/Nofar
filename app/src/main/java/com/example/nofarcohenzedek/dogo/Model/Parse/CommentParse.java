package com.example.nofarcohenzedek.dogo.Model.Parse;

import com.example.nofarcohenzedek.dogo.Model.Comment;
import com.example.nofarcohenzedek.dogo.Model.Common.CommentConsts;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.Model;
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
    public static void addToCommentsTable(long userId, String text, long rating){
        ParseObject newCommentParseObject = new ParseObject(CommentConsts.COMMENTS_TABLE);

        newCommentParseObject.put(CommentConsts.USER_ID, userId);
        newCommentParseObject.put(CommentConsts.TEXT, text);
        newCommentParseObject.put(CommentConsts.RATING, rating);

        newCommentParseObject.saveInBackground();
    }

    public static void addCommentsToDogWalker(final DogWalker dogWalker) {
        ParseQuery<ParseObject> query = new ParseQuery<>(CommentConsts.COMMENTS_TABLE);
        query.whereEqualTo(CommentConsts.USER_ID, dogWalker.getId());

        try {
            List<ParseObject> list = query.find();

            List<Comment> comments = new LinkedList<>();

            for (ParseObject parseObject : list) {
                String text = parseObject.getString(CommentConsts.TEXT);
                long rating = parseObject.getLong(CommentConsts.RATING);
                comments.add(new Comment(text, rating));
            }

            dogWalker.setComments(comments);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // TODO: delete this func
    public static void getCommentsOfDogWalker(long userId, final Model.GetCommentsListener listener) {
        ParseQuery<ParseObject> query = new ParseQuery<>(CommentConsts.COMMENTS_TABLE);
        query.whereEqualTo(CommentConsts.USER_ID, userId);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                List<Comment> comments = new LinkedList<>();

                for (ParseObject po : list) {
                    if (e == null) {
                        String text = po.getString(CommentConsts.TEXT);
                        long rating = po.getLong(CommentConsts.RATING);
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
