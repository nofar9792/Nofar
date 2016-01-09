package com.example.nofarcohenzedek.dogo;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.Model;

import java.util.List;

public class DogsListActivity extends Activity {

    Boolean isOwner;
    Long userId;
    List<DogOwner> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogs_list);

        setActionBar((Toolbar) findViewById(R.id.dogsListToolBar));
        getActionBar().setDisplayShowTitleEnabled(false);

        isOwner = getIntent().getBooleanExtra("isOwner", false);
        userId = getIntent().getLongExtra("userId", 0);

        Model.getInstance().getOwnersConnectToWalker(userId, new Model.GetDogOwnersListener() {
            @Override
            public void onResult(List<DogOwner> allDogWalkers) {
                list = allDogWalkers;
            }
        });

        if (list != null) {
            CustomAdapter adapter = new CustomAdapter();
            ListView listView = (ListView) findViewById(R.id.dogsOfDogWalker);
            listView.setAdapter(adapter);
        }
        else
        {
            ((TextView)findViewById(R.id.errorInDogsList)).setText("אין כלבים להצגה");
        }

//        if (isOwner)
//        {
//            findViewById(R.id.dogOwnerInDogsList).setVisibility(View.VISIBLE);
//
//            // TODO : get all dogs of owner
//
//            // temp
//            Dog[] dogs = {new Dog(2,"Jina", DogSize.Medium, 12,""),
//                        new Dog(4,"Olive", DogSize.Medium, 5,"")};
//
//
//        }
        //   else
        // {
        //findViewById(R.id.dogWalkerInDogsList).setVisibility(View.VISIBLE);


        //}
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        if (isOwner) {
            getMenuInflater().inflate(R.menu.menu_prime_dog_owner, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_prime_dog_walker, menu);
        }

        return true;
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
        } else if (id == R.id.myProfile) {
            intent = new Intent(this, MyProfileActivity.class);

        }

        intent.putExtra("isOwner", isOwner);
        intent.putExtra("userId", getIntent().getLongExtra("userId", 0));
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return list.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.dog_in_dogs_list, null);
            }

            TextView ownerName = (TextView) findViewById(R.id.ownerNameInDogsList);
            TextView dogName = (TextView) findViewById(R.id.dogNameInDogsList);
            TextView address = (TextView) findViewById(R.id.addressInDogsList);

            DogOwner owner = list.get(position);

            ownerName.setText(owner.getFirstName() + " " + owner.getLastName());
            dogName.setText(owner.getDogs().get(0).getName());
            address.setText(owner.getAddress());


            return convertView;
        }
    }

}
