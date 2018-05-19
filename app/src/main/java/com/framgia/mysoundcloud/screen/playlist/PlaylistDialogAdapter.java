package com.framgia.mysoundcloud.screen.playlist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framgia.mysoundcloud.data.model.Playlist;
import com.framgia.mysoundcloud.data.model.Track;
import com.framgia.mysoundcloud.data.model.User;
import com.framgia.mysoundcloud.data.repository.TrackRepository;
import com.framgia.mysoundcloud.data.source.TrackDataSource;
import com.framgia.mysoundcloud.data.source.local.SharePreferences;
import com.framgia.mysoundcloud.utils.Constant;

import java.util.List;



public class PlaylistDialogAdapter extends RecyclerView.Adapter<PlaylistDialogAdapter.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private List<Playlist> mPlaylists;
    private Track[] mTracks;
    private TrackDataSource.OnHandleDatabaseListener mListener;

    public PlaylistDialogAdapter(TrackDataSource.OnHandleDatabaseListener listener, Track... tracks) {
        User user = SharePreferences.getInstance().getUser();
        if (user == null) return;
        String id = user.getId();
        mPlaylists = TrackRepository.getInstance().getDetailPlaylistbyIdUser(id);
        for (int i = 0; i < mPlaylists.size(); i++) {
            if (mPlaylists.get(i).getName().equals(Constant.TABLE_FAVORITE)) {
                mPlaylists.remove(i);
            }
        }
        mTracks = tracks;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }

        View view = mLayoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mPlaylists == null) return;
        holder.bindData(mPlaylists.get(position));
    }

    @Override
    public int getItemCount() {
        return mPlaylists != null ? mPlaylists.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private Playlist mPlaylist;
        private TextView mTextTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            mTextTitle = itemView.findViewById(android.R.id.text1);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TrackRepository.getInstance()
                            .addTracksToPlaylist(mPlaylist.getId(), mListener, mTracks);
                }
            });
        }

        public void bindData(Playlist playlist) {
            if (playlist == null) return;
            mPlaylist = playlist;
            mTextTitle.setText(playlist.getName());
        }
    }
}
