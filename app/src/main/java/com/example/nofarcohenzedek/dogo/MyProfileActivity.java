package com.example.nofarcohenzedek.dogo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.nofarcohenzedek.dogo.Model.Dog;
import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.DogSize;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.User;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MyProfileActivity extends Activity {

    Boolean isOwner;
    Long id;
    String userName;

    EditText firstName;
    EditText lastName;
    EditText phone;
    EditText address;
    EditText city;
    EditText age;
    EditText price;
    CheckBox morning;
    CheckBox afternoon;
    CheckBox evening;
    EditText dogName;
    RadioButton isBig;
    RadioButton isMedium;
    RadioButton isSmall;
    EditText dogAge;
    String dogPic;

    private String errorMessage;
    static final int SELECT_PHOTO = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        setActionBar((Toolbar)findViewById(R.id.myProfileToolBar));
        getActionBar().setDisplayShowTitleEnabled(false);

        isOwner = getIntent().getBooleanExtra("isOwner", false);

        if (!isOwner){
            findViewById(R.id.dogWalkerSectionInMyProfile).setVisibility(View.VISIBLE);
        }
        else
        {
            findViewById(R.id.dogOwnerSectionInMyProfile).setVisibility(View.VISIBLE);
        }

        Model.getInstance().getCurrentUser(new Model.GetUserListener() {
            @Override
            public void onResult(User user)
            {
                id = user.getId();
                userName = user.getUserName();

                firstName = (EditText) findViewById(R.id.firstNameMP);
                lastName = (EditText) findViewById(R.id.lastNameMP);
                phone = (EditText) findViewById(R.id.phoneNumberMP);
                address = (EditText) findViewById(R.id.addressMP);
                city = (EditText) findViewById(R.id.cityMP);
                age = (EditText) findViewById(R.id.ageMP);
                price = (EditText) findViewById(R.id.priceForHourMP);
                morning = (CheckBox) findViewById(R.id.cbx_isComfortableOnMorningMP);
                afternoon = (CheckBox) findViewById(R.id.cbx_isComfortableOnAfternoonMP);
                evening = (CheckBox) findViewById(R.id.cbx_isComfortableOnEveningMP);
                dogName = (EditText) findViewById(R.id.dogNameMP);
                isBig = (RadioButton) findViewById(R.id.isBigMP);
                isMedium = (RadioButton) findViewById(R.id.isMediumMP);
                isSmall = (RadioButton) findViewById(R.id.isSmallMP);
                dogAge = (EditText) findViewById(R.id.dogAgeMP);

                firstName.setText(user.getFirstName());
                lastName.setText(user.getLastName());
                phone.setText(user.getPhoneNumber());
                address.setText(user.getAddress());
                city.setText(user.getCity());

                if (!isOwner){
                    age.setText(Long.toString(((DogWalker)user).getAge()));
                    price.setText(Long.toString(((DogWalker)user).getPriceForHour()));
                    morning.setChecked(((DogWalker) user).isComfortableOnMorning());
                    afternoon.setChecked(((DogWalker)user).isComfortableOnAfternoon());
                    evening.setChecked(((DogWalker)user).isComfortableOnEvening());
                }
                else
                {
                    dogName.setText(((DogOwner) user).getDog().getName());
                    DogSize size = ((DogOwner)user).getDog().getSize();
                    isBig.setChecked((size == DogSize.Large ? true : false));
                    isMedium.setChecked((size == DogSize.Medium ? true : false));
                    isSmall.setChecked((size == DogSize.Small ? true : false));
                    dogAge.setText(Long.toString(((DogOwner) user).getDog().getAge()));

                    // TODO : DOG PIC IS EMPTY! LOAD PIC!
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu)
    {
        if (isOwner)
        {
            getMenuInflater().inflate(R.menu.menu_prime_dog_owner, menu);
        }
        else
        {
            getMenuInflater().inflate(R.menu.menu_prime_dog_walker, menu);
        }

        return true;
    }

    public void saveChangesClick(View view)
    {
        if (isValid())
        {
            String firstNameVal = firstName.getText().toString();
            String lastNameVal = lastName.getText().toString();
            String phoneVal = phone.getText().toString();
            String addressVal = address.getText().toString();
            String cityVal = city.getText().toString();

            if (isOwner)
            {
                String dogNameVal = dogName.getText().toString();
                Long dogAgeVal = Long.valueOf(dogAge.getText().toString());
                DogSize sizeVal = (isSmall.isChecked() ? DogSize.Small : (isMedium.isChecked() ? DogSize.Medium : DogSize.Large));

                Model.getInstance().updateDogOwner(new DogOwner(id, userName, firstNameVal, lastNameVal, phoneVal, addressVal, cityVal,
                        new Dog(dogNameVal, sizeVal, dogAgeVal, dogPic)));
            }
            else
            {
                Long ageVal = Long.valueOf(age.getText().toString());
                int priceVal = Integer.valueOf(price.getText().toString());
                Boolean morningVal = morning.isChecked();
                Boolean noonVal = afternoon.isChecked();
                Boolean eveningVal = evening.isChecked();

                Model.getInstance().updateDogWalker(new DogWalker(id, userName, firstNameVal, lastNameVal, phoneVal, addressVal, cityVal,
                        ageVal, priceVal, morningVal, noonVal, eveningVal));
            }

            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent = null;

        if (id == R.id.searchDW) {
            intent = new Intent(this, SearchActivity.class);
        } else if (id == R.id.map) {
            intent = new Intent(this, MapsActivity.class);
        } else if (id == R.id.tripsReport) {
            intent = new Intent(this, TripsReportActivity.class);
        } else if (id == R.id.messages) {
            intent = new Intent(this, MessagesActivity.class);
        } else if (id == R.id.dogsList) {
            intent = new Intent(this, DogsListActivity.class);

        }

        intent.putExtra("isOwner", isOwner);
        intent.putExtra("userId", getIntent().getLongExtra("userId",0));
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

    public void openGalleryBTN(View view)
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }
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
                    ((ImageView)findViewById(R.id.dogPicMP)).setImageBitmap(yourSelectedImage);
                }
        }
    }

    private boolean isValid()
    {
        boolean isValid = true;
        errorMessage = "";

        if(firstName.getText().toString().isEmpty() ||
            lastName.getText().toString().isEmpty() ||
            phone.getText().toString().isEmpty() ||
                city.getText().toString().isEmpty() ||
                address.getText().toString().isEmpty())
        {
            errorMessage = "אנא מלא את כל שדות ההרשמה.";
            isValid = false;
        }
        else if (isOwner)
        {
            if (dogName.getText().toString().isEmpty() || dogAge.getText().toString().isEmpty() || dogPic.isEmpty() ||
                    (!isBig.isChecked() && !isMedium.isChecked() && !isSmall.isChecked()))
            {
                errorMessage = "אנא מלא את פרטי הכלב.";
                isValid = false;
            }
        }
        else
        {
            if (price.getText().toString().isEmpty() || age.getText().toString().isEmpty())
            {
                errorMessage = "אנא מלא את כל הפרטים.";
                isValid = false;
            }
        }

        TextView error = (TextView)findViewById(R.id.error);
        error.setText(errorMessage);

        return isValid;
    }
}
