package com.example.nofarcohenzedek.dogo.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by carmel on 25-May-16.
 */
public enum PathAction {
    @SerializedName("pickup")
    Pickup,
    @SerializedName("return")
    Return,
    @SerializedName("walk")
    Walk,
    @SerializedName("wait")
    Wait,
    @SerializedName("start")
    Start
}
