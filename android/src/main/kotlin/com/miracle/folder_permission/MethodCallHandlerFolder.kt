package com.miracle.folder_permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.UriPermission
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.storage.StorageManager
import android.provider.DocumentsContract
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.documentfile.provider.DocumentFile
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry


/** MethodCallHandlerFolder */
class MethodCallHandlerFolder(private val context: Context) :
        MethodChannel.MethodCallHandler,
        PluginRegistry.ActivityResultListener {
    private lateinit var channel: MethodChannel
    private var activity: Activity? = null
    var result: MethodChannel.Result? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    override  fun onMethodCall(call: MethodCall, resultm: MethodChannel.Result) {
        val callMethod = call.method
        val callArguments = call.arguments as Map<String, Any>
        result = resultm

        when (callMethod) {

            "getFolderPermission" ->{
                var path : String = callArguments["FolderPath"] as String
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    getFolderPermission(path)
                }
            }

            "getFolderData" ->{

                var path : String = callArguments["FolderPath"] as String
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    getFolderData(path)
                }
            }

            "checkAppInstalled" ->{

                var packageName : String = callArguments["packageName"] as String
                val installed = checkAppInstalled(context, packageName)
                result!!.success(installed)
            }

            else -> resultm.notImplemented()
        }

    }

    private fun getFolderPermission(path: String){
        Log.d("getFolderPermission","getFolderPermission called")
        val storageManager = activity?.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            storageManager.primaryStorageVolume.createOpenDocumentTreeIntent()
        } else {
            return
        }

        var wa_status_uri : Uri
        if(path == "com.whatsapp"){
           wa_status_uri = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fmedia/document/primary%3AAndroid%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses")
        }  else if(path == "com.whatsapp.w4b"){
           wa_status_uri = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fmedia/document/primary%3AAndroid%2Fmedia%2Fcom.whatsapp.w4b%2FWhatsApp Business%2FMedia%2F.Statuses")
        } else if(path == "com.gbwhatsapp") {
           wa_status_uri = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fmedia/document/primary%3AAndroid%2Fmedia%2Fcom.gbwhatsapp%2FGBWhatsApp%2FMedia%2F.Statuses")
        } else  {
           wa_status_uri = Uri.parse(path)
        }

        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI,wa_status_uri)
        intent.putExtra("android.content.extra.SHOW_ADVANCED", true)

        activity!!.startActivityForResult(intent,1244)

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun getFolderData(path: String){
        Log.d("getFolderData","getFolderData called")

        val permissions: List<UriPermission> = activity!!.contentResolver.getPersistedUriPermissions()

        Log.d("permissions",permissions.toString())
        var index: Int? = null

        for (i in permissions.indices) {
            if(permissions.get(i).uri.toString().contains(path)){
               index =i
            }
        }

        if(index != null){
            activity!!.contentResolver.takePersistableUriPermission(
                permissions.get(index).getUri(),
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            var  fileUris : ArrayList<String> = ArrayList()


            val fileDoc = DocumentFile.fromTreeUri(context,  permissions.get(index).getUri())
            for (file: DocumentFile in fileDoc!!.listFiles()) {
                if (!file.name!!.endsWith(".nomedia")) {
//                    Log.e("name", file.uri.toString())
                    fileUris.add(file.uri.toString())
                }
            }
            result!!.success(fileUris)
        } else {
            result!!.error("123","Permission denied","user had not permission to use these images")
        }

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {

        Log.d("resultCode", resultCode.toString())

        if (resultCode == Activity.RESULT_OK) {

            val treeUri = data?.data
            if(treeUri != null){
                activity!!.contentResolver.takePersistableUriPermission(treeUri,Intent.FLAG_GRANT_READ_URI_PERMISSION)

                data.data?.let {

                    activity!!.contentResolver.takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )

                }

            }

            result!!.success(resultCode)
        }
        return true
    }


    private fun checkAppInstalled(context: Context, pkgName: String?): Boolean {
        if (pkgName == null || pkgName.isEmpty()) {
           return false
        }
        var packageInfo: PackageInfo?
        try {
            try {
                packageInfo = context.packageManager.getPackageInfo(
                    pkgName,
                    PackageManager.GET_ACTIVITIES or PackageManager.GET_SERVICES
                )
                if (packageInfo == null) {
                    //donothing
                } else {
                    return true //true为安装了，false为未安装
                }
            } catch (e: Exception) {
                packageInfo = null
                e.printStackTrace()
            }
            val packageManager: PackageManager = context.packageManager
            val info: List<PackageInfo> = packageManager.getInstalledPackages(0)
            if (info.isEmpty()) return false
            for (i in info.indices) {
                val name: String = info[i].packageName
                Log.e("IsAppInstalledPlugin", name)
                if (pkgName == name) {
                    return true
                }
            }

        } catch (e: Exception) {
            packageInfo = null
            e.printStackTrace()
        }
        return false
    }

    fun init(messenger: BinaryMessenger) {
        channel = MethodChannel(messenger, "folder_permission")
        channel.setMethodCallHandler(this)
    }

    fun setActivity(activity: Activity?) {
        this.activity = activity
    }


    fun dispose() {
        if (::channel.isInitialized) {
            channel.setMethodCallHandler(null)
        }
    }

}