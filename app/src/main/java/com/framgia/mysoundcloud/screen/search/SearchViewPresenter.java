package com.framgia.mysoundcloud.screen.search;

import com.framgia.mysoundcloud.data.model.Track;
import com.framgia.mysoundcloud.data.repository.TrackRepository;
import com.framgia.mysoundcloud.data.source.TrackDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonng266 on 13/03/2018.
 */

public class SearchViewPresenter implements SearchViewContract.Presenter,
        TrackDataSource.OnFetchDataListener<Track> {

    private SearchViewContract.View mView;

    @Override
    public void setView(SearchViewContract.View view) {
        mView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void searchTrack(String trackName, int offSet) {
        TrackRepository.getInstance().searchTracksRemote(trackName, offSet, this);

        mView.showLoadingIndicator();
    }

    @Override
    public void onFetchDataSuccess(List<Track> data) {
        mView.hideLoadingIndicator();

        if (data == null || data.isEmpty()) {
            mView.showNoTrack();
            return;
        }

        mView.showTracks((ArrayList<Track>) data);
    }

    @Override
    public void onFetchDataFailure(String message) {
        mView.hideLoadingIndicator();
        mView.showLoadingTracksError(message);
    }
}
