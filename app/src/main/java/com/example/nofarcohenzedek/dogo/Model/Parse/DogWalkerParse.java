package com.example.nofarcohenzedek.dogo.Model.Parse;

import com.example.nofarcohenzedek.dogo.Model.Common.WalkerConsts;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class DogWalkerParse {
    public static void addToDogWalkersTable(long userId, long age, int priceForHour, boolean isComfortableOnMorning, boolean isComfortableOnAfternoon, boolean isComfortableOnEvening) {
        ParseObject newDogWalkerParseObject = new ParseObject(WalkerConsts.DOG_WALKERS_TABLE);

        newDogWalkerParseObject.put(WalkerConsts.USER_ID, userId);
        newDogWalkerParseObject.put(WalkerConsts.AGE, age);
        newDogWalkerParseObject.put(WalkerConsts.PRICE_FOR_HOUR, priceForHour);
        newDogWalkerParseObject.put(WalkerConsts.IS_COMFORTABLE_ON_MORNING, isComfortableOnMorning);
        newDogWalkerParseObject.put(WalkerConsts.IS_COMFORTABLE_ON_AFTERNOON, isComfortableOnAfternoon);
        newDogWalkerParseObject.put(WalkerConsts.IS_COMFORTABLE_ON_EVENING, isComfortableOnEvening);

        newDogWalkerParseObject.saveInBackground();
    }

    public static void addDogWalkerDetails(DogWalker dogWalker) {
        ParseQuery<ParseObject> query = new ParseQuery<>(WalkerConsts.DOG_WALKERS_TABLE);
        query.whereEqualTo(WalkerConsts.USER_ID, dogWalker.getId());

        try {
            ParseObject parseObject = query.getFirst();
            dogWalker.setAge(parseObject.getInt(WalkerConsts.AGE));
            dogWalker.setPriceForHour(parseObject.getInt(WalkerConsts.PRICE_FOR_HOUR));
            dogWalker.setIsComfortableOnMorning(parseObject.getBoolean(WalkerConsts.IS_COMFORTABLE_ON_MORNING));
            dogWalker.setIsComfortableOnAfternoon(parseObject.getBoolean(WalkerConsts.IS_COMFORTABLE_ON_AFTERNOON));
            dogWalker.setIsComfortableOnEvening(parseObject.getBoolean(WalkerConsts.IS_COMFORTABLE_ON_EVENING));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void updateDogWalker(final DogWalker dogWalker) {
        ParseQuery<ParseObject> query = new ParseQuery<>(WalkerConsts.DOG_WALKERS_TABLE);
        query.whereEqualTo(WalkerConsts.USER_ID, dogWalker.getId());

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    parseObject.put(WalkerConsts.AGE, dogWalker.getAge());
                    parseObject.put(WalkerConsts.PRICE_FOR_HOUR, dogWalker.getPriceForHour());
                    parseObject.put(WalkerConsts.IS_COMFORTABLE_ON_MORNING, dogWalker.isComfortableOnMorning());
                    parseObject.put(WalkerConsts.IS_COMFORTABLE_ON_AFTERNOON, dogWalker.isComfortableOnAfternoon());
                    parseObject.put(WalkerConsts.IS_COMFORTABLE_ON_EVENING, dogWalker.isComfortableOnEvening());

                    parseObject.saveInBackground();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    // TODO: think if to delte this func
    public static void getDogWalkerDetailsById(long id, final ModelParse.GetDogWalkerDetailsListener listener) {
        ParseQuery<ParseObject> query = new ParseQuery<>(WalkerConsts.DOG_WALKERS_TABLE);
        query.whereEqualTo(WalkerConsts.USER_ID, id);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    long userId = parseObject.getLong(WalkerConsts.USER_ID);
                    int age = parseObject.getInt(WalkerConsts.AGE);
                    int priceForHour = parseObject.getInt(WalkerConsts.PRICE_FOR_HOUR);
                    Boolean isComfortableOnMorning = parseObject.getBoolean(WalkerConsts.IS_COMFORTABLE_ON_MORNING);
                    Boolean isComfortableOnAfternoon = parseObject.getBoolean(WalkerConsts.IS_COMFORTABLE_ON_AFTERNOON);
                    Boolean isComfortableOnEvening = parseObject.getBoolean(WalkerConsts.IS_COMFORTABLE_ON_EVENING);

                    listener.onResult(userId, age, priceForHour, isComfortableOnMorning, isComfortableOnAfternoon, isComfortableOnEvening);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
