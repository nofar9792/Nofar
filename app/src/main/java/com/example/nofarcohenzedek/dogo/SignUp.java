package com.example.nofarcohenzedek.dogo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void checkTypeOfUserBTNClick(View view)
    {
        RadioButton isOwner = (RadioButton)findViewById(R.id.isOwner);
        RadioButton isWalker = (RadioButton)findViewById(R.id.isWalker);
        LinearLayout layoutWalker = (LinearLayout)findViewById(R.id.layoutWalker);
        LinearLayout layoutOwner = (LinearLayout)findViewById(R.id.layoutOwner);

        if(isOwner.isChecked())
        {
            layoutOwner.setVisibility(View.VISIBLE);
            layoutWalker.setVisibility(View.GONE);
        }
        else if(isWalker.isChecked())
        {
            layoutWalker.setVisibility(View.VISIBLE);
            layoutOwner.setVisibility(View.GONE);
        }
    }

}
