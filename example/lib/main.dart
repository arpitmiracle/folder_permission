import 'dart:io';
import 'package:flutter/material.dart';
import 'package:folder_permission/folder_permission.dart';
import 'package:uri_to_file/uri_to_file.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final _folderPermission = FolderPermission();
  List<File> files= [];

  @override
  void initState() {
    getImages();
    super.initState();
  }

  getImages()async{
    try{
      var installed = await _folderPermission.checkAppInstalled(arguments: {"packageName" : FolderPermission.WHATSAPP});
      if(installed == true){
        var data = await _folderPermission.getFolderData(arguments: {"FolderPath" : FolderPermission.WHATSAPP});
        if(data != null){
          for(var i=0; i<data.length; i++){
            if(!data[i].toString().contains(".mp4")){
              files.add(await toFile(data[i].toString()));
            }

          }

          setState(() {});
        }
      }


    } catch(e){
      // if(e.toString().contains("Permission denied")){
      //
      //   var data = await _folderPermission.getFolderPermission(arguments: {"FolderPath" : FolderPermission.WHATSAPP});
      //   if(data == -1){
      //     getImages();
      //     setState(() {});
      //   }
      //
      // }
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [

              ElevatedButton(onPressed: () async {
                var data = await _folderPermission.getFolderPermission(arguments: {"FolderPath" : FolderPermission.WHATSAPP});

                if(data == -1){
                  getImages();
                }
              }, child: Text("Open Folder Permission")),

              ...List.generate(files.length, (index) => Container(margin: EdgeInsets.all(10),child: Image.file(files[index],)))

            ],
          ),
        ),
      ),
    );
  }

}
