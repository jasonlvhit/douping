package com.github.jasonlvhit.douping.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.jasonlvhit.douping.R;
import com.github.jasonlvhit.douping.data.DBManager;
import com.github.jasonlvhit.douping.data.User;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2014/12/19.
 */
public class LoginWebView extends Activity {
    private static final String LOG_TAG = LoginWebView.class.getSimpleName();
    private WebView webView;
    private static DBManager dbManager;

    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        dbManager = new DBManager(getBaseContext());
        Intent intent = getIntent();

        String link = intent.getStringExtra("link");
        setContentView(R.layout.activity_review);
        webView = (WebView) findViewById(R.id.webview);
        progressBar = ProgressDialog.show(this, "用豆瓣账号登录", "Loading...");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String purl) {
                (new FetchOAuthInfo()).execute(purl);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.i(LOG_TAG, "Finished loading URL: " +url);
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }
        });
        webView.loadUrl(link);
    }




    class FetchOAuthInfo extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... purl) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String content = null;
            ArrayList<HashMap<String, Object>> hashMaps = new ArrayList<HashMap<String, Object>>();
            try {
                URL url = new URL(purl[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                content = IOUtils.toString(inputStream, "utf-8");
                Log.v(LOG_TAG, content);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content;
        }

        @Override
        protected void onPostExecute(String content) {
            super.onPostExecute(content);
            if(content != null){
                Log.v(LOG_TAG, content);
            }
            else{
                onDestroy();
            }

            if (content.equals("error")) {
                onDestroy();
            } else {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(content);
                    User user = new User(jsonObject.getInt("user_id"), jsonObject.getString("username"),
                            jsonObject.getString("avatar"));
                    dbManager.add(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplication().startActivity(intent);
            }

        }
    }

}
