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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nofarcohenzedek.dogo.Model.Dog;
import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.DogSize;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.User;
import com.example.nofarcohenzedek.dogo.Model.Utilities;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class SignUpActivity extends Activity {

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
    private String age;
    private String priceForHour;
    private CheckBox isComfortable6To8;
    private CheckBox isComfortable8To10;
    private CheckBox isComfortable10To12;
    private CheckBox isComfortable12To14;
    private CheckBox isComfortable14To16;
    private CheckBox isComfortable16To18;
    private CheckBox isComfortable18To20;
    private CheckBox isComfortable20To22;
    private static final int SELECT_PHOTO = 100;
    private String errorMessage;
    private Bitmap dogPic;
    private boolean isSaved;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    /**
     * When user choose on type of user, This method check which type he chosen
     * and visible the relevant layout
     *
     * @param view
     */
    public void checkTypeOfUserBTNClick(View view) {
        // Get the value of radio buttons
        RadioButton isOwner = (RadioButton) findViewById(R.id.isOwner);
        RadioButton isWalker = (RadioButton) findViewById(R.id.isWalker);

        // Get the layouts
        LinearLayout layoutWalker = (LinearLayout) findViewById(R.id.layoutWalker);
        LinearLayout layoutOwner = (LinearLayout) findViewById(R.id.layoutOwner);

        progressBar = (ProgressBar) findViewById(R.id.signUpProgressBar);

        // Check which radio chosen
        if (isOwner.isChecked()) {
            layoutOwner.setVisibility(View.VISIBLE);
            layoutWalker.setVisibility(View.GONE);
        } else if (isWalker.isChecked()) {
            layoutWalker.setVisibility(View.VISIBLE);
            layoutOwner.setVisibility(View.GONE);
        }
    }

    /**
     * This method save the new user
     *
     * @param view
     */
    public void saveBTN(View view) {
        final TextView error = (TextView) findViewById(R.id.error);
        isSaved = false;

        // Get all current details
        initAllDetails();

        // Check if all details are validate
        if (isValid()) {
            // Check the type of user, and save this user on db
            progressBar.setVisibility(View.VISIBLE);
            if (isOwner.isChecked()) {

                // Create the dog object
                Dog dog = new Dog(dogName,
                        (isSmall.isChecked() ? DogSize.Small : (isMedium.isChecked() ? DogSize.Medium : DogSize.Large)),
                        Long.parseLong(dogAge), picRef);

                // Save the owner on DB
                Model.getInstance().addDogOwner(userName, password, firstName, lastName, phoneNumber, address, city, dog,
                        isComfortable6To8.isChecked(), isComfortable8To10.isChecked(), isComfortable10To12.isChecked(),
                        isComfortable12To14.isChecked(), isComfortable14To16.isChecked(), isComfortable16To18.isChecked(),
                        isComfortable18To20.isChecked(), isComfortable20To22.isChecked(), new Model.GetIdListener() {
                            @Override
                            public void onResult(long id, boolean isSucceed) {
                                if (isSucceed) {
                                    if (picRef != null) {
                                        Model.getInstance().saveImage(picRef, dogPic, new Model.IsSucceedListener() {
                                            @Override
                                            public void onResult(boolean isSucceed) {
                                                if (!isSucceed) {
                                                    Toast.makeText(getApplicationContext(), "אירעה שגיאה בעת שמירת התמונה", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                    Model.getInstance().logIn(userName, password, new Model.GetUserListener() {
                                        @Override
                                        public void onResult(User user) {
                                            if (user != null) {
                                                Intent intent = new Intent(getApplicationContext(), ActionBarActivity.class);
                                                intent.putExtra("userId", user.getId());

                                                if (user instanceof DogOwner) {
                                                    intent.putExtra("isOwner", true);
                                                    intent.putExtra("address", user.getAddress() + ", " + user.getCity());
                                                } else {
                                                    intent.putExtra("isOwner", false);
                                                }

                                                startActivity(intent);
                                            } else {
                                                TextView error = (TextView) findViewById(R.id.error);
                                                error.setText("אירעה שגיאה בעת ההתחברות. ");
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });

                                } else {
                                    Toast.makeText(getApplicationContext(), "אירעה שגיאה בתהליך ההרשמה", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        }, new Model.ExceptionListener() {
                            @Override
                            public void onResult(String message) {
                                if (message.equals("user already exist")) {
                                    error.setText("שם משתמש זה קיים כבר, אנא בחר שם משתמש אחר.");
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });

            } else {
                // Save the walker on DB
                Model.getInstance().addDogWalker(userName, password, firstName, lastName, phoneNumber, address, city, Long.parseLong(age),
                        Integer.parseInt(priceForHour), isComfortable6To8.isChecked(), isComfortable8To10.isChecked(), isComfortable10To12.isChecked(),
                        isComfortable12To14.isChecked(), isComfortable14To16.isChecked(), isComfortable16To18.isChecked(),
                        isComfortable18To20.isChecked(), isComfortable20To22.isChecked(), new Model.GetIdListener() {
                            @Override
                            public void onResult(long id, boolean isSucceed) {
                                if (isSucceed) {
                                    isSaved = true;
                                    Toast.makeText(getApplicationContext(), "שמירה בוצעה בהצלחה", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "אירעה שגיאה בתהליך ההרשמה", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);

                                }

                                // Connect to dogo with the new username and password - only if user succeed to sign.
                                if (isSaved) {
                                    Model.getInstance().logIn(userName, password, new Model.GetUserListener() {
                                        @Override
                                        public void onResult(User user) {
                                            if (user != null) {
                                                Intent intent = new Intent(getApplicationContext(), ActionBarActivity.class);
                                                intent.putExtra("userId", user.getId());

                                                if (user instanceof DogOwner) {
                                                    intent.putExtra("isOwner", true);
                                                    intent.putExtra("address", user.getAddress() + ", " + user.getCity());
                                                } else {
                                                    intent.putExtra("isOwner", false);
                                                }

                                                startActivity(intent);
                                            } else {
                                                TextView error = (TextView) findViewById(R.id.error);
                                                error.setText("הייתה בעיה עם ההרשמה, אנא נסה מאוחר יותר.");
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                }
                            }
                        }, new Model.ExceptionListener() {
                            @Override
                            public void onResult(String message) {
                                if (message.equals("user already exist")) {
                                    error.setText("שם משתמש זה קיים כבר, אנא בחר שם משתמש אחר.");
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        }

        progressBar.setVisibility(View.GONE);

    }

    /**
     * This method open the user gallery foe chosen the dog picture
     *
     * @param view
     */
    public void openGalleryBTN(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    /**
     * Check if all details of user arn't empty
     *
     * @return
     */
    private boolean isValid() {
        boolean isValid = true;
        errorMessage = "";

        if (!isOwner.isChecked() && !isWalker.isChecked()) {
            errorMessage = "אנא בחר/י את סוג המשתמש";
            isValid = false;
        } else if (firstName.isEmpty() || lastName.isEmpty() || userName.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() ||
                city.isEmpty() || address.isEmpty()) {
            errorMessage = "אנא מלא את כל שדות ההרשמה.";
            isValid = false;
        }else if (Utilities.getLocationFromAddress(address,getApplicationContext()) == null){
            errorMessage = "כתובת לא ולידית. אנא ודא איות נכון.";
            isValid = false;
        }
        else if (isOwner.isChecked()) {
            if (dogName.isEmpty() || dogAge.isEmpty() ||
                    (!isBig.isChecked() && !isMedium.isChecked() && !isSmall.isChecked())) {
                errorMessage = "אנא מלא את פרטי הכלב.";
                isValid = false;
            }
        } else if (isWalker.isChecked()) {
            if (priceForHour.isEmpty() || age.isEmpty()) {
                errorMessage = "אנא מלא את כל הפרטים.";
                isValid = false;
            }
        }

        TextView error = (TextView) findViewById(R.id.error);
        error.setText(errorMessage);

        return isValid;
    }

    /**
     * Get all details of user
     */
    private void initAllDetails() {
        isOwner = (RadioButton) findViewById(R.id.isOwner);
        isWalker = (RadioButton) findViewById(R.id.isWalker);
        firstName = ((EditText) findViewById(R.id.firstName)).getText().toString();
        lastName = ((EditText) findViewById(R.id.lastName)).getText().toString();
        userName = ((EditText) findViewById(R.id.userName)).getText().toString();
        password = ((EditText) findViewById(R.id.password)).getText().toString();
        phoneNumber = ((EditText) findViewById(R.id.phoneNumber)).getText().toString();
        city = ((EditText) findViewById(R.id.city)).getText().toString();
        address = ((EditText) findViewById(R.id.address)).getText().toString();
        dogName = ((EditText) findViewById(R.id.dogName)).getText().toString();
        dogAge = ((EditText) findViewById(R.id.dogAge)).getText().toString();
        isSmall = (RadioButton) findViewById(R.id.isSmall);
        isMedium = (RadioButton) findViewById(R.id.isMedium);
        isBig = (RadioButton) findViewById(R.id.isBig);
        age = ((EditText) findViewById(R.id.age)).getText().toString();
        priceForHour = ((EditText) findViewById(R.id.priceForHour)).getText().toString();
        isComfortable6To8 = (CheckBox) findViewById(R.id.checkbox6To8);
        isComfortable8To10 = (CheckBox) findViewById(R.id.checkbox8To10);
        isComfortable10To12 = (CheckBox) findViewById(R.id.checkbox10To12);
        isComfortable12To14 = (CheckBox) findViewById(R.id.checkbox12To14);
        isComfortable14To16 = (CheckBox) findViewById(R.id.checkbox14To16);
        isComfortable16To18 = (CheckBox) findViewById(R.id.checkbox16To18);
        isComfortable18To20 = (CheckBox) findViewById(R.id.checkbox18To20);
        isComfortable20To22 = (CheckBox) findViewById(R.id.checkbox20To22);
    }

    /**
     * This method called when user choose picture
     *
     * @param requestCode
     * @param resultCode
     * @param imageReturnedIntent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    dogPic = BitmapFactory.decodeStream(imageStream);

                    // Save selected image, and path
                    picRef = String.valueOf(selectedImage.getPath().hashCode());
                    ((ImageView) findViewById(R.id.dogPic)).setImageBitmap(dogPic);
                }
        }
    }
}
