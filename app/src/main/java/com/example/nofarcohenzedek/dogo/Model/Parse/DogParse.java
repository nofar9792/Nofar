package com.example.nofarcohenzedek.dogo.Model.Parse;

import com.example.nofarcohenzedek.dogo.Model.Dog;
import com.example.nofarcohenzedek.dogo.Model.DogSize;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class DogParse {
    final static String DOGS_TABLE = "DOGS";
    final static String DOG_ID = "dogId";
    final static String NAME = "name";
    final static String SIZE = "size";
    final static String AGE = "age";
    final static String PIC_REF = "picRef";

    public static long addToDogsTable(Dog dog){
        long newDogId = getNextId();
        ParseObject newDogParseObject = new ParseObject(DOGS_TABLE);

        newDogParseObject.put(DOG_ID, newDogId);
        newDogParseObject.put(NAME, dog.getName());
        newDogParseObject.put(SIZE, dog.getSize().name());
        newDogParseObject.put(AGE, dog.getAge());
        newDogParseObject.put(PIC_REF, dog.getPicRef());

        newDogParseObject.saveInBackground();
        return newDogId;
    }

    public static long addToDogsTable(String name, DogSize size, long age, String picRef){
        long newDogId = getNextId();
        ParseObject newDogParseObject = new ParseObject(DOGS_TABLE);

        newDogParseObject.put(DOG_ID, newDogId);
        newDogParseObject.put(NAME, name);
        newDogParseObject.put(SIZE, size.name());
        newDogParseObject.put(AGE, age);
        newDogParseObject.put(PIC_REF, picRef);

        newDogParseObject.saveInBackground();
        return newDogId;
    }

    public static void getDogById(long id, final Model.GetDogListener listener) {
        ParseQuery<ParseObject> query = new ParseQuery<>(DOGS_TABLE);

        query.whereEqualTo(DOG_ID, id);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                Dog dog = null;
                if (e == null) {
                    long id = parseObject.getLong(DOG_ID);
                    String name = parseObject.getString(NAME);
                    DogSize dogSize = DogSize.valueOf(parseObject.getString(SIZE));
                    long age = parseObject.getLong(AGE);
                    String picRef = parseObject.getString(PIC_REF);
                    dog = new Dog(id, name, dogSize, age, picRef);
                } else {
                    e.printStackTrace();
                }
                listener.onResult(dog);
            }
        });
    }

    public static void getDogsByIds(List<Long> dogIds, final Model.GetDogsListener listener) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(DOGS_TABLE);
        query.whereContainedIn(DOG_ID, dogIds);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                List<Dog> dogs = new LinkedList<Dog>();

                if (e == null) {
                    for (ParseObject parseObject : list) {
                        long id = parseObject.getLong(DOG_ID);
                        String name = parseObject.getString(NAME);
                        DogSize dogSize = DogSize.valueOf(parseObject.getString(SIZE));
                        long age = parseObject.getLong(AGE);
                        String picRef = parseObject.getString(PIC_REF);
                        dogs.add(new Dog(id, name, dogSize, age, picRef));
                    }
                    listener.onResult(dogs);

                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void updateDog(long id, final String name, final DogSize size, final long age, final String picRef) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(DOGS_TABLE);
        query.whereEqualTo(DOG_ID, id);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    parseObject.put(NAME, name);
                    parseObject.put(SIZE, size.name());
                    parseObject.put(AGE, age);
                    parseObject.put(PIC_REF, picRef);

                    parseObject.saveInBackground();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void deleteDog(long id) {
        ParseQuery<ParseObject> query = new ParseQuery<>(DOGS_TABLE);
        query.whereEqualTo(DOG_ID, id);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    parseObject.deleteInBackground();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private static long getNextId(){
        ParseQuery<ParseObject> query = new ParseQuery<>(DOGS_TABLE);
        ParseObject parseObject = null;

        try {
            parseObject = query.addDescendingOrder(DOG_ID).getFirst();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return (parseObject.getLong(DOG_ID) + 1);
    }
}
