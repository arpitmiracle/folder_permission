package com.miracle.folder_permission

import android.app.Activity
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding

/** FolderPermissionPlugin */
class FolderPermissionPlugin: FlutterPlugin, ActivityAware {

    private var activityBinding: ActivityPluginBinding? = null
    private lateinit var methodCallHandler: MethodCallHandlerFolder
    private lateinit var activity: Activity



    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {

        methodCallHandler = MethodCallHandlerFolder(binding.applicationContext)
        methodCallHandler.init(binding.binaryMessenger)

    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        if (::methodCallHandler.isInitialized) {
            methodCallHandler.dispose()
        }
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {

        activity = binding.activity
        methodCallHandler.setActivity(binding.activity)
        binding.addActivityResultListener(methodCallHandler)
        activityBinding = binding
    }

    override fun onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity()
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        onAttachedToActivity(binding)
    }

    override fun onDetachedFromActivity() {
        activityBinding?.removeActivityResultListener(methodCallHandler)
        activityBinding = null
        methodCallHandler.setActivity(null)
    }

}
