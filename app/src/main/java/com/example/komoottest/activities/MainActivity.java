package com.example.komoottest.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ListView;

import com.example.komoottest.Constants;
import com.example.komoottest.R;
import com.example.komoottest.services.LocationListenerService;


public class MainActivity extends ActionBarActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_start_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(
                        Constants.SharedPreferences.SHARED_PREFERENCES_NAME,
                        Context.MODE_PRIVATE);
                boolean isTrackingStarted = sharedPreferences.getBoolean(Constants.SharedPreferences.SHARED_PREFERENCES_KEY_LOCATION_TRACING_STARTED, false);
                Intent intent = new Intent(MainActivity.this, LocationListenerService.class);
                if (isTrackingStarted) {
                    MainActivity.this.stopService(intent);
                } else {
                    MainActivity.this.startService(intent);
                }
            }
        });
    }
}
