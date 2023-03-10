package com.tcf.sma.Activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.tcf.sma.databinding.ActivityGeneralWebviewBinding;


import java.io.IOException;

public class GeneralWebviewActivity extends AppCompatActivity {

    ActivityGeneralWebviewBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGeneralWebviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        WebSettings settings = binding.webview.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);

        binding.webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//                Toast.makeText(GeneralWebviewActivity.this, "", Toast.LENGTH_SHORT).show();
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }

            @Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                super.onFormResubmission(view, dontResend, resend);
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }

            @Override
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
                super.onScaleChanged(view, oldScale, newScale);
            }
        });

        binding.webview.setWebChromeClient(new WebChromeClient(){

        });

        binding.webview.loadUrl("https://fast.com/");

//        new runScript().execute();





       /* try {
            Ion.with(getApplicationContext()).load("https://fast.com/#").followRedirect(true).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {
                    Log.d(TAG, "onCompleted: ");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        
    }

}