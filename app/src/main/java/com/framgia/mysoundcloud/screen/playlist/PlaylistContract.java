package com.framgia.mysoundcloud.screen.playlist;

import com.framgia.mysoundcloud.data.model.Playlist;
import com.framgia.mysoundcloud.screen.BasePresenter;

import java.util.List;

/**
 * Created by sonng266 on 15/03/2018.
 */

public interface PlaylistContract {
    /**
     * View.
     */
    interface View {
        void showPlaylist(List<Playlist> playlists);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter<PlaylistContract.View> {
        void loadPlaylist();
    }
}
