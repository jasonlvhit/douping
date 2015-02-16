/*
 * Copyright (C) 2015 jasonlvhit.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.jasonlvhit.douping.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.jasonlvhit.douping.R;
import com.github.jasonlvhit.douping.data.DBManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Jason Lyu on 2014/12/13.
 */
public class ReviewActivity extends Activity {
    private static final String LOG_TAG = ReviewActivity.class.getSimpleName();
    private static WebView webView;
    private String result;
    private ProgressBar Pbar;
    private String mLink;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.collect, menu);
        return true;
    }

    private String getCollectUrl() throws UnsupportedEncodingException {
        final String encodedURL = URLEncoder.encode(mLink, "UTF-8");
        return "http://douping.sinaapp.com/android/user/" +
                String.valueOf(new DBManager(getApplicationContext()).query().user_id) +
                "/collect?link="+mLink;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_collect) {
            (new CollectTask()).execute("http://douping.sinaapp.com/android/user/1/collect?link=http://movie.douban.com/review/5663444");
            Toast.makeText(getApplicationContext(), "收藏成功", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getActionBar().setTitle(getString(R.string.review_title));
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //android.R.drawable.ic_menu_close_clear_cancel);
        Intent intent = getIntent();
        mLink = intent.getStringExtra("link");
        setContentView(R.layout.activity_review);
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        Pbar = (ProgressBar)findViewById(R.id.pB1);
        webView.getSettings().setDefaultFontSize(18);
        webView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100 && Pbar.getVisibility() == ProgressBar.GONE) {
                    Pbar.setVisibility(ProgressBar.VISIBLE);

                }
                Pbar.setProgress(progress);
                if (progress == 100) {
                    Pbar.setVisibility(ProgressBar.GONE);

                }
            }
        });
        //webView.loadUrl(link);
        
        FetchReview fetchReview = new FetchReview();
        fetchReview.execute(mLink);

    }

    public class CollectTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            try {
                Log.v(LOG_TAG, params[0]);
                InputStream inputStream;
                HttpURLConnection conn = (HttpURLConnection) new URL(params[0])
                        .openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                //conn.setDoInput(true);
                conn.setDoOutput(false);
                conn.connect();
                inputStream = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        inputStream, "UTF-8"));
                StringBuffer outBuffer = new StringBuffer();
                String string;
                while ((string = br.readLine()) != null)
                    outBuffer.append(string);
                String content = outBuffer.toString();
                inputStream.close();
                Log.v(LOG_TAG, content);

                return "Succesful";

            } catch (IOException e) {
                e.printStackTrace();
                return "Unable to retrieve web page.URL may be invalid.";

            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.v(LOG_TAG, result);
        }
    }

    public class FetchReview extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            Document documents = null;
            try {
                documents = Jsoup.connect(strings[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements content = documents.select("#link-report");
            Elements title = documents.select("h1");
            result = "<h2>" + title.html() + "</h2>";
            result += content.html();
            return result;
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            webView.loadData(result, "text/html; charset=UTF-8", null);
        }
    }

}
