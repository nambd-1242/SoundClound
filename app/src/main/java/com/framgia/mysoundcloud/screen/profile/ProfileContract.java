package com.framgia.mysoundcloud.screen.profile;

import com.framgia.mysoundcloud.data.model.User;
import com.framgia.mysoundcloud.screen.BasePresenter;



public interface ProfileContract {
    /**
     * View.
     */
    interface View {
        void initData(User user);

        void logoutFacebook();

        void logoutGoogle();

        void logOutSuccess();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter<ProfileContract.View> {
        void loadData();

        void doLogout();
    }
}
