package com.example.nofarcohenzedek.dogo.Model;

/**
 * Created by carmel on 25-May-16.
 */
public class PathMilestone
{
    PathAction action;
    int duration;
    String address;

    public PathMilestone(){}

    public PathMilestone (PathAction action, int duration, String address)
    {
        this.action = action;
        this.duration = duration;
        this.address = address;
    }

    public PathAction getAction(){return this.action;}

    public void setAction (PathAction action){this.action = action;}

    public int getDuration (){return this.duration;}

    public void setDuration(int duration) {this.duration = duration;}

    public String getAddress(){return this.address;}

    public void setAddress(String address){this.address = address;}

}
