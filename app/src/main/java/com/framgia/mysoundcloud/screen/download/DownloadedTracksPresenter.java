package com.framgia.mysoundcloud.screen.download;

import com.framgia.mysoundcloud.data.model.Track;
import com.framgia.mysoundcloud.data.repository.TrackRepository;
import com.framgia.mysoundcloud.data.source.TrackDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonng266 on 09/03/2018.
 */

public class DownloadedTracksPresenter implements DownloadViewContract.Presenter,
        TrackDataSource.OnFetchDataListener<Track> {

    private DownloadViewContract.View mView;

    @Override
    public void setView(DownloadViewContract.View view) {
        mView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void loadTrack() {
        TrackRepository.getInstance().getTracksLocal(this);
    }

    @Override
    public void onFetchDataSuccess(List<Track> data) {
        mView.hideLoadingIndicator();

        if (data == null || data.isEmpty()) {
            mView.showNoTrack();
        } else {
            mView.showTracks((ArrayList<Track>) data);
        }
    }

    @Override
    public void onFetchDataFailure(String message) {
        mView.hideLoadingIndicator();
        mView.showLoadingTracksError(message);
    }
}
