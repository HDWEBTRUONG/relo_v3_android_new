package main.ui.webview;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by quynguyen on 3/27/17.
 */

public class CustomWebViewClient extends WebViewClient {
    /* (non-Java doc)
     * @see android.webkit.WebViewClient#shouldOverrideUrlLoading(android.webkit.WebView, java.lang.String)
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return super.shouldOverrideUrlLoading(view, url);
    }
}
