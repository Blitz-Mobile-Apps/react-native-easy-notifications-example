#import <UIKit/UIKit.h>
#import <React/RCTEventEmitter.h>
#import "React/RCTBridgeModule.h"
@interface NotificationHandler: RCTEventEmitter <UIApplicationDelegate, RCTBridgeModule>{
  NSDictionary *remoteNotification;
  
}
@end

