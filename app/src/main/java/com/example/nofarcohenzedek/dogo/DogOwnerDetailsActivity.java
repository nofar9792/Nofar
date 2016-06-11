package com.example.nofarcohenzedek.dogo;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
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

public class DogOwnerDetailsActivity extends Activity {

    private ProgressBar progressBar;
    private Long walkerId;
    private Long ownerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_owner_details);

        progressBar = (ProgressBar) findViewById(R.id.dogOwnerProgressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);

        ownerId = getIntent().getLongExtra("ownerId", 0);
        walkerId = getIntent().getLongExtra("walkerId", 0);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Guttman Yad-Brush.ttf");

        final TextView firstName = (TextView) findViewById(R.id.firstNameInDetails);
        firstName.setTypeface(font);
        final TextView lastName = (TextView) findViewById(R.id.lastNameInDetails);
        final TextView city = (TextView) findViewById(R.id.cityInDetails);
        final TextView address = (TextView) findViewById(R.id.addressInDetails);
        final TextView dogName = (TextView) findViewById(R.id.dogNameForDetais);
        final TextView dogAge = (TextView) findViewById(R.id.dogAgeForDetais);
        final RadioButton isBig = (RadioButton) findViewById(R.id.isBigForDetais);
        final RadioButton isMedium = (RadioButton) findViewById(R.id.isMediumForDetais);
        final RadioButton isSmall = (RadioButton) findViewById(R.id.isSmallForDetais);
        final ImageView dogPic = (ImageView) findViewById(R.id.dogPicForDetais);
        final CheckBox isComfortable6To8 = (CheckBox) findViewById(R.id.checkbox6To8);
        final CheckBox isComfortable8To10 = (CheckBox) findViewById(R.id.checkbox8To10);
        final CheckBox isComfortable10To12 = (CheckBox) findViewById(R.id.checkbox10To12);
        final CheckBox isComfortable12To14 = (CheckBox) findViewById(R.id.checkbox12To14);
        final CheckBox isComfortable14To16 = (CheckBox) findViewById(R.id.checkbox14To16);
        final CheckBox isComfortable16To18 = (CheckBox) findViewById(R.id.checkbox16To18);
        final CheckBox isComfortable18To20 = (CheckBox) findViewById(R.id.checkbox18To20);
        final CheckBox isComfortable20To22 = (CheckBox) findViewById(R.id.checkbox20To22);

        // Get the dog owner
        Model.getInstance().getUserById(ownerId, new Model.GetUserListener() {
            @Override
            public void onResult(User user) {
                if (user != null) {
                    if (user instanceof DogOwner) {
                        firstName.setText(user.getFirstName());
                        lastName.setText(user.getLastName());
                        city.setText(user.getCity());
                        address.setText(user.getAddress());
                        Dog dog = ((DogOwner) user).getDog();
                        dogName.setText(dog.getName());
                        dogAge.setText(String.valueOf(dog.getAge()));
                        isComfortable6To8.setChecked(user.isComfortable6To8());
                        isComfortable8To10.setChecked(user.isComfortable8To10());
                        isComfortable10To12.setChecked(user.isComfortable10To12());
                        isComfortable12To14.setChecked(user.isComfortable12To14());
                        isComfortable14To16.setChecked(user.isComfortable14To16());
                        isComfortable16To18.setChecked(user.isComfortable16To18());
                        isComfortable18To20.setChecked(user.isComfortable18To20());
                        isComfortable20To22.setChecked(user.isComfortable20To22());

                        // Load the picture of dog
                        final String picRef = dog.getPicRef();

                        if (picRef != null) {
                            if (Utilities.isFileExistInDevice(picRef)) {
                                dogPic.setImageBitmap(Utilities.loadImageFromDevice(picRef));
                            } else {
                                Model.getInstance().getImage(picRef, new Model.GetBitmapListener() {
                                    @Override
                                    public void onResult(Bitmap picture) {
                                        dogPic.setImageBitmap(picture);
                                        Utilities.saveImageOnDevice(picRef, picture);
                                    }
                                });
                            }
                        }

                        // Check which size the dog is.
                        if (dog.getSize().name().equals(DogSize.Large.name())) {
                            isBig.setChecked(true);
                            isMedium.setChecked(false);
                            isSmall.setChecked(false);
                        } else if (dog.getSize().name().equals(DogSize.Medium.name())) {
                            isBig.setChecked(false);
                            isMedium.setChecked(true);
                            isSmall.setChecked(false);
                        } else if (dog.getSize().name().equals(DogSize.Small.name())) {
                            isBig.setChecked(false);
                            isMedium.setChecked(false);
                            isSmall.setChecked(true);
                        }
                    }
                }

                progressBar.setVisibility(View.GONE);
            }
        });

        final Button sendRequestButton = (Button) findViewById(R.id.sendRequest);

        Model.getInstance().checkRequestExist(ownerId, walkerId, new Model.IsSucceedListener() {
            @Override
            public void onResult(boolean isRequestExist) {
               if(!isRequestExist){
                   sendRequestButton.setVisibility(View.VISIBLE);
            }
        }});
    }

    public void sendRequestClick(View view)
    {
        Model.getInstance().addRequest(ownerId, walkerId, new Model.IsSucceedListener() {
            @Override
            public void onResult(boolean isSucceed) {
                if (isSucceed) {
                    Toast.makeText(getApplicationContext(), "בקשה נשלחה בהצלחה", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext().getApplicationContext(), "אירעה שגיאה, אנא נסה שוב", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
