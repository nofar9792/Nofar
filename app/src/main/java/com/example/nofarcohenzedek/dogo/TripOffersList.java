package com.example.nofarcohenzedek.dogo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.TripOffer;
import com.example.nofarcohenzedek.dogo.Model.User;
import com.example.nofarcohenzedek.dogo.Model.Utilities;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class TripOffersList extends Fragment {

    private long userId;
    private boolean isOwner;
    private String ownerAddress;
    private DogWalker walker;
    private ProgressBar progressBar;

    private ListView listView;
    private List<TripOffer> allOffers;

    public TripOffersList() {
    }

    public TripOffersList(long id, boolean isOwner, String ownerAddress) {
        this.userId = id;
        this.isOwner = isOwner;
        this.ownerAddress = ownerAddress;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_trip_offers_list, container, false);

        allOffers = new ArrayList<TripOffer>();
        listView = (ListView) view.findViewById(R.id.tripOffersList);
        progressBar = (ProgressBar) view.findViewById(R.id.tripOffersProgressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);

        if (isOwner) {

            view.findViewById(R.id.offersWalkerLayout).setVisibility(View.GONE);

            ((ImageButton) view.findViewById(R.id.addNewOfferBtn)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), OwnerTripOffering.class);
                    intent.putExtra("ownerId", userId);
                    intent.putExtra("isDetails", false);
                    intent.putExtra("ownerAddress", ownerAddress);
                    startActivity(intent);
                }
            });

            progressBar.setVisibility(View.VISIBLE);
            Model.getInstance().getTripOffersByOwnerId(this.userId, new Model.GetTripOffersListener() {
                @Override
                public void onResult(List<TripOffer> offers) {

                    if (offers != null && !offers.isEmpty()) {
                        allOffers = offers;

                        CustomAdapter adapter = new CustomAdapter();
                        listView.setAdapter(adapter);

                    } else {
                        ((TextView) getActivity().findViewById(R.id.errorInTripOffersList)).setText("לא נמצאו פרסומי טיולים");
                    }

                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            view.findViewById(R.id.addNewOfferBtn).setVisibility(View.GONE);

            Model.getInstance().getUserById(userId, new Model.GetUserListener() {
                        @Override
                        public void onResult(User user) {
                            if (user != null & user instanceof DogWalker) {
                                walker = (DogWalker) user;
                                view.findViewById(R.id.showOffersByDistance).setEnabled(true);
                            }
                        }
                    }
            );

            view.findViewById(R.id.showOffersByDistance).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((TextView) getActivity().findViewById(R.id.errorInTripOffersList)).setText("");
                    allOffers.clear();
                    progressBar.setVisibility(View.VISIBLE);

                    Model.getInstance().getAllTripOffersByAgeAndPrice(walker.getAge(), walker.getPriceForHour(),
                            new Model.GetTripOffersListener() {
                                @Override
                                public void onResult(List<TripOffer> offers) {

                                    if (((EditText) view.findViewById(R.id.raduisForOffersList)).getText().toString().equals("")) {
                                        allOffers = offers;
                                    }
                                    // calculate distance
                                    else {
                                        long maxDistanceIsMeters = Long.parseLong(((EditText) view.findViewById(R.id.raduisForOffersList)).getText().toString());

                                        final LatLng walkerCoor = Utilities.getLocationFromAddress(walker.getAddress() + ", " + walker.getCity(), getActivity().getApplicationContext());
                                        final Location walkerLocation = new Location("walker");
                                        walkerLocation.setLatitude(walkerCoor.latitude);
                                        walkerLocation.setLongitude(walkerCoor.longitude);

                                        for (TripOffer offer : offers) {

                                            LatLng offerCoor = Utilities.getLocationFromAddress(offer.getOwnerAddress(), getActivity().getApplicationContext());
                                            Location offerLocation = new Location("offer");
                                            offerLocation.setLatitude(offerCoor.latitude);
                                            offerLocation.setLongitude(offerCoor.longitude);

                                            if (walkerLocation.distanceTo(offerLocation) <= (float) maxDistanceIsMeters) {
                                                allOffers.add(offer);
                                            }
                                        }
                                    }

                                    CustomAdapter adapter = new CustomAdapter();
                                    listView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();

                                    if (allOffers.size() == 0) {
                                        ((TextView) getActivity().findViewById(R.id.errorInTripOffersList)).setText("לא נמצאו פרסומי טיולים");
                                    }

                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                }
            });
        }

        return view;
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return allOffers.size();
        }

        @Override
        public Object getItem(int position) {
            return allOffers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return allOffers.get(position).getOwnerId();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.trip_offer_row_layout, null);
            }

            TextView startDate = (TextView) convertView.findViewById(R.id.tripOfferStartDateInList);
            TextView endDate = (TextView) convertView.findViewById(R.id.tripOfferEndDateInList);

            startDate.setText(allOffers.get(position).getFromDate());
            endDate.setText(allOffers.get(position).getToDate());

            if (isOwner) {
                convertView.findViewById(R.id.tripOfferOwnerDetails).setVisibility(View.GONE);
            }

            convertView.findViewById(R.id.tripOfferDetails).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), OwnerTripOffering.class);
                    intent.putExtra("ownerId", allOffers.get(position).getOwnerId());
                    intent.putExtra("fromDate", allOffers.get(position).getFromDate());
                    intent.putExtra("toDate", allOffers.get(position).getToDate());
                    intent.putExtra("isDetails", true);
                    startActivity(intent);

                }
            });

            convertView.findViewById(R.id.tripOfferOwnerDetails).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), DogOwnerDetailsActivity.class);
                    intent.putExtra("ownerId", allOffers.get(position).getOwnerId());
                    intent.putExtra("walkerId", userId);
                    startActivity(intent);
                }
            });


            // Delete event

            if (isOwner) {
                final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE: {

                                Model.getInstance().deleteTripOffer(allOffers.get(position).getOwnerId(),
                                        allOffers.get(position).getFromDate(), allOffers.get(position).getToDate()
                                        , new Model.IsSucceedListener() {
                                            @Override
                                            public void onResult(boolean isSucceed) {
                                                if (isSucceed) {
                                                    allOffers.remove(position);
                                                    notifyDataSetChanged();
                                                    Toast.makeText(getActivity().getApplicationContext(), "ההצעה נמחקה בהצלחה", Toast.LENGTH_SHORT).show();
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
