package com.framgia.mysoundcloud.screen.musicgenres;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.framgia.mysoundcloud.R;
import com.framgia.mysoundcloud.data.model.Track;
import com.framgia.mysoundcloud.data.source.remote.DownloadTrackManager;
import com.framgia.mysoundcloud.screen.BaseTrackRecyclerViewAdapter;
import com.framgia.mysoundcloud.screen.main.MainViewConstract;
import com.framgia.mysoundcloud.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonng266 on 02/03/2018.
 */

public class MusicGenresAdapter extends BaseTrackRecyclerViewAdapter<BaseTrackRecyclerViewAdapter.BaseViewHolder>
        implements DownloadTrackManager.DownloadListener {

    private MainViewConstract.TrackListListener mListener;

    public MusicGenresAdapter(Context context, MainViewConstract.TrackListListener listener) {
        super(context);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater()
                .inflate(R.layout.item_song, parent, false));
    }

    @Override
    public void onDownloadError(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloading() {
        Toast.makeText(mContext, mContext.getString(R.string.msg_downloading),
                Toast.LENGTH_SHORT).show();
    }

    public List<Track> addMoreData(List<Track> tracks) {
        if (tracks == null) return mTracks;

        if (mTracks == null) mTracks = new ArrayList<>();
        mTracks.addAll(tracks);
        notifyDataSetChanged();
        return mTracks;
    }

    /**
     * ViewHolder
     */
    class ViewHolder extends BaseViewHolder {

        private TextView mTextPlaybackCount;
        private TextView mTextLikeCount;
        private TextView mTextOptions;
        private Track mTrack;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void setupUI(View itemView) {
            mTextLikeCount = itemView.findViewById(R.id.text_number_favorite);
            mTextPlaybackCount = itemView.findViewById(R.id.text_number_play);

            setupOptionsMenu(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener == null) return;
                    mListener.onPlayedTrack(getAdapterPosition(), mTracks);
                }
            });
        }

        @Override
        protected void bindData(int position) {
            if (mTracks == null) return;
            mTrack = mTracks.get(position);
            mTextTitle.setText(mTrack.getTitle());
            mTextPlaybackCount.setText(String.valueOf(mTrack.getDownloadCount()));
            if (mTrack.getPublisherMetadata() != null) {
                mTextArtist.setText(mTrack.getPublisherMetadata().getArtist());
            }
            mTextLikeCount.setText(String.valueOf(mTrack.getLikesCount()));
            mTextDuration.setText(StringUtil.parseMilliSecondsToTimer(mTrack.getFullDuration()));
            Glide.with(mContext).load(mTrack.getArtworkUrl()).into(mImageTrack);
        }

        private void setupOptionsMenu(View itemView) {
            mTextOptions = itemView.findViewById(R.id.text_options);
            mTextOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(mContext, mTextOptions);
                    popupMenu.inflate(R.menu.options_menu_item_track);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_add_to_next_up:
                                    if (mListener == null) return true;
                                    mListener.onAddedToNextUp(mTrack);
                                    return true;
                                case R.id.action_download:
                                    new DownloadTrackManager(mContext,
                                            MusicGenresAdapter.this)
                                            .downloadTrack(mTrack);
                                    return true;
                                case R.id.action_add_to_playlist:
                                    if (mListener == null) return true;
                                    mListener.onAddToPlaylist(mTrack);
                                    return true;

                                case R.id.action_add_farvotie :
                                    if (mListener == null) return true;
                                    mListener.onAddToFavorite(mTrack);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popupMenu.show();
                }
            });
        }
    }
}
