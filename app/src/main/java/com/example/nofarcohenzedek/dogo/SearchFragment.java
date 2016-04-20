package com.example.nofarcohenzedek.dogo;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.User;
import com.example.nofarcohenzedek.dogo.Model.Utilities;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment
{
    private List<DogWalker> list;
    private ProgressBar progressBar;
    private DogOwner owner;
    private Long userId;

    private View currentView;
    private Bundle args;

    public SearchFragment(){}

    public SearchFragment(Long id){
        userId = id;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_search, container, false);
        currentView = view;
        progressBar = (ProgressBar) view.findViewById(R.id.searchProgressBar);
        super.onCreateView(inflater, container, savedInstanceState);

        args = getArguments();

        ((ListView)view.findViewById(R.id.searchResultList)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getActivity().getApplicationContext(), DogWalkerDetailsActivity.class);
                intent.putExtra("walkerId", Long.toString(id));
                intent.putExtra("ownerId", userId);
                startActivity(intent);
            }
        });

//        ((RadioButton)view.findViewById(R.id.searchByDistance)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                radioButtonSearchDWBy(v);
//            }
//        });
//
//        ((RadioButton)view.findViewById(R.id.searchByParameters)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                radioButtonSearchDWBy(v);
//            }
//        });

        ((ImageButton)view.findViewById(R.id.searchBTN)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBTNClick(v);
            }
        });

        return view;
    }

//    public void radioButtonSearchDWBy(View view)
//    {
//        RadioButton searchByRadius = (RadioButton)currentView.findViewById(R.id.searchByDistance);
//        RadioButton searchByParametes = (RadioButton)currentView.findViewById(R.id.searchByParameters);
//        LinearLayout layoutDistance = (LinearLayout)currentView.findViewById(R.id.searchByDistanceLayout);
//        LinearLayout layoutParameters = (LinearLayout)currentView.findViewById(R.id.searchByParametersLayout);
//
//        if(searchByRadius.isChecked())
//        {
//            layoutDistance.setVisibility(View.VISIBLE);
//            layoutParameters.setVisibility(View.GONE);
//        }
//        else if(searchByParametes.isChecked())
//        {
//            layoutParameters.setVisibility(View.VISIBLE);
//            layoutDistance.setVisibility(View.GONE);
//        }
//
//        ImageButton searchBTN = (ImageButton) currentView.findViewById(R.id.searchBTN);
//        searchBTN.setEnabled(true);
//    }

    public void searchBTNClick (View view) {
        progressBar.setVisibility(View.VISIBLE);

        // check if by distance or by param
        //RadioButton byDistance = (RadioButton) currentView.findViewById(R.id.searchByDistance);
        //RadioButton byParams = (RadioButton) currentView.findViewById(R.id.searchByParameters);

        //if (byDistance.isChecked())
        //{
        final String radius = ((EditText) currentView.findViewById(R.id.radiusForSearch)).getText().toString();
        final String age = ((EditText) currentView.findViewById(R.id.ageForSearch)).getText().toString();
        final String price = ((EditText) currentView.findViewById(R.id.priceForSearch)).getText().toString();
        final boolean morning = ((CheckBox) currentView.findViewById(R.id.cbx_isComfortableOnMorningForSearch)).isChecked();
        final boolean noon = ((CheckBox) currentView.findViewById(R.id.cbx_isComfortableOnAfternoonForSearch)).isChecked();
        final boolean evening = ((CheckBox) currentView.findViewById(R.id.cbx_isComfortableOnEveningForSearch)).isChecked();

        if (owner == null) {
            Model.getInstance().getUserById(userId, new Model.GetUserListener() {
                @Override
                public void onResult(User user) {
                    owner = (DogOwner) user;
                    searchAndShowResults(radius, age, price, morning, noon, evening);
                }
            });
        } else {
            searchAndShowResults(radius, age, price, morning, noon, evening);
        }
    }

    private void searchAndShowResults(final String radius,
                                      final String age, final String price,
                                      final boolean morning, final boolean noon, final boolean evening)
    {
        // list that contains all dog walkers and we remove form her the ones that doesn't fit
        final List<DogWalker> temp = new ArrayList<DogWalker>();

        // search by parameters (the existing ones..)
        Model.getInstance().getAllDogWalkers(new Model.GetDogWalkersListener() {
            @Override
            public void onResult(List<DogWalker> dogWalkers) {
                List<DogWalker> temp = new ArrayList<DogWalker>();

                // work around concurrentModificationException
                for (DogWalker dogWalker : dogWalkers) {
                    temp.add(dogWalker);
                }

                for (DogWalker dogWalker : dogWalkers) {
                    if (!age.equals("") && dogWalker.getAge() < Long.valueOf(age)) {
                        temp.remove(dogWalker);
                    }

                    if (!price.equals("") && dogWalker.getPriceForHour() > Integer.valueOf(price)) {
                        temp.remove(dogWalker);
                    }

                    if (morning && !dogWalker.isComfortableOnMorning() ||
                            noon && !dogWalker.isComfortableOnAfternoon() ||
                            evening && !dogWalker.isComfortableOnEvening()) {
                        temp.remove(dogWalker);
                    }
                }

                dogWalkers.clear();

                // work around concurrentModificationException
                for (DogWalker walker : temp) {
                    dogWalkers.add(walker);
                }

                // search by distance- if not empty
                if (!radius.equals("")) {

                    Long radiusInMeters = Long.valueOf(radius);


                    final LatLng ownerCoor = Utilities.getLocationFromAddress(owner.getAddress() + ", " + owner.getCity(), getActivity().getApplicationContext());
                    final Location ownerLocation = new Location("owner");
                    ownerLocation.setLatitude(ownerCoor.latitude);
                    ownerLocation.setLongitude(ownerCoor.longitude);

                    for (DogWalker dogWalker : dogWalkers) {
                        LatLng currentCoor = Utilities.getLocationFromAddress(dogWalker.getAddress() + ", " + dogWalker.getCity(), getActivity().getApplicationContext());
                        Location currLocation = new Location("walker");
                        currLocation.setLatitude(currentCoor.latitude);
                        currLocation.setLongitude(currentCoor.longitude);

                        if (ownerLocation.distanceTo(currLocation) > (float) radiusInMeters) {
                            temp.remove(dogWalker);
                        }

                    }
                }

                list = temp;

                CustomAdapter adapter = new CustomAdapter();
                ListView listView = (ListView) currentView.findViewById(R.id.searchResultList);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                progressBar.setVisibility(View.GONE);

            }
        });
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
                convertView = inflater.inflate(R.layout.dog_walker_for_search, null);
            }

            TextView name = (TextView) convertView.findViewById(R.id.nameInSearch);
            TextView  address = (TextView) convertView.findViewById(R.id.addressInSearch);
            TextView age = (TextView) convertView.findViewById(R.id.ageInSearch);

            DogWalker walker = list.get(position);

            name.setText(walker.getFirstName() + " " + walker.getLastName());
            age.setText(Long.toString(walker.getAge()));
            address.setText(walker.getAddress() + ", " + walker.getCity());

            return convertView;
        }
    }
}


