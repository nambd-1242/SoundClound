package com.framgia.mysoundcloud.data.source.remote;

import android.os.AsyncTask;

import com.framgia.mysoundcloud.data.model.Collection;
import com.framgia.mysoundcloud.data.model.CollectionResult;
import com.framgia.mysoundcloud.data.model.CollectionResultSearch;
import com.framgia.mysoundcloud.data.model.CollectionSearch;
import com.framgia.mysoundcloud.data.model.Track;
import com.framgia.mysoundcloud.data.source.TrackDataSource;
import com.framgia.mysoundcloud.utils.Constant;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public  class FetchTrackSearchFromUrl extends AsyncTask<String, Void, List<Track>> {

    protected TrackDataSource.OnFetchDataListener<Track> mListener;

    protected FetchTrackSearchFromUrl(TrackDataSource.OnFetchDataListener<Track> listener) {
        mListener = listener;
    }

    @Override
    protected List<Track> doInBackground(String... strings) {
        try {
            String url = strings[0];
            String result = getJSONStringFromURL(url);
            CollectionResultSearch collectionResult =
                    new Gson().fromJson(result, CollectionResultSearch.class);
            if (collectionResult == null) {
                mListener.onFetchDataFailure(Constant.ERROR_NULL);
                return null;
            }
            List<Track> tracks = new ArrayList<>();
            for (Track collection : collectionResult.getCollection()) {
                tracks.add(collection);
            }
            return tracks;
        } catch (IOException e) {
            mListener.onFetchDataFailure(e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Track> tracks) {
        if (mListener == null) {
            mListener.onFetchDataFailure(Constant.NULL_RESULT);
        } else {
            mListener.onFetchDataSuccess(tracks);

        }
    }


    protected String getJSONStringFromURL(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        httpURLConnection.setRequestMethod(Constant.REQUEST_METHOD_GET);
        httpURLConnection.setReadTimeout(Constant.READ_TIME_OUT);
        httpURLConnection.setConnectTimeout(Constant.CONNECT_TIME_OUT);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append(Constant.BREAK_LINE);
        }
        br.close();
        httpURLConnection.disconnect();
        return sb.toString();
    }


}
