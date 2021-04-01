import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:media_notificationx/media_notificationx.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

String status = 'hidden';

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
    //  initPlatformState();

    MediaNotificationx.setListener('pause', () {
      setState(() => status = 'pause');
    });

    MediaNotificationx.setListener('play', () {
      setState(() => status = 'play');
    });

    MediaNotificationx.setListener('next', () {});

    MediaNotificationx.setListener('previous', () {});

    MediaNotificationx.setListener('select', () {});
  }


  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: new AppBar(
          title: const Text('Media Notification with image'),
        ),
        body: new Center(
            child: Container(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: <Widget>[
              RaisedButton(
                  child: Text('Show Notification with image'),
                  onPressed: () {
                    MediaNotificationx.showNotificationManager(
                        title: 'Title',
                        author: 'Song author',
                        imageUrl:
                            'https://s3.amazonaws.com/images.seroundtable.com/google-rainbow-texture-1491566442.jpg');
                    setState(() => status = 'play');
                  }),
            ],
          ),
        )),
      ),
    );
    ;
  }
}
