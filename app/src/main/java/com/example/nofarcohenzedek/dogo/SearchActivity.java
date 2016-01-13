package com.example.nofarcohenzedek.dogo;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.Model;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity {

    private List<DogWalker> list;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        progressBar = (ProgressBar) findViewById(R.id.searchProgressBar);

        setActionBar((Toolbar) findViewById(R.id.searchToolBar));
        getActionBar().setDisplayShowTitleEnabled(false);

        ((ListView)findViewById(R.id.searchResultList)).setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getApplicationContext(),DogWalkerDetails.class);
                intent.putExtra("walkerId", Long.toString(id));
                intent.putExtra("ownerId", getIntent().getLongExtra("userId",0));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_prime_dog_owner, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent = null;

        if (id == R.id.dogsList) {
            intent = new Intent(this, DogsListActivity.class);
        } else if (id == R.id.map) {
            intent = new Intent(this, MapsActivity.class);
        } else if (id == R.id.tripsReport) {
            intent = new Intent(this, TripsReportActivity.class);
        } else if (id == R.id.messages) {
            intent = new Intent(this, MessagesActivity.class);
        } else if (id == R.id.myProfile) {
            intent = new Intent(this, MyProfileActivity.class);

        }

        intent.putExtra("isOwner", true);
        intent.putExtra("userId", getIntent().getLongExtra("userId",0));
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

    public void radioButtonSearchDWBy(View view)
    {
        RadioButton searchByRadius = (RadioButton)findViewById(R.id.searchByDistance);
        RadioButton searchByParametes = (RadioButton)findViewById(R.id.searchByParameters);
        LinearLayout layoutDistance = (LinearLayout)findViewById(R.id.searchByDistanceLayout);
        LinearLayout layoutParameters = (LinearLayout)findViewById(R.id.searchByParametersLayout);

        if(searchByRadius.isChecked())
        {
            layoutDistance.setVisibility(View.VISIBLE);
            layoutParameters.setVisibility(View.GONE);
        }
        else if(searchByParametes.isChecked())
        {
            layoutParameters.setVisibility(View.VISIBLE);
            layoutDistance.setVisibility(View.GONE);
        }

        Button searchBTN = (Button) findViewById(R.id.searchBTN);
        searchBTN.setEnabled(true);
    }

    public void searchBTNClick (View view)
    {
        progressBar.setVisibility(View.VISIBLE);

        // check if by distance or by param
        RadioButton byDistance = (RadioButton) findViewById(R.id.searchByDistance);
        RadioButton byParams = (RadioButton) findViewById(R.id.searchByParameters);

        if (byDistance.isChecked())
        {
            String radius = ((EditText)findViewById(R.id.radiusForSearch)).getText().toString();

            if (!radius.equals(""))
            {
                searchByDistance(Long.valueOf(radius));
            }
        }
        else if (byParams.isChecked())
        {
            String age = ((EditText)findViewById(R.id.ageForSearch)).getText().toString();
            String price = ((EditText)findViewById(R.id.priceForSearch)).getText().toString();
            boolean morning = ((CheckBox)findViewById(R.id.cbx_isComfortableOnMorningForSearch)).isChecked();
            boolean noon = ((CheckBox)findViewById(R.id.cbx_isComfortableOnAfternoonForSearch)).isChecked();
            boolean evening = ((CheckBox)findViewById(R.id.cbx_isComfortableOnEveningForSearch)).isChecked();

            searchByParameters(age, price, morning, noon, evening);
        }

        if (list != null)
        {
            CustomAdapter adapter = new CustomAdapter();
            ListView listView = (ListView) findViewById(R.id.searchResultList);
            listView.setAdapter(adapter);

        }

        progressBar.setVisibility(View.GONE);
    }

    private void searchByDistance(Long radiusInMeters)
    {
    }

    private  void searchByParameters (final String age, final String price, final boolean morning, final boolean noon, final boolean evening)
    {
        Model.getInstance().getAllDogWalkers(new Model.GetDogWalkersListener()
        {
            @Override
            public void onResult(List<DogWalker> dogWalkers)
            {
                List<DogWalker> temp = new ArrayList<DogWalker>();

                // work around concurrentModificationExeption
                for (DogWalker dogWalker : dogWalkers)
                {
                    temp.add(dogWalker);
                }

                for (DogWalker dogWalker : dogWalkers)
                {
                    if (!age.equals("") && dogWalker.getAge() < Long.valueOf(age))
                    {
                        temp.remove(dogWalker);
                    }

                    if (!price.equals("") && dogWalker.getPriceForHour() > Integer.valueOf(price))
                    {
                        temp.remove(dogWalker);
                    }

                    if (morning && !dogWalker.isComfortableOnMorning() ||
                            noon && !dogWalker.isComfortableOnAfternoon() ||
                            evening && !dogWalker.isComfortableOnEvening())
                    {
                        temp.remove(dogWalker);
                    }
                }

                list = temp;
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
                LayoutInflater inflater = getLayoutInflater();
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


