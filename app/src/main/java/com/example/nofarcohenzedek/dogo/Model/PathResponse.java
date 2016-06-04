package com.example.nofarcohenzedek.dogo.Model;

import java.util.List;

/**
 * Created by omrih on 6/4/2016.
 */
public class PathResponse {
    private List<PathMilestone> path;
    private String startTime;

    public List<PathMilestone> getPath() {
        return path;
    }

    public void setPath(List<PathMilestone> path) {
        this.path = path;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
