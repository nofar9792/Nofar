package com.example.nofarcohenzedek.dogo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.User;

public class LoginActivity extends Activity {

   // public User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void loginBtnClick(View view)
    {
        EditText userName = (EditText) findViewById(R.id.userNameLogin);
        EditText password = (EditText) findViewById(R.id.passwordLogin);
        Model.getInstance().logIn(userName.getText().toString(), password.getText().toString(), new Model.GetUserListener2() {
            @Override
            public void onResult(User user)
            {
              //  currentUser = user;

                if (user instanceof DogOwner)
                {
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtra("isOwner", true);
                    intent.putExtra("userId", user.getId());
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), DogsListActivity.class);
                    intent.putExtra("isOwner", false);
                    intent.putExtra("userId", user.getId());
                    startActivity(intent);
                }

            }
        });
    }

}
