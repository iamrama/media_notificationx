import 'dart:async';

import 'package:flutter/services.dart';

//** To manage the player is played or not the class to notify the player is playing the song [MediaNotificationManager]
class MediaNotificationx {
  static const MethodChannel _channel =
      const MethodChannel('media_notificationx');
  static Map<String, Function> _listenersMap = new Map();

  // [_utilsHandlerManager] called by [setListener] to add all events
  // to platform side for an action.
  static Future<dynamic> _utilsHandlerManager(MethodCall methodCall) async {
    _listenersMap.forEach((event, callback) {
      if (methodCall.method == event) {
        callback();
      }
    });
  }

  /// To show your media notification you have to pass [title] and
  /// [author]  and [Image] of music. If music is pausing you have to set
  /// [isPlaying] false.
  static Future showNotificationManager(
      {title = '', author = '', imageUrl = '', isPlaying = true}) async {
    try {
      final Map<String, dynamic> params = <String, dynamic>{
        'title': title,
        'author': author,
        'imageUrl': imageUrl,
        'isPlaying': isPlaying
      };
      await _channel.invokeMethod('showNotificationManager', params);
      _channel.setMethodCallHandler(_utilsHandlerManager);
    } on PlatformException catch (e) {
      print("Failed to show notification: '${e.message}'.");
    }
  }

  /// Set listener for all action we need to listen of notification.
  static setListener(String event, Function callback) {
    _listenersMap.addAll({event: callback});
  }
}
