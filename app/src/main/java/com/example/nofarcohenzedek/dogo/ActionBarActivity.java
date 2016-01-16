package com.example.nofarcohenzedek.dogo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

public class ActionBarActivity extends Activity
{
    MapsActivity mapsFragment;
    MyProfileActivity myProfileFragment;
    SearchActivity searchFragment;
    MessagesActivity messagesFragment;
    DogsListActivity dogsListFragment;
    TripsReportActivity tripsReportActivity;
    FragmentManager manager;
    Long userId;
    boolean isOwner;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_bar);

        setActionBar((Toolbar) findViewById(R.id.actionBarToolBar));
        getActionBar().setDisplayShowTitleEnabled(false);

        isOwner = getIntent().getBooleanExtra("isOwner", false);
        userId = getIntent().getLongExtra("userId", 0);
        address = getIntent().getStringExtra("address");

        manager = getFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();

        mapsFragment = new MapsActivity();
        myProfileFragment = new MyProfileActivity();
        searchFragment = new SearchActivity();
        messagesFragment = new MessagesActivity();
        dogsListFragment = new DogsListActivity();
        tripsReportActivity = new TripsReportActivity();

        myProfileFragment.setListener(new MyProfileActivity.Listener() {
            @Override
            public void onFinish()
            {
                FragmentTransaction trans = manager.beginTransaction();
                trans.remove(myProfileFragment);

                if (isOwner) {
                    trans.add(R.id.LayoutContainer, mapsFragment, "maps");
                } else {
                    trans.add(R.id.LayoutContainer, dogsListFragment, "dogsList");
                }
                trans.commit();
            }
        });

        Bundle args = new Bundle();
        args.putLong("userId", userId);

        // set start activities
        if (isOwner)
        {
            args.putString("address", address);
            mapsFragment.setArguments(args);
            fragmentTransaction.add(R.id.LayoutContainer, mapsFragment,"maps");
        }
        else
        {
            dogsListFragment.setArguments(args);
            fragmentTransaction.add(R.id.LayoutContainer, dogsListFragment,"dogsList");
        }

        fragmentTransaction.commit();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        FragmentTransaction newTrans = manager.beginTransaction();
        Bundle args = new Bundle();
        args.putLong("userId", userId);
        args.putBoolean("isOwner",isOwner);

        if (id == R.id.searchDW)
        {
            if (manager.findFragmentByTag("search") == null)
            {
                args.putString("address", address);
                removeAllFragments();
                searchFragment.setArguments(args);
                newTrans.replace(R.id.LayoutContainer, searchFragment, "search");
            }
        }
        else if (id == R.id.showMap)
        {
            if (manager.findFragmentByTag("maps") == null)
            {
                args.putString("address", address);
                removeAllFragments();
                mapsFragment.setArguments(args);
                newTrans.replace(R.id.LayoutContainer, mapsFragment, "maps");
            }
        }

        else if (id == R.id.tripsReport)
        {
            if (manager.findFragmentByTag("trips") == null)
            {
                removeAllFragments();
                tripsReportActivity.setArguments(args);
                newTrans.replace(R.id.LayoutContainer, tripsReportActivity, "trips");
            }
        }
        else if (id == R.id.messages)
        {
            if (manager.findFragmentByTag("messages") == null)
            {
                removeAllFragments();
                messagesFragment.setArguments(args);
                newTrans.replace(R.id.LayoutContainer, messagesFragment, "messages");
            }
        }
        else if (id == R.id.myProfile)
        {
            if (manager.findFragmentByTag("myProfile") == null)
            {
                removeAllFragments();
                myProfileFragment.setArguments(args);
                newTrans.replace(R.id.LayoutContainer, myProfileFragment, "myProfile");
            }
        }
        else if (id == R.id.dogsList)
        {
            if (manager.findFragmentByTag("dogsList") == null)
            {
                removeAllFragments();
                dogsListFragment.setArguments(args);
                newTrans.replace(R.id.LayoutContainer, dogsListFragment, "dogsList");
            }
        }

        newTrans.commit();

        return super.onOptionsItemSelected(item);
    }

    public void removeAllFragments()
    {
        FragmentTransaction trans1 = manager.beginTransaction();

        trans1.remove(mapsFragment);
        trans1.remove(dogsListFragment);
        trans1.remove(messagesFragment);
        trans1.remove(myProfileFragment);
        trans1.remove(tripsReportActivity);
        trans1.remove(searchFragment);

        trans1.commit();
    }

}
