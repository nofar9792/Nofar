package com.example.nofarcohenzedek.dogo.Model;

import java.util.List;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class DogWalker extends User{

    private long age;
    private int priceForHour;
    private double averageRating;
    private List<Comment> comments;
    private boolean isComfortableOnMorning;
    private boolean isComfortableOnAfternoon;
    private boolean isComfortableOnEvening;

    public DogWalker(long id, String userName, String password, String firstName, String lastName, String phoneNumber,
                     String address, String city,long age, int priceForHour, double averageRating, List<Comment> comments, boolean isComfortableOnMorning, boolean isComfortableOnAfternoon, boolean isComfortableOnEvening) {
        super(id, userName, password, firstName, lastName, phoneNumber, address, city);
        this.age = age;
        this.priceForHour = priceForHour;
        this.averageRating = averageRating;
        this.comments = comments;
        this.isComfortableOnMorning = isComfortableOnMorning;
        this.isComfortableOnAfternoon = isComfortableOnAfternoon;
        this.isComfortableOnEvening = isComfortableOnEvening;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public int getPriceForHour() {
        return priceForHour;
    }

    public void setPriceForHour(int priceForHour) {
        this.priceForHour = priceForHour;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public boolean isComfortableOnMorning() {
        return isComfortableOnMorning;
    }

    public void setIsComfortableOnMorning(boolean isComfortableOnMorning) {
        this.isComfortableOnMorning = isComfortableOnMorning;
    }

    public boolean isComfortableOnAfternoon() {
        return isComfortableOnAfternoon;
    }

    public void setIsComfortableOnAfternoon(boolean isComfortableOnAfternoon) {
        this.isComfortableOnAfternoon = isComfortableOnAfternoon;
    }

    public boolean isComfortableOnEvening() {
        return isComfortableOnEvening;
    }

    public void setIsComfortableOnEvening(boolean isComfortableOnEvening) {
        this.isComfortableOnEvening = isComfortableOnEvening;
    }
}
