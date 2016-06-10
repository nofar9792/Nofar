package com.example.nofarcohenzedek.dogo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.User;

public class LoginActivity extends Activity {
    private String errorMessage;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        FontReplacer.replaceDefaultFont(getApplicationContext(), "DEFAULT", "fonts/Guttman Yad-Brush.ttf");
        FontReplacer.replaceDefaultFont(getApplicationContext(), "MONOSPACE", "fonts/Guttman Yad-Brush.ttf");
        FontReplacer.replaceDefaultFont(getApplicationContext(), "SERIF", "fonts/Guttman Yad-Brush.ttf");
        FontReplacer.replaceDefaultFont(getApplicationContext(), "SANS_SERIF", "fonts/Guttman Yad-Brush.ttf");
        //Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Guttman Yad-Brush.ttf");
        //TextView userNameTextView = (TextView) findViewById(R.id.lbl_userNameLogin);
        //userNameTextView.setTypeface(font);
        //TextView passwordTextView = (TextView) findViewById(R.id.lbl_passwordLogin);
        //userNameTextView.setTypeface(font);
    }

    public void loginBtnClick(View view)
    {
        EditText userName = (EditText) findViewById(R.id.userNameLogin);
        EditText password = (EditText) findViewById(R.id.passwordLogin);

        if (isValid(userName.getText().toString(), password.getText().toString())) {
            progressBar = (ProgressBar) findViewById(R.id.loginProgressBarr);
            progressBar.setVisibility(View.VISIBLE);
            Model.getInstance().logIn(userName.getText().toString(), password.getText().toString(), new Model.GetUserListener()
            {
                @Override
                public void onResult(User user)
                {
                    if (user != null)
                    {
                        Intent intent = new Intent(getApplicationContext(),ActionBarActivity.class);
                        intent.putExtra("userId", user.getId());

                        if (user instanceof DogOwner)
                        {
                            intent.putExtra("isOwner", true);
                            intent.putExtra("address", user.getAddress() + ", " + user.getCity());
                        }
                        else
                        {
                            intent.putExtra("isOwner", false);
                        }
                        progressBar.setVisibility(View.GONE);
                        startActivity(intent);
                    }
                    else
                    {
                        TextView error = (TextView)findViewById(R.id.error);
                        error.setText("שם משתמש או הסיסמא אינם נכונים.");
                        progressBar.setVisibility(View.GONE);
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
