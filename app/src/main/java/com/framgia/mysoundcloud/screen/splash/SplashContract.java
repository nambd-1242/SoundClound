package com.framgia.mysoundcloud.screen.splash;

import com.framgia.mysoundcloud.data.model.User;
import com.framgia.mysoundcloud.screen.BasePresenter;



interface SplashContract {
    /**
     * View.
     */
    interface View {
        void showMainApp(User user);

        void loginActivity();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter<SplashContract.View> {
        void startingDelay(long millisecond);
    }

}
