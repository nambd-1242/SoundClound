package com.framgia.mysoundcloud.utils.music;

import com.framgia.mysoundcloud.data.model.Track;

import java.util.List;

/**
 * Created by sonng266 on 04/03/2018.
 */

public interface MusicPlayerManager {


    void release();

    void changeMediaState();

    void playNextTrack();

    void playPreviousTrack();

    void startProgressCallback();

    void endProgressCallback();

    void setPlaybackInfoListener(PlaybackInfoListener listener);

    void seekTo(int percent);

    Track getCurrentTrack();

    int getCurrentState();

    List<Track> getListTrack();

    void playTrackAtPosition(int position, Track... tracks);

    void addToNextUp(Track track);

    int getCurrentTrackPosition();

    @PlaybackInfoListener.LoopType
    int getLoopType();

    void changeLoopType();

    boolean isShuffle();

    void changeShuffleState();
}
