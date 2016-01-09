package com.example.nofarcohenzedek.dogo.Model;

import java.util.Date;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class Trip
{
    private long id;
    private long dogOwnerId;
    private long dogWalkerId;
    private DogOwner dogOwner;
    private DogWalker dogWalker;
    private Date startOfWalking;
    private Date endOfWalking;
    private Boolean isPaid;

    public Trip(long id,long dogOwnerId, long dogWalkerId, Date startOfWalking, Date endOfWalking, Boolean isPaid)
    {
        this.id = id;
        this.dogOwnerId = dogOwnerId;
        this.dogWalkerId = dogWalkerId;
        this.startOfWalking = startOfWalking;
        this.endOfWalking = endOfWalking;
        this.isPaid = isPaid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDogOwnerId() {
        return dogOwnerId;
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
