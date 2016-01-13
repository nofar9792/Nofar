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
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.User;

import java.util.LinkedList;
import java.util.List;

public class MessagesActivity extends Activity {
    private ListView list;
    private List<User> data;
    private Boolean isOwner;
    private ProgressBar progressBar;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        setActionBar((Toolbar) findViewById(R.id.messagesToolBar));
        //getActionBar().setDisplayShowTitleEnabled(false);

        isOwner = getIntent().getBooleanExtra("isOwner", false);
        progressBar = (ProgressBar) findViewById(R.id.messagesProgressBar);
        data = new LinkedList<>();
        progressBar.setVisibility(View.VISIBLE);
        list = (ListView) findViewById(R.id.messagesList);
        final MessagesAdapter adapter = new MessagesAdapter();

        Model.getInstance().getCurrentUser(new Model.GetUserListener() {
            @Override
            public void onResult(User user) {
                currentUser = user;

                if (user instanceof DogWalker) {
                    Model.getInstance().getRequestForDogWalker(user.getId(), new Model.GetDogOwnersListener() {
                        @Override
                        public void onResult(List<DogOwner> dogOwners) {
                            for (DogOwner dogOwner : dogOwners) {
                                data.add(dogOwner);
                                list.setAdapter(adapter);
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
                else{
                    Model.getInstance().getRequestOfDogOwner(user.getId(), new Model.GetDogWalkersListener() {
                        @Override
                        public void onResult(List<DogWalker> dogWalkers) {
                            for (DogWalker dogWalker : dogWalkers) {
                                data.add(dogWalker);
                                list.setAdapter(adapter);
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

//        Model.getInstance().getCurrentUser(new Model.GetUserListener2() {
//            @Override
//            public void onResult(User user) {
//                if (user instanceof DogOwner) {
//                    getMenuInflater().inflate(R.menu.menu_prime_dog_owner, menu);
//                } else {
//                    getMenuInflater().inflate(R.menu.menu_prime_dog_walker, menu);
//                }
//            }
//        });

        if (isOwner) {
            getMenuInflater().inflate(R.menu.menu_prime_dog_owner, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_prime_dog_walker, menu);
        }

        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // todo: ???
        int id = item.getItemId();

        Intent intent = null;

        if (id == R.id.searchDW) {
            intent = new Intent(this, SearchActivity.class);
        } else if (id == R.id.map) {
            intent = new Intent(this, MapsActivity.class);
        } else if (id == R.id.tripsReport) {
            intent = new Intent(this, TripsReportActivity.class);
        } else if (id == R.id.dogsList) {
            intent = new Intent(this, DogsListActivity.class);
        } else if (id == R.id.myProfile) {
            intent = new Intent(this, MyProfileActivity.class);

        }

        intent.putExtra("isOwner", isOwner);
        intent.putExtra("userId", getIntent().getLongExtra("userId", 0));
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

    class MessagesAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(currentUser instanceof DogWalker) {
                if (convertView == null) {
                    LayoutInflater inflater = getLayoutInflater();
                    convertView = inflater.inflate(R.layout.walker_requests_row_layout, null);
                    Button acceptButton = (Button) convertView.findViewById(R.id.acceptButton);
                    Button declineButton = (Button) convertView.findViewById(R.id.declineButton);
                    acceptButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Model.getInstance().acceptRequest(data.get(position).getId(), currentUser.getId());
                            data.remove(position);
                            notifyDataSetChanged();
                        }
                    });

                    declineButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Model.getInstance().declineRequest(data.get(position).getId(), currentUser.getId());
                            data.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                }

                TextView dogOwnerNameTV = (TextView) convertView.findViewById(R.id.dogOwnerName);
                TextView dogNameTV = (TextView) convertView.findViewById(R.id.dogName);

                DogOwner dogOwner = (DogOwner) data.get(position);
                dogOwnerNameTV.setText(dogOwner.getFirstName() + " " + dogOwner.getLastName());
                dogNameTV.setText(dogOwner.getDog().getName());
            }
            else{
                if (convertView == null) {
                    LayoutInflater inflater = getLayoutInflater();
                    convertView = inflater.inflate(R.layout.owner_requests_row_layout, null);
                }

                TextView dogWalkerNameTV = (TextView) convertView.findViewById(R.id.dogWalkerName);

                DogWalker dogWalker = (DogWalker) data.get(position);
                dogWalkerNameTV.setText(dogWalker.getFirstName() + " " + dogWalker.getLastName());
            }
            return convertView;
        }
    }
}
