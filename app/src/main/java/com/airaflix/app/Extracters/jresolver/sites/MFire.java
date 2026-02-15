package com.airaflix.app.Extracters.jresolver.sites;


import static com.airaflix.app.Extracters.jresolver.utils.Utils.putModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.airaflix.app.Extracters.jresolver.Jresolver;
import com.airaflix.app.Extracters.jresolver.model.Jmodel;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MFire {

    @SuppressLint("StaticFieldLeak")
    private static WebView webView;
    private static boolean stopMMHandler = false;

    @SuppressLint("SetJavaScriptEnabled")
    public static void fetch(Context context, String url, final Jresolver.OnTaskCompleted onTaskCompleted) {
        webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().getAllowContentAccess();
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.addJavascriptInterface(new MyInterface(), "xGetter");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.evaluateJavascript(
                        "(function() { return ('<html>'+document.getElementsByTagName('head')[0].innerHTML+'</html>'); })();",
                        html -> {

                            html = html.replaceAll("\\s", "");

                            final String regex = "window\\.location\\.href.*?=.*?'(.*?)';";
                            final Pattern pattern = Pattern.compile(regex);
                            final Matcher matcher = pattern.matcher(html);
                            if (matcher.find()) {
                                ArrayList<Jmodel> jModels = new ArrayList<>();
                                putModel(matcher.group(1),"Normal",jModels);
                                onTaskCompleted.onTaskCompleted(jModels,false);
                            }else {
                                onTaskCompleted.onError();
                            }
                        });
            }
        });

        webView.loadUrl(url);
    }

    private static class MyInterface {
        @SuppressWarnings("unused")
        @JavascriptInterface
        public void error(final String error) {
            new Handler(Looper.getMainLooper()).post(() -> {
                destroyWebView();
                stopMMHandler = true;
            });
        }
    }

    private static void destroyWebView() {
        if (webView!=null) {
            webView.loadUrl("about:blank");
        }
    }

}
