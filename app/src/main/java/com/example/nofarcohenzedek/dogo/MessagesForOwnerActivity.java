//package com.example.nofarcohenzedek.dogo;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toolbar;
//
//import com.example.nofarcohenzedek.dogo.Model.DogWalker;
//import com.example.nofarcohenzedek.dogo.Model.Model;
//import com.example.nofarcohenzedek.dogo.Model.User;
//
//import java.util.LinkedList;
//import java.util.List;
//
///**
// * Created by Nofar Cohen Zedek on 11-Jan-16.
// */
//public class MessagesForOwnerActivity  extends Activity {
//    ListView list;
//    List<DogWalker> data;
//    ProgressBar progressBar;
//    User currentUser;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_messages);
//        setActionBar((Toolbar) findViewById(R.id.messagesToolBar));
//        //getActionBar().setDisplayShowTitleEnabled(false);
//
//        progressBar = (ProgressBar) findViewById(R.id.messagesProgressBar);
//        data = new LinkedList<>();
//        progressBar.setVisibility(View.VISIBLE);
//        list = (ListView) findViewById(R.id.messagesList);
//        final MessagesAdapter adapter = new MessagesAdapter();
//        list.setAdapter(adapter);
//
//        Model.getInstance().getCurrentUser(new Model.GetUserListener() {
//            @Override
//            public void onResult(User user) {
//                currentUser = user;
//
//                Model.getInstance().getRequestOfDogOwner(user.getId(), new Model.GetDogWalkersListener() {
//                    @Override
//                    public void onResult(List<DogWalker> dogWalkers) {
//                        data = dogWalkers;
////                        for (DogWalker dogWalker : dogWalkers) {
////                            data.add(dogWalker);
////                        }
//                        progressBar.setVisibility(View.GONE);
//                    }
//                });
//
//            }
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(final Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//
////        Model.getInstance().getCurrentUser(new Model.GetUserListener2() {
////            @Override
////            public void onResult(User user) {
////                if (user instanceof DogOwner) {
////                    getMenuInflater().inflate(R.menu.menu_prime_dog_owner, menu);
////                } else {
////                    getMenuInflater().inflate(R.menu.menu_prime_dog_walker, menu);
////                }
////            }
////        });
//
////        if (isOwner) {
////            getMenuInflater().inflate(R.menu.menu_prime_dog_owner, menu);
////        } else {
////            getMenuInflater().inflate(R.menu.menu_prime_dog_walker, menu);
////        }
//
//        return true;
//        //return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // todo: ???
//        int id = item.getItemId();
//
//        Intent intent = null;
//
//        if (id == R.id.searchDW) {
//            intent = new Intent(this, SearchActivity.class);
//        } else if (id == R.id.map) {
//            intent = new Intent(this, MapsActivity.class);
//        } else if (id == R.id.tripsReport) {
//            intent = new Intent(this, TripsReportActivity.class);
//        } else if (id == R.id.dogsList) {
//            intent = new Intent(this, DogsListActivity.class);
//        } else if (id == R.id.myProfile) {
//            intent = new Intent(this, MyProfileActivity.class);
//
//        }
//
//        intent.putExtra("isOwner", false);
//        intent.putExtra("userId", getIntent().getLongExtra("userId", 0));
//        startActivity(intent);
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    class MessagesAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return data.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return data.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//
//            if (convertView == null) {
//                LayoutInflater inflater = getLayoutInflater();
//                convertView = inflater.inflate(R.layout.owner_requests_row_layout, null);
//            }
//
//            TextView dogWalkerNameTV = (TextView) convertView.findViewById(R.id.dogWalkerName);
//
//            DogWalker dogWalker = data.get(position);
//            dogWalkerNameTV.setText(dogWalker.getFirstName() + " " + dogWalker.getLastName());
//            return convertView;
//        }
//    }
//}
//
