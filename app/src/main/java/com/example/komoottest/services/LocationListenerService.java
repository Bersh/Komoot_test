package com.example.komoottest.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.komoottest.Constants;
import com.example.komoottest.R;
import com.example.komoottest.activities.MainActivity;

import java.util.Random;

/**
 * @author <a href="mailto:iBersh20@gmail.com">Iliya Bershadskiy</a>
 * @since 30.03.2015
 */
public class LocationListenerService extends Service implements LocationListener {
    private static final int REQUEST_CODE_SENDER = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        enableLocationTracking();
        startForeground(new Random().nextInt(), createForegroundNotification());
        SharedPreferences sharedPreferences = getSharedPreferences(
                Constants.SharedPreferences.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.SharedPreferences.SHARED_PREFERENCES_KEY_LOCATION_TRACING_STARTED, true);
        editor.apply();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disableLocationTracking();
        SharedPreferences sharedPreferences = getSharedPreferences(
                Constants.SharedPreferences.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.SharedPreferences.SHARED_PREFERENCES_KEY_LOCATION_TRACING_STARTED, false);
        editor.apply();
        Toast.makeText(this, getString(R.string.location_tracking_stopped), Toast.LENGTH_SHORT).show();
        stopForeground(true);
    }

    /**
     * Enable location tracking using one of the location provider (LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
     * and notify user about particular provider using Toast.
     */
    private void enableLocationTracking() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Constants.LOCATION_MIN_TIME, Constants.LOCATION_MIN_DISTANCE, this);
            Toast.makeText(this, getString(R.string.provider_gps), Toast.LENGTH_SHORT).show();
        } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Constants.LOCATION_MIN_TIME, Constants.LOCATION_MIN_DISTANCE, this);
            Toast.makeText(this, getString(R.string.provider_network), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.provider_unknown), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Disable location tracking.
     */
    private void disableLocationTracking() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        manager.removeUpdates(this);
    }

    private Notification createForegroundNotification() {
        //create Intent instance for PendingIntent
        Intent intent = new Intent(this, MainActivity.class);
        //create PendingIntent instance
        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE_SENDER, intent, 0);
        //create Notification instance
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_content))
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap())
                .build();
        return notification;
    }

    @Override
    public void onLocationChanged(Location location) {
        //todo implement
        //if we received this update then we walked at least 100 meters. Load image

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        //restart tracking here
        disableLocationTracking();
        enableLocationTracking();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
