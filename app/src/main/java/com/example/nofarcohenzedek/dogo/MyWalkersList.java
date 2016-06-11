package com.example.nofarcohenzedek.dogo;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.Model;

import java.util.List;

public class MyWalkersList extends Fragment {

    private ListView listView;
    private List<DogWalker> allWalkers;
    private long userId;
    private ProgressBar progressBar;

    public MyWalkersList(){}

    public MyWalkersList(long userId){
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.activity_my_walkers_list, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        progressBar = (ProgressBar) view.findViewById(R.id.walkersListProgressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
        listView = (ListView)view.findViewById(R.id.myWalkersList);

        Model.getInstance().getWalkersConnectToOwner(userId, new Model.GetDogWalkersListener() {
            @Override
            public void onResult(List<DogWalker> dogWalkers) {
                allWalkers = dogWalkers;

                if (allWalkers != null && allWalkers.size() != 0){
                    CustomAdapter adapter = new CustomAdapter();
                    listView.setAdapter(adapter);


                } else{
                    ((TextView) view.findViewById(R.id.errorInWalkersList)).setText("אין מוליכי כלבים שמקושרים אלייך");
                }

                progressBar.setVisibility(View.GONE);
            }
        });

        return view;
    }

    private class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return allWalkers.size();
        }

        @Override
        public Object getItem(int position) {
            return allWalkers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return allWalkers.get(position).getId();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.walker_row_layout, null);
            }

            TextView walkerName = (TextView) convertView.findViewById(R.id.walkerFullNameInList);
            TextView walkerAddress = (TextView) convertView.findViewById(R.id.walkerAddressInList);
            TextView walkerPhone = (TextView) convertView.findViewById(R.id.walkerPhoneInList);

            walkerName.setText(allWalkers.get(position).getFirstName() + " " + allWalkers.get(position).getLastName());
            walkerAddress.setText(allWalkers.get(position).getAddress() + ", " + allWalkers.get(position).getCity());
            walkerPhone.setText(allWalkers.get(position).getPhoneNumber());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity().getApplicationContext(),DogWalkerDetailsActivity.class);
                    intent.putExtra("ownerId",userId);
                    intent.putExtra("walkerId", allWalkers.get(position).getId());
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }

}
