package com.framgia.mysoundcloud.data.source.local.config;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.framgia.mysoundcloud.data.model.Playlist;
import com.framgia.mysoundcloud.data.model.PublisherMetadata;
import com.framgia.mysoundcloud.data.model.Track;
import com.framgia.mysoundcloud.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonng266 on 14/03/2018.
 */

public class PlaylistTrackDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_PLAYLIST_ENTRIES = "CREATE TABLE "
            + PlaylistEntry.TABLE_NAME_PLAYLIST
            + " ( "
            + PlaylistEntry.COLUMN_NAME_PLAYLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PlaylistEntry.COLUMN_NAME_PLAYLIST + " TEXT NOT NULL"
            + " );";

    private static final String SQL_CREATE_TRACK_ENTRIES = "CREATE TABLE "
            + PlaylistEntry.TABLE_NAME_TRACK + " ( "
            + Track.TrackEntity.ARTWORK_URL + " TEXT, "
            + Track.TrackEntity.DESCRIPTION + " TEXT, "
            + Track.TrackEntity.DOWNLOADABLE + " INTEGER, "
            + Track.TrackEntity.DOWNLOAD_URL + " TEXT, "
            + Track.TrackEntity.DURATION + " INTEGER, "
            + Track.TrackEntity.ID + " INTEGER NOT NULL, "
            + Track.TrackEntity.PLAYBACK_COUNT + " INTEGER, "
            + Track.TrackEntity.TITLE + " TEXT, "
            + Track.TrackEntity.URI + " TEXT, "
            + Track.TrackEntity.USERNAME + " TEXT, "
            + Track.TrackEntity.LIKES_COUNT + " INTEGER, "
            + " PRIMARY KEY( "
            + Track.TrackEntity.ID
            + " )"
            + ");";

    private static final String SQL_CREATE_PLAYLIST_HAS_TRACK = "CREATE TABLE "
            + PlaylistEntry.TABLE_NAME_PLAYLIST_HAS_TRACK
            + " ( "
            + PlaylistEntry.COLUMN_NAME_PLAYLIST_ID + " INTEGER NOT NULL,"
            + PlaylistEntry.COLUMN_NAME_TRACK_ID + " INTEGER NOT NULL, "
            + "PRIMARY KEY( "
            + PlaylistEntry.COLUMN_NAME_TRACK_ID + " , "
            + PlaylistEntry.COLUMN_NAME_PLAYLIST_ID
            + " ) "
            + ");";

    private static PlaylistTrackDbHelper sPlaylistTrackDbHelper;

    public static PlaylistTrackDbHelper getInstance(@NonNull Context context) {
        if (sPlaylistTrackDbHelper == null) {
            sPlaylistTrackDbHelper = new PlaylistTrackDbHelper(context);
        }
        return sPlaylistTrackDbHelper;
    }

    private PlaylistTrackDbHelper(Context context) {
        super(context, Constant.DATABASE_NAME, null, Constant.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PLAYLIST_ENTRIES);
        db.execSQL(SQL_CREATE_TRACK_ENTRIES);
        db.execSQL(SQL_CREATE_PLAYLIST_HAS_TRACK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insertTrack(Track track) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Track.TrackEntity.ARTWORK_URL, track.getArtworkUrl());
        values.put(Track.TrackEntity.DESCRIPTION, track.getDescription());
        if (track.isDownloadable()) {
            values.put(Track.TrackEntity.DOWNLOADABLE, 1);
        } else {
            values.put(Track.TrackEntity.DOWNLOADABLE, 0);
        }
        values.put(Track.TrackEntity.DOWNLOAD_URL, track.getDowloadUrl());
        values.put(Track.TrackEntity.DURATION, track.getFullDuration());
        values.put(Track.TrackEntity.ID, track.getId());
        values.put(Track.TrackEntity.PLAYBACK_COUNT, track.getDownloadCount());
        values.put(Track.TrackEntity.TITLE, track.getTitle());
        values.put(Track.TrackEntity.URI, track.getUri());
        values.put(Track.TrackEntity.USERNAME, track.getPublisherMetadata().getArtist());
        values.put(Track.TrackEntity.LIKES_COUNT, track.getLikesCount());
        database.insert(PlaylistTrackDbHelper.PlaylistEntry.TABLE_NAME_TRACK, null, values);
        database.close();
    }

    public void insertPlaylist(String namePlaylist) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PlaylistEntry.COLUMN_NAME_PLAYLIST, namePlaylist);
        database.insert(PlaylistEntry.TABLE_NAME_PLAYLIST, null, values);
        database.close();
    }

    public void insertToTablePlaylistHasTrack(int trackId, int playlistId) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PlaylistEntry.COLUMN_NAME_PLAYLIST_ID, playlistId);
        values.put(PlaylistEntry.COLUMN_NAME_TRACK_ID, trackId);
        database.insert(PlaylistEntry.TABLE_NAME_PLAYLIST_HAS_TRACK, null, values);
        database.close();
    }

    public List<Playlist> getAllPlaylist() {
        SQLiteDatabase database = getReadableDatabase();

        Cursor cursor = database.query(PlaylistEntry.TABLE_NAME_PLAYLIST, null,
                null, null, null, null, null);

        List<Playlist> playlists = new ArrayList<>();
        while (cursor.moveToNext()) {
            Playlist playlist = new Playlist();
            playlist.setName(cursor.getString(
                    cursor.getColumnIndexOrThrow(PlaylistEntry.COLUMN_NAME_PLAYLIST)));
            playlist.setId(cursor.getInt(
                    cursor.getColumnIndexOrThrow(PlaylistEntry.COLUMN_NAME_PLAYLIST_ID)));
            playlists.add(playlist);
        }

        cursor.close();
        database.close();

        return playlists;
    }

    public List<Track> getTracksWithPlaylistId(int playlistId) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "
                        + PlaylistEntry.TABLE_NAME_PLAYLIST_HAS_TRACK
                        + " , "
                        + PlaylistEntry.TABLE_NAME_TRACK
                        + " WHERE "
                        + PlaylistEntry.TABLE_NAME_PLAYLIST_HAS_TRACK + "."
                        + PlaylistEntry.COLUMN_NAME_PLAYLIST_ID
                        + " = "
                        + playlistId
                        + " AND "
                        + PlaylistEntry.TABLE_NAME_TRACK + "." + Track.TrackEntity.ID
                        + " = "
                        + PlaylistEntry.TABLE_NAME_PLAYLIST_HAS_TRACK + "."
                        + PlaylistEntry.COLUMN_NAME_TRACK_ID
                , null);

        if (cursor == null) return null;

        List<Track> tracks = new ArrayList<>();
        cursor.moveToFirst();
        do {
            Track track = parseTrackFromCusor(cursor);
            if (track != null) {
                tracks.add(track);
            }
        } while (cursor.moveToNext());

        cursor.close();
        database.close();

        return tracks;
    }

    private Track parseTrackFromCusor(Cursor cursor) {
        Track track = new Track();

        int downloadable = cursor.getInt(
                cursor.getColumnIndexOrThrow(Track.TrackEntity.DOWNLOADABLE));

        if (downloadable == 1) {
            track.setDownloadable(true);
        } else {
            track.setDownloadable(false);
        }

        track.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Track.TrackEntity.ID)));
        track.setFullDuration(cursor.getInt(cursor.getColumnIndexOrThrow(Track.TrackEntity.DURATION)));
        track.setUri(cursor.getString(cursor.getColumnIndexOrThrow(Track.TrackEntity.URI)));
        track.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(Track.TrackEntity.TITLE)));
        track.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(Track.TrackEntity.DESCRIPTION)));
        track.setArtworkUrl(cursor.getString(cursor.getColumnIndexOrThrow(Track.TrackEntity.ARTWORK_URL)));
        track.setLikesCount(cursor.getInt(cursor.getColumnIndexOrThrow(Track.TrackEntity.LIKES_COUNT)));
        track.setDownloadCount(cursor.getInt(cursor.getColumnIndexOrThrow(Track.TrackEntity.PLAYBACK_COUNT)));
        track.setDowloadUrl(cursor.getString(cursor.getColumnIndexOrThrow(Track.TrackEntity.DOWNLOAD_URL)));
        PublisherMetadata publisherMetadata = new PublisherMetadata(cursor.getString(cursor.getColumnIndexOrThrow(Track.TrackEntity.USERNAME)));
        track.setPublisherMetadata(publisherMetadata);

        return track;
    }

    /**
     * Inner class that defines the table playlist contents
     */
    public static final class PlaylistEntry {
        public static final String TABLE_NAME_PLAYLIST = "Playlists";
        public static final String TABLE_NAME_TRACK = "Tracks";
        public static final String TABLE_NAME_PLAYLIST_HAS_TRACK = "PlaylistTrack";
        public static final String COLUMN_NAME_PLAYLIST = "playlist_name";
        public static final String COLUMN_NAME_PLAYLIST_ID = "playlist_id";
        public static final String COLUMN_NAME_TRACK_ID = "track_id";
    }
}
