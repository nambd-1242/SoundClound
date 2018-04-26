package com.framgia.mysoundcloud.screen.splash;

import android.os.Handler;

/**
 * Created by sonng266 on 26/02/2018.
 */

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
                mView.showMainApp();
            }
        }, millisecond);
    }
}
