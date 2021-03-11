package com.houlik.libhoulik.android.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * WebView 结合 JSoup
 * 例子 :
 * 解析script = document.select("script");
 * public final class HTMLResult{
 *         @JavascriptInterface
 *         public void getResult(String html){
 *             Document document = Jsoup.parse(html);
 *             //这是解析script
 *             Elements elements = document.select("script");
 *             for (Element e:elements
 *                  ) {
 *                 // e = script 完整数据
 *                 // e.data() = 移除 script, 剩下 json 的数据
 *             }
 *         }
 *     }
 *
 * @author houlik
 * @since 2020/5/31
 */
public class WebViewUtils {

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    public WebViewUtils(Activity activity){
        webView = new WebView(activity);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);//硬件加速
        webView.clearCache(true);
        webView.clearHistory();
        WebSettings webSettings = webView.getSettings();
        //设置是否开启DOM存储API权限，默认false，未开启，设置为true，WebView能够使用DOM storage API
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);

    }

    @SuppressLint("SetJavaScriptEnabled")
    public WebViewUtils(WebView webView){
        this.webView = webView;
        this.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        webView.clearCache(true);
        webView.clearHistory();
        WebSettings webSettings = this.webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);

    }

    @SuppressLint({"JavascriptInterface"})
    public void connectURL(final String url, HTMLResult htmlResult){
        webView.loadUrl(url);
        webView.addJavascriptInterface(htmlResult, "method");
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.setWebViewClient(null);
                webView.loadUrl("javascript:window.method.getResult(document.getElementsByTagName('html')[0].innerHTML);");
            }
        });
    }

    //实现类也必须添加 @JavascriptInterface
    public interface HTMLResult{
        @JavascriptInterface
        void getResult(String html);
    }

}