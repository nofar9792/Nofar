package com.example.nofarcohenzedek.dogo.Model.Parse;

import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class DogWalkerParse {
    final static String DOG_WALKERS_TABLE = "DOG_WALKERS";
    final static String USER_ID = "userId";
    final static String AGE = "age";
    final static String PRICE_FOR_HOUR = "priceForHour";
    final static String IS_COMFORTABLE_ON_MORNING = "isComfortableOnMorning";
    final static String IS_COMFORTABLE_ON_AFTERNOON = "isComfortableOnAfternoon";
    final static String IS_COMFORTABLE_ON_EVENING = "isComfortableOnEvening";


    public static void addToDogWalkersTable(long userId,long age, int priceForHour, boolean isComfortableOnMorning, boolean isComfortableOnAfternoon, boolean isComfortableOnEvening){
        ParseObject newDogWalkerParseObject = new ParseObject(DOG_WALKERS_TABLE);

        newDogWalkerParseObject.put(USER_ID, userId);
        newDogWalkerParseObject.put(AGE, age);
        newDogWalkerParseObject.put(PRICE_FOR_HOUR, priceForHour);
        newDogWalkerParseObject.put(IS_COMFORTABLE_ON_MORNING, isComfortableOnMorning);
        newDogWalkerParseObject.put(IS_COMFORTABLE_ON_AFTERNOON, isComfortableOnAfternoon);
        newDogWalkerParseObject.put(IS_COMFORTABLE_ON_EVENING, isComfortableOnEvening);

        newDogWalkerParseObject.saveInBackground();
    }

    public static void addDogWalkerDetails(DogWalker dogWalker) {
        ParseQuery<ParseObject> query = new ParseQuery<>(DOG_WALKERS_TABLE);
        query.whereEqualTo(USER_ID, dogWalker.getId());

        try {
            ParseObject parseObject = query.getFirst();
            dogWalker.setAge(parseObject.getInt(AGE));
            dogWalker.setPriceForHour(parseObject.getInt(PRICE_FOR_HOUR));
            dogWalker.setIsComfortableOnMorning(parseObject.getBoolean(IS_COMFORTABLE_ON_MORNING));
            dogWalker.setIsComfortableOnAfternoon(parseObject.getBoolean(IS_COMFORTABLE_ON_AFTERNOON));
            dogWalker.setIsComfortableOnEvening(parseObject.getBoolean(IS_COMFORTABLE_ON_EVENING));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    // TODO: think if to delte this func
    public static void getDogWalkerDetailsById(long id, final ModelParse.GetDogWalkerDetailsListener listener) {
        ParseQuery<ParseObject> query = new ParseQuery<>(DOG_WALKERS_TABLE);
        query.whereEqualTo(USER_ID, id);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {

                if (e == null) {
                    long userId = parseObject.getLong(USER_ID);
                    int age = parseObject.getInt(AGE);
                    int priceForHour = parseObject.getInt(PRICE_FOR_HOUR);
                    Boolean isComfortableOnMorning = parseObject.getBoolean(IS_COMFORTABLE_ON_MORNING);
                    Boolean isComfortableOnAfternoon = parseObject.getBoolean(IS_COMFORTABLE_ON_AFTERNOON);
                    Boolean isComfortableOnEvening = parseObject.getBoolean(IS_COMFORTABLE_ON_EVENING);

                    listener.onResult(userId, age, priceForHour, isComfortableOnMorning, isComfortableOnAfternoon, isComfortableOnEvening);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
