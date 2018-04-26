package com.framgia.mysoundcloud.screen.splash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.framgia.mysoundcloud.utils.Navigator;
import com.framgia.mysoundcloud.screen.main.MainActivity;

public class SplashActivity extends AppCompatActivity implements SplashContract.View {

    private final int SPLASH_DISPLAY_LENGTH = 700;
    private SplashContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new SplashPresenter();
        mPresenter.setView(this);
        mPresenter.startingDelay(SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void showMainApp() {
        new Navigator(this).startActivity(MainActivity.class, false);
        finish();
    }
}
