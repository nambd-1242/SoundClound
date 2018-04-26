package com.framgia.mysoundcloud.data.repository;

import com.framgia.mysoundcloud.data.model.Playlist;
import com.framgia.mysoundcloud.data.model.Track;
import com.framgia.mysoundcloud.data.source.TrackDataSource;
import com.framgia.mysoundcloud.data.source.local.TrackLocalDataSource;
import com.framgia.mysoundcloud.data.source.remote.TrackRemoteDataSource;

import java.util.List;

/**
 * Created by sonng266 on 28/02/2018.
 */

public class TrackRepository implements TrackDataSource.RemoteDataSource,
        TrackDataSource.LocalDataSource {

    private static TrackRepository sTrackRepository;

    private TrackDataSource.LocalDataSource mTrackLocalDataSource;
    private TrackDataSource.RemoteDataSource mTrackRemoteDataSource;

    private TrackRepository(LocalDataSource trackLocalDataSource,
                            RemoteDataSource trackRemoteDataSource) {
        mTrackLocalDataSource = trackLocalDataSource;
        mTrackRemoteDataSource = trackRemoteDataSource;
    }

    public static TrackRepository getInstance() {
        if (sTrackRepository == null) {
            sTrackRepository = new TrackRepository(
                    TrackLocalDataSource.getInstance(),
                    TrackRemoteDataSource.getInstance());
        }
        return sTrackRepository;

    }

    @Override
    public void getTracksLocal(OnFetchDataListener<Track> listener) {
        if (mTrackLocalDataSource != null) {
            mTrackLocalDataSource.getTracksLocal(listener);
        }
    }

    @Override
    public void searchTracksLocal(String trackName, OnFetchDataListener<Track> listener) {
        if (mTrackLocalDataSource != null) {
            mTrackLocalDataSource.searchTracksLocal(trackName, listener);
        }
    }

    @Override
    public boolean deleteTrack(Track track) {
        return mTrackLocalDataSource != null && mTrackLocalDataSource.deleteTrack(track);
    }

    @Override
    public void addTracksToPlaylist(int playlistId,
                                    OnHandleDatabaseListener listener, Track... tracks) {
        if (mTrackLocalDataSource == null) return;
        mTrackLocalDataSource.addTracksToPlaylist(playlistId, listener, tracks);
    }

    @Override
    public void addTracksToNewPlaylist(String newPlaylistName,
                                       OnHandleDatabaseListener listener, Track... tracks) {
        if (mTrackLocalDataSource == null) return;
        mTrackLocalDataSource.addTracksToNewPlaylist(newPlaylistName, listener, tracks);
    }


    @Override
    public List<Playlist> getPlaylist() {
        if (mTrackLocalDataSource == null) return null;
        return mTrackLocalDataSource.getPlaylist();
    }

    @Override
    public List<Playlist> getDetailPlaylist() {
        if (mTrackLocalDataSource == null) return null;
        return mTrackLocalDataSource.getDetailPlaylist();
    }

    @Override
    public void getTracksRemote(String genre, int limit, int offSet,
                                OnFetchDataListener<Track> listener) {
        if (mTrackRemoteDataSource == null) return;
        mTrackRemoteDataSource.getTracksRemote(genre, limit, offSet, listener);
    }

    @Override
    public void searchTracksRemote(String trackName, int offSet,
                                   OnFetchDataListener<Track> listener) {
        if (mTrackRemoteDataSource != null) {
            mTrackRemoteDataSource.searchTracksRemote(trackName, offSet, listener);
        }
    }
}
