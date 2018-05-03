package com.framgia.mysoundcloud.screen.login;

import com.framgia.mysoundcloud.data.model.User;
import com.framgia.mysoundcloud.data.repository.UserRepository;
import com.framgia.mysoundcloud.data.source.UserDataSource;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

/**
 * Created by buidanhnam on 4/28/2018.
 */

public class LoginPresenter implements LoginContact.LoginPresenter {
    private LoginContact.LoginView loginView;
    private UserRepository userRepository;
    public LoginPresenter(LoginContact.LoginView loginView) {
        this.loginView = loginView;
        userRepository = UserRepository.getInstance();
    }

    @Override
    public void setView(LoginContact.LoginView view) {
          this.loginView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void login() {
         loginView.showDialogLogin();
    }

    @Override
    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            userRepository.login(account, new UserDataSource.ResultCallBack<User>() {
                @Override
                public void onSuccess(User user) {
                   loginView.onSuccess(user);
                }

                @Override
                public void onFailure(String mes) {
                   loginView.onFailure();
                }
            });
        } catch (ApiException e) {
            e.getStatusCode();
        } }
}
