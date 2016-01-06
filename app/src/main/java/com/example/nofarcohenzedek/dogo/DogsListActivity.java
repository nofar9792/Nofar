package com.example.nofarcohenzedek.dogo;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.example.nofarcohenzedek.dogo.MapsActivity;
import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.R;
import com.example.nofarcohenzedek.dogo.SearchActivity;

public class DogsListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogs_list);

        setActionBar((Toolbar)findViewById(R.id.dogsListToolBar));
        getActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        if (Model.getInstance().getCurrentUser() instanceof DogOwner)
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.searchDW)
        {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }
        if (id == R.id.showMap)
        {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
