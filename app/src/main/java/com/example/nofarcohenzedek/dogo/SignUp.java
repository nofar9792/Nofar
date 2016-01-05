package com.example.nofarcohenzedek.dogo;

import android.app.Activity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

public class SignUp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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
