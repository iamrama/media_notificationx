package com.rk.medianotify.media_notificationx.src;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.media.session.MediaButtonReceiver;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.rk.medianotify.media_notificationx.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class NotificationServiceManager extends Service {
    public static int NOTIFICATION_ID = 1;
    public static final String CHANNEL_ID = "media_notificationx";
    public static final String MEDIA_SESSION_TAG = "media_notificationx";

    Bitmap bitmap;
    boolean isPlaying;
    String title= "";
    String author= "";
    String imageUrl= "";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         isPlaying = intent.getBooleanExtra("isPlaying", true);
         title = intent.getStringExtra("title");
         author = intent.getStringExtra("author");
         imageUrl = intent.getStringExtra("imageUrl");
        getBitmapfromUrl(imageUrl);
        createNotificationChannel();


        //TODO(ALI): add media mediaSession Buttons and handle them


        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    " Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            serviceChannel.setDescription("media_notificationx");
            serviceChannel.setShowBadge(false);
            serviceChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        stopForeground(true);
    }



    public void getBitmapfromUrl(final String imageUrl) {
        try {

            class ImageFromUrl extends AsyncTask<String, Void, Bitmap> {


                @Override
                protected Bitmap doInBackground(String[] params) {
                    try {
                        URL url = new URL(imageUrl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        bitmap = BitmapFactory.decodeStream(input);
                        return bitmap;
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        return null;

                    }
                }

                @Override
                protected void onPostExecute(Bitmap message) {
                    bitmap = message;
                    notificationChannel();
                }
            }
            ImageFromUrl imageFromUrl = new ImageFromUrl();
            imageFromUrl.execute(imageUrl);


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }

    public  void notificationChannel(){

        MediaSessionCompat mediaSession = new MediaSessionCompat(this, MEDIA_SESSION_TAG);




        Intent intent = new Intent(this, NotificationBroadcastReceiver.class)
                .setAction("playItem")
                .putExtra("title", title)
                .putExtra("author", author)
                .putExtra("imageUrl", imageUrl)
                .putExtra("play", !isPlaying);
        PendingIntent pendingToggleIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        MediaButtonReceiver.handleIntent(mediaSession, intent);

        int iconPlay = R.drawable.baseline_play_arrow_black_48;
        String titlePause = "pause";
        if (isPlaying) {
            iconPlay = R.drawable.baseline_pause_black_48;
            titlePause = "play";
        }


        Intent nextIntent = new Intent(this, NotificationBroadcastReceiver.class)
                .setAction("next");
        PendingIntent pendingNextIntent = PendingIntent.getBroadcast(this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent prevIntent = new Intent(this, NotificationBroadcastReceiver.class)
                .setAction("previous");
        PendingIntent pendingPrevIntent = PendingIntent.getBroadcast(this, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent selectIntent = new Intent(this, NotificationBroadcastReceiver.class)
                .setAction("select");
        PendingIntent selectPendingIntent = PendingIntent.getBroadcast(this, 0, selectIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//        MediaButtonReceiver.handleIntent(mediaSession, selectIntent);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .addAction(R.drawable.baseline_skip_previous_black_48, "previous", pendingPrevIntent)
                .addAction(iconPlay, titlePause, pendingToggleIntent)
                .addAction(R.drawable.baseline_skip_next_black_48, "next", pendingNextIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2)
                        .setShowCancelButton(true)
                        .setMediaSession(mediaSession.getSessionToken()))
                .setSmallIcon(R.drawable.baseline_skip_next_black_48)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setVibrate(new long[]{0L})
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentTitle(title)
                .setContentText(author)

                .setSubText(title)
                .setContentIntent(selectPendingIntent)
                .setLargeIcon(bitmap)
                .build();

        startForeground(NOTIFICATION_ID, notification);
        if (!isPlaying) {
            stopForeground(false);
        }
    }
}


