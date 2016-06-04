package com.example.nofarcohenzedek.dogo.Model;

/**
 * Created by carmel on 25-May-16.
 */
public class PathMilestone
{
    PathAction type;
    int duration;
    String waypoint;

    public PathMilestone(){}

    public PathMilestone (PathAction action, int duration, String address)
    {
        this.type = action;
        this.duration = duration;
        this.waypoint = address;
    }

    public PathAction getAction(){return this.type;}

    public void setAction (PathAction action){this.type = action;}

    public int getDuration (){return this.duration;}

    public void setDuration(int duration) {this.duration = duration;}

    public String getAddress(){return this.waypoint;}

    public void setAddress(String address){this.waypoint = address;}

}
