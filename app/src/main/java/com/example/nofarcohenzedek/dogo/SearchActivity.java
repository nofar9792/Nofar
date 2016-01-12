package com.example.nofarcohenzedek.dogo;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toolbar;

public class SearchActivity extends Activity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        progressBar = (ProgressBar) findViewById(R.id.searchProgressBar);

        setActionBar((Toolbar) findViewById(R.id.searchToolBar));
        getActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_prime_dog_owner, menu);

        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent = null;

        if (id == R.id.dogsList) {
            intent = new Intent(this, DogsListActivity.class);
        } else if (id == R.id.map) {
            intent = new Intent(this, MapsActivity.class);
        } else if (id == R.id.tripsReport) {
            intent = new Intent(this, TripsReportActivity.class);
        } else if (id == R.id.messages) {
            intent = new Intent(this, MessagesActivity.class);
        } else if (id == R.id.myProfile) {
            intent = new Intent(this, MyProfileActivity.class);

        }

        intent.putExtra("isOwner", true);
        intent.putExtra("userId", getIntent().getLongExtra("userId",0));
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

    public void radioButtonSearchDWBy(View view)
    {
        RadioButton searchByRadius = (RadioButton)findViewById(R.id.searchByDistance);
        RadioButton searchByParametes = (RadioButton)findViewById(R.id.searchByParameters);
        LinearLayout layoutDistance = (LinearLayout)findViewById(R.id.searchByDistanceLayout);
        LinearLayout layoutParameters = (LinearLayout)findViewById(R.id.searchByParametersLayout);

        if(searchByRadius.isChecked())
        {
            layoutDistance.setVisibility(View.VISIBLE);
            layoutParameters.setVisibility(View.GONE);
        }
        else if(searchByParametes.isChecked())
        {
            layoutParameters.setVisibility(View.VISIBLE);
            layoutDistance.setVisibility(View.GONE);
        }

        Button searchBTN = (Button) findViewById(R.id.searchBTN);
        searchBTN.setEnabled(true);
    }

    public void searchBTNClick (View view)
    {

    }

}
