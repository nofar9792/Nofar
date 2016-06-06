package com.example.nofarcohenzedek.dogo.Model.Parse;

import com.example.nofarcohenzedek.dogo.Model.Common.DogConsts;
import com.example.nofarcohenzedek.dogo.Model.Dog;
import com.example.nofarcohenzedek.dogo.Model.DogSize;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class DogParse {
    public static void addToDogsTable(long userId, Dog dog, final Model.IsSucceedListener listener) {
        ParseObject newDogParseObject = new ParseObject(DogConsts.DOGS_TABLE);

        newDogParseObject.put(DogConsts.USER_ID, userId);
        newDogParseObject.put(DogConsts.NAME, dog.getName());
        newDogParseObject.put(DogConsts.SIZE, dog.getSize().name());
        newDogParseObject.put(DogConsts.AGE, dog.getAge());

        if(dog.getPicRef() != null) {
            newDogParseObject.put(DogConsts.PIC_REF, dog.getPicRef());
        }
        newDogParseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    listener.onResult(true);
                } else {
                    e.printStackTrace();
                    listener.onResult(false);
                }
            }
        });
    }

    // string because android is stupid. for more information ask carmel
    public static void getOwnersIdsHashMapWithDogNames(List<String> ids, final Model.GetIdsAndDogNamesHashMapListener listener){
        ParseQuery<ParseObject> query = new ParseQuery<>(DogConsts.DOGS_TABLE);

        ArrayList<Long> longIds = new ArrayList<>();
        final HashMap<Long,String> returnList = new HashMap<>();

        for (String id : ids){
            longIds.add(Long.parseLong(id));
        }

        query.whereContainedIn(DogConsts.USER_ID,longIds).selectKeys(Arrays.asList(DogConsts.USER_ID,DogConsts.NAME));

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null){
                    for (ParseObject po : list){
                        returnList.put(po.getLong(DogConsts.USER_ID),po.getString(DogConsts.NAME));
                    }

                    listener.OnResult(returnList);
                }
                else{
                    e.printStackTrace();

                    listener.OnResult(null);
                }
            }
        });
    }

    public static void getDogByUserId(long userId, final Model.GetDogListener listener) {
        ParseQuery<ParseObject> query = new ParseQuery<>(DogConsts.DOGS_TABLE);

        query.whereEqualTo(DogConsts.USER_ID, userId);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e == null){
                    String name = parseObject.getString(DogConsts.NAME);
                    DogSize dogSize = DogSize.valueOf(parseObject.getString(DogConsts.SIZE));
                    long age = parseObject.getLong(DogConsts.AGE);
                    String picRef = parseObject.getString(DogConsts.PIC_REF);
                    listener.onResult(new Dog(name, dogSize, age, picRef));
                }else {
                    listener.onResult(null);
                }
            }
        });
    }

    public static void updateDog(long userId, final Dog dog, final Model.IsSucceedListener listener) {
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

                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if( e == null){
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