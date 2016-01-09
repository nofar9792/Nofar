package com.example.nofarcohenzedek.dogo.Model;

import java.util.List;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class DogOwner extends User {
    private Dog dog;

    public DogOwner(long id, String userName, String firstName, String lastName, String phoneNumber,
                    String address, String city,Dog dog) {
        super(id, userName, firstName, lastName, phoneNumber, address, city);
        this.dog = dog;
    }


    public DogOwner(long id, String userName, String firstName, String lastName, String phoneNumber,
                    String address, String city) {
        super(id, userName, firstName, lastName, phoneNumber, address, city);
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }
}
