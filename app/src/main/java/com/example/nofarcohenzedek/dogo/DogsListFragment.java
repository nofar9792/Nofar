package com.example.nofarcohenzedek.dogo;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DogsListFragment extends Fragment
{
    private Long userId;
    private List<DogOwner> list;
    private Map<Long, Long> tripsByOwnerId;
    private ProgressBar progressBar;
    private Context context;

    public  DogsListFragment(){}

    public DogsListFragment(Long id)
    {
        userId = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        context = container.getContext();
        final View view = inflater.inflate(R.layout.activity_dogs_list, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        Bundle args = getArguments();
        progressBar = (ProgressBar) view.findViewById(R.id.dogsListProgressBar);

//        userId = args.getLong("userId");

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

    public void onItemClickListener(View view, final Long ownerId)
    {
        if (view.getTag().equals("startTripTag"))
        {
            View currentListItem = (View) view.getParent();

            currentListItem.findViewById(R.id.endTripBtn).setEnabled(true);
            currentListItem.findViewById(R.id.startTripBtn).setEnabled(false);

            // Replace the imageButton to disabled
            ((ImageButton)currentListItem.findViewById(R.id.endTripBtn)).setImageResource(R.drawable.finish_trip_button);
            ((ImageButton)currentListItem.findViewById(R.id.startTripBtn)).setImageResource(R.drawable.start_trip_disable_button);

            Model.getInstance().startTrip(ownerId, userId, new Model.GetIdListener() {
                @Override
                public void onResult(long tripId, boolean isSucceed) {
                    if (isSucceed) {
                        tripsByOwnerId.put(ownerId, tripId);
                        Toast.makeText(context, "טיול התחיל בהצלחה", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "אירעה שגיאה, אנא נסה שוב", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else if (view.getTag().equals("endTripTag"))
        {
            View currentListItem = (View) view.getParent();

            currentListItem.findViewById(R.id.endTripBtn).setEnabled(false);
            currentListItem.findViewById(R.id.startTripBtn).setEnabled(true);

            // Replace the imageButton to disabled
            ((ImageButton)currentListItem.findViewById(R.id.endTripBtn)).setImageResource(R.drawable.finish_trip_disable_button);
            ((ImageButton)currentListItem.findViewById(R.id.startTripBtn)).setImageResource(R.drawable.start_trip_button);

            Model.getInstance().endTrip(tripsByOwnerId.get(ownerId), new Model.IsSucceedListener() {
                @Override
                public void onResult(boolean isSucceed) {
                    if (isSucceed) {
                        Toast.makeText(context, "טיול הסתיים בהצלחה", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "אירעה שגיאה, אנא נסה שוב", Toast.LENGTH_SHORT).show();
                    }
                }
            });
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
            TextView phone = (TextView) convertView.findViewById(R.id.phoneInDogsList);

            final DogOwner owner = list.get(position);

            ownerName.setText(owner.getFirstName() + " " + owner.getLastName());
            dogName.setText(owner.getDog().getName());
            address.setText(owner.getAddress() + ", " + owner.getCity());
            phone.setText(owner.getPhoneNumber());

            // work around
            ImageButton startTrip = (ImageButton) convertView.findViewById(R.id.startTripBtn);
            ImageButton endTrip = (ImageButton) convertView.findViewById(R.id.endTripBtn);

            endTrip.setEnabled(false);

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
