package com.framgia.mysoundcloud.screen.splash;

import com.framgia.mysoundcloud.screen.BasePresenter;

/**
 * Created by sonng266 on 26/02/2018.
 */

interface SplashContract {
    /**
     * View.
     */
    interface View {
        void showMainApp();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter<SplashContract.View> {
        void startingDelay(long millisecond);
    }

}
