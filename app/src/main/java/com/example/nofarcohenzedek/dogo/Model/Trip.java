package com.example.nofarcohenzedek.dogo.Model;

import android.widget.DatePicker;
import android.widget.TimePicker;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class Trip
{
    private long id;
    private DogOwner owner;
    private Dog dog;
    private DogWalker walker;
    private TimePicker startOfWalking;
    private TimePicker endOfWalking;
    private DatePicker dateOfWalking;

    public Trip(long Id, long OwnerId, long DogId, long WalkerId, TimePicker StartOfWalking,
                TimePicker EndOfWalking, DatePicker DateOfWalking)
    {
        id = Id;
        owner = null; // todo: get person by id from model
        dog = null; // todo: get dog by id from model
        walker = null; // todo: get person by id from model
        startOfWalking = StartOfWalking;
        endOfWalking = EndOfWalking;
        dateOfWalking = DateOfWalking;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DogOwner getOwner() {
        return owner;
    }

    public void setOwner(DogOwner owner) {
        this.owner = owner;
    }

    public Dog getDogs() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public DogWalker getWalker() {
        return walker;
    }

    public void setWalker(DogWalker walker) {
        this.walker = walker;
    }

    public TimePicker getStartOfWalking() {
        return startOfWalking;
    }

    public void setStartOfWalking(TimePicker startOfWalking) {
        this.startOfWalking = startOfWalking;
    }

    public TimePicker getEndOfWalking() {
        return endOfWalking;
    }

    public void setEndOfWalking(TimePicker endOfWalking) {
        this.endOfWalking = endOfWalking;
    }

    public DatePicker getDateOfWalking() {
        return dateOfWalking;
    }

    public void setDateOfWalking(DatePicker dateOfWalking) {
        this.dateOfWalking = dateOfWalking;
    }
}
