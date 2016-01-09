package com.example.nofarcohenzedek.dogo;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toolbar;

import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.User;

public class MyProfileActivity extends Activity {

    Boolean isOwner;

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

        Model.getInstance().getCurrentUser(new Model.GetUserListener() {
            @Override
            public void onResult(User user)
            {
                EditText firstName = (EditText) findViewById(R.id.firstNameMP);
                EditText lastName = (EditText) findViewById(R.id.lastNameMP);
                EditText phone = (EditText) findViewById(R.id.phoneNumberMP);
                EditText address = (EditText) findViewById(R.id.addressMP);
                EditText city = (EditText) findViewById(R.id.cityMP);
                EditText age = (EditText) findViewById(R.id.ageMP);
                EditText price = (EditText) findViewById(R.id.priceForHourMP);
                CheckBox morning = (CheckBox) findViewById(R.id.cbx_isComfortableOnMorningMP);
                CheckBox afternoon = (CheckBox) findViewById(R.id.cbx_isComfortableOnAfternoonMP);
                CheckBox evening = (CheckBox) findViewById(R.id.cbx_isComfortableOnEveningMP);

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

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

//        Model.getInstance().getCurrentUser(new Model.GetUserListener2() {
//            @Override
//            public void onResult(User user) {
//                if (user instanceof DogOwner) {
//                    getMenuInflater().inflate(R.menu.menu_prime_dog_owner, menu);
//                } else {
//                    getMenuInflater().inflate(R.menu.menu_prime_dog_walker, menu);
//                }
//            }
//        });

        if (isOwner)
        {
            getMenuInflater().inflate(R.menu.menu_prime_dog_owner, menu);
        }
        else
        {
            getMenuInflater().inflate(R.menu.menu_prime_dog_walker, menu);
        }

        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    public void saveChangesClick(View view)
    {
        // TODO : Update user
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
}
