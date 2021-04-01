package com.rk.medianotify.media_notificationx.src;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.rk.medianotify.media_notificationx.MediaNotificationxPlugin;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case "previous":
                MediaNotificationxPlugin.callEventHandler("previous");
                break;
            case "next":
                MediaNotificationxPlugin.callEventHandler("next");
                break;
            case "playItem":
                String title = intent.getStringExtra("title");
                String author = intent.getStringExtra("author");
                String imageUrl = intent.getStringExtra("imageUrl");
                boolean play = intent.getBooleanExtra("play",true);

                if(play)
                    MediaNotificationxPlugin.callEventHandler("play");
                else
                    MediaNotificationxPlugin.callEventHandler("pause");

                MediaNotificationxPlugin.showNotificationManager(title, author,imageUrl,play);
                break;
            case "select":
                Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                context.sendBroadcast(closeDialog);
                String packageName = context.getPackageName();
                PackageManager pm = context.getPackageManager();
                Intent launchIntent = pm.getLaunchIntentForPackage(packageName);
                context.startActivity(launchIntent);

                MediaNotificationxPlugin.callEventHandler("select");
        }
    }
}