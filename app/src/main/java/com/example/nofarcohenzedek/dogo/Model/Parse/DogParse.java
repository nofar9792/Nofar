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
        newDogParseObject.put(DogConsts.PIC_REF, dog.getPicRef());

        newDogParseObject.saveInBackground();
    }

    public static void addToDogsTable(long userId, String name, DogSize size, long age, String picRef) {
        ParseObject newDogParseObject = new ParseObject(DogConsts.DOGS_TABLE);

        newDogParseObject.put(DogConsts.USER_ID, userId);
        newDogParseObject.put(DogConsts.NAME, name);
        newDogParseObject.put(DogConsts.SIZE, size.name());
        newDogParseObject.put(DogConsts.AGE, age);
        newDogParseObject.put(DogConsts.PIC_REF, picRef);

        newDogParseObject.saveInBackground();
    }

//    public static void getDogById(long id, final Model.GetDogListener listener) {
//        ParseQuery<ParseObject> query = new ParseQuery<>(DOGS_TABLE);
//
//        query.whereEqualTo(DOG_ID, id);
//        query.getFirstInBackground(new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject parseObject, ParseException e) {
//                Dog dog = null;
//                if (e == null) {
//                    long id = parseObject.getLong(DOG_ID);
//                    String name = parseObject.getString(NAME);
//                    DogSize dogSize = DogSize.valueOf(parseObject.getString(SIZE));
//                    long age = parseObject.getLong(AGE);
//                    String picRef = parseObject.getString(PIC_REF);
//                    dog = new Dog(id, name, dogSize, age, picRef);
//                } else {
//                    e.printStackTrace();
//                }
//                listener.onResult(dog);
//            }
//        });
//    }

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

    // todo: maybe i need this func for get somw dogs of walker that take them to a trip
//    public static void getDogsByIds(List<Long> dogIds, final Model.GetDogsListener listener) {
//        ParseQuery<ParseObject> query = new ParseQuery<>(DOGS_TABLE);
//        query.whereContainedIn(DOG_ID, dogIds);
//
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> list, ParseException e) {
//                List<Dog> dogs = new LinkedList<>();
//
//                if (e == null) {
//                    for (ParseObject parseObject : list) {
//                        long id = parseObject.getLong(DOG_ID);
//                        String name = parseObject.getString(NAME);
//                        DogSize dogSize = DogSize.valueOf(parseObject.getString(SIZE));
//                        long age = parseObject.getLong(AGE);
//                        String picRef = parseObject.getString(PIC_REF);
//                        dogs.add(new Dog(id, name, dogSize, age, picRef));
//                    }
//                    listener.onResult(dogs);
//
//                } else {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

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
                    parseObject.put(DogConsts.PIC_REF, dog.getPicRef());

                    parseObject.saveInBackground();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}