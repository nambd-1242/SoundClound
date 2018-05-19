package com.framgia.mysoundcloud.screen.playlistdetail;

import com.framgia.mysoundcloud.data.model.Track;
import com.framgia.mysoundcloud.screen.BasePresenter;
import com.framgia.mysoundcloud.screen.BaseView;

/**
 * Created by sonng266 on 09/03/2018.
 */

public interface PlayListDetailViewContract {
    /**
     * View.
     */
    interface View extends BaseView {
        void showMessage(String message);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter<PlayListDetailViewContract.View> {
        void loadTrack(String flag);

        void deleteTrackFavorie(Track track);
    }

    interface DeleteTrackListener {
        void onDeleteClicked(Track track);
    }
}
