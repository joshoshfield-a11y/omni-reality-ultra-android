package com.omnireality.ultra

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.view.WindowManager
import android.webkit.JavascriptInterface
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class MainActivity : ComponentActivity() {

    private lateinit var web: WebView
    private var pendingPermissionRequest: PermissionRequest? = null

    // Runtime CAMERA/MIC permissions -> then answer the WebView's PermissionRequest
    private val mediaPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grants ->
            pendingPermissionRequest?.let { req ->
                val wantsAudio = req.resources.any { it == PermissionRequest.RESOURCE_AUDIO_CAPTURE }
                val wantsVideo = req.resources.any { it == PermissionRequest.RESOURCE_VIDEO_CAPTURE }
                val camOk = checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                val micOk = checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                val allowed = req.resources.filter {
                    (it == PermissionRequest.RESOURCE_VIDEO_CAPTURE && camOk) ||
                    (it == PermissionRequest.RESOURCE_AUDIO_CAPTURE && micOk)
                }.toTypedArray()
                if (allowed.isNotEmpty() &&
                    (!wantsVideo || camOk) && (!wantsAudio || micOk)
                ) req.grant(allowed) else req.deny()
                pendingPermissionRequest = null
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Edge-to-edge immersive (Galaxy S25 full-bleed display)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        web = WebView(this)
        web.setBackgroundColor(Color.BLACK)
        web.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = true
            mediaPlaybackRequiresUserGesture = false
        }
        web.addJavascriptInterface(OmniBridge(), "Omni")
        web.webViewClient = WebViewClient()
        web.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest) {
                val wantsCamera = request.resources.any {
                    it == PermissionRequest.RESOURCE_VIDEO_CAPTURE
                }
                val wantsMic = request.resources.any {
                    it == PermissionRequest.RESOURCE_AUDIO_CAPTURE
                }
                val needed = mutableListOf<String>()
                if (wantsCamera && checkSelfPermission(Manifest.permission.CAMERA) !=
                    PackageManager.PERMISSION_GRANTED
                ) needed.add(Manifest.permission.CAMERA)
                if (wantsMic && checkSelfPermission(Manifest.permission.RECORD_AUDIO) !=
                    PackageManager.PERMISSION_GRANTED
                ) needed.add(Manifest.permission.RECORD_AUDIO)
                if (needed.isEmpty()) {
                    request.grant(request.resources)
                } else if (wantsCamera || wantsMic) {
                    pendingPermissionRequest = request
                    mediaPermissions.launch(needed.toTypedArray())
                } else {
                    request.deny()
                }
            }
        }
        web.loadUrl("file:///android_asset/www/index.html")
        setContentView(web)
    }

    /** JS bridge: window.Omni.saveVideo/saveImage -> MediaStore (gallery-visible) */
    inner class OmniBridge {
        @JavascriptInterface
        fun saveVideo(base64Data: String, filename: String, mime: String): Boolean =
            saveToStore(base64Data, filename, mime, Environment.DIRECTORY_MOVIES)

        @JavascriptInterface
        fun saveImage(base64Data: String, filename: String): Boolean =
            saveToStore(base64Data, filename, "image/png", Environment.DIRECTORY_PICTURES)
    }

    private fun saveToStore(
        base64Data: String, filename: String, mime: String, dir: String
    ): Boolean {
        return try {
            val bytes = Base64.decode(base64Data, Base64.DEFAULT)
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, mime)
                put(MediaStore.MediaColumns.RELATIVE_PATH, "$dir/OmniReality")
            }
            val collection = if (dir == Environment.DIRECTORY_MOVIES)
                MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            else
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val uri = contentResolver.insert(collection, values) ?: return false
            contentResolver.openOutputStream(uri)?.use { it.write(bytes) } ?: return false
            runOnUiThread {
                Toast.makeText(this, "Saved: $filename", Toast.LENGTH_SHORT).show()
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (::web.isInitialized && web.canGoBack()) web.goBack() else super.onBackPressed()
    }
}
