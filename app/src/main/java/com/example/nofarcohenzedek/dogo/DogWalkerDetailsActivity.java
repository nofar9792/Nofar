package com.example.nofarcohenzedek.dogo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.User;

public class DogWalkerDetailsActivity extends Activity
{
    private Long walkerId;
    private Long ownerId;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_walker_details);

        progressBar = (ProgressBar) findViewById(R.id.dogWalkerDetailsProgressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);

        walkerId = getIntent().getLongExtra("walkerId", 0);
        ownerId = getIntent().getLongExtra("ownerId", 0);

        final TextView firstName = (TextView) findViewById(R.id.firstNameInDetails);
        final TextView lastName = (TextView) findViewById(R.id.lastNameInDetails);
        final TextView phoneNumber = (TextView) findViewById(R.id.phoneNumberInDetails);
        final TextView city = (TextView) findViewById(R.id.cityInDetails);
        final TextView address = (TextView) findViewById(R.id.addressInDetails);
        final TextView age = (TextView) findViewById(R.id.ageInDetails);
        final TextView price = (TextView) findViewById(R.id.priceForHourInDetails);
        final CheckBox isComfortable6To8 = (CheckBox) findViewById(R.id.checkbox6To8);
        final CheckBox isComfortable8To10 = (CheckBox) findViewById(R.id.checkbox8To10);
        final CheckBox isComfortable10To12 = (CheckBox) findViewById(R.id.checkbox10To12);
        final CheckBox isComfortable12To14 = (CheckBox) findViewById(R.id.checkbox12To14);
        final CheckBox isComfortable14To16 = (CheckBox) findViewById(R.id.checkbox14To16);
        final CheckBox isComfortable16To18 = (CheckBox) findViewById(R.id.checkbox16To18);
        final CheckBox isComfortable18To20 = (CheckBox) findViewById(R.id.checkbox18To20);
        final CheckBox isComfortable20To22 = (CheckBox) findViewById(R.id.checkbox20To22);

        Model.getInstance().getUserById(walkerId, new Model.GetUserListener() {
            @Override
            public void onResult(User user) {
                DogWalker dogWalker = (DogWalker) user;
                firstName.setText(dogWalker.getFirstName());
                lastName.setText(dogWalker.getLastName());
                phoneNumber.setText(dogWalker.getPhoneNumber());
                city.setText(dogWalker.getCity());
                address.setText(dogWalker.getAddress());
                age.setText(Long.toString(dogWalker.getAge()));
                price.setText(Integer.toString(dogWalker.getPriceForHour()));
                isComfortable6To8.setChecked(dogWalker.isComfortable6To8());
                isComfortable8To10.setChecked(dogWalker.isComfortable8To10());
                isComfortable10To12.setChecked(dogWalker.isComfortable10To12());
                isComfortable12To14.setChecked(dogWalker.isComfortable12To14());
                isComfortable14To16.setChecked(dogWalker.isComfortable14To16());
                isComfortable16To18.setChecked(dogWalker.isComfortable16To18());
                isComfortable18To20.setChecked(dogWalker.isComfortable18To20());
                isComfortable20To22.setChecked(dogWalker.isComfortable20To22());

                progressBar.setVisibility(View.GONE);
            }
        });

        final ImageButton sendRequestButton = (ImageButton) findViewById(R.id.sendRequest);

        Model.getInstance().checkRequestExist(ownerId, walkerId, new Model.IsSucceedListener() {
            @Override
            public void onResult(boolean isRequestExist) {
                if (!isRequestExist) {
                    sendRequestButton.setVisibility(View.VISIBLE);
                }
            }
        });
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
