package com.framgia.mysoundcloud.data.source.remote;

import com.framgia.mysoundcloud.data.model.Track;
import com.framgia.mysoundcloud.data.source.TrackDataSource;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sonng266 on 13/03/2018.
 */

public class SearchTrackFromUrl extends BaseFetchTrackFromUrl {

    protected SearchTrackFromUrl(TrackDataSource.OnFetchDataListener<Track> listener) {
        super(listener);
    }

}
