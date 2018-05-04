package com.framgia.mysoundcloud.data.source;

import com.framgia.mysoundcloud.data.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by buidanhnam on 4/28/2018.
 */

public interface UserDataSource {
    interface UserLocalDataSource {
        void getInforUser(String idUser, ResultCallBack<User> callBack);

        void addUser(User user, ResultCallBack<User> callBack);
    }

    interface UserRemoveDataSource {
        void login(GoogleSignInAccount user, UserDataSource.ResultCallBack<User> callBack);

        void login(User user, UserDataSource.ResultCallBack<User> callBack);
    }

    interface ResultCallBack<T> {
        void onSuccess(T t);

        void onFailure(String mes);
    }
}
