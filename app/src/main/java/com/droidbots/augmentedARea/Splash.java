package com.droidbots.augmentedARea;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;


public class Splash extends Activity {
    String[] permissions = {"android.permission.CAMERA"};
    int permsRequestCode = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);

            requestPermissions(permissions,permsRequestCode);
            new Handler().postDelayed(new Runnable() {

                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(Splash.this, MainActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, 2000);


    }
}
