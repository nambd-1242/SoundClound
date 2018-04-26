package com.framgia.mysoundcloud.screen.search;

import com.framgia.mysoundcloud.screen.BasePresenter;
import com.framgia.mysoundcloud.screen.BaseView;

/**
 * Created by sonng266 on 13/03/2018.
 */

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
