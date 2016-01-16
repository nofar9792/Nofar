package com.example.nofarcohenzedek.dogo.Model.Parse;

import com.example.nofarcohenzedek.dogo.Model.Common.DogConsts;
import com.example.nofarcohenzedek.dogo.Model.Dog;
import com.example.nofarcohenzedek.dogo.Model.DogSize;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class DogParse {
    public static void addToDogsTable(long userId, Dog dog) {
        ParseObject newDogParseObject = new ParseObject(DogConsts.DOGS_TABLE);

        newDogParseObject.put(DogConsts.USER_ID, userId);
        newDogParseObject.put(DogConsts.NAME, dog.getName());
        newDogParseObject.put(DogConsts.SIZE, dog.getSize().name());
        newDogParseObject.put(DogConsts.AGE, dog.getAge());

        if(dog.getPicRef() != null) {
            newDogParseObject.put(DogConsts.PIC_REF, dog.getPicRef());
        }
        newDogParseObject.saveInBackground();
    }

    public static void getDogByUserId(long userId, final Model.GetDogListener listener) {
        ParseQuery<ParseObject> query = new ParseQuery<>(DogConsts.DOGS_TABLE);

        query.whereEqualTo(DogConsts.USER_ID, userId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                Dog dog = null;
                if (e == null) {
                    String name = parseObject.getString(DogConsts.NAME);
                    DogSize dogSize = DogSize.valueOf(parseObject.getString(DogConsts.SIZE));
                    long age = parseObject.getLong(DogConsts.AGE);
                    String picRef = parseObject.getString(DogConsts.PIC_REF);
                    dog = new Dog(name, dogSize, age, picRef);
                } else {
                    e.printStackTrace();
                }
                listener.onResult(dog);
            }
        });
    }

    public static Dog getDogByUserIdSync(long userId) {
        Dog dog = null;
        ParseQuery<ParseObject> query = new ParseQuery<>(DogConsts.DOGS_TABLE);

        query.whereEqualTo(DogConsts.USER_ID, userId);
        try {
            ParseObject parseObject = query.getFirst();
            String name = parseObject.getString(DogConsts.NAME);
            DogSize dogSize = DogSize.valueOf(parseObject.getString(DogConsts.SIZE));
            long age = parseObject.getLong(DogConsts.AGE);
            String picRef = parseObject.getString(DogConsts.PIC_REF);
            dog = new Dog(name, dogSize, age, picRef);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dog;
    }

    public static void updateDog(long userId, final Dog dog) {
        ParseQuery<ParseObject> query = new ParseQuery<>(DogConsts.DOGS_TABLE);
        query.whereEqualTo(DogConsts.USER_ID, userId);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    parseObject.put(DogConsts.NAME, dog.getName());
                    parseObject.put(DogConsts.SIZE, dog.getSize().name());
                    parseObject.put(DogConsts.AGE, dog.getAge());

                    if(dog.getPicRef() != null){
                        parseObject.put(DogConsts.PIC_REF, dog.getPicRef());
                    }

                    parseObject.saveInBackground();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}