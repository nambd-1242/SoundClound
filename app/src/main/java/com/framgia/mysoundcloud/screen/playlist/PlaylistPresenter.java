package com.framgia.mysoundcloud.screen.playlist;

import com.framgia.mysoundcloud.data.model.Playlist;
import com.framgia.mysoundcloud.data.repository.TrackRepository;
import com.framgia.mysoundcloud.data.source.TrackDataSource;

import java.util.List;

/**
 * Created by sonng266 on 15/03/2018.
 */

public class PlaylistPresenter implements PlaylistContract.Presenter, TrackDataSource.OnFetchDataListener<Playlist> {

    private PlaylistContract.View mView;

    @Override
    public void setView(PlaylistContract.View view) {
        mView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void loadPlaylist() {
        onFetchDataSuccess(TrackRepository.getInstance().getDetailPlaylist());
    }

    @Override
    public void onFetchDataSuccess(List<Playlist> data) {
        mView.showPlaylist(data);
    }

    @Override
    public void onFetchDataFailure(String message) {
    }
}
