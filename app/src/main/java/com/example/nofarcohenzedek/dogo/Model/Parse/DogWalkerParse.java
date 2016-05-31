package com.example.nofarcohenzedek.dogo.Model.Parse;

import com.example.nofarcohenzedek.dogo.Model.Common.WalkerConsts;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class DogWalkerParse {
    public static void addToDogWalkersTable(long userId, long age, int priceForHour, boolean isComfortableOnMorning, boolean isComfortableOnAfternoon, boolean isComfortableOnEvening,  final Model.IsSucceedListener listener) {
        ParseObject newDogWalkerParseObject = new ParseObject(WalkerConsts.DOG_WALKERS_TABLE);

        newDogWalkerParseObject.put(WalkerConsts.USER_ID, userId);
        newDogWalkerParseObject.put(WalkerConsts.AGE, age);
        newDogWalkerParseObject.put(WalkerConsts.PRICE_FOR_HOUR, priceForHour);
//        newDogWalkerParseObject.put(WalkerConsts.IS_COMFORTABLE_ON_MORNING, isComfortableOnMorning);
//        newDogWalkerParseObject.put(WalkerConsts.IS_COMFORTABLE_ON_AFTERNOON, isComfortableOnAfternoon);
//        newDogWalkerParseObject.put(WalkerConsts.IS_COMFORTABLE_ON_EVENING, isComfortableOnEvening);

        newDogWalkerParseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    listener.onResult(true);
                } else {
                    listener.onResult(false);
                }
            }
        });
    }

    public static void addDogWalkerDetails(final DogWalker dogWalker, final Model.GetUserListener listener) {
        ParseQuery<ParseObject> query = new ParseQuery<>(WalkerConsts.DOG_WALKERS_TABLE);
        query.whereEqualTo(WalkerConsts.USER_ID, dogWalker.getId());

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    dogWalker.setAge(parseObject.getInt(WalkerConsts.AGE));
                    dogWalker.setPriceForHour(parseObject.getInt(WalkerConsts.PRICE_FOR_HOUR));
//                    dogWalker.setIsComfortableOnMorning(parseObject.getBoolean(WalkerConsts.IS_COMFORTABLE_ON_MORNING));
//                    dogWalker.setIsComfortableOnAfternoon(parseObject.getBoolean(WalkerConsts.IS_COMFORTABLE_ON_AFTERNOON));
//                    dogWalker.setIsComfortableOnEvening(parseObject.getBoolean(WalkerConsts.IS_COMFORTABLE_ON_EVENING));

                    listener.onResult(dogWalker);
                }else{
                    listener.onResult(null);
                }
            }
        });
    }

    public static void updateDogWalker(final DogWalker dogWalker, final Model.IsSucceedListener listener) {
        ParseQuery<ParseObject> query = new ParseQuery<>(WalkerConsts.DOG_WALKERS_TABLE);
        query.whereEqualTo(WalkerConsts.USER_ID, dogWalker.getId());

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    parseObject.put(WalkerConsts.AGE, dogWalker.getAge());
                    parseObject.put(WalkerConsts.PRICE_FOR_HOUR, dogWalker.getPriceForHour());
//                    parseObject.put(WalkerConsts.IS_COMFORTABLE_ON_MORNING, dogWalker.isComfortableOnMorning());
//                    parseObject.put(WalkerConsts.IS_COMFORTABLE_ON_AFTERNOON, dogWalker.isComfortableOnAfternoon());
//                    parseObject.put(WalkerConsts.IS_COMFORTABLE_ON_EVENING, dogWalker.isComfortableOnEvening());

                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                listener.onResult(true);
                            }else {
                                e.printStackTrace();
                                listener.onResult(false);
                            }
                        }
                    });
                } else {
                    e.printStackTrace();
                    listener.onResult(false);
                }
            }
        });
    }
}
