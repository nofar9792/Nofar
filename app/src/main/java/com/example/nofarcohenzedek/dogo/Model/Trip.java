package com.example.nofarcohenzedek.dogo.Model;

import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Date;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class Trip
{
    private long dogOwnerId;
    private long dogId;
    private long dogWalkerId;
    private DogOwner dogOwner;
    private Dog dog;
    private DogWalker dogWalker;
    private Date startOfWalking;
    private Date endOfWalking;
    private Boolean isPaid;

    public Trip(long dogOwnerId, long dogId, long dogWalkerId, Date startOfWalking, Date endOfWalking, Boolean isPaid)
    {
//        dogOwner = null; // todo: get person by id from model
//        dog = null; // todo: get dog by id from model
//        dogWalker = null; // todo: get person by id from model
        this.dogOwnerId = dogOwnerId;
        this.dogId = dogId;
        this.dogWalkerId = dogWalkerId;
        this.startOfWalking = startOfWalking;
        this.endOfWalking = endOfWalking;
        this.isPaid = isPaid;
    }

    public long getDogOwnerId() {
        return dogOwnerId;
    }

    public long getDogId() {
        return dogId;
    }

    public long getDogWalkerId() {
        return dogWalkerId;
    }

    public DogOwner getDogOwner() {
        return dogOwner;
    }

    public void setDogOwner(DogOwner dogOwner) {
        this.dogOwner = dogOwner;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public DogWalker getDogWalker() {
        return dogWalker;
    }

    public void setDogWalker(DogWalker dogWalker) {
        this.dogWalker = dogWalker;
    }

    public Date getStartOfWalking() {
        return startOfWalking;
    }

    public void setStartOfWalking(Date startOfWalking) {
        this.startOfWalking = startOfWalking;
    }

    public Date getEndOfWalking() {
        return endOfWalking;
    }

    public void setEndOfWalking(Date endOfWalking) {
        this.endOfWalking = endOfWalking;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }
}
