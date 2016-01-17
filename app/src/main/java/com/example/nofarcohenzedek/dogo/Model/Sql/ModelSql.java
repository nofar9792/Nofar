package com.example.nofarcohenzedek.dogo.Model.Sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nofarcohenzedek.dogo.Model.Common.RequestConsts;
import com.example.nofarcohenzedek.dogo.Model.Common.WalkerConsts;
import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.Request;
import com.example.nofarcohenzedek.dogo.Model.User;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class ModelSql
{
    Helper sqlDb;
    SQLiteDatabase db;

    public ModelSql(Context context) {
        if (sqlDb == null){
            sqlDb = new Helper(context);
            db = sqlDb.getReadableDatabase();
            sqlDb.onUpgrade(db,1,2);
        }
    }

    //region Dog Walker Methods
    public DogWalker getDogWalkerById(SQLiteDatabase db, long id) {
        User user = UserSql.getUserById(db, id);

        if(user != null){
            DogWalkerSql.addDogWalkerDetails(db, (DogWalker)user);

            return (DogWalker)user;
        }
        return null;
    }

    public List<DogWalker> getAllDogWalkers(){
        List<DogWalker> dogWalkers = UserSql.getDogWalkerUsers(db);
        for(DogWalker dogWalker : dogWalkers){
            DogWalkerSql.addDogWalkerDetails(db, dogWalker);
        }
        return dogWalkers;
    }

    public void addDogWalker(DogWalker dogWalker) {
        UserSql.addToUsersTable(db, dogWalker);
        DogWalkerSql.addToDogWalkersTable(db, dogWalker);
    }
    //endregion

    //region Dog Owner Methods
    public void addDogOwner(DogOwner dogOwner) {
        UserSql.addToUsersTable(db, dogOwner);
        DogSql.addToDogsTable(db,dogOwner.getId(), dogOwner.getDog());
    }

    public DogOwner getDogOwnerById(SQLiteDatabase db, long id) {
        User user = UserSql.getUserById(db, id);
        if(user != null){
            ((DogOwner)user).setDog(DogSql.getDogByUserId(db, id));
            return ((DogOwner)user);
        }
        return null;
    }
    //endregion

    //region Request Methods
    public List<DogOwner> getRequestForDogWalker(long dogWalkerId) {
        List<Long> ids = RequestSql.getRequestForDogWalker(db, dogWalkerId);
        List<DogOwner> dogOwners = new LinkedList<>();

        for(long id : ids){
            dogOwners.add(getDogOwnerById(db, id));
        }

        return  dogOwners;
    }

    public List<DogWalker> getRequestForDogOwner(long dogOwnerId) {
        List<Long> ids = RequestSql.getRequestForDogOwner(db, dogOwnerId);
        List<DogWalker> dogWalkers = new LinkedList<>();

        for(long id : ids){
            dogWalkers.add(getDogWalkerById(db, id));
        }

        return  dogWalkers;
    }

    public List<DogOwner> getOwnersConnectToWalker(long dogWalkerId) {
        List<Long> ids = RequestSql.getOwnersConnectToWalker(db, dogWalkerId);
        List<DogOwner> dogOwners = new LinkedList<>();

        for(long id : ids){
            dogOwners.add(getDogOwnerById(db, id));
        }

        return  dogOwners;
    }

    public List<DogWalker> getWalkersConnectToOwner(long dogOwnerId) {
        List<Long> ids = RequestSql.getWalkersConnectToOwner(db, dogOwnerId);
        List<DogWalker> dogWalkers = new LinkedList<>();

        for(long id : ids){
            dogWalkers.add(getDogWalkerById(db, id));
        }

        return  dogWalkers;
    }

    public void addRequest(Request request) {
        RequestSql.addToRequestTable(db, request);
    }
    // endregion

    //region LastUpdateDate Methods
    public String getDogWalkersLastUpdateDate() {
        return LastUpdateSql.getLastUpdateDate(db, WalkerConsts.DOG_WALKERS_TABLE);
    }

    public void setDogWalkersLastUpdateDate(Date newDate){
        LastUpdateSql.setLastUpdateDate(db, WalkerConsts.DOG_WALKERS_TABLE, newDate);
    }

    public String getRequestsLastUpdateDate() {
        return  LastUpdateSql.getLastUpdateDate(db, RequestConsts.REQUESTS_TABLE);
    }

    public void setRequestsLastUpdateDate(Date newDate) {
        LastUpdateSql.setLastUpdateDate(db, RequestConsts.REQUESTS_TABLE, newDate);
    }
    //endregion

    class Helper extends SQLiteOpenHelper {
        public Helper(Context context) {
            super(context, "database.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            LastUpdateSql.create(db);
            UserSql.create(db);
            DogWalkerSql.create(db);
            DogSql.create(db);
            RequestSql.create(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            LastUpdateSql.drop(db);
            UserSql.drop(db);
            DogWalkerSql.drop(db);
            DogSql.drop(db);
            RequestSql.drop(db);

            onCreate(db);
        }
    }
}

