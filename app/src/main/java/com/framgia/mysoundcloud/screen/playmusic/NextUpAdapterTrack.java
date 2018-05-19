package com.framgia.mysoundcloud.screen.playmusic;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.framgia.mysoundcloud.R;
import com.framgia.mysoundcloud.screen.BaseTrackRecyclerViewAdapter;
import com.framgia.mysoundcloud.utils.StringUtil;



public class NextUpAdapterTrack extends
        BaseTrackRecyclerViewAdapter<BaseTrackRecyclerViewAdapter.BaseViewHolder> {

    private NextUpDialogFragment.NextUpItemClickedListener mListener;
    private int mCurrentPosition;

    NextUpAdapterTrack(Context context,
                       NextUpDialogFragment.NextUpItemClickedListener listener,
                       int currentTrackPosition) {
        super(context);
        mListener = listener;
        mCurrentPosition = currentTrackPosition;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater()
                .inflate(R.layout.fragment_next_up_dialog_item, parent, false));
    }

    public class ViewHolder extends BaseViewHolder {

        private ImageView mImageCurrentTrack;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void setupUI(View itemView) {
            mImageCurrentTrack = itemView.findViewById(R.id.image_current_track);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener == null) return;
                    mListener.onTrackPositionClicked(getAdapterPosition());
                    mCurrentPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        protected void bindData(int position) {
            if (mTracks == null) return;
            updateIconPlaying();
            mTextTitle.setText(mTracks.get(position).getTitle());
            if (mTracks.get(position).getPublisherMetadata() != null) {
                mTextArtist.setText(mTracks.get(position).getPublisherMetadata().getArtist());
            }
            mTextDuration.setText(StringUtil.parseMilliSecondsToTimer(
                    mTracks.get(position).getFullDuration()));
            Glide.with(mContext).load(mTracks.get(position).getArtworkUrl())
                    .into(mImageTrack);
        }

        private void updateIconPlaying() {
            if (getAdapterPosition() == mCurrentPosition) {
                mImageCurrentTrack.setVisibility(View.VISIBLE);
            } else {
                mImageCurrentTrack.setVisibility(View.GONE);
            }
        }
    }
}
