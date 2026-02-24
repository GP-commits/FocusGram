package com.example.instafocus

import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // HARD DAILY GATE
        if (UsageTracker.isLimitReached(this)) {
            finish()
            return
        }

        webView = WebView(this)

        webView.settings.apply {
            javaScriptEnabled = true
            setSupportMultipleWindows(false)
            javaScriptCanOpenWindowsAutomatically = false
        }

        webView.webViewClient = WebViewClient()

        // Disable downloads
        webView.setDownloadListener { _, _, _, _, _ -> }

        webView.loadUrl("https://www.instagram.com")

        setContentView(webView)
    }

    override fun onResume() {
        super.onResume()
        UsageTracker.startSession()
    }

    override fun onPause() {
        super.onPause()
        UsageTracker.endSession(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        val cm = CookieManager.getInstance()
        cm.removeAllCookies(null)
        cm.flush()
    }
}
