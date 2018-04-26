package com.framgia.mysoundcloud.screen.musicgenres;

import com.framgia.mysoundcloud.data.model.Track;
import com.framgia.mysoundcloud.data.repository.TrackRepository;
import com.framgia.mysoundcloud.data.source.TrackDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonng266 on 27/02/2018.
 */

public class MusicGenresPresenter implements MusicGenresContract.Presenter,
        TrackDataSource.OnFetchDataListener<Track> {

    private MusicGenresContract.View mView;

    @Override
    public void setView(MusicGenresContract.View view) {
        this.mView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void loadTrack(String genre, int limit, int offSet) {
        mView.showLoadingIndicator();

        TrackRepository trackRepository = TrackRepository.getInstance();
        trackRepository.getTracksRemote(genre, limit, offSet, this);
    }

    @Override
    public void onFetchDataSuccess(List<Track> data) {
        mView.hideLoadingIndicator();

        if (data == null || data.isEmpty()) {
            mView.showNoTrack();
            return;
        }

        mView.showTracks((ArrayList<Track>) data);
        mView.updateTextNumberTrack();
    }

    @Override
    public void onFetchDataFailure(String message) {
        mView.hideLoadingIndicator();

        mView.showLoadingTracksError(message);
    }
}
