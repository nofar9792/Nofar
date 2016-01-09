package com.example.nofarcohenzedek.dogo.Model;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class Dog {
    private String name;
    private DogSize size;
    private long age;
    private String picRef;


    public Dog(String name, DogSize size, long age, String picRef)
    {
        this.name = name;
        this.size = size;
        this.age = age;
        this.picRef = picRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DogSize getSize() {
        return size;
    }

    public void setSize(DogSize size) {
        this.size = size;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public String getPicRef() {
        return picRef;
    }

    public void setPicRef(String picRef) {
        this.picRef = picRef;
    }
}
