package com.example.nofarcohenzedek.dogo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.Model;

import java.util.List;

public class DogWalkerDetails extends Activity
{
    Long walkerId;
    Long ownerId;
    ProgressBar progressBar;

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
        final TextView city = (TextView) findViewById(R.id.cityInDetails);
        final TextView address = (TextView) findViewById(R.id.addressInDetails);
        final TextView age = (TextView) findViewById(R.id.ageInDetails);
        final TextView price = (TextView) findViewById(R.id.priceForHourInDetails);
        final CheckBox morning = (CheckBox) findViewById(R.id.morningInDetails);
        final CheckBox noon = (CheckBox) findViewById(R.id.afternoonInDetails);
        final CheckBox evening = (CheckBox) findViewById(R.id.eveningInDetails);

        Model.getInstance().getDogWalkerById(walkerId, new Model.GetDogWalkerListener() {
            @Override
            public void onResult(DogWalker dogWalker) {
                firstName.setText(dogWalker.getFirstName());
                lastName.setText(dogWalker.getLastName());
                city.setText(dogWalker.getCity());
                address.setText(dogWalker.getAddress());
                age.setText(Long.toString(dogWalker.getAge()));
                price.setText(Integer.toString(dogWalker.getPriceForHour()));
                morning.setChecked(dogWalker.isComfortableOnMorning());
                noon.setChecked(dogWalker.isComfortableOnAfternoon());
                evening.setChecked(dogWalker.isComfortableOnEvening());
            }
        });

        final Button askNum = (Button) findViewById(R.id.askNumber);

        Model.getInstance().getRequestForDogWalker(walkerId, new Model.GetDogOwnersListener()
        {
            @Override
            public void onResult(List<DogOwner> dogOwners)
            {
                for (DogOwner owner : dogOwners)
                {
                    if (owner.getId() == ownerId)
                    {
                        askNum.setEnabled(false);
                        break;
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void askNumberClick(View view)
    {
        Model.getInstance().addRequest(ownerId,walkerId);
        finish();
    }
}
