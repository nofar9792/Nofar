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
import android.widget.ListView;
import android.widget.ProgressBar;
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
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
        super.onCreateView(inflater, container, savedInstanceState);

        args = getArguments();

        ((ListView)view.findViewById(R.id.searchResultList)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getActivity().getApplicationContext(), DogWalkerDetailsActivity.class);
                intent.putExtra("walkerId", id);
                intent.putExtra("ownerId", userId);
                startActivity(intent);
            }
        });


        view.findViewById(R.id.searchBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBTNClick(v);
            }
        });

        return view;
    }

    public void searchBTNClick (View view) {
        progressBar.setVisibility(View.VISIBLE);

        final String radius = ((EditText) currentView.findViewById(R.id.radiusForSearch)).getText().toString();
        final String age = ((EditText) currentView.findViewById(R.id.ageForSearch)).getText().toString();
        final String price = ((EditText) currentView.findViewById(R.id.priceForSearch)).getText().toString();
        final boolean isComfortable6To8 = ((CheckBox) currentView.findViewById(R.id.checkbox6To8)).isChecked();
        final boolean isComfortable8To10 = ((CheckBox) currentView.findViewById(R.id.checkbox8To10)).isChecked();
        final boolean isComfortable10To12 = ((CheckBox) currentView.findViewById(R.id.checkbox10To12)).isChecked();
        final boolean isComfortable12To14 = ((CheckBox) currentView.findViewById(R.id.checkbox12To14)).isChecked();
        final boolean isComfortable14To16 = ((CheckBox) currentView.findViewById(R.id.checkbox14To16)).isChecked();
        final boolean isComfortable16To18 = ((CheckBox) currentView.findViewById(R.id.checkbox16To18)).isChecked();
        final boolean isComfortable18To20 = ((CheckBox) currentView.findViewById(R.id.checkbox18To20)).isChecked();
        final boolean isComfortable20To22 = ((CheckBox) currentView.findViewById(R.id.checkbox20To22)).isChecked();

        if (owner == null) {
            Model.getInstance().getUserById(userId, new Model.GetUserListener() {
                @Override
                public void onResult(User user) {
                    owner = (DogOwner) user;
                    searchAndShowResults(radius, age, price,isComfortable6To8, isComfortable8To10, isComfortable10To12,
                            isComfortable12To14, isComfortable14To16, isComfortable16To18, isComfortable18To20, isComfortable20To22);
                }
            });
        } else {
            searchAndShowResults(radius, age, price, isComfortable6To8, isComfortable8To10, isComfortable10To12,
                    isComfortable12To14, isComfortable14To16, isComfortable16To18, isComfortable18To20, isComfortable20To22);
        }
    }

    private void searchAndShowResults(final String radius,
                                      final String age, final String price,
                                      final boolean isComfortable6To8, final boolean isComfortable8To10, final boolean isComfortable10To12,
                                      final boolean isComfortable12To14, final boolean isComfortable14To16, final boolean isComfortable16To18,
                                      final boolean isComfortable18To20, final boolean isComfortable20To22)
    {
        // search by parameters (the existing ones..)
        Model.getInstance().getAllDogWalkers(new Model.GetDogWalkersListener() {
            @Override
            public void onResult(List<DogWalker> dogWalkers) {
                // list that contains all dog walkers and we remove form her the ones that doesn't fit
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

                    if (isComfortable6To8 && !dogWalker.isComfortable6To8() ||
                        isComfortable8To10 && !dogWalker.isComfortable8To10() ||
                        isComfortable10To12 && !dogWalker.isComfortable10To12() ||
                        isComfortable12To14 && !dogWalker.isComfortable12To14() ||
                        isComfortable14To16 && !dogWalker.isComfortable14To16() ||
                        isComfortable16To18 && !dogWalker.isComfortable16To18() ||
                        isComfortable18To20 && !dogWalker.isComfortable18To20() ||
                        isComfortable20To22 && !dogWalker.isComfortable20To22()) {
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


