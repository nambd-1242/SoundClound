package com.framgia.mysoundcloud.utils;

import android.Manifest;

/**
 * Created by sonng266 on 28/02/2018.
 */

public final class Constant {

    public static final String ERROR_NULL = "data null";
    public static final String EXTRA_ID_PLAYLIST = "id_playlist";
    public static final String TABLE_FAVORITE = "tbt_favorite";
    public static final String DOWLOAD = "dowload";
    public static final String FAVORITE = "favorite";
    public static final String ARGUMENT_FLAG = "flag_playlist";

    private Constant() {
    }

    // Network
    public static final String BASE_URL = "https://api-v2.soundcloud.com/";
    public static final String PARA_MUSIC_GENRE = "charts?kind=top&genre=soundcloud%3Agenres%3A";
    public static final String PARA_SEARCH_TRACK = "search/tracks?facet=genre&limit=10&linked_partitioning=1&q=";
    public static final String PARA_OFFSET = "offset";
    public static final String PARA_STREAM = "stream";
    public static final String CLIENT_ID = "client_id";
    public static final String REQUEST_METHOD_GET = "GET";
    public static final String LIMIT = "limit";
    public static final int READ_TIME_OUT = 5000; /* milliseconds */
    public static final int CONNECT_TIME_OUT = 5000; /* milliseconds */
    public static final int OFFSET_DEFAULT = 0;
    public static final int LIMIT_DEFAULT = 10;
    public static final String NULL_RESULT = "null";
    public static final String[] MUSIC_GENRES = {"all-music", "all-audio",
            "alternativerock", "ambient", "classical", "country"};

    // String
    public static final String BREAK_LINE = "\n";
    public static final String[] PERMISSIONS =
            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};

    // Int
    public static final int MAX_SEEK_BAR = 100;

    // Bundle
    public static final String ARGUMENT_TRACK_LIST_LISTENER = "ARGUMENT_TRACK_LIST_LISTENER";
    public static final String ARGUMENT_NEXT_UP_LISTENER = "ARGUMENT_NEXT_UP_LISTENER";
    public static final String ARGUMENT_CURRENT_TRACK_POSITION = "ARGUMENT_CURRENT_TRACK_POSITION";
    public static final String USER = "ARGUMENT_CURRENT_USER";


    // SQL
    public static final String DATABASE_NAME = "MySoundCloud.db";
    public static final int DATABASE_VERSION = 1;
}
