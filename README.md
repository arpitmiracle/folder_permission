# folder_permission

folder permission package is mainly developed to give specific permission in flutter.

Since Android 11 (API level 30), most user-installed apps are not visible by default. In your manifest, you must statically declare which apps you are going to get info about, as in the following:

```bash
    <queries>
        <!-- Explicit apps you know in advance about: -->
        <package android:name="com.whatsapp"/>
        <package android:name="com.whatsapp.w4b"/>
        <package android:name="com.gbwhatsapp"/>
    </queries>

```

## usage :
```bash
  final _folderPermission = FolderPermission();
```

this is for get permission of any folder for Android 11

```bash
  var data = await _folderPermission.getFolderPermission(arguments: {"FolderPath" : FolderPermission.WHATSAPP});
```

this is for get data from any folder and it returns the List of uri paths.

```bash
var data = await _folderPermission.getFolderData(arguments: {"FolderPath" : FolderPermission.WHATSAPP});
```

