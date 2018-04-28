package com.framgia.mysoundcloud.data.source.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.framgia.mysoundcloud.data.model.User;
import com.google.gson.Gson;

/**
 * Created by Sony on 1/12/2018.
 */

public class SharePreferences {
    private static final String USER = "userCurrent";
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
        if(user == null)
            return null;
        return new Gson().fromJson(user, User.class);
    }

    public void putUser(User user) {
        if (user == null) return;
        String s = new Gson().toJson(user);
        mSharedPreferences.edit().putString(USER, s).apply();
    }
}
