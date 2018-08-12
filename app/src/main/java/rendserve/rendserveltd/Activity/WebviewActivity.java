package rendserve.rendserveltd.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import rendserve.rendserveltd.R;
import utils.RendserveConstant;

public class WebviewActivity extends AppCompatActivity {

    private WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Intent intent ;
        intent = getIntent();
        String url = intent.getStringExtra("Url");

        webview = (WebView)findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview.setWebViewClient(new Callback());


        if (url.endsWith(".pdf")) {
            //String urlEncoded = URLEncoder.encode(RendserveConstant.PDF_URL+url, "UTF-8");
            String new_url = "http://docs.google.com/gview?embedded=true&url="+ RendserveConstant.PDF_URL+url;
            webview.loadUrl(new_url);

        }

    }
    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return(false);
        }
    }
}
