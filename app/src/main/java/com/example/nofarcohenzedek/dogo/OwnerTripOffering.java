package com.example.nofarcohenzedek.dogo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OwnerTripOffering extends Fragment {

    private long id;

    public OwnerTripOffering(){

    }

    public OwnerTripOffering(long id)
    {
        this.id = id;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_search, container, false);

        return view;
    }

}
