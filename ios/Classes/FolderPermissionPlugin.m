#import "FolderPermissionPlugin.h"
#if __has_include(<folder_permission/folder_permission-Swift.h>)
#import <folder_permission/folder_permission-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "folder_permission-Swift.h"
#endif

@implementation FolderPermissionPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFolderPermissionPlugin registerWithRegistrar:registrar];
}
@end
