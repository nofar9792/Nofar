package com.example.nofarcohenzedek.dogo.Model.Sql;

import com.example.nofarcohenzedek.dogo.Model.Dog;
import com.example.nofarcohenzedek.dogo.Model.DogSize;

import java.util.List;

/**
 * Created by Nofar Cohen Zedek on 03-Jan-16.
 */
public class DogSql {
    private static final String TABLE = "DOG";
    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String PIC_REF = "PIC_REF";
    private static final String SIZE = "SIZE";
    private static final String AGE = "AGE";

    public static void addDog(long userId, Dog dog)
    {

    }

    public static Dog getDogById(long userId)
    {
        return null;
    }

    public static List<Dog> getAllDogsOfOwner(long userId)
    {
        return null;
    }

    public static void updateDog(long id, String name, DogSize size, long age, String picRef)
    {

    }

    public static void deleteDog(Dog dog)
    {

    }
}
