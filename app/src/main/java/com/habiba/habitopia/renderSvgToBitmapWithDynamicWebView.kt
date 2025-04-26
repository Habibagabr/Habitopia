package com.habiba.habitopia.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient

fun renderSvgToBitmapWithDynamicWebView(
    context: Context,
    svgUrl: String,
    width: Int,
    height: Int,
    onBitmapReady: (Bitmap) -> Unit
) {
    val webView = WebView(context).apply {
        settings.javaScriptEnabled = true
        layoutParams = ViewGroup.LayoutParams(width, height)
        setBackgroundColor(Color.TRANSPARENT)
        visibility = View.INVISIBLE
    }

    val rootView = (context as Activity).window.decorView as ViewGroup
    rootView.addView(webView)

    webView.webViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            webView.postDelayed({
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                webView.draw(canvas)
                rootView.removeView(webView)
                onBitmapReady(bitmap)
            }, 800)
        }
    }

    webView.loadUrl(svgUrl)
}
