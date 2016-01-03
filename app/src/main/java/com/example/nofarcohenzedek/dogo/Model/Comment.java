package com.example.nofarcohenzedek.dogo.Model;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class Comment {
    private String text;
    private long rating;

    public Comment(String text,long rating)
    {
        this.text = text;
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }
}
