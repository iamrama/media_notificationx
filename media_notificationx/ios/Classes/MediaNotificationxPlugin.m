#import "MediaNotificationxPlugin.h"
#if __has_include(<media_notificationx/media_notificationx-Swift.h>)
#import <media_notificationx/media_notificationx-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "media_notificationx-Swift.h"
#endif

@implementation MediaNotificationxPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftMediaNotificationxPlugin registerWithRegistrar:registrar];
}
@end
