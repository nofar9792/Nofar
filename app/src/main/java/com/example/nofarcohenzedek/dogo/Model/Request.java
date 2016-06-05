package com.example.nofarcohenzedek.dogo.Model;

public class Request {
    private long dogOwnerId;
    private long dogWalkerId;
    private DogOwner dogOwner;
    private DogWalker dogWalker;
    private RequestStatus requestStatus;
    private boolean isOwnerAskedWalker;

    public Request(long dogOwnerId, long dogWalkerId, RequestStatus requestStatus, boolean isOwnerAskedWalker) {
        this.dogOwnerId = dogOwnerId;
        this.dogWalkerId = dogWalkerId;
        this.requestStatus = requestStatus;
        this.isOwnerAskedWalker = isOwnerAskedWalker;
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

    public boolean isOwnerAskedWalker() {
        return isOwnerAskedWalker;
    }

    public void setIsOwnerAskedWalker(boolean isOwnerAskedWalker) {
        this.isOwnerAskedWalker = isOwnerAskedWalker;
    }
}
