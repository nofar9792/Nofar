package com.example.nofarcohenzedek.dogo.Model;

import java.util.LinkedList;

public class ArrayOfDogWalker {
    private LinkedList<DogWalker> list;

    public ArrayOfDogWalker(){
        list = new LinkedList<>();
    }

    public DogWalker get (int index){
        return list.get(index);
    }

    public LinkedList<DogWalker> getList (){
        return list;
    }

    public int size(){
        return list.size();
    }
}
