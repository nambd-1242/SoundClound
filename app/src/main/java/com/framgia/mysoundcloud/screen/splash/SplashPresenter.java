package com.framgia.mysoundcloud.screen.splash;

import android.os.Handler;

import com.framgia.mysoundcloud.data.model.User;
import com.framgia.mysoundcloud.data.source.local.SharePreferences;



public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View mView;

    @Override
    public void setView(SplashContract.View view) {
        this.mView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void startingDelay(long millisecond) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                User user = SharePreferences.getInstance().getUser();
                if (user == null) {
                    mView.loginActivity();
                } else {
                    mView.showMainApp(user);
                }

            }
        }, millisecond);
    }
}
