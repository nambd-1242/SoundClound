package com.framgia.mysoundcloud.data.source.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.framgia.mysoundcloud.data.model.User;
import com.google.gson.Gson;


public class SharePreferences {
    private static final String USER = "userCurrent";
    private static final String TYPE_LOGIN = "type";
    private static final String KEY = "soundclound";

    private SharedPreferences mSharedPreferences;

    private static SharePreferences instance;

    public static SharePreferences getInstance() {
        return instance;
    }

    public static void init(Context context) {
        instance = new SharePreferences(context);
    }

    public SharePreferences(Context context) {
        mSharedPreferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
    }

    public User getUser() {
        String user = mSharedPreferences.getString(USER, null);
        if (user == null)
            return null;
        return new Gson().fromJson(user, User.class);
    }

    public void putTypeLogin(@User.TYPE_LOGIN String type) {
        if (type == null) return;
        mSharedPreferences.edit().putString(TYPE_LOGIN, type).apply();
    }

    public String getType() {
        return mSharedPreferences.getString(TYPE_LOGIN, null);

    }

    public void putUser(User user) {
        if (user == null) return;
        String s = new Gson().toJson(user);
        mSharedPreferences.edit().putString(USER, s).apply();
    }

    public void removeUser() {
        mSharedPreferences.edit().clear().apply();
    }

}
