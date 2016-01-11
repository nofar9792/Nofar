package com.example.nofarcohenzedek.dogo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.User;

public class LoginActivity extends Activity {
    private String errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void loginBtnClick(View view)
    {
        EditText userName = (EditText) findViewById(R.id.userNameLogin);
        EditText password = (EditText) findViewById(R.id.passwordLogin);

        if (isValid(userName.getText().toString(), password.getText().toString())) {
            Model.getInstance().logIn(userName.getText().toString(), password.getText().toString(), new Model.GetUserListener() {
                @Override
                public void onResult(User user)
                {
                    if (user != null) {
                        if (user instanceof DogOwner) {
                            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                            intent.putExtra("isOwner", true);
                            intent.putExtra("userId", user.getId());
                            intent.putExtra("address", user.getAddress() + ", " + user.getCity());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), DogsListActivity.class);
                            intent.putExtra("isOwner", false);
                            intent.putExtra("userId", user.getId());
                            startActivity(intent);
                        }
                    }
                    else
                    {
                        TextView error = (TextView)findViewById(R.id.error);
                        error.setText("שם משתמש או הסיסמא אינם נכונים.");
                    }
                }
            });
        }
    }

    private boolean isValid(String userName, String password)
    {
        boolean isValid = true;
        errorMessage = "";

        if (userName.isEmpty() || password.isEmpty()) {
            errorMessage = "אנא מלא את כל שדות ההתחברות.";
            isValid = false;
        }

        TextView error = (TextView)findViewById(R.id.error);
        error.setText(errorMessage);

        return isValid;
    }

}
