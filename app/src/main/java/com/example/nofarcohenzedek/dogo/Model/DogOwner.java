package com.example.nofarcohenzedek.dogo.Model;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class DogOwner extends User {
    private Dog dog;

    public DogOwner(){
        super();
    }

    public DogOwner(long id, String userName, String firstName, String lastName, String phoneNumber,
                    String address, String city,Dog dog, boolean isComfortable6To8, boolean isComfortable8To10, boolean isComfortable10To12,
                    boolean isComfortable12To14, boolean isComfortable14To16, boolean isComfortable16To18, boolean isComfortable18To20,
                    boolean isComfortable20To22) {
        super(id, userName,firstName, lastName, phoneNumber, address, city, isComfortable6To8, isComfortable8To10, isComfortable10To12,
                isComfortable12To14, isComfortable14To16, isComfortable16To18, isComfortable18To20, isComfortable20To22);
        this.dog = dog;
    }

    public DogOwner(long id, String userName, String firstName, String lastName, String phoneNumber,
                    String address, String city, boolean isComfortable6To8, boolean isComfortable8To10, boolean isComfortable10To12,
                    boolean isComfortable12To14, boolean isComfortable14To16, boolean isComfortable16To18, boolean isComfortable18To20,
                    boolean isComfortable20To22) {
        super(id, userName,firstName, lastName, phoneNumber, address, city, isComfortable6To8, isComfortable8To10, isComfortable10To12,
                isComfortable12To14, isComfortable14To16, isComfortable16To18, isComfortable18To20, isComfortable20To22);
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }
}
