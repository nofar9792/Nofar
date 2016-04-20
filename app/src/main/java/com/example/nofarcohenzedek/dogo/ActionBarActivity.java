package com.example.nofarcohenzedek.dogo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

public class ActionBarActivity extends AppCompatActivity
{
    TabLayout tabLayout;
    ViewPager viewPager;


//    MapsFragment mapsFragment;
//    MyProfileFragment myProfileFragment;
//    SearchFragment searchFragment;
//    MessagesFragment messagesFragment;
//    DogsListFragment dogsListFragment;
//    TripsReportFragment tripsReportFragment;
//    FragmentManager manager;
    Long userId;
    boolean isOwner;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_bar);

        isOwner = getIntent().getBooleanExtra("isOwner", false);
        userId = getIntent().getLongExtra("userId", 0);
        address = getIntent().getStringExtra("address");

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(), getApplicationContext()));

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        // set icons
        tabLayout.getTabAt(0).setIcon(R.drawable.graydog);
        tabLayout.getTabAt(1).setIcon(R.drawable.grayman);
        tabLayout.getTabAt(2).setIcon(R.drawable.grayreport);
        tabLayout.getTabAt(3).setIcon(R.drawable.graymessage);

        if (isOwner){
            tabLayout.getTabAt(4).setIcon(R.drawable.graysearch);
        }
    }

    private class CustomAdapter extends FragmentPagerAdapter {

        private String walkerFragments [] = {"List","Profile","Report","Messages"};
        private String ownerFragments [] = {"List", "Profile", "Report", "Messages", "Search"};

        public CustomAdapter(android.support.v4.app.FragmentManager supportFragmentManager, Context applicationContext) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:
                    return new DogsListFragment(userId);
                case 1:
                    return new MyProfileFragment(userId, isOwner);
                case 2:
                    return new TripsReportFragment(userId, isOwner);
                case 3:
                    return new MessagesFragment(userId, isOwner);
                case 4:
                    if (isOwner) {
                        return new SearchFragment(userId);
                    }
                    else {
                        return null;
                    }
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            if (isOwner){
                return ownerFragments.length;
            }
            else {
                return walkerFragments.length;
            }
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            if (isOwner) {
                return ownerFragments[position];
            }
            else {
                return walkerFragments[position];
            }
        }
    }
}
