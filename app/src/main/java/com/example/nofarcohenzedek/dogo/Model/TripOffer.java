package com.example.nofarcohenzedek.dogo.Model;

/**
 * Created by carmel on 04-Jun-16.
 */
public class TripOffer {
    private long ownerId;
    private String ownerAddress;
    private String fromDate;
    private String toDate;
    private long minimalAge;
    private long maximalPrice;
    private boolean isComfortable6To8;
    private boolean isComfortable8To10;
    private boolean isComfortable10To12;
    private boolean isComfortable12To14;
    private boolean isComfortable14To16;
    private boolean isComfortable16To18;
    private boolean isComfortable18To20;
    private boolean isComfortable20To22;

    public TripOffer(){};

    public TripOffer(long ownerId, String ownerAddress,String fromDate, String toDate, long minimalAge, long maximalPrice, boolean isComfortable6To8,
                     boolean isComfortable8To10, boolean isComfortable10To12, boolean isComfortable12To14, boolean isComfortable14To16,
                     boolean isComfortable16To18, boolean isComfortable18To20,boolean isComfortable20To22) {
        this.ownerId = ownerId;
        this.ownerAddress = ownerAddress;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.minimalAge = minimalAge;
        this.maximalPrice = maximalPrice;
        this.isComfortable6To8 = isComfortable6To8;
        this.isComfortable8To10 = isComfortable8To10;
        this.isComfortable10To12 = isComfortable10To12;
        this.isComfortable12To14 = isComfortable12To14;
        this.isComfortable14To16 = isComfortable14To16;
        this.isComfortable16To18 = isComfortable16To18;
        this.isComfortable18To20 = isComfortable18To20;
        this.isComfortable20To22 = isComfortable20To22;
    }

    public long getOwnerId () {return this.ownerId;}

    public void setOwnerId(long ownerId) {this.ownerId = ownerId;}

    public String getOwnerAddress() {return this.ownerAddress;}

    public void setOwnerAddress(String ownerAddress) {this.ownerAddress = ownerAddress;}

    public String getFromDate() {return this.fromDate;}

    public void setFromDate(String fromDate) {this.fromDate = fromDate;}

    public String getToDate() {return this.toDate;}

    public void setToDate(String toDate) {this.toDate = toDate;}

    public long getMinimalAge() {return this.minimalAge;}

    public void setMinimalAge(long minimalAge){this.minimalAge = minimalAge;}

    public long getMaximalPrice() {return this.maximalPrice;}

    public void setMaximalPrice(long maximalPrice) {this.maximalPrice = maximalPrice;}

    public boolean getIsComfortable6To8() {
        return isComfortable6To8;
    }

    public void setIsComfortable6To8(boolean isComfortable6To8) {
        this.isComfortable6To8 = isComfortable6To8;
    }

    public boolean getIsComfortable8To10() {
        return isComfortable8To10;
    }

    public void setIsComfortable8To10(boolean isComfortable8To10) {
        this.isComfortable8To10 = isComfortable8To10;
    }

    public boolean getIsComfortable10To12() {
        return isComfortable10To12;
    }

    public void setIsComfortable10To12(boolean isComfortable10To12) {
        this.isComfortable10To12 = isComfortable10To12;
    }

    public boolean getIsComfortable12To14() {
        return isComfortable12To14;
    }

    public void setIsComfortable12To14(boolean isComfortable12To14) {
        this.isComfortable12To14 = isComfortable12To14;
    }

    public boolean getIsComfortable14To16() {
        return isComfortable14To16;
    }

    public void setIsComfortable14To16(boolean isComfortable14To16) {
        this.isComfortable14To16 = isComfortable14To16;
    }

    public boolean getIsComfortable16To18() {
        return isComfortable16To18;
    }

    public void setIsComfortable16To18(boolean isComfortable16To18) {
        this.isComfortable16To18 = isComfortable16To18;
    }

    public boolean getIsComfortable18To20() {
        return isComfortable18To20;
    }

    public void setIsComfortable18To20(boolean isComfortable18To20) {
        this.isComfortable18To20 = isComfortable18To20;
    }

    public boolean getIsComfortable20To22() {
        return isComfortable20To22;
    }

    public void setIsComfortable20To22(boolean isComfortable20To22) {
        this.isComfortable20To22 = isComfortable20To22;
    }
}
