import 'package:flutter/services.dart';

class FolderPermission {
  final methodChannel = const MethodChannel('folder_permission');

  static String BUSINESSWHATSAPP = "com.whatsapp.w4b";
  static String WHATSAPP = "com.whatsapp";
  static String GBWHATSAPP = "com.gbwhatsapp";

  Future<int?> getFolderPermission({required arguments}) async {
    final permission = await methodChannel.invokeMethod<int?>('getFolderPermission',arguments);
    return permission;
  }

  Future<List?> getFolderData({required arguments}) async {
    final data = await methodChannel.invokeMethod<List?>('getFolderData',arguments);
    return data;
  }

  Future<bool?> checkAppInstalled({required arguments}) async {
    final installed = await methodChannel.invokeMethod<bool?>('checkAppInstalled',arguments);
    return installed;
  }

}
