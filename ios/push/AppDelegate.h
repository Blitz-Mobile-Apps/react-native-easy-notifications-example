/**
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

#import <React/RCTBridgeDelegate.h>
#import <UIKit/UIKit.h>
#import "NotificationHandler.h"

@interface AppDelegate  : UIResponder <UIApplicationDelegate, RCTBridgeDelegate>
{
  NotificationHandler *nHandler;
  NSDictionary *dic;
}
@property (nonatomic, strong) UIWindow *window;

@end
