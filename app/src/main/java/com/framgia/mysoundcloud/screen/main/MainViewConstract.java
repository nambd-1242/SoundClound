package com.framgia.mysoundcloud.screen.main;

import android.os.Parcelable;

import com.framgia.mysoundcloud.data.model.Track;
import com.framgia.mysoundcloud.screen.BasePresenter;

import java.util.List;

/**
 * Created by sonng266 on 26/02/2018.
 */

public interface MainViewConstract {
    /**
     * View.
     */
    interface View {
        void updateTitle(String title);

        void showTabLayout();

        void hideTabLayout();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter<MainViewConstract.View> {
    }

    /**
     * TrackListListener
     */
    interface TrackListListener extends Parcelable {
        void onPlayedTrack(int position, List<Track> tracks);

        void onAddedToNextUp(Track track);

        void onAddToPlaylist(Track track);
    }
}
