package com.example.nofarcohenzedek.dogo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.example.nofarcohenzedek.dogo.Model.Dog;
import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.DogSize;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.Trip;
import com.example.nofarcohenzedek.dogo.Model.User;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Model.getInstance().init(getApplicationContext());

        //2
//        Model.getInstance().getOwnersConnectToWalker(2, new Model.GetDogOwnersListener() {
//            @Override
//            public void onResult(List<DogOwner> dogOwners) {
//                Model.getInstance().getOwnersConnectToWalker(2, new Model.GetDogOwnersListener() {
//                    @Override
//                    public void onResult(List<DogOwner> dogOwners) {
//                        String s = dogOwners.get(0).getCity();
//
//                    }
//                });
//
//
//            }
//        });

        //2
//        Model.getInstance().getWalkersConnectToOwner(4, new Model.GetDogWalkersListener() {
//            @Override
//            public void onResult(List<DogWalker> dogWalkers) {
//                Model.getInstance().getWalkersConnectToOwner(4, new Model.GetDogWalkersListener() {
//                    @Override
//                    public void onResult(List<DogWalker> dogWalkers) {
//                        String s = dogWalkers.get(0).getCity();
//
//                    }
//                });
//
//            }
//        });

        //3
//        Model.getInstance().getRequestForDogWalker(3, new Model.GetDogOwnersListener() {
//            @Override
//            public void onResult(List<DogOwner> dogOwners) {
//                Model.getInstance().getRequestForDogWalker(3, new Model.GetDogOwnersListener() {
//                    @Override
//                    public void onResult(List<DogOwner> dogOwners) {
//                        String s = dogOwners.get(0).getCity();
//                    }
//                });
//            }
//        });

//        //2
        Model.getInstance().getRequestOfDogOwner(9, new Model.GetDogWalkersListener() {
            @Override
            public void onResult(List<DogWalker> dogWalkers) {
                Model.getInstance().getRequestOfDogOwner(9, new Model.GetDogWalkersListener() {
                    @Override
                    public void onResult(List<DogWalker> dogWalkers) {
                        String s = dogWalkers.get(0).getCity();
                    }
                });
            }
        });

        ImageButton signUp = (ImageButton) findViewById(R.id.sign_up_button);
    }

    public void signUpBTNClick(View view)
    {
        Intent intent = new Intent(this, SignUp.class);
        startActivityForResult(intent, 1);
    }


    public void loginBTNClick(View view)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
