package com.framgia.mysoundcloud.screen.download;

import com.framgia.mysoundcloud.data.model.Track;
import com.framgia.mysoundcloud.screen.BasePresenter;
import com.framgia.mysoundcloud.screen.BaseView;

/**
 * Created by sonng266 on 09/03/2018.
 */

public interface DownloadViewContract {
    /**
     * View.
     */
    interface View extends BaseView {
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter<DownloadViewContract.View> {
        void loadTrack();
    }

    interface DeleteTrackListener {
        void onDeleteClicked(Track track);
    }
}
