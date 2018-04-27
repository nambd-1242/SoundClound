package com.framgia.mysoundcloud.screen.playlistdetail;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.framgia.mysoundcloud.R;
import com.framgia.mysoundcloud.data.model.Track;
import com.framgia.mysoundcloud.screen.BaseTrackRecyclerViewAdapter;
import com.framgia.mysoundcloud.screen.main.MainViewConstract;
import com.framgia.mysoundcloud.utils.StringUtil;




public class TracksAdapter
        extends BaseTrackRecyclerViewAdapter<BaseTrackRecyclerViewAdapter.BaseViewHolder> {

    private MainViewConstract.TrackListListener mListener;
    private PlayListDetailViewContract.DeleteTrackListener mDeleteTrackListener;

    TracksAdapter(Context context, MainViewConstract.TrackListListener listener,
                  PlayListDetailViewContract.DeleteTrackListener deleteTrackListener) {
        super(context);
        mListener = listener;
        mDeleteTrackListener = deleteTrackListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater()
                .inflate(R.layout.item_downloaded_track, parent, false));
    }

    class ViewHolder extends BaseViewHolder {

        private TextView mTextOptions;
        private Track mTrack;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void setupUI(View itemView) {
            mTextOptions = itemView.findViewById(R.id.text_options);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener == null) return;
                    mListener.onPlayedTrack(getAdapterPosition(), mTracks);
                }
            });

            mTextOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popupMenu = new PopupMenu(mContext, mTextOptions);
                    popupMenu.inflate(R.menu.options_menu_item_download_track);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_add_to_next_up:
                                    if (mListener == null) return true;
                                    mListener.onAddedToNextUp(mTrack);
                                    return true;
                                case R.id.action_delete:
                                    if (mDeleteTrackListener == null) return false;
                                    mDeleteTrackListener.onDeleteClicked(mTrack);
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

        @Override
        protected void bindData(int position) {
            if (mTracks == null) return;
            mTrack = mTracks.get(position);
            mTextDuration.setText(StringUtil.parseMilliSecondsToTimer(mTrack.getFullDuration()));
            mTextTitle.setText(mTrack.getTitle());
            mTextArtist.setText(mTrack.getPublisherMetadata().getArtist());
            Glide.with(mContext)
                    .load(mTrack.getArtworkUrl())
                    .placeholder(R.drawable.bg_splash_screen)
                    .into(mImageTrack);
        }
    }
}
