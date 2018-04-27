package com.framgia.mysoundcloud.screen.playlistdetail;

import com.framgia.mysoundcloud.data.model.Track;
import com.framgia.mysoundcloud.data.repository.TrackRepository;
import com.framgia.mysoundcloud.data.source.TrackDataSource;
import com.framgia.mysoundcloud.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonng266 on 09/03/2018.
 */

public class PlaylistDetailPresenter implements PlayListDetailViewContract.Presenter,
        TrackDataSource.OnFetchDataListener<Track> {

    private PlayListDetailViewContract.View mView;

    @Override
    public void setView(PlayListDetailViewContract.View view) {
        mView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void loadTrack(String flag) {
        if(flag.equals(Constant.FAVORITE)){
            TrackRepository.getInstance().getTrackbyTable(this ,Constant.TABLE_FAVORITE);
        }
        if(flag.equals(Constant.DOWLOAD)){
            TrackRepository.getInstance().getTracksLocal(this);
        }

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
