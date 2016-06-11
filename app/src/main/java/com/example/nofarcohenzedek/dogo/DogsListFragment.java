package com.example.nofarcohenzedek.dogo;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DogsListFragment extends Fragment {
    private Long userId;
    private List<DogOwner> list;
    private Map<Long, Long> tripsByOwnerId;
    private ProgressBar progressBar;
    private Context context;

    private ArrayList<String> ownersIDsToCalculatePath;
    private ArrayList<String> walkTimesToCalculatePath;
    private ListView listView;
    private ArrayList<ItemInList> itemsData;

    public DogsListFragment() {
    }

    public DogsListFragment(Long id) {
        userId = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        final View view = inflater.inflate(R.layout.activity_dogs_list, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        itemsData = new ArrayList<ItemInList>();

        Bundle args = getArguments();
        progressBar = (ProgressBar) view.findViewById(R.id.dogsListProgressBar);
        listView = (ListView) view.findViewById(R.id.dogsOfDogWalker);

//        userId = args.getLong("userId");

        ImageButton calculatePathBtn = (ImageButton) view.findViewById(R.id.calculatePathButton);

        calculatePathBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View listItem;
                TextView errorView = (TextView) v.getRootView().findViewById(R.id.calculatePathError);

                boolean isValid = true;

                errorView.setText("");
                ownersIDsToCalculatePath.clear();
                walkTimesToCalculatePath.clear();

                for (int i = 0; i < listView.getCount(); i++) {
                    if (itemsData.get(i).isChecked)
                    {
                        if (isInt(itemsData.get(i).time))
                        {
                            ownersIDsToCalculatePath.add(Long.toString(list.get(i).getId()));
                            walkTimesToCalculatePath.add(itemsData.get(i).time);
                        }
                        else
                        {
                            isValid = false;
                            errorView.setText("הכנס מספר לשדה זמן הטיול");
                            break;
                        }
                    }
                }

                if (isValid && ownersIDsToCalculatePath.size() == 0) {
                    errorView.setText("בחר לפחות כלב אחד מהרשימה");
                    isValid = false;
                }

                if (isValid) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), MapPathActivity.class);
                    intent.putStringArrayListExtra("ownerIds",ownersIDsToCalculatePath);
                    intent.putStringArrayListExtra("walkTimes",walkTimesToCalculatePath);
                    intent.putExtra("userId",userId);
                    startActivity(intent);
                }
            }
        });

        tripsByOwnerId = new HashMap<Long, Long>();
        ownersIDsToCalculatePath = new ArrayList<String>();
        walkTimesToCalculatePath = new ArrayList<String>();


        Model.getInstance().getOwnersConnectToWalker(userId, new Model.GetDogOwnersListener() {
            @Override
            public void onResult(List<DogOwner> allDogWalkers) {
                list = allDogWalkers;

                if (list != null && list.size() != 0) {
                    CustomAdapter adapter = new CustomAdapter();
                    ListView listView = (ListView) view.findViewById(R.id.dogsOfDogWalker);
                    listView.setAdapter(adapter);

                    for (int i = 0; i < list.size(); i++) {
                        ItemInList item = new ItemInList();
                        item.position = i;
                        itemsData.add(item);
                    }

                } else {
                    ((TextView) view.findViewById(R.id.errorInDogsList)).setText("אין כלבים להצגה");
                }

                progressBar.setVisibility(View.GONE);
            }
        });

        return view;
    }

    private boolean isInt(String value) {
        try {
            Integer i = Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    class ItemInList {
        int position;
        boolean isChecked;
        String time;
    }

    public void onItemClickListener(View view, final Long ownerId) {
        if (view.getTag().equals("startTripTag")) {
            View currentListItem = (View) view.getParent();

            currentListItem.findViewById(R.id.endTripBtn).setEnabled(true);
            currentListItem.findViewById(R.id.startTripBtn).setEnabled(false);

            // Replace the imageButton to disabled
            ((ImageButton) currentListItem.findViewById(R.id.endTripBtn)).setImageResource(R.drawable.finish_trip_button);
            ((ImageButton) currentListItem.findViewById(R.id.startTripBtn)).setImageResource(R.drawable.start_trip_disable_button);

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
        } else if (view.getTag().equals("endTripTag")) {
            View currentListItem = (View) view.getParent();

            currentListItem.findViewById(R.id.endTripBtn).setEnabled(false);
            currentListItem.findViewById(R.id.startTripBtn).setEnabled(true);

            // Replace the imageButton to disabled
            ((ImageButton) currentListItem.findViewById(R.id.endTripBtn)).setImageResource(R.drawable.finish_trip_disable_button);
            ((ImageButton) currentListItem.findViewById(R.id.startTripBtn)).setImageResource(R.drawable.start_trip_button);

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

    private class CustomAdapter extends BaseAdapter {

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
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.dog_in_dogs_list, null);
            }

            TextView ownerName = (TextView) convertView.findViewById(R.id.ownerNameInDogsList);
            TextView address = (TextView) convertView.findViewById(R.id.addressInDogsList);

            final LinearLayout walkTimeLayout = (LinearLayout) convertView.findViewById(R.id.timeTravelLayout);

            final DogOwner owner = list.get(position);

            ownerName.setText(owner.getFirstName() + " " + owner.getLastName());
            address.setText(owner.getAddress() + ", " + owner.getCity());

            // IS CHECKED
            final CheckBox dogInListCheckBox = (CheckBox) convertView.findViewById(R.id.dogInListCheckBox);

            dogInListCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (dogInListCheckBox.isChecked()) {
                        walkTimeLayout.setVisibility(View.VISIBLE);
                        itemsData.get(position).isChecked = true;
                    } else {
                        walkTimeLayout.setVisibility(View.GONE);
                        itemsData.get(position).isChecked = false;
                    }
                }
            });

            //TRAVEL TIME
            final EditText travelTime = (EditText) convertView.findViewById(R.id.timeToWalkTextbox);

            travelTime.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    itemsData.get(position).time = s.toString();

                }
            });

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

            View dogDetailsInListItemView = convertView.findViewById(R.id.dogDetailsInListItem);

            dogDetailsInListItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // on click - open the dog owner details
                    Intent intent = new Intent(getActivity().getApplicationContext(), DogOwnerDetailsActivity.class);
                    intent.putExtra("ownerId", owner.getId());
                    intent.putExtra("walkerId", userId);
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }
}
