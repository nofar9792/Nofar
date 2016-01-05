package com.example.nofarcohenzedek.dogo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.Model;

public class DogWalkerDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_walker_details);

        Intent intent = getIntent();
        final String dogWalkerId = intent.getStringExtra("id");

        final TextView firstName = (TextView) findViewById(R.id.firstNameInDetails);
        final TextView lastName = (TextView) findViewById(R.id.lastNameInDetails);
        final TextView city = (TextView) findViewById(R.id.cityInDetails);
        final TextView address = (TextView) findViewById(R.id.addressInDetails);
        final TextView age = (TextView) findViewById(R.id.ageInDetails);
        final TextView price = (TextView) findViewById(R.id.priceForHourInDetails);
        final CheckBox morning = (CheckBox) findViewById(R.id.morningInDetails);
        final CheckBox noon = (CheckBox) findViewById(R.id.afternoonInDetails);
        final CheckBox evening = (CheckBox) findViewById(R.id.eveningInDetails);

        Model.getInstance().getDogWalkerById(Long.parseLong(dogWalkerId), new Model.GetDogWalkerListener() {
            @Override
            public void onResult(DogWalker dogWalker)
            {
                firstName.setText(dogWalker.getFirstName());
                lastName.setText(dogWalker.getLastName());
                city.setText(dogWalker.getCity());
                address.setText(dogWalker.getAddress());
                age.setText((int) dogWalker.getAge());
                price.setText(dogWalker.getPriceForHour());
                morning.setChecked(dogWalker.isComfortableOnMorning());
                noon.setChecked(dogWalker.isComfortableOnAfternoon());
                evening.setChecked(dogWalker.isComfortableOnEvening());

            }
        });

    }
}
