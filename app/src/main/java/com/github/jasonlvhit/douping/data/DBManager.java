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

package com.github.jasonlvhit.douping.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Jason Lyu on 2014/12/19.
 */
public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add user
     *
     * @param user
     */
    public void add(User user) {
        db.beginTransaction();    //开始事务
        try {
            db.execSQL("INSERT INTO user VALUES(null, ?, ?, ?)", new Object[]{user.user_id, user.username,
                    user.avatar});
            db.setTransactionSuccessful();    //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    /**
     * delete old user
     *
     */
    public void delete() {
        db.delete("user", null, null);
    }

    /**
     * query all persons, return User instance
     *
     * @return user
     */
    public User query() {
        Cursor c = queryTheCursor();
        if (!c.moveToFirst()){
            return null;
        }
        User user = new User();
        user._id = c.getInt(c.getColumnIndex("_id"));
        user.user_id = c.getInt(c.getColumnIndex("user_id"));
        user.username = c.getString(c.getColumnIndex("username"));
        user.avatar = c.getString(c.getColumnIndex("avatar"));
        c.close();
        return user;
    }

    /**
     * query all user, return cursor
     *
     * @return Cursor
     */
    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM user", null);
        return c;
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }
}
