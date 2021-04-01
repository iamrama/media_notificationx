package com.rk.medianotify.media_notificationx;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.rk.medianotify.media_notificationx.src.NotificationServiceManager;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** MediaNotificationxPlugin */
public class MediaNotificationxPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private static MethodChannel channel;
  private static Context mContext;


  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    mContext = flutterPluginBinding.getApplicationContext();
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "media_notificationx");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {

    switch (call.method) {
      case "showNotificationManager":
        final String title = call.argument("title");
        final String author = call.argument("author");
        final String imageUrl = call.argument("imageUrl");
        final boolean isPlaying = call.argument("isPlaying");
        showNotificationManager(title, author,imageUrl, isPlaying);
        result.success(null);
        break;
      default:
        result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  public static void callEventHandler(String event) {

    MediaNotificationxPlugin.channel.invokeMethod(event, null, new Result() {
      @Override
      public void success(Object o) {
        // this will be called with o = "some string"
      }

      @Override
      public void error(String s, String s1, Object o) {}

      @Override
      public void notImplemented() {}
    });
  }
  
  public static void showNotificationManager(String title, String author,String imageUrl, boolean play) {

    Intent serviceIntent = new Intent(mContext, NotificationServiceManager.class);
    serviceIntent.putExtra("title", title);
    serviceIntent.putExtra("author", author);
    serviceIntent.putExtra("imageUrl",imageUrl);
    serviceIntent.putExtra("isPlaying", play);

    mContext.startService(serviceIntent);
  }

}
