package com.example.nofarcohenzedek.dogo.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.example.nofarcohenzedek.dogo.Model.Parse.ModelParse;
import com.example.nofarcohenzedek.dogo.Model.Sql.ModelSql;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Nofar Cohen Zedek on 02-Jan-16.
 */
public class Model {
    //region Private Members
    private ModelSql modelSql;
    private ModelParse modelParse;
    private String baseUrl = "http://db.cs.colman.ac.il/DogoServer/api/";
    private XStream xstream = new XStream(new DomDriver());
    private static final Model instance = new Model();
    //endregion

    //region Singletone Methods
    private Model() {
        xstream.alias("User", User.class);
        xstream.alias("ArrayOfDogWalker", ArrayOfDogWalker.class);
        xstream.addImplicitCollection(ArrayOfDogWalker.class, "list");
        xstream.alias("ArrayOfDogOwner", ArrayOfDogOwner.class);
        xstream.addImplicitCollection(ArrayOfDogOwner.class, "list");
        xstream.alias("DogWalker", DogWalker.class);
        xstream.alias("DogOwner", DogOwner.class);
    }

    public void init(Context context) {
//        if(modelSql == null){
//            modelSql = new ModelSql(context);
//        }
//        if(modelParse == null){
//            modelParse = new ModelParse(context);
//        }
    }

    public static Model getInstance() {
        return instance;
    }
    //endregion

    //region User Methods
    public void logIn(String userName, String password, GetUserListener listener) {
        Object dogWalker = tryLoginDogWalker(userName, password);
        if(dogWalker != null){
            if(dogWalker instanceof DogWalker){
                listener.onResult((DogWalker)dogWalker);
            }else {
                Object dogOwner = tryLoginDogOwner(userName, password);
                if(dogOwner != null){
                    if(dogOwner instanceof DogOwner){
                        listener.onResult((DogOwner)dogOwner);
                    }else {
                        listener.onResult(null);
                    }
                }
            }
        }
    }

    private Object tryLoginDogWalker(String userName, String password){
        String url = baseUrl + "DogWalkers?userName=" + userName;

        try {
            Object dogWalkers = new HttpAsyncTask().execute(url).get();

            // If it is not null is means that now we are with the second thread (not the main thread)
            if(dogWalkers != null &&
               dogWalkers instanceof ArrayOfDogWalker &&
               ((ArrayOfDogWalker)dogWalkers).size() != 0 &&
               ((ArrayOfDogWalker)dogWalkers).get(0).getPassword().equals(password)){
               return ((ArrayOfDogWalker)dogWalkers).get(0);
            }else {
                return new Object();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object tryLoginDogOwner(String userName, String password){
        String url = baseUrl + "DogOwners?userName=" + userName;

        try {
            Object dogOwners = new HttpAsyncTask().execute(url).get();

            // If it is not null is means that now we are with the second thread (not the main thread)
            if(dogOwners != null){
                if(dogOwners instanceof ArrayOfDogOwner &&
                   ((ArrayOfDogOwner)dogOwners).size() != 0 &&
                   ((ArrayOfDogOwner)dogOwners).get(0).getPassword().equals(password)) {
                    return ((ArrayOfDogOwner)dogOwners).get(0);
                }else {
                    return new Object();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // This line will execute only in the main thread
        return null;
    }

    public void logOut() {
        //modelParse.logOut();
    }

    public void getUserById(long userId, final Model.GetUserListener listener) {
        Object dogWalker = tryGetDogWalkerById(userId);
        if(dogWalker != null){
            if(dogWalker instanceof DogWalker){
                listener.onResult((DogWalker)dogWalker);
            }else {
                Object dogOwner = tryGetDogOwnerById(userId);
                if(dogOwner != null){
                    if(dogOwner instanceof DogOwner){
                        listener.onResult((DogOwner)dogOwner);
                    }else {
                        listener.onResult(null);
                    }
                }
            }
        }
    }

    private Object tryGetDogWalkerById(long userId){
        String url = baseUrl + "DogWalkers/" + userId;

        try {
            Object dogWalker = new HttpAsyncTask().execute(url).get();

            // If it is not null is means that now we are with the second thread (not the main thread)
            if(dogWalker != null &&
               dogWalker instanceof DogWalker){
                return dogWalker;
            }else {
                return new Object();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object tryGetDogOwnerById(long userId){
        String url = baseUrl + "DogOwners/" + userId;

        try {
            Object dogOwner = new HttpAsyncTask().execute(url).get();

            // If it is not null is means that now we are with the second thread (not the main thread)
            if(dogOwner != null &&
               dogOwner instanceof DogOwner){
                return dogOwner;
            }else {
                return new Object();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //endregion

    //region Dog Walker Methods
    public void getAllDogWalkers(final Model.GetDogWalkersListener listener) {
        String url = baseUrl + "DogWalkers";

        try {
            Object dogWalkers = new HttpAsyncTask().execute(url).get();

            // If it is not null is means that now we are with the second thread (not the main thread)
            if(dogWalkers != null &&
               dogWalkers instanceof ArrayOfDogWalker){

                listener.onResult(((ArrayOfDogWalker)dogWalkers).getList());

            }else {
                // !!!
                listener.onResult((LinkedList<DogWalker>)new Object());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        final List<DogWalker> dogWalkersResult = new LinkedList<>();
//        listener.onResult(dogWalkersResult);
    }
    
    public void addDogWalker(String userName, String password, String firstName, String lastName, String phoneNumber,
                             String address, String city, long age, int priceForHour, boolean isComfortable6To8, boolean isComfortable8To10, boolean isComfortable10To12,
                             boolean isComfortable12To14, boolean isComfortable14To16, boolean isComfortable16To18, boolean isComfortable18To20,
                             boolean isComfortable20To22, GetIdListener listener,  ExceptionListener exceptionListener) {
       // todo: send request to server
    }

    public void updateDogWalker(DogWalker dogWalker, IsSucceedListener listener){
        // todo: send request to server
//        modelParse.updateDogWalker(dogWalker, listener);
    }

    //endregion

    //region Dog Owner Methods
    public void addDogOwner(String userName, String password, String firstName, String lastName, String phoneNumber,
                            String address, String city, Dog dog, boolean isComfortable6To8, boolean isComfortable8To10, boolean isComfortable10To12,
                            boolean isComfortable12To14, boolean isComfortable14To16, boolean isComfortable16To18, boolean isComfortable18To20,
                            boolean isComfortable20To22, GetIdListener listener, ExceptionListener exceptionListener) {
        // todo: send request to server

    }

    public void updateDogOwner(DogOwner dogOwner, IsSucceedListener listener){
        // todo: send request to server
//        modelParse.updateDogOwner(dogOwner, listener);
    }

    //endregion

    //region Trip Methods
    public void getTripsByDogOwnerId(long dogOwnerId, final GetTripsListener listener) {
        modelParse.getTripsByDogOwnerId(dogOwnerId, listener);
    }

    public void getTripsByDogWalkerId(long dogWalkerId, final GetTripsListener listener) {
        modelParse.getTripsByDogWalkerId(dogWalkerId, listener);
    }

    public void startTrip(long dogOwnerId, long dogWalkerId, GetIdListener listener) {
        modelParse.startTrip(dogOwnerId, dogWalkerId, listener);
    }

    public void endTrip(long tripId, IsSucceedListener listener) {
        modelParse.endTrip(tripId, listener);
    }

    public void payTrip(long tripId, IsSucceedListener listener) {
        modelParse.payTrip(tripId, listener);
    }
    //endregion

    //region Request Methods
    public void addRequest(long dogOwnerId, long dogWalkerId, IsSucceedListener listener) {
        modelParse.addRequest(dogOwnerId, dogWalkerId, listener);
    }

    public void acceptRequest(long dogOwnerId, long dogWalkerId, IsSucceedListener listener) {
        modelParse.acceptRequest(dogOwnerId, dogWalkerId, listener);
    }

    public void declineRequest(long dogOwnerId, long dogWalkerId, IsSucceedListener listener) {
        modelParse.declineRequest(dogOwnerId, dogWalkerId, listener);
    }

    // Connections between walker to some owners
    public void getOwnersConnectToWalker(final long dogWalkerId, final GetDogOwnersListener listener) {
        final List<DogOwner> dogOwnersResult = new LinkedList<>();
        listener.onResult(dogOwnersResult);
    }

    // Messages for dog walker
    public void getRequestForDogWalker(final long dogWalkerId, final GetDogOwnersListener listener) {
        final List<DogOwner> dogOwnersResult = new LinkedList<>();
        listener.onResult(dogOwnersResult);
    }

    // Messages of dog owner
    public void getRequestOfDogOwner(final long dogOwnerId, final GetDogWalkersListener listener) {
        final List<DogWalker> dogWalkersResult = new LinkedList<>();
        listener.onResult(dogWalkersResult);
    }
    //endregion

    //region Image Methods
    public void saveImage(String imageName, Bitmap picture, IsSucceedListener listener){
        modelParse.saveImage(imageName, picture, listener);
    }

    public void getImage(String imageName, Model.GetBitmapListener listener){
        modelParse.getImage(imageName, listener);
    }
    //endregion

    //region Interfaces
    public interface GetUserListener {
        void onResult(User user);
    }

    public interface GetDogListener {
        void onResult(Dog dog);
    }
    public interface GetDogWalkersListener {
        void onResult(List<DogWalker> dogWalkers);
    }

    public interface GetDogOwnersListener {
        void onResult(List<DogOwner> dogOwners);
    }

    public interface GetTripsListener {
        void onResult(List<Trip> trips);
    }

    public interface GetRequestsListener {
        void onResult(List<Request> requests);
    }

    public interface GetBitmapListener {
        void onResult(Bitmap picture);
    }

    public interface IsSucceedListener {
        void onResult(boolean isSucceed);
    }

    public interface GetIdListener {
        void onResult(long id, boolean isSucceed);
    }

    public interface ExceptionListener {
        void onResult(String message);
    }

    //endregion

    public Object GET(String url){
        InputStream inputStream = null;
        String xml = "";
        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpGet);

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null) {
                xml = convertInputStreamToString(inputStream);
                //xml = removeRedudantParts(xml);
            }
            else
                xml = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        if (xml.equals("")) {
            return new Object();
        }

        Object object = new Object();

        try {
            object = xstream.fromXML(xml);
        }catch (Exception e){
            e.printStackTrace();
        }

        return object;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    private String removeRedudantParts(String xml) {
        xml = xml.replaceFirst("<ArrayOfDogWalker xmlns:i=\\\"http://www.w3.org/2001/XMLSchema-instance\\\" xmlns=\\\"http://schemas.datacontract.org/2004/07/DoGoService.DataObjects\">", "");
        xml = xml.replaceFirst("</ArrayOfDogWalker>", "");
        xml = xml.replaceFirst("<ArrayOfDogWalker xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://schemas.datacontract.org/2004/07/DoGoService.DataObjects\" />", "");
        xml = xml.replaceFirst("<ArrayOfDogOwner xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://schemas.datacontract.org/2004/07/DoGoService.DataObjects\">", "");
        xml = xml.replaceFirst("</ArrayOfDogOwner>", "");
        xml = xml.replaceFirst("<ArrayOfDogOwner xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://schemas.datacontract.org/2004/07/DoGoService.DataObjects\" />", "");
        return xml;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... urls) {
            return GET(urls[0]);
        }
    }
}

