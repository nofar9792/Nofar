package com.example.nofarcohenzedek.dogo.Model.Parse;

import com.example.nofarcohenzedek.dogo.Model.Common.RequestConsts;
import com.example.nofarcohenzedek.dogo.Model.Common.TripOfferConsts;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.TripOffer;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by carmel on 04-Jun-16.
 */
public class TripsOfferingParse {

    public static void addToTripsOfferingTable(TripOffer newOffer, final Model.IsSucceedListener listener){
        ParseObject tripOfferParseObject = new ParseObject(TripOfferConsts.TRIP_OFFER_TABLE);

        tripOfferParseObject.put(TripOfferConsts.OWNER_ID,newOffer.getOwnerId());
        tripOfferParseObject.put(TripOfferConsts.OWNER_FULL_ADDRESS,newOffer.getOwnerAddress());
        tripOfferParseObject.put(TripOfferConsts.FROM_DATE,newOffer.getFromDate());
        tripOfferParseObject.put(TripOfferConsts.TO_DATE,newOffer.getToDate());
        tripOfferParseObject.put(TripOfferConsts.MINIMAL_AGE,newOffer.getMinimalAge());
        tripOfferParseObject.put(TripOfferConsts.MAXIMAL_PRICE,newOffer.getMaximalPrice());
        tripOfferParseObject.put(TripOfferConsts.IS_COMFORTABLE_6_TO_8,newOffer.getIsComfortable6To8());
        tripOfferParseObject.put(TripOfferConsts.IS_COMFORTABLE_8_TO_10,newOffer.getIsComfortable8To10());
        tripOfferParseObject.put(TripOfferConsts.IS_COMFORTABLE_10_TO_12,newOffer.getIsComfortable10To12());
        tripOfferParseObject.put(TripOfferConsts.IS_COMFORTABLE_12_TO_14,newOffer.getIsComfortable12To14());
        tripOfferParseObject.put(TripOfferConsts.IS_COMFORTABLE_14_TO_16,newOffer.getIsComfortable14To16());
        tripOfferParseObject.put(TripOfferConsts.IS_COMFORTABLE_16_TO_18,newOffer.getIsComfortable16To18());
        tripOfferParseObject.put(TripOfferConsts.IS_COMFORTABLE_18_TO_20,newOffer.getIsComfortable18To20());
        tripOfferParseObject.put(TripOfferConsts.IS_COMFORTABLE_20_TO_22,newOffer.getIsComfortable20To22());

        tripOfferParseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    listener.onResult(true);
                }
                else{
                    e.printStackTrace();
                    listener.onResult(false);
                }
            }
        });

    }

    public static void getAllTripOffersByAgeAndPrice(long walkerAge, long walkerPrice, final Model.GetTripOffersListener listener){
        ParseQuery<ParseObject> query = new ParseQuery<>(TripOfferConsts.TRIP_OFFER_TABLE);
        query.whereGreaterThanOrEqualTo(TripOfferConsts.MAXIMAL_PRICE,walkerPrice);
        query.whereLessThanOrEqualTo(TripOfferConsts.MINIMAL_AGE,walkerAge);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {

                    List<TripOffer> offers = new ArrayList<TripOffer>();

                    for (ParseObject po : list) {
                        offers.add(convertParseObjectTpTripOffer(po));
                    }

                    listener.onResult(offers);
                } else {
                    e.printStackTrace();

                    listener.onResult(null);
                }
            }
        });
    }

    public static void getAllOffersByOwnerId(long ownerId, final Model.GetTripOffersListener listener){
        ParseQuery<ParseObject> query = new ParseQuery<>(TripOfferConsts.TRIP_OFFER_TABLE);
        query.whereEqualTo(TripOfferConsts.OWNER_ID, ownerId);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {

                    List<TripOffer> offers = new ArrayList<TripOffer>();

                    for (ParseObject po : list) {
                        offers.add(convertParseObjectTpTripOffer(po));
                    }

                    listener.onResult(offers);
                } else {
                    e.printStackTrace();

                    listener.onResult(null);
                }
            }
        });

    }

    public static void getTripOffer(long ownerId, String fromDate, String toDate, final Model.GetTripOffersListener listener){
        ParseQuery<ParseObject> query = new ParseQuery<>(TripOfferConsts.TRIP_OFFER_TABLE);
        query.whereEqualTo(TripOfferConsts.OWNER_ID,ownerId).whereEqualTo(TripOfferConsts.FROM_DATE,fromDate)
        .whereEqualTo(TripOfferConsts.TO_DATE,toDate);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null){
                    List<TripOffer> offers = new ArrayList<TripOffer>();

                    for (ParseObject po : list) {
                        offers.add(convertParseObjectTpTripOffer(po));
                    }
                    listener.onResult(offers);
                }
                else {
                    e.printStackTrace();

                    listener.onResult(null);
                }
            }
        });
    }

    private static TripOffer convertParseObjectTpTripOffer(ParseObject object){
        TripOffer offer = new TripOffer();
        offer.setOwnerId(object.getLong(TripOfferConsts.OWNER_ID));
        offer.setOwnerAddress(object.getString(TripOfferConsts.OWNER_FULL_ADDRESS));
        offer.setFromDate(object.getString(TripOfferConsts.FROM_DATE));
        offer.setToDate(object.getString(TripOfferConsts.TO_DATE));
        offer.setMinimalAge(object.getLong(TripOfferConsts.MINIMAL_AGE));
        offer.setMaximalPrice(object.getLong(TripOfferConsts.MAXIMAL_PRICE));
        offer.setIsComfortable6To8(object.getBoolean(TripOfferConsts.IS_COMFORTABLE_6_TO_8));
        offer.setIsComfortable8To10(object.getBoolean(TripOfferConsts.IS_COMFORTABLE_8_TO_10));
        offer.setIsComfortable10To12(object.getBoolean(TripOfferConsts.IS_COMFORTABLE_10_TO_12));
        offer.setIsComfortable12To14(object.getBoolean(TripOfferConsts.IS_COMFORTABLE_12_TO_14));
        offer.setIsComfortable14To16(object.getBoolean(TripOfferConsts.IS_COMFORTABLE_14_TO_16));
        offer.setIsComfortable16To18(object.getBoolean(TripOfferConsts.IS_COMFORTABLE_16_TO_18));
        offer.setIsComfortable18To20(object.getBoolean(TripOfferConsts.IS_COMFORTABLE_18_TO_20));
        offer.setIsComfortable20To22(object.getBoolean(TripOfferConsts.IS_COMFORTABLE_20_TO_22));


        return offer;
    }
}
