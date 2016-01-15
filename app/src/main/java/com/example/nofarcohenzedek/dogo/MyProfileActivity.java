package com.example.nofarcohenzedek.dogo;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.GpsStatus;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class MyProfileActivity extends Fragment {
    private Boolean isOwner;
    private Long id;
    private String userName;
    private EditText firstName;
    private EditText lastName;
    private EditText phone;
    private EditText address;
    private EditText city;
    private EditText age;
    private EditText price;
    private CheckBox morning;
    private CheckBox afternoon;
    private CheckBox evening;
    private EditText dogName;
    private RadioButton isBig;
    private RadioButton isMedium;
    private RadioButton isSmall;
    private EditText dogAge;
    private String dogPic;
    private String errorMessage;
    private static final int SELECT_PHOTO = 100;
    private ProgressBar progressBar;
    View currentView;

    public interface Listener
    {
        void onFinish();
    }

    Listener listener;

    public void setListener (Listener listener){this.listener = listener;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_my_profile, container, false);
        currentView = view;
        super.onCreateView(inflater, container, savedInstanceState);

        Bundle args = getArguments();
        progressBar = (ProgressBar) view.findViewById(R.id.myProfileProgressBar);

        isOwner = args.getBoolean("isOwner");

        if (!isOwner)
        {
            view.findViewById(R.id.dogWalkerSectionInMyProfile).setVisibility(View.VISIBLE);
        }
        else
        {
            view.findViewById(R.id.dogOwnerSectionInMyProfile).setVisibility(View.VISIBLE);
        }

        Model.getInstance().getCurrentUser(new Model.GetUserListener() {
            @Override
            public void onResult(User user) {
                id = user.getId();
                userName = user.getUserName();

                firstName = (EditText) currentView.findViewById(R.id.firstNameMP);
                lastName = (EditText) currentView.findViewById(R.id.lastNameMP);
                phone = (EditText) currentView.findViewById(R.id.phoneNumberMP);
                address = (EditText) currentView.findViewById(R.id.addressMP);
                city = (EditText) currentView.findViewById(R.id.cityMP);
                age = (EditText) currentView.findViewById(R.id.ageMP);
                price = (EditText) currentView.findViewById(R.id.priceForHourMP);
                morning = (CheckBox) currentView.findViewById(R.id.cbx_isComfortableOnMorningMP);
                afternoon = (CheckBox) currentView.findViewById(R.id.cbx_isComfortableOnAfternoonMP);
                evening = (CheckBox) currentView.findViewById(R.id.cbx_isComfortableOnEveningMP);
                dogName = (EditText) currentView.findViewById(R.id.dogNameMP);
                isBig = (RadioButton) currentView.findViewById(R.id.isBigMP);
                isMedium = (RadioButton) currentView.findViewById(R.id.isMediumMP);
                isSmall = (RadioButton) currentView.findViewById(R.id.isSmallMP);
                dogAge = (EditText) currentView.findViewById(R.id.dogAgeMP);

                firstName.setText(user.getFirstName());
                lastName.setText(user.getLastName());
                phone.setText(user.getPhoneNumber());
                address.setText(user.getAddress());
                city.setText(user.getCity());

                if (!isOwner) {
                    age.setText(Long.toString(((DogWalker) user).getAge()));
                    price.setText(Long.toString(((DogWalker) user).getPriceForHour()));
                    morning.setChecked(((DogWalker) user).isComfortableOnMorning());
                    afternoon.setChecked(((DogWalker) user).isComfortableOnAfternoon());
                    evening.setChecked(((DogWalker) user).isComfortableOnEvening());
                } else {
                    dogName.setText(((DogOwner) user).getDog().getName());
                    DogSize size = ((DogOwner) user).getDog().getSize();
                    isBig.setChecked((size == DogSize.Large ? true : false));
                    isMedium.setChecked((size == DogSize.Medium ? true : false));
                    isSmall.setChecked((size == DogSize.Small ? true : false));
                    dogAge.setText(Long.toString(((DogOwner) user).getDog().getAge()));

                    // TODO : DOG PIC IS EMPTY! LOAD PIC!
                }

                progressBar.setVisibility(View.GONE);
            }
        });

        // listeners

        ((Button)view.findViewById(R.id.saveChangesMyProfile)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChangesClick(v);
            }
        });

        ((Button)view.findViewById(R.id.btn_openGallery)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryBTN(v);
            }
        });

        return view;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_profile);
//
//        setActionBar((Toolbar) findViewById(R.id.myProfileToolBar));
//        getActionBar().setDisplayShowTitleEnabled(false);
//        progressBar = (ProgressBar) findViewById(R.id.myProfileProgressBar);
//
//        isOwner = getIntent().getBooleanExtra("isOwner", false);
//
//        if (!isOwner){
//            findViewById(R.id.dogWalkerSectionInMyProfile).setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            findViewById(R.id.dogOwnerSectionInMyProfile).setVisibility(View.VISIBLE);
//        }
//
//        Model.getInstance().getCurrentUser(new Model.GetUserListener() {
//            @Override
//            public void onResult(User user)
//            {
//                id = user.getId();
//                userName = user.getUserName();
//
//                firstName = (EditText) findViewById(R.id.firstNameMP);
//                lastName = (EditText) findViewById(R.id.lastNameMP);
//                phone = (EditText) findViewById(R.id.phoneNumberMP);
//                address = (EditText) findViewById(R.id.addressMP);
//                city = (EditText) findViewById(R.id.cityMP);
//                age = (EditText) findViewById(R.id.ageMP);
//                price = (EditText) findViewById(R.id.priceForHourMP);
//                morning = (CheckBox) findViewById(R.id.cbx_isComfortableOnMorningMP);
//                afternoon = (CheckBox) findViewById(R.id.cbx_isComfortableOnAfternoonMP);
//                evening = (CheckBox) findViewById(R.id.cbx_isComfortableOnEveningMP);
//                dogName = (EditText) findViewById(R.id.dogNameMP);
//                isBig = (RadioButton) findViewById(R.id.isBigMP);
//                isMedium = (RadioButton) findViewById(R.id.isMediumMP);
//                isSmall = (RadioButton) findViewById(R.id.isSmallMP);
//                dogAge = (EditText) findViewById(R.id.dogAgeMP);
//
//                firstName.setText(user.getFirstName());
//                lastName.setText(user.getLastName());
//                phone.setText(user.getPhoneNumber());
//                address.setText(user.getAddress());
//                city.setText(user.getCity());
//
//                if (!isOwner){
//                    age.setText(Long.toString(((DogWalker)user).getAge()));
//                    price.setText(Long.toString(((DogWalker)user).getPriceForHour()));
//                    morning.setChecked(((DogWalker) user).isComfortableOnMorning());
//                    afternoon.setChecked(((DogWalker)user).isComfortableOnAfternoon());
//                    evening.setChecked(((DogWalker)user).isComfortableOnEvening());
//                }
//                else
//                {
//                    dogName.setText(((DogOwner) user).getDog().getName());
//                    DogSize size = ((DogOwner)user).getDog().getSize();
//                    isBig.setChecked((size == DogSize.Large ? true : false));
//                    isMedium.setChecked((size == DogSize.Medium ? true : false));
//                    isSmall.setChecked((size == DogSize.Small ? true : false));
//                    dogAge.setText(Long.toString(((DogOwner) user).getDog().getAge()));
//
//                    // TODO : DOG PIC IS EMPTY! LOAD PIC!
//                }
//
//                progressBar.setVisibility(View.GONE);
//            }
//        });
//    }


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

            listener.onFinish();
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        Intent intent = null;
//
//        if (id == R.id.searchDW) {
//            intent = new Intent(this, SearchActivity.class);
//        } else if (id == R.id.map) {
//            intent = new Intent(this, MapsActivity.class);
//        } else if (id == R.id.tripsReport) {
//            intent = new Intent(this, TripsReportActivity.class);
//        } else if (id == R.id.messages) {
//            intent = new Intent(this, MessagesActivity.class);
//        } else if (id == R.id.dogsList) {
//            intent = new Intent(this, DogsListActivity.class);
//
//        }
//
//        intent.putExtra("isOwner", isOwner);
//        intent.putExtra("userId", getIntent().getLongExtra("userId",0));
//        startActivity(intent);
//
//        return super.onOptionsItemSelected(item);
//    }

    public void openGalleryBTN(View view)
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode)
        {
            case SELECT_PHOTO:
                if(resultCode == getActivity().RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);

                    dogPic = selectedImage.getPath();
                    ((ImageView)currentView.findViewById(R.id.dogPicMP)).setImageBitmap(yourSelectedImage);
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

        TextView error = (TextView)currentView.findViewById(R.id.error);
        error.setText(errorMessage);

        return isValid;
    }
}
