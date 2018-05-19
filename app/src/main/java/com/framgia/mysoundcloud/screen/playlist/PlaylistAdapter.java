package com.framgia.mysoundcloud.screen.playlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.framgia.mysoundcloud.R;
import com.framgia.mysoundcloud.data.model.Playlist;
import com.framgia.mysoundcloud.data.model.Track;
import com.framgia.mysoundcloud.screen.main.MainViewConstract;

import java.util.ArrayList;
import java.util.List;



public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    private Context mContext;
    private List<Playlist> mPlaylists;
    private MainViewConstract.TrackListListener mListener;
    private CallBack callBack;
    private LayoutInflater mLayoutInflater;

    public PlaylistAdapter(Context context, MainViewConstract.TrackListListener listener , CallBack callBack) {
        mContext = context;
        mListener = listener;
        this.callBack = callBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }

        View view = mLayoutInflater.inflate(R.layout.item_playlist, parent, false);

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

    public void replaceData(List<Playlist> playlists) {
        if (mPlaylists == null) mPlaylists = new ArrayList<>();

        mPlaylists.clear();
        if (playlists != null) {
            mPlaylists.addAll(playlists);
        }

        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextTitle;
        private ImageView mImagePlaylist;
        private TextView mTextTotalTracks;
        private TextView mTxtMore;
        private Playlist mPlaylist;

        public ViewHolder(View itemView) {
            super(itemView);

            mTextTitle = itemView.findViewById(R.id.text_playlist_name);
            mTextTotalTracks = itemView.findViewById(R.id.text_playlist_total_tracks);
            mImagePlaylist = itemView.findViewById(R.id.image_playlist);
            mImagePlaylist = itemView.findViewById(R.id.image_playlist);
            mTxtMore = itemView.findViewById(R.id.text_options);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener == null) return;
                    mListener.onPlayedTrack(0, mPlaylist.getTracks());
                }
            });
            mTxtMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.onItemMoreClick(mPlaylist ,v);
                }
            });
        }

        public void bindData(Playlist playlist) {
            if (playlist == null) return;
            mPlaylist = playlist;
            mTextTitle.setText(playlist.getName());
            if (playlist.getTracks() == null || playlist.getTracks().isEmpty()) return;
            mTextTotalTracks.setText(String.format("%d tracks", playlist.getTracks().size()));
            Glide.with(mContext)
                    .load(playlist.getTracks().get(0).getArtworkUrl())
                    .into(mImagePlaylist);
        }
    }
    public interface CallBack {
        void onItemMoreClick(Playlist playlist , View view);
    }
}
