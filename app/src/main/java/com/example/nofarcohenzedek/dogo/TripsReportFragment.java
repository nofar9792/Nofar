package com.example.nofarcohenzedek.dogo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.Trip;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TripsReportFragment extends Fragment {

    private Boolean isOwner;
    private Long userId;
    private List<Trip> allTrips;
    private ProgressBar progressBar;
    private Context context;

    private ListView listView;

    public TripsReportFragment(){}

    public TripsReportFragment(Long id, boolean IsOwner)
    {
        userId = id;
        isOwner = IsOwner;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        context = container.getContext();
        final View view = inflater.inflate(R.layout.activity_trips_report, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        Bundle args = getArguments();
        progressBar = (ProgressBar) view.findViewById(R.id.tripsReportProgressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
        listView = (ListView) view.findViewById(R.id.tripsList);
        //isOwner = args.getBoolean("isOwner");
        //userId = args.getLong("userId");

        // Get all trips that connected to current user
        if(isOwner)
        {
            Model.getInstance().getTripsByDogOwnerId(userId, new Model.GetTripsListener() {
                @Override
                public void onResult(List<Trip> trips) {
                    allTrips = trips;
                    Collections.sort(allTrips,new TripsComparator());

                    if (allTrips != null && !allTrips.isEmpty()) {
                        CustomAdapter adapter = new CustomAdapter();
                        listView.setAdapter(adapter);
                    }
                    else
                    {
                        ((TextView)view.findViewById(R.id.errorInTripsList)).setText("אין טיולים להצגה");
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
        else
        {
            Model.getInstance().getTripsByDogWalkerId(userId, new Model.GetTripsListener() {
                @Override
                public void onResult(List<Trip> trips) {
                    allTrips = trips;
                    Collections.sort(allTrips,new TripsComparator());


                    if (allTrips != null && !allTrips.isEmpty()) {
                        CustomAdapter adapter = new CustomAdapter();
                        ListView listView = (ListView) view.findViewById(R.id.tripsList);
                        listView.setAdapter(adapter);
                    }
                    else
                    {
                        ((TextView)view.findViewById(R.id.errorInTripsList)).setText("אין טיולים להצגה");
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        return view;
    }

    /**
     * pay for trip - on select 'isPaid' checkBox
     * @param view
     */
    public void payTripBTN(View view)
    {
        CheckBox isPaid = ((CheckBox)view);

        // pay for this trip - only if the user is walker and the trip wasn't paid.
        if (isPaid.isChecked() && !isOwner)
        {
            Model.getInstance().payTrip(((Long) isPaid.getTag()), new Model.IsSucceedListener() {
                @Override
                public void onResult(boolean isSucceed) {
                    if(isSucceed){
                        Toast.makeText(context, "הטיול עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context, "אירעה שגיאה, אנא נסה שוב", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            isPaid.setEnabled(false);
        }
    }

    public class TripsComparator implements Comparator<Trip> {
        @Override
        public int compare(Trip t1, Trip t2) {
            return t2.getStartOfWalking().compareTo(t1.getStartOfWalking());
        }
    }


    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return allTrips.size();
        }

        @Override
        public Object getItem(int position) {
            return allTrips.get(position);
        }

        @Override
        public long getItemId(int position) {
            return allTrips.get(position).getId();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.raw_trips_layout, null);
            }

            convertView.findViewById(R.id.isPaid).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    payTripBTN(v);
                }
            });

            TextView dogName = (TextView) convertView.findViewById(R.id.dogName);
            TextView ownerName = (TextView) convertView.findViewById(R.id.ownerName);
            TextView walkerName = (TextView) convertView.findViewById(R.id.walkerName);
            TextView date = (TextView) convertView.findViewById(R.id.date);
            TextView startTime = (TextView) convertView.findViewById(R.id.startTime);
            TextView endTime = (TextView) convertView.findViewById(R.id.endTime);
            TextView priceForTrip = (TextView) convertView.findViewById(R.id.priceForTrip);
            CheckBox isPaid = (CheckBox) convertView.findViewById(R.id.isPaid);

            // Get all properties from TRIP object
            final Trip trip = allTrips.get(position);
            dogName.setText(trip.getDogOwner().getDog().getName());
            ownerName.setText(trip.getDogOwner().getFirstName());
            walkerName.setText(trip.getDogWalker().getFirstName());
            date.setText(new SimpleDateFormat("dd/MM/yyyy").format(trip.getStartOfWalking()));
            startTime.setText(new SimpleDateFormat("HH:mm").format(trip.getStartOfWalking()));
            endTime.setText(new SimpleDateFormat("HH:mm").format(trip.getEndOfWalking()));
            isPaid.setChecked(trip.getIsPaid());

            // Calculate the price for this trip
            double hoursOfTrip;
            double minutesOfTrip;

            if (trip.getEndOfWalking().getMinutes() < trip.getStartOfWalking().getMinutes()) {
                hoursOfTrip = trip.getEndOfWalking().getHours() - 1 - trip.getStartOfWalking().getHours();
                minutesOfTrip = 60 + trip.getEndOfWalking().getMinutes() - trip.getStartOfWalking().getMinutes();
            } else {
                hoursOfTrip = trip.getEndOfWalking().getHours() - trip.getStartOfWalking().getHours();
                minutesOfTrip = trip.getEndOfWalking().getMinutes() - trip.getStartOfWalking().getMinutes();
            }

            double price = trip.getDogWalker().getPriceForHour() * (hoursOfTrip + (minutesOfTrip / 60));

            // Show only 2-3 digits after point
            String stringPrice = String.valueOf(price);
            if(stringPrice.length() > 6) {
                stringPrice = stringPrice.substring(0, 6);
            }

            priceForTrip.setText(stringPrice);

            // Add to 'isPaid' checkBox tag - trip id
            isPaid.setTag(trip.getId());

            // if isPaid checked or the current user is owner, so the checkbox is disabled
            if(isPaid.isChecked() ||  isOwner)
            {
                isPaid.setEnabled(false);
            }


            // Delete event

            if (!isOwner) {
                final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE: {

                                Model.getInstance().deleteTrip(trip.getId(), new Model.IsSucceedListener() {
                                    @Override
                                    public void onResult(boolean isSucceed) {
                                        if (isSucceed) {
                                            allTrips.remove(position);
                                            notifyDataSetChanged();
                                            Toast.makeText(getActivity().getApplicationContext(), "הטיול נמחק בהצלחה", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity().getApplicationContext(), "אירעה שגיאה בעת המחיקה", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                                break;
                            }
                        }
                    }
                };

                convertView.setOnLongClickListener(
                        new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                                dialog.setMessage("Delete?").setPositiveButton("Yes", dialogClickListener)
                                        .setNegativeButton("No", dialogClickListener).show();

                                return false;
                            }
                        }
                );
            }

            return convertView;
        }
    }
}
