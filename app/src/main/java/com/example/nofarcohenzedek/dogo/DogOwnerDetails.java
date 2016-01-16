package com.example.nofarcohenzedek.dogo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.nofarcohenzedek.dogo.Model.Dog;
import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.DogSize;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DogOwnerDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_owner_details);

        Intent intent = getIntent();
        final String dogOwnerId = intent.getStringExtra("id");

        final TextView firstName = (TextView) findViewById(R.id.firstNameInDetails);
        final TextView lastName = (TextView) findViewById(R.id.lastNameInDetails);
        final TextView city = (TextView) findViewById(R.id.cityInDetails);
        final TextView address = (TextView) findViewById(R.id.addressInDetails);
        final TextView dogName = (TextView) findViewById(R.id.dogNameForDetais);
        final TextView dogAge = (TextView) findViewById(R.id.dogAgeForDetais);
        final RadioButton isBig = (RadioButton) findViewById(R.id.isBigForDetais);
        final RadioButton isMedium = (RadioButton) findViewById(R.id.isMediumForDetais);
        final RadioButton isSmall = (RadioButton) findViewById(R.id.isSmallForDetais);
        final ImageView dogPic = (ImageView) findViewById(R.id.dogPicForDetais);

        Model.getInstance().getUserById(Long.parseLong(dogOwnerId), new Model.GetUserListener() {
            @Override
            public void onResult(User user) {
                if (user instanceof DogOwner) {
                    firstName.setText(user.getFirstName());
                    lastName.setText(user.getLastName());
                    city.setText(user.getCity());
                    address.setText(user.getAddress());
                    Dog dog = ((DogOwner) user).getDog();
                    dogName.setText(dog.getName());
                    dogAge.setText(String.valueOf(dog.getAge()));

                    // Create ImageView
                    // // TODO: 16/01/2016 load the image of dog
                    //                InputStream imageStream = null;
                    //                try {
                    //                    Uri selectedImage = Uri.parse("content://com.google.android.apps.photos.contentprovider/0/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F32317/NO_TRANSFORM/66117322");
                    //                    imageStream = getContentResolver().openInputStream(selectedImage);
                    //                } catch (FileNotFoundException e) {
                    //                    e.printStackTrace();
                    //                }
                    //                dogPic.setImageBitmap(BitmapFactory.decodeStream(imageStream));
                    //
                    //                dogPic.setImageURI(Uri.fromFile(new File(dog.getPicRef())));

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
        });
    }
}
