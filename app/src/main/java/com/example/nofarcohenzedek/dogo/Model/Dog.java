package com.example.nofarcohenzedek.dogo.Model;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class Dog {
    private long id;
    private String name;
    private DogSize size;
    private long age;
    private String picRef;

    public Dog(long id, String name, DogSize size, long age, String picRef)
    {
        this.id = id;
        this.name = name;
        this.size = size;
        this.age = age;
        this.picRef = picRef;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
