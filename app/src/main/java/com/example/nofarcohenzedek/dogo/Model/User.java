package com.example.nofarcohenzedek.dogo.Model;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class User
{
    private long id;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String city;
    private boolean isComfortable6To8;
    private boolean isComfortable8To10;
    private boolean isComfortable10To12;
    private boolean isComfortable12To14;
    private boolean isComfortable14To16;
    private boolean isComfortable16To18;
    private boolean isComfortable18To20;
    private boolean isComfortable20To22;
    public User(){

    }

    public User(long id, String userName, String firstName, String lastName, String phoneNumber,
                String address, String city, boolean isComfortable6To8, boolean isComfortable8To10, boolean isComfortable10To12,
                boolean isComfortable12To14, boolean isComfortable14To16, boolean isComfortable16To18, boolean isComfortable18To20,
                boolean isComfortable20To22) {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.isComfortable6To8 = isComfortable6To8;
        this.isComfortable8To10 = isComfortable8To10;
        this.isComfortable10To12 = isComfortable10To12;
        this.isComfortable12To14 = isComfortable12To14;
        this.isComfortable14To16 = isComfortable14To16;
        this.isComfortable16To18 = isComfortable16To18;
        this.isComfortable18To20 = isComfortable18To20;
        this.isComfortable20To22 = isComfortable20To22;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isComfortable6To8() {
        return isComfortable6To8;
    }

    public void setIsComfortable6To8(boolean isComfortable6To8) {
        this.isComfortable6To8 = isComfortable6To8;
    }

    public boolean isComfortable8To10() {
        return isComfortable8To10;
    }

    public void setIsComfortable8To10(boolean isComfortable8To10) {
        this.isComfortable8To10 = isComfortable8To10;
    }

    public boolean isComfortable10To12() {
        return isComfortable10To12;
    }

    public void setIsComfortable10To12(boolean isComfortable10To12) {
        this.isComfortable10To12 = isComfortable10To12;
    }

    public boolean isComfortable12To14() {
        return isComfortable12To14;
    }

    public void setIsComfortable12To14(boolean isComfortable12To14) {
        this.isComfortable12To14 = isComfortable12To14;
    }

    public boolean isComfortable14To16() {
        return isComfortable14To16;
    }

    public void setIsComfortable14To16(boolean isComfortable14To16) {
        this.isComfortable14To16 = isComfortable14To16;
    }

    public boolean isComfortable16To18() {
        return isComfortable16To18;
    }

    public void setIsComfortable16To18(boolean isComfortable16To18) {
        this.isComfortable16To18 = isComfortable16To18;
    }

    public boolean isComfortable18To20() {
        return isComfortable18To20;
    }

    public void setIsComfortable18To20(boolean isComfortable18To20) {
        this.isComfortable18To20 = isComfortable18To20;
    }

    public boolean isComfortable20To22() {
        return isComfortable20To22;
    }

    public void setIsComfortable20To22(boolean isComfortable20To22) {
        this.isComfortable20To22 = isComfortable20To22;
    }
}
