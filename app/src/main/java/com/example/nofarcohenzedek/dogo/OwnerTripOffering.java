package com.example.nofarcohenzedek.dogo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.TripOffer;

import java.util.List;

public class OwnerTripOffering extends Activity {

    private long id;
    private boolean isDetails;
    private TripOffer offer;

    private EditText fromDate;
    private EditText toDate;
    private EditText minimalAge;
    private EditText maximalPrice;
    private CheckBox isComfortable6To8;
    private CheckBox isComfortable8To10;
    private CheckBox isComfortable10To12;
    private CheckBox isComfortable12To14;
    private CheckBox isComfortable14To16;
    private CheckBox isComfortable16To18;
    private CheckBox isComfortable18To20;
    private CheckBox isComfortable20To22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_trip_offering);

        id = getIntent().getLongExtra("ownerId", 0);
        isDetails = getIntent().getBooleanExtra("isDetails", true);

        fromDate = (EditText) findViewById(R.id.tripOfferStartDate);
        toDate = (EditText) findViewById(R.id.tripOfferEndDate);
        isComfortable6To8 = (CheckBox) findViewById(R.id.tripcheckbox6To8);
        isComfortable8To10 = (CheckBox) findViewById(R.id.tripcheckbox8To10);
        isComfortable10To12 = (CheckBox) findViewById(R.id.tripcheckbox10To12);
        isComfortable12To14 = (CheckBox) findViewById(R.id.tripcheckbox12To14);
        isComfortable14To16 = (CheckBox) findViewById(R.id.tripcheckbox14To16);
        isComfortable16To18 = (CheckBox) findViewById(R.id.tripcheckbox16To18);
        isComfortable18To20 = (CheckBox) findViewById(R.id.tripcheckbox18To20);
        isComfortable20To22 = (CheckBox) findViewById(R.id.tripcheckbox20To22);
        minimalAge = (EditText) findViewById(R.id.ageForTripOffering);
        maximalPrice = (EditText) findViewById(R.id.priceForTripOffering);

        if (isDetails){
            findViewById(R.id.ownerSectionInTripOffering).setVisibility(View.GONE);

            fromDate.setEnabled(false);
            toDate.setEnabled(false);
            isComfortable6To8.setEnabled(false);
            isComfortable8To10.setEnabled(false);
            isComfortable10To12.setEnabled(false);
            isComfortable12To14.setEnabled(false);
            isComfortable14To16.setEnabled(false);
            isComfortable16To18.setEnabled(false);
            isComfortable18To20.setEnabled(false);
            isComfortable20To22.setEnabled(false);

            Model.getInstance().getTripOffer(id, getIntent().getStringExtra("fromDate"), getIntent().getStringExtra("toDate"),
                    new Model.GetTripOffersListener() {
                @Override
                public void onResult(List<TripOffer> offers) {
                    if (offers != null && offers.size() >0){
                        offer = offers.get(0);

                        fromDate.setText(offer.getFromDate());
                        toDate.setText(offer.getToDate());
                        isComfortable6To8.setChecked(offer.getIsComfortable6To8());
                        isComfortable8To10.setChecked(offer.getIsComfortable8To10());
                        isComfortable10To12.setChecked(offer.getIsComfortable10To12());
                        isComfortable12To14.setChecked(offer.getIsComfortable12To14());
                        isComfortable14To16.setChecked(offer.getIsComfortable14To16());
                        isComfortable16To18.setChecked(offer.getIsComfortable16To18());
                        isComfortable18To20.setChecked(offer.getIsComfortable18To20());
                        isComfortable20To22.setChecked(offer.getIsComfortable20To22());
                    }
                }
            });
        }

        Button saveBTN = (Button) findViewById(R.id.saveOffer);
        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveOfferClick(v);
            }
        });
    }

    public void onSaveOfferClick(View view)
    {
        if (isDataValid()){

            String fromDate = this.fromDate.getText().toString();
            String toDate = this.toDate.getText().toString();
            boolean isComfortable6To8 = this.isComfortable6To8.isChecked();
            boolean isComfortable8To10 = this.isComfortable8To10.isChecked();
            boolean isComfortable10To12 = this.isComfortable10To12.isChecked();
            boolean isComfortable12To14 = this.isComfortable12To14.isChecked();
            boolean isComfortable14To16 = this.isComfortable14To16.isChecked();
            boolean isComfortable16To18 = this.isComfortable16To18.isChecked();
            boolean isComfortable18To20 = this.isComfortable18To20.isChecked();
            boolean isComfortable20To22 = this.isComfortable20To22.isChecked();
            long minimalAge = 0;
            long maximalPrice = 1000;

            if(!this.minimalAge.getText().toString().equals("")){
                minimalAge = Long.parseLong(this.minimalAge.getText().toString());
            }
            if (!this.maximalPrice.getText().toString().equals("")) {
                maximalPrice = Long.parseLong(this.maximalPrice.getText().toString());
            }

            final TripOffer newOffer = new TripOffer(id,getIntent().getStringExtra("ownerAddress"),fromDate,toDate,minimalAge,maximalPrice, isComfortable6To8,
                    isComfortable8To10,isComfortable10To12,isComfortable12To14,isComfortable14To16,isComfortable16To18,
                    isComfortable18To20,isComfortable20To22);

            Model.getInstance().getTripOffer(id, fromDate, toDate, new Model.GetTripOffersListener() {
                @Override
                public void onResult(List<TripOffer> offers) {
                    // if the offer doesnt already exist
                    if (offers != null && offers.size() == 0) {
                        Model.getInstance().addTripOffer(newOffer, new Model.IsSucceedListener() {
                            @Override
                            public void onResult(boolean isSucceed) {
                                if (isSucceed) {
                                    Toast.makeText(getApplicationContext(), "הבקשה התפרסמה", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "אירעה שגיאה בעת פרסום הבקשה", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else if (offers != null && offers.size() > 0){
                        Toast.makeText(getApplicationContext(), "קיימת כבר בקשה בתאריכים אלו", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "אירעה שגיאה בעת פרסום הבקשה", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private boolean isDataValid()
    {
       return true;
    }

}
