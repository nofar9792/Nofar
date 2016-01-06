package com.example.nofarcohenzedek.dogo.Model;

import java.util.List;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class DogOwner extends User {
    private List<Dog> dogs;

    public DogOwner(long id, String userName, String firstName, String lastName, String phoneNumber,
                    String address, String city,List<Dog> dogs) {
        super(id, userName, firstName, lastName, phoneNumber, address, city);
        this.dogs = dogs;
    }


    public DogOwner(long id, String userName, String firstName, String lastName, String phoneNumber,
                    String address, String city) {
        super(id, userName, firstName, lastName, phoneNumber, address, city);
    }

    public List<Dog> getDogs() {
        return dogs;
    }

    public void setDogs(List<Dog> dogs) {
        this.dogs = dogs;
    }
}
