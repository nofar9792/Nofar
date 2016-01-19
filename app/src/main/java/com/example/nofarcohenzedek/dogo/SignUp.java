package com.example.nofarcohenzedek.dogo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nofarcohenzedek.dogo.Model.Dog;
import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.DogSize;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.User;

import java.io.FileNotFoundException;
import java.io.InputStream;

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
    private String dogAge;
    private String picRef;
    private RadioButton isSmall;
    private RadioButton isMedium;
    private RadioButton isBig;
    private String  age;
    private String priceForHour;
    private CheckBox isComfortableOnAfternoon;
    private CheckBox isComfortableOnMorning;
    private CheckBox isComfortableOnEvening;
    private static final int SELECT_PHOTO = 100;
    private String errorMessage;
    private  Bitmap dogPic;
    private boolean isSaved;

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
    public void saveBTN(View view) {
        TextView error = (TextView) findViewById(R.id.error);
        isSaved = true;

        // Get all current details
        initAllDetails();

        // Check if all details are validate
        if (isValid()) {
            // Check the type of user, and save this user on db
            if (isOwner.isChecked()) {

                // Create the dog object
                Dog dog = new Dog(dogName,
                        (isSmall.isChecked() ? DogSize.Small : (isMedium.isChecked() ? DogSize.Medium : DogSize.Large)),
                        Long.parseLong(dogAge), picRef);

                // Save the owner on DB
                try {
                    Model.getInstance().addDogOwner(userName, password, firstName, lastName, phoneNumber, address, city, dog, new Model.GetIdListener() {
                        @Override
                        public void onResult(long id, boolean isSucceed) {
                            if(isSucceed) {
                                if (picRef != null) {
                                    Model.getInstance().saveImage(picRef, dogPic, new Model.IsSucceedListener() {
                                        @Override
                                        public void onResult(boolean isSucceed) {
                                            if(isSucceed){
                                                Toast.makeText(getApplicationContext(),"שמירה בוצעה בהצלחה" , Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(getApplicationContext(), "אירעה שגיאה בעת שמירת התמונה", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }else {
                                Toast.makeText(getApplicationContext(), "אירעה שגיאה בתהליך ההרשמה", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    // Check if this userName exist
                    if (e.getMessage().equals("user already exist")){
                        error.setText("שם משתמש זה קיים כבר, אנא בחר שם משתמש אחר.");
                    }

                    isSaved = false;
                    e.printStackTrace();
                }
            } else {
                // Save the walker on DB
                try {
                    Model.getInstance().addDogWalker(userName, password, firstName, lastName, phoneNumber, address, city, Long.parseLong(age),
                            Integer.parseInt(priceForHour), isComfortableOnMorning.isChecked(), isComfortableOnAfternoon.isChecked(),
                            isComfortableOnEvening.isChecked(), new Model.GetIdListener() {
                                @Override
                                public void onResult(long id, boolean isSucceed) {
                                    if(isSucceed){
                                        Toast.makeText(getApplicationContext(),"שמירה בוצעה בהצלחה" , Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(getApplicationContext(), "אירעה שגיאה בתהליך ההרשמה", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } catch (Exception e) {
                    // Check if this userName exist
                    if (e.getMessage().equals("user already exist"))
                    {
                        error.setText("שם משתמש זה קיים כבר, אנא בחר שם משתמש אחר.");
                    }

                    isSaved = false;
                    e.printStackTrace();
                }
            }

            // Connect to dogo with the new username and password - only if user succeed to sign.
            if (isSaved) {
                Model.getInstance().logIn(userName, password, new Model.GetUserListener() {
                    @Override
                    public void onResult(User user) {
                        if (user != null)
                        {
                            Intent intent = new Intent(getApplicationContext(),ActionBarActivity.class);
                            intent.putExtra("userId", user.getId());

                            if (user instanceof DogOwner)
                            {
                                intent.putExtra("isOwner", true);
                                intent.putExtra("address", user.getAddress() + ", " + user.getCity());
                            }
                            else
                            {
                                intent.putExtra("isOwner", false);
                            }

                            startActivity(intent);
                        } else {
                            TextView error = (TextView) findViewById(R.id.error);
                            error.setText("הייתה בעיה עם ההרשמה, אנא נסה מאוחר יותר.");
                        }
                    }
                });
            }
        }
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
    private boolean isValid()
    {
        boolean isValid = true;
        errorMessage = "";

        if(!isOwner.isChecked() && !isWalker.isChecked())
        {
            errorMessage = "אנא בחר/י את סוג המשתמש";
            isValid = false;
        }
        else if(firstName.isEmpty() || lastName.isEmpty() || userName.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() ||
                city.isEmpty() || address.isEmpty())
        {
            errorMessage = "אנא מלא את כל שדות ההרשמה.";
            isValid = false;
        }
        else if (isOwner.isChecked())
        {
            if (dogName.isEmpty() || dogAge.isEmpty() ||
                    (!isBig.isChecked() && !isMedium.isChecked() && !isSmall.isChecked()))
            {
                errorMessage = "אנא מלא את פרטי הכלב.";
                isValid = false;
            }
        }

        else if (isWalker.isChecked())
        {
            if (priceForHour.isEmpty() || age.isEmpty())
            {
                errorMessage = "אנא מלא את כל הפרטים.";
                isValid = false;
            }
        }

        TextView error = (TextView)findViewById(R.id.error);
        error.setText(errorMessage);

        return isValid;
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
        dogAge = ((EditText) findViewById(R.id.dogAge)).getText().toString();
        isSmall = (RadioButton)findViewById(R.id.isSmall);
        isMedium = (RadioButton)findViewById(R.id.isMedium);
        isBig = (RadioButton)findViewById(R.id.isBig);
        age = ((EditText) findViewById(R.id.age)).getText().toString();
        priceForHour = ((EditText) findViewById(R.id.priceForHour)).getText().toString();
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
                    dogPic = BitmapFactory.decodeStream(imageStream);

                    // Save selected image, and path
                    picRef = selectedImage.getPath();
                    ((ImageView)findViewById(R.id.dogPic)).setImageBitmap(dogPic);
                }
        }
    }
}
