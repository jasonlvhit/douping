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

/**
 * Created by Jason Lyu on 2014/12/19.
 */
public class User {

    public static final String TABLENAME = "user";

    public int _id;
    public int user_id;
    public String username;
    public String avatar;

    public User() {
    }

    public User(int user_id, String username, String avatar) {
        this.user_id = user_id;
        this.username = username;
        this.avatar = avatar;
    }
}
