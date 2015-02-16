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

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.jasonlvhit.douping.R;
import com.github.jasonlvhit.douping.view.LoadingFooter;
import com.github.jasonlvhit.douping.view.OnLoadNextListener;
import com.github.jasonlvhit.douping.view.PageListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jason Lyu on 2015/2/16.
 */
public class SearchResultFragment extends Fragment {
    private final String LOG_TAG = SearchResultFragment.class.getSimpleName();
    private String mQuery = null;
    private List<Map<String, Object>> mTempList = new ArrayList<Map<String, Object>>();
    private ReviewListAdapter mListAdapter = new ReviewListAdapter();

    private PageListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (PageListView)rootView.findViewById(R.id.listview_reviews);
        mListView.setLoadNextListener(new OnLoadNextListener() {
            @Override
            public void onLoadNext() {
                ;
            }
        });
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ReviewActivity.class);
                intent.putExtra("link", (String)mTempList.get(i).get("link"));
                startActivity(intent);
            }
        });
        try {
            new ReviewDownloader().execute(getUrl());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mQuery = args.getString("query");
        setRetainInstance(true);

    }



    private String getUrl() throws UnsupportedEncodingException {
        final String encodedURL = URLEncoder.encode(mQuery, "UTF-8");
        return "http://douping.sinaapp.com/android/search?q="+encodedURL;
    }

    private class ReviewListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTempList.size();
        }

        @Override
        public Object getItem(int position) {
            return mTempList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ReviewListAdapter.ViewHolder holder = null;
            if (convertView == null) {
                holder = new ReviewListAdapter.ViewHolder();
                convertView = getActivity().getLayoutInflater().inflate(R.layout.listitem_review,
                        null);
                holder.title = (TextView) convertView
                        .findViewById(R.id.list_item_title);
                holder.description = (TextView) convertView
                        .findViewById(R.id.list_item_description);
                holder.useful = (TextView) convertView
                        .findViewById(R.id.list_item_useful);

                convertView.setTag(holder);
            } else {
                holder = (ReviewListAdapter.ViewHolder) convertView.getTag();
            }
            holder.title.setText((String) mTempList.get(position).get(
                    "title"));
            holder.description.setText((String) mTempList.get(position).get(
                    "description"));
            holder.useful.setText((String) mTempList.get(position).get(
                    "douban_useful"));
            return convertView;
        }

        private class ViewHolder {
            TextView title;
            TextView description;
            TextView useful;
        }

    }


    private class ReviewDownloader extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                Log.v(LOG_TAG, params[0]);
                InputStream inputStream = null;
                HttpURLConnection conn = (HttpURLConnection) new URL(params[0])
                        .openConnection();
                conn.setReadTimeout(20000);
                //conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
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
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("reviews");
                for (int i = 0; i < jsonArray.length(); i++) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("title",
                            jsonArray.getJSONObject(i).getString("title"));
                    map.put("description",
                            jsonArray.getJSONObject(i).getString("description"));
                    map.put("link", jsonArray.getJSONObject(i)
                            .getString("review_link"));
                    map.put("douban_useful", jsonArray.getJSONObject(i)
                            .getString("useful"));
                    mTempList.add(map);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {
            mListAdapter.notifyDataSetChanged();
            mListView.setState(LoadingFooter.State.TheEnd);
        }
    }
}

