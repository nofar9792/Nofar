package com.example.nofarcohenzedek.dogo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.User;

import java.util.List;

public class DogWalkerDetailsActivity extends Activity
{
    private Long walkerId;
    private Long ownerId;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_walker_details);

        progressBar = (ProgressBar) findViewById(R.id.dogWalkerDetailsProgressBar);

        walkerId = Long.valueOf(getIntent().getStringExtra("walkerId"));
        ownerId = getIntent().getLongExtra("ownerId",0);

        final TextView firstName = (TextView) findViewById(R.id.firstNameInDetails);
        final TextView lastName = (TextView) findViewById(R.id.lastNameInDetails);
        final TextView phonenNumber = (TextView) findViewById(R.id.phoneNumberInDetails);
        final TextView city = (TextView) findViewById(R.id.cityInDetails);
        final TextView address = (TextView) findViewById(R.id.addressInDetails);
        final TextView age = (TextView) findViewById(R.id.ageInDetails);
        final TextView price = (TextView) findViewById(R.id.priceForHourInDetails);
        final CheckBox morning = (CheckBox) findViewById(R.id.morningInDetails);
        final CheckBox noon = (CheckBox) findViewById(R.id.afternoonInDetails);
        final CheckBox evening = (CheckBox) findViewById(R.id.eveningInDetails);

        Model.getInstance().getUserById(walkerId, new Model.GetUserListener() {
            @Override
            public void onResult(User user) {
                DogWalker dogWalker = (DogWalker)user;
                firstName.setText(dogWalker.getFirstName());
                lastName.setText(dogWalker.getLastName());
                phonenNumber.setText(dogWalker.getPhoneNumber());
                city.setText(dogWalker.getCity());
                address.setText(dogWalker.getAddress());
                age.setText(Long.toString(dogWalker.getAge()));
                price.setText(Integer.toString(dogWalker.getPriceForHour()));
                morning.setChecked(dogWalker.isComfortableOnMorning());
                noon.setChecked(dogWalker.isComfortableOnAfternoon());
                evening.setChecked(dogWalker.isComfortableOnEvening());

                progressBar.setVisibility(View.GONE);
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
