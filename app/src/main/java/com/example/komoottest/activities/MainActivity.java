package com.example.komoottest.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ListView;

import com.example.komoottest.App;
import com.example.komoottest.Constants;
import com.example.komoottest.R;
import com.example.komoottest.adapters.ImagesStreamAdapter;
import com.example.komoottest.events.RefreshStreamEvent;
import com.example.komoottest.services.LocationListenerService;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import de.greenrobot.event.EventBus;

/**
 * @author <a href="mailto:iBersh20@gmail.com">Iliya Bershadskiy</a>
 * @since 31.03.2015
 */
public class MainActivity extends ActionBarActivity {
    private ListView listView;
    private ImagesStreamAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list);
        findViewById(R.id.btn_start_stop).setOnClickListener(new BtnStartStopOnClickListener());
        adapter = new ImagesStreamAdapter(this, App.getImageFileNames(Constants.IMAGES_FOLDER));
        listView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(RefreshStreamEvent event) {
        adapter.clear();
        adapter.addAll(App.getImageFileNames(Constants.IMAGES_FOLDER));
        EventBus.getDefault().removeStickyEvent(RefreshStreamEvent.class);
    }

    private class BtnStartStopOnClickListener implements View.OnClickListener {
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
                try {
                    FileUtils.cleanDirectory(Constants.IMAGES_FOLDER);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MainActivity.this.startService(intent);
            }
        }
    }
}
