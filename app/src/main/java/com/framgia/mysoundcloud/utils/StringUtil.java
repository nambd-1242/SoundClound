package com.framgia.mysoundcloud.utils;

import com.framgia.mysoundcloud.BuildConfig;

/**
 * Created by sonng266 on 01/03/2018.
 */

public class StringUtil {
    public static String convertUrlFetchMusicGenre(String genre, int limit, int offset) {
        return String.format("%s%s%s&%s=%s&%s=%d&%s=%d", Constant.BASE_URL,
                Constant.PARA_MUSIC_GENRE, genre, Constant.CLIENT_ID,
                BuildConfig.API_KEY, Constant.LIMIT, limit, Constant.PARA_OFFSET, offset);

    }

    public static String getUrlStreamTrack(String uriTrack) {
        return String.format("%s/%s?%s=%s", uriTrack, Constant.PARA_STREAM,
                Constant.CLIENT_ID, BuildConfig.API_KEY);
    }

    public static String convertUrlDownloadTrack(String url) {
        return String.format("%s?%s=%s", url, Constant.CLIENT_ID, BuildConfig.API_KEY);
    }

    public static String convertUrlSearchTrack(String trackName, int offset) {
        return String.format("%s%s%s&%s=%d&%s=%s", Constant.BASE_URL, Constant.PARA_SEARCH_TRACK, trackName,
                Constant.PARA_OFFSET, offset, Constant.CLIENT_ID,
                BuildConfig.API_KEY);
    }

    public static String parseMilliSecondsToTimer(long milliseconds) {
        String parseTimer = "";
        String secondsString = "";

        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        if (hours > 0) {
            parseTimer = hours + ":";
        }
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }
        parseTimer = parseTimer + minutes + ":" + secondsString;
        return parseTimer;
    }
}
