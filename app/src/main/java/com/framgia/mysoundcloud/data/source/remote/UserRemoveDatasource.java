package com.framgia.mysoundcloud.data.source.remote;

import com.framgia.mysoundcloud.data.model.User;
import com.framgia.mysoundcloud.data.source.UserDataSource;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by buidanhnam on 4/28/2018.
 */

public class UserRemoveDatasource implements UserDataSource.UserRemoveDataSource {
    private static UserRemoveDatasource userRemoveDatasource;

    public static UserRemoveDatasource getInstance() {
        if (userRemoveDatasource == null) {
            userRemoveDatasource = new UserRemoveDatasource();
        }
        return userRemoveDatasource;
    }

    @Override
    public void login(GoogleSignInAccount user, UserDataSource.ResultCallBack<User> callBack) {
        if (user == null)
            callBack.onFailure("null");
        else {
            User userLocal = new User();
            userLocal.setId(user.getId());
            userLocal.setEmail(user.getEmail());
            userLocal.setName(user.getDisplayName());
            userLocal.setToken(user.getIdToken());
            if (user.getPhotoUrl() != null) {
                userLocal.setImage(user.getPhotoUrl().toString());
            }
            userLocal.setToken(user.getIdToken());
            callBack.onSuccess(userLocal);
        }
    }

    @Override
    public void login(User user, UserDataSource.ResultCallBack<User> callBack) {
        if (user != null)
            callBack.onSuccess(user);
    }
}
