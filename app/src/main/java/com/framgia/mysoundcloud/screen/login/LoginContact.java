package com.framgia.mysoundcloud.screen.login;

import com.framgia.mysoundcloud.data.model.User;
import com.framgia.mysoundcloud.screen.BasePresenter;
import com.framgia.mysoundcloud.screen.BaseView;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

/**
 * Created by buidanhnam on 4/28/2018.
 */

public interface LoginContact {
    interface LoginView {
        void onSuccess(User user);

        void showDialogLogin();

        void onFailure();

    }

    interface LoginPresenter extends BasePresenter<LoginView> {
        void login();

        void handleSignInResult(Task<GoogleSignInAccount> completedTask);

    }
}
