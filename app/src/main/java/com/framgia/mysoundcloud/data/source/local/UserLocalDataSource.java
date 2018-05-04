package com.framgia.mysoundcloud.data.source.local;

import android.content.Context;

import com.framgia.mysoundcloud.data.model.User;
import com.framgia.mysoundcloud.data.source.UserDataSource;
import com.framgia.mysoundcloud.data.source.local.config.PlaylistTrackDbHelper;

/**
 * Created by buidanhnam on 4/28/2018.
 */

public class UserLocalDataSource implements UserDataSource.UserLocalDataSource {
    private static UserLocalDataSource localDataSource;

    private Context mContext;
    private PlaylistTrackDbHelper mPlaylistTrackDbHelper;

    public static void init(Context context) {
        if (localDataSource == null) {
            localDataSource = new UserLocalDataSource(context);
        }
    }

    public static UserLocalDataSource getInstance() {
        return localDataSource;
    }

    private UserLocalDataSource(Context context) {
        mContext = context;
        mPlaylistTrackDbHelper = PlaylistTrackDbHelper.getInstance(context);
    }

    @Override
    public void getInforUser(String idUser, UserDataSource.ResultCallBack<User> callBack) {
        mPlaylistTrackDbHelper.getUserbyId(idUser ,callBack);
    }

    @Override
    public void addUser(User user, UserDataSource.ResultCallBack<User> callBack) {
        if (user == null) {
            callBack.onFailure("null");
        } else {
            mPlaylistTrackDbHelper.addUser(user, callBack);
        }
    }
}
