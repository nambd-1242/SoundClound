package com.framgia.mysoundcloud.screen.splash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.framgia.mysoundcloud.data.model.User;
import com.framgia.mysoundcloud.screen.login.LoginActivity;
import com.framgia.mysoundcloud.screen.main.MainActivity;
import com.framgia.mysoundcloud.utils.Constant;
import com.framgia.mysoundcloud.utils.Navigator;

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
    public void showMainApp(User user) {
        if (user == null) return;
        new Navigator(this).startActivity(MainActivity.class, false);
        finish();
    }

    @Override
    public void loginActivity() {
        new Navigator(this).startActivity(LoginActivity.class, false);
        finish();
    }
}
