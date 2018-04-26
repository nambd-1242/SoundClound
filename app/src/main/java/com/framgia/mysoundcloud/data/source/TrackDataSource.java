package com.framgia.mysoundcloud.data.source;

import com.framgia.mysoundcloud.data.model.Playlist;
import com.framgia.mysoundcloud.data.model.Track;

import java.util.List;

/**
 * Created by sonng266 on 28/02/2018.
 */

public interface TrackDataSource {

    /**
     * LocalData For Tracks
     */
    interface LocalDataSource extends TrackDataSource {
        void getTracksLocal(OnFetchDataListener<Track> listener);

        void searchTracksLocal(String trackName, OnFetchDataListener<Track> listener);

        boolean deleteTrack(Track track);

        void addTracksToPlaylist(int playlistId,
                                 OnHandleDatabaseListener listener, Track... tracks);

        void addTracksToNewPlaylist(String newPlaylistName,
                                    OnHandleDatabaseListener listener, Track... tracks);

        List<Playlist> getPlaylist();

        List<Playlist> getDetailPlaylist();
    }

    /**
     * RemoteData For Tracks
     */
    interface RemoteDataSource extends TrackDataSource {
        void getTracksRemote(String genre, int limit, int offSet,
                             OnFetchDataListener<Track> listener);

        void searchTracksRemote(String trackName, int offSet, OnFetchDataListener<Track> listener);
    }

    interface OnFetchDataListener<T> {
        void onFetchDataSuccess(List<T> data);

        void onFetchDataFailure(String message);
    }

    interface OnHandleDatabaseListener {
        void onHandleSuccess(String message);

        void onHandleFailure(String message);
    }
}
