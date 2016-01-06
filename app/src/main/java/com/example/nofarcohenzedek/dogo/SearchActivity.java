package com.example.nofarcohenzedek.dogo;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toolbar;

import com.example.nofarcohenzedek.dogo.R;

public class SearchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setActionBar((Toolbar)findViewById(R.id.searchToolBar));
        getActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_prime, menu);

        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        if (id == R.id.showMap)
//        {
//            Intent intent = new Intent(this, MapsActivity.class);
//            startActivity(intent);
//        }

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
