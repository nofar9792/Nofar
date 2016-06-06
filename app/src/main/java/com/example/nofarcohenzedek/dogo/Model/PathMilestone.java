package com.example.nofarcohenzedek.dogo.Model;

/**
 * Created by carmel on 25-May-16.
 */
public class PathMilestone
{
    PathAction type;
    int duration;
    long userId;
    String waypoint;

    public PathMilestone(){}

    public PathMilestone (PathAction action, int duration, long userId, String address)
    {
        this.type = action;
        this.duration = duration;
        this.waypoint = address;
        this.userId = userId;
    }

    public PathAction getAction(){return this.type;}

    public void setAction (PathAction action){this.type = action;}

    public int getDuration (){return this.duration;}

    public void setDuration(int duration) {this.duration = duration;}

    public long getOwnerId(){return userId;}

    public void setOwnerId(long id){this.userId = id;}

    public String getAddress(){return this.waypoint;}

    public void setAddress(String address){this.waypoint = address;}

}
