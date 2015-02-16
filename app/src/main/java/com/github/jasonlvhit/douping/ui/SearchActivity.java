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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.github.jasonlvhit.douping.R;

/**
 * Created by Jason Lyu on 2015/2/15.
 */
public class SearchActivity extends FragmentActivity
        implements TextView.OnEditorActionListener{

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
        if (event == null ||(event.getAction() == KeyEvent.ACTION_DOWN) &&
                (i == KeyEvent.KEYCODE_ENTER)) {
            // Perform action on key press
            String queryText = String.valueOf(textView.getText());
            //Toast.makeText(SearchActivity.this, queryText, Toast.LENGTH_SHORT).show();
            InputMethodManager imm=
                    (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

            imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
            Fragment fragment = new SearchResultFragment();
            Bundle args = new Bundle();
            args.putString("query" , queryText);
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();

            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        EditText editText=
                (EditText)menu.findItem(R.id.add).getActionView()
                        .findViewById(R.id.edit_search);

        editText.setOnEditorActionListener(this);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }
}
