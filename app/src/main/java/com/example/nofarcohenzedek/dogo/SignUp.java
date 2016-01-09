package com.example.nofarcohenzedek.dogo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.nofarcohenzedek.dogo.Model.Dog;
import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.DogSize;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.User;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SignUp extends Activity {

    private RadioButton isOwner;
    private RadioButton isWalker;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String phoneNumber;
    private String city;
    private String address;
    private String dogName;
    private Long dogAge;
    private String dogPic;
    private RadioButton isSmall;
    private RadioButton isMedium;
    private RadioButton isBig;
    private Long age;
    private int priceForHour;
    private CheckBox isComfortableOnAfternoon;
    private CheckBox isComfortableOnMorning;
    private CheckBox isComfortableOnEvening;
    private static final int SELECT_PHOTO = 100;
    private String errorMessage;
    User newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    /**
     * When user choose on type of user, This method check which type he chosen
     * and visible the relevant layout
     * @param view
     */
    public void checkTypeOfUserBTNClick(View view)
    {
        // Get the value of radio buttons
        RadioButton isOwner = (RadioButton)findViewById(R.id.isOwner);
        RadioButton isWalker = (RadioButton)findViewById(R.id.isWalker);

        // Get the layouts
        LinearLayout layoutWalker = (LinearLayout)findViewById(R.id.layoutWalker);
        LinearLayout layoutOwner = (LinearLayout)findViewById(R.id.layoutOwner);

        // Check which radio chosen
        if(isOwner.isChecked())
        {
            layoutOwner.setVisibility(View.VISIBLE);
            layoutWalker.setVisibility(View.GONE);
        }
        else if(isWalker.isChecked())
        {
            layoutWalker.setVisibility(View.VISIBLE);
            layoutOwner.setVisibility(View.GONE);
        }
    }

    /**
     * This method save the new user
     * @param view
     */
    public void saveBTN(View view)
    {
        // Get all current details
        initAllDetails();

        // Check if all details are valid
        isInputValid();

        // Check the type of user, and create the object 'newUser' respectively
        if(isOwner.isChecked())
        {
            // TODO: 06/01/2016 - get the last id from DB, instead '0'
            Dog myDog = new Dog(0, dogName,
                    (isSmall.isChecked() ? DogSize.Small : (isMedium.isChecked() ? DogSize.Medium : DogSize.Large)),
                    dogAge, dogPic);

            List<Dog> allDogs = new ArrayList<>();
            allDogs.add(myDog);

            // TODO: 06/01/2016 - get the last id from DB, instead '0'
            newUser = new DogOwner(0, userName, firstName, lastName, phoneNumber, address, city, allDogs);
        }
        else
        {
            // TODO: 06/01/2016 - get the last id from DB, instead '0'
            newUser = new DogWalker(0, userName, firstName, lastName, phoneNumber, address, city, age, priceForHour,
                                    isComfortableOnMorning.isChecked(), isComfortableOnAfternoon.isChecked(),
                                    isComfortableOnEvening.isChecked());
        }

        // TODO: 06/01/2016 - save tue user on DB
    }

    /**
     * This method open the user gallery foe chosen the dog picture
     * @param view
     */
    public void openGalleryBTN(View view)
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    /**
     * Check if all details of user arn't empty
     * @return
     */
    private boolean isInputValid()
    {
//        if(!isOwner.isChecked() && !isWalker.isChecked())
//        {
//            errorMessage = "תבחר/י בבקשה את סוג המשתמש";
//        }
//        else if(firstName.isEmpty() ||
//        private String lastName;
//        private String userName;
//        private String password;
//        private String phoneNumber;
//        private String city;
//        private String address;
//        private String dogName;
//        private Long dogAge;
//        private String dogPic;
//        private RadioButton isSmall;
//        private RadioButton isMedium;
//        private RadioButton isBig;
//        private Long age;
//        private int priceForHour;)
//        {
//
//        }

        return true;
    }

    /**
     * Get all details of user
     */
    private void initAllDetails()
    {
        isOwner = (RadioButton)findViewById(R.id.isOwner);
        isWalker = (RadioButton)findViewById(R.id.isWalker);
        firstName = ((EditText)findViewById(R.id.firstName)).getText().toString();
        lastName = ((EditText)findViewById(R.id.lastName)).getText().toString();
        userName = ((EditText)findViewById(R.id.userName)).getText().toString();
        password = ((EditText)findViewById(R.id.password)).getText().toString();
        phoneNumber = ((EditText)findViewById(R.id.phoneNumber)).getText().toString();
        city = ((EditText)findViewById(R.id.city)).getText().toString();
        address = ((EditText)findViewById(R.id.address)).getText().toString();
        dogName = ((EditText)findViewById(R.id.dogName)).getText().toString();
        dogAge = Long.parseLong(((EditText) findViewById(R.id.dogAge)).getText().toString());
        isSmall = (RadioButton)findViewById(R.id.isSmall);
        isMedium = (RadioButton)findViewById(R.id.isMedium);
        isBig = (RadioButton)findViewById(R.id.isBig);
        age = Long.parseLong(((EditText) findViewById(R.id.age)).getText().toString());
        priceForHour = Integer.parseInt(((EditText) findViewById(R.id.priceForHour)).getText().toString());
        isComfortableOnAfternoon = (CheckBox)findViewById(R.id.cbx_isComfortableOnAfternoon);
        isComfortableOnMorning = (CheckBox)findViewById(R.id.cbx_isComfortableOnMorning);
        isComfortableOnEvening = (CheckBox)findViewById(R.id.cbx_isComfortableOnEvening);
    }

    /**
     * This method called when user choose picture
     * @param requestCode
     * @param resultCode
     * @param imageReturnedIntent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode)
        {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);

                    dogPic = selectedImage.getPath();
                    ((ImageView)findViewById(R.id.dogPic)).setImageBitmap(yourSelectedImage);
                }
        }
    }
}
