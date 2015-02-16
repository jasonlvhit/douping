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

package com.github.jasonlvhit.douping.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.github.jasonlvhit.douping.R;
import com.github.jasonlvhit.douping.view.titanic.Titanic;
import com.github.jasonlvhit.douping.view.titanic.TitanicTextView;

/**
 * Created by Jason Lyu on 2015/2/15.
 */
public class LoadingFooter {
    protected View mLoadingFooter;

    //TextView mLoadingText;

    TitanicTextView mTitanicText;

    private Titanic mTitanic;

    protected State mState = State.Idle;

    public static enum State {
        Idle, TheEnd, Loading
    }

    public LoadingFooter(Context context) {
        mLoadingFooter = LayoutInflater.from(context).inflate(R.layout.loading_footer, null);
        mLoadingFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 屏蔽点击
            }
        });
        //mLoadingText = (TextView) mLoadingFooter.findViewById(R.id.textView);
        mTitanicText = (TitanicTextView) mLoadingFooter.findViewById(R.id.tv_titanic);
        mTitanic = new Titanic();
        mTitanic.start(mTitanicText);
        setState(State.Idle);
    }

    public View getView() {
        return mLoadingFooter;
    }

    public State getState() {
        return mState;
    }

    public void setState(final State state, long delay) {
        mLoadingFooter.postDelayed(new Runnable() {
            @Override
            public void run() {
                setState(state);
            }
        }, delay);
    }

    public void setState(State status) {
        if (mState == status) {
            return;
        }
        mState = status;

        mLoadingFooter.setVisibility(View.VISIBLE);
        switch (status) {
            case Loading:
                //mLoadingText.setVisibility(View.GONE);
                mTitanicText.setVisibility(View.VISIBLE);
                break;
            case TheEnd:
                //mLoadingText.setVisibility(View.VISIBLE);
                mTitanicText.setVisibility(View.GONE);
                break;
            default:
                mLoadingFooter.setVisibility(View.GONE);
                break;
        }
    }
}
