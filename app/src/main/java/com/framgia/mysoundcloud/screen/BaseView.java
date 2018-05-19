package com.framgia.mysoundcloud.screen;

import com.framgia.mysoundcloud.data.model.Track;

import java.util.ArrayList;



public interface BaseView {
    void showTracks(ArrayList<Track> trackList);

    void showNoTrack();

    void showLoadingTracksError(String message);

    void showLoadingIndicator();

    void hideLoadingIndicator();
}
