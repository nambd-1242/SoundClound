package com.framgia.mysoundcloud.screen.search;

import com.framgia.mysoundcloud.screen.BasePresenter;
import com.framgia.mysoundcloud.screen.BaseView;



public interface SearchViewContract {
    /**
     * View
     */
    interface View extends BaseView {
    }

    /**
     * Presenter
     */
    interface Presenter extends BasePresenter<SearchViewContract.View> {
        void searchTrack(String trackName, int offSet);
    }

}
