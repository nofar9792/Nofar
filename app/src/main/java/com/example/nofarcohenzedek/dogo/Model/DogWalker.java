package com.example.nofarcohenzedek.dogo.Model;

public class DogWalker extends User{
    private long age;
    private int priceForHour;

    public DogWalker(){
        super();
    }

    public DogWalker(long id, String userName, String firstName, String lastName, String phoneNumber,
                     String address, String city, long age, int priceForHour , boolean isComfortable6To8, boolean isComfortable8To10, boolean isComfortable10To12,
                     boolean isComfortable12To14, boolean isComfortable14To16, boolean isComfortable16To18, boolean isComfortable18To20,
                     boolean isComfortable20To22) {
        super(id, userName, firstName, lastName, phoneNumber, address, city, isComfortable6To8, isComfortable8To10, isComfortable10To12,
                isComfortable12To14, isComfortable14To16, isComfortable16To18, isComfortable18To20, isComfortable20To22);
        this.age = age;
        this.priceForHour = priceForHour;
    }

    public DogWalker(long id, String userName, String firstName, String lastName, String phoneNumber,
                     String address, String city, boolean isComfortable6To8, boolean isComfortable8To10, boolean isComfortable10To12,
                     boolean isComfortable12To14, boolean isComfortable14To16, boolean isComfortable16To18, boolean isComfortable18To20,
                     boolean isComfortable20To22){
        super(id, userName, firstName, lastName, phoneNumber, address, city, isComfortable6To8, isComfortable8To10, isComfortable10To12,
                isComfortable12To14, isComfortable14To16, isComfortable16To18, isComfortable18To20, isComfortable20To22);
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public int getPriceForHour() {
        return priceForHour;
    }

    public void setPriceForHour(int priceForHour) {
        this.priceForHour = priceForHour;
    }
}
