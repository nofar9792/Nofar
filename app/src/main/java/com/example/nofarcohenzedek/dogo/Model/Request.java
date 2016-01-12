package com.example.nofarcohenzedek.dogo.Model;

/**
 * Created by Nofar Cohen Zedek on 10-Jan-16.
 */
public class Request {
    private long dogOwnerId;
    private long dogWalkerId;
    private DogOwner dogOwner;
    private DogWalker dogWalker;
    private RequestStatus requestStatus;

    public Request(long dogOwnerId, long dogWalkerId, RequestStatus requestStatus) {
        this.dogOwnerId = dogOwnerId;
        this.dogWalkerId = dogWalkerId;
        this.requestStatus = requestStatus;
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

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }
}
