package com.example.nofarcohenzedek.dogo;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.AvoidXfermode;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.Model;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DogsListActivity extends Fragment
{
    private Long userId;
    private List<DogOwner> list;
    private Map<Long, Long> tripsByOwnerId;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.activity_dogs_list, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        Bundle args = getArguments();
        progressBar = (ProgressBar) view.findViewById(R.id.dogsListProgressBar);

        userId = args.getLong("userId");

        tripsByOwnerId = new HashMap<Long, Long>();

        Model.getInstance().getOwnersConnectToWalker(userId, new Model.GetDogOwnersListener() {
            @Override
            public void onResult(List<DogOwner> allDogWalkers) {
                list = allDogWalkers;

                if (list != null && list.size() != 0) {
                    CustomAdapter adapter = new CustomAdapter();
                    ListView listView = (ListView) view.findViewById(R.id.dogsOfDogWalker);
                    listView.setAdapter(adapter);
                } else {
                    ((TextView) view.findViewById(R.id.errorInDogsList)).setText("אין כלבים להצגה");
                }

                progressBar.setVisibility(View.GONE);
            }
        });


        return view;
    }

//        @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dogs_list);
//
//        setActionBar((Toolbar) findViewById(R.id.dogsListToolBar));
//        getActionBar().setDisplayShowTitleEnabled(false);
//        progressBar = (ProgressBar) findViewById(R.id.dogsListProgressBar);
//
//        userId = getIntent().getLongExtra("userId", 0);
//
//        tripsByOwnerId = new HashMap<Long, Long>();
//
//        Model.getInstance().getOwnersConnectToWalker(userId, new Model.GetDogOwnersListener() {
//            @Override
//            public void onResult(List<DogOwner> allDogWalkers) {
//                list = allDogWalkers;
//
//                if (list != null && list.size() != 0) {
//                    CustomAdapter adapter = new CustomAdapter();
//                    ListView listView = (ListView) findViewById(R.id.dogsOfDogWalker);
//                    listView.setAdapter(adapter);
//                } else {
//                    ((TextView) findViewById(R.id.errorInDogsList)).setText("אין כלבים להצגה");
//                }
//
//                progressBar.setVisibility(View.GONE);
//            }
//        });
//    }

    public void onItemClickListener(View view, Long ownerId)
    {
        if (view.getTag().equals("startTripTag"))
        {
            View currentListItem = (View) view.getParent();

            currentListItem.findViewById(R.id.endTripBtn).setEnabled(true);
            currentListItem.findViewById(R.id.startTripBtn).setEnabled(false);

            Long tripId = Model.getInstance().startTrip(ownerId, userId);
            tripsByOwnerId.put(ownerId,tripId);

        }
        else if (view.getTag().equals("endTripTag"))
        {
            View currentListItem = (View) view.getParent();

            currentListItem.findViewById(R.id.endTripBtn).setEnabled(false);
            currentListItem.findViewById(R.id.startTripBtn).setEnabled(true);

            Model.getInstance().endTrip(tripsByOwnerId.get(ownerId));
            tripsByOwnerId.remove(ownerId);
        }
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
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.dog_in_dogs_list, null);
            }

            TextView ownerName = (TextView) convertView.findViewById(R.id.ownerNameInDogsList);
            TextView dogName = (TextView) convertView.findViewById(R.id.dogNameInDogsList);
            TextView address = (TextView) convertView.findViewById(R.id.addressInDogsList);

            final DogOwner owner = list.get(position);

            ownerName.setText(owner.getFirstName() + " " + owner.getLastName());
            dogName.setText(owner.getDog().getName());
            address.setText(owner.getAddress() + ", " + owner.getCity());

            // work around
            Button startTrip = (Button) convertView.findViewById(R.id.startTripBtn);
            Button endTrip = (Button) convertView.findViewById(R.id.endTripBtn);

            startTrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener(v, owner.getId());
                }
            });

            endTrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener(v, owner.getId());
                }
            });

            return convertView;
        }
    }

}
