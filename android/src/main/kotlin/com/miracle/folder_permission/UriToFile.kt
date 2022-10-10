package com.miracle.folder_permission

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.Exception

class UriToFile(private val context: Context) {

    public fun fromUri( uriString: String?) : String? {
        val uri = Uri.parse(uriString)
        if (uri != null) {
            val scheme = uri.scheme
            if (scheme != null && scheme == ContentResolver.SCHEME_CONTENT) {
                val fileName = getFileName(uri)
                if (fileName != null && !fileName.isEmpty()) {
                    val name = fileName.substring(0, fileName.lastIndexOf('.'))
                    val ext = fileName.substring(fileName.lastIndexOf('.'))
                    if (!name.isEmpty()) {
                     return copyFile( uri, name, ext)
                    }
                }
            }
        }
        return null
    }

    private fun getFileName(uri: Uri): String? {
        var filename: String? = null
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (index != -1) {
                filename = cursor.getString(index)
            }
        }
        if (filename == null) {
            filename = uri.lastPathSegment
        }
        cursor?.close()
        return filename
    }

    private fun copyFile(uri: Uri, name: String, ext: String) : String? {
        try {
            val assetFileDescriptor = context.contentResolver.openAssetFileDescriptor(
                uri, "r"
            )
            val inputChannel = FileInputStream(
                assetFileDescriptor!!.fileDescriptor
            ).channel
            val parent = File(context.filesDir.toString() + File.separator + "uri_to_file")
            parent.mkdirs()
            val file =
                File(context.filesDir.toString() + File.separator + "uri_to_file" + File.separator + name + ext)
            file.deleteOnExit()
            val outputChannel = FileOutputStream(file).channel
            var bytesTransferred: Long = 0
            while (bytesTransferred < inputChannel.size()) {
                bytesTransferred += outputChannel.transferFrom(
                    inputChannel,
                    bytesTransferred,
                    inputChannel.size()
                )
            }
            val filepath = file.canonicalPath
            if (filepath != null && !filepath.isEmpty()) {
                return filepath
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return  null
    }
}