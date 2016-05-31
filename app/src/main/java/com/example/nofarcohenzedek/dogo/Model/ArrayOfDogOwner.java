package com.example.nofarcohenzedek.dogo.Model;

import java.util.LinkedList;

public class ArrayOfDogOwner {
    private LinkedList<DogOwner> list;

    public ArrayOfDogOwner(){
        list = new LinkedList<>();
    }

    public DogOwner get (int index){
        return list.get(index);
    }

    public LinkedList<DogOwner> getList (){
        return list;
    }

    public int size(){
        return list.size();
    }
}
