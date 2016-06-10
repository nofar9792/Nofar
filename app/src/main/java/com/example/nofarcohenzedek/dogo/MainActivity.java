package com.example.nofarcohenzedek.dogo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.nofarcohenzedek.dogo.Model.Model;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context appContext = getApplicationContext();
        FontReplacer.replaceDefaultFont(appContext, "DEFAULT", "fonts/Guttman Yad-Brush.ttf");
        FontReplacer.replaceDefaultFont(appContext, "MONOSPACE", "fonts/Guttman Yad-Brush.ttf");
        FontReplacer.replaceDefaultFont(appContext, "SERIF", "fonts/Guttman Yad-Brush.ttf");
        FontReplacer.replaceDefaultFont(appContext, "SANS_SERIF", "fonts/Guttman Yad-Brush.ttf");
        Model.getInstance().init(appContext);
    }

    public void signUpBTNClick(View view)
    {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivityForResult(intent, 1);
    }


    public void loginBTNClick(View view)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
