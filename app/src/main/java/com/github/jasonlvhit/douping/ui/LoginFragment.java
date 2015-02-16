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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.jasonlvhit.douping.R;

/**
 * Created by Jason Lyu on 2014/12/13.
 */
public class LoginFragment extends Fragment {
    private static final String LOG_TAG = LoginFragment.class.getSimpleName();
    private static Button mLoginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        mLoginButton = (Button) rootView.findViewById(R.id.login_button);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), com.github.jasonlvhit.douping.ui.LoginWebView.class);
                intent.putExtra("link",
                        "https://www.douban.com/service/auth2/auth?" +
                                "client_id=00b6bcb16f27d372022436144d777a56&" +
                                "redirect_uri=https://douping.sinaapp.com/android/oauth" +
                                "&response_type=code");
                startActivity(intent);
            }
        });
        return rootView;
    }

    public class LoginTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

}
