package com.example.komoottest.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.komoottest.App;
import com.example.komoottest.Constants;
import com.example.komoottest.R;
import com.example.komoottest.activities.MainActivity;
import com.example.komoottest.api.ApiManager;
import com.example.komoottest.events.RefreshStreamEvent;
import com.example.komoottest.model.PhotosDto;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * RHis service will track user location and start loading images when needed
 *
 * @author <a href="mailto:iBersh20@gmail.com">Iliya Bershadskiy</a>
 * @since 30.03.2015
 */
public class LocationListenerService extends Service implements LocationListener {
    private static final int REQUEST_CODE_SENDER = 0;
    private PowerManager.WakeLock wakeLock;
    private ApiManager apiManager;

    @Override
    public void onCreate() {
        super.onCreate();
        apiManager = new ApiManager();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Tag");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        enableLocationTracking();
        startForeground(new Random().nextInt(), createForegroundNotification());
        SharedPreferences sharedPreferences = getSharedPreferences(
                Constants.SharedPreferences.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.SharedPreferences.SHARED_PREFERENCES_KEY_LOCATION_TRACING_STARTED, true);
        editor.apply();
        if (!wakeLock.isHeld()) {
            wakeLock.acquire();
        }
        return START_STICKY;
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
        if (!wakeLock.isHeld()) {
            wakeLock.release();
        }
        stopForeground(true);
    }

    /**
     * Enable location tracking using one of the location provider (LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
     * and notify user about particular provider using Toast.
     */
    private void enableLocationTracking() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String bestProvider = manager.getBestProvider(criteria, false);
        if (manager.isProviderEnabled(bestProvider)) {
            manager.requestLocationUpdates(bestProvider, Constants.LOCATION_MIN_TIME, Constants.LOCATION_MIN_DISTANCE, this);
            Toast.makeText(this, getString(R.string.location_tracking_started), Toast.LENGTH_SHORT).show();
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
        Log.d(Constants.LOG_TAG, "onLocationChanged()");
        //if we received this update then we walked at least 100 meters. Load image
        apiManager.getImagesForLocation(location.getLatitude(), location.getLongitude(), new Callback<PhotosDto>() {
            @Override
            public void success(PhotosDto photosDto, Response response) {
                if (photosDto.getPhotos() != null && !photosDto.getPhotos().isEmpty()) {
                    String photoUrl = photosDto.getPhotos().get(0).getPhotoFileUrl();
                    Picasso.with(getApplicationContext())
                            .load(photoUrl)
                            .into(new SaveToSDCardTarget());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(Constants.LOG_TAG, error.getMessage(), error);
                Toast.makeText(LocationListenerService.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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


    /**
     * Save loaded image to sd card
     */
    private static class SaveToSDCardTarget implements Target {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            File file = new File(App.getNewFileName(Constants.IMAGES_FOLDER));
            try {
                FileOutputStream fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();
                EventBus.getDefault().postSticky(new RefreshStreamEvent());
            } catch (IOException e) {
                Log.e(Constants.LOG_TAG, e.getMessage(), e);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.e(Constants.LOG_TAG, "Image loading failed");
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }
}
