# folder_permission

folder permission package is mainly developed to give specific permission in flutter.

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
