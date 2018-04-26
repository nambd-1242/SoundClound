package com.framgia.mysoundcloud.screen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.mysoundcloud.R;
import com.framgia.mysoundcloud.data.model.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonng266 on 26/02/2018.
 */

public abstract class BaseTrackRecyclerViewAdapter<V extends BaseTrackRecyclerViewAdapter.BaseViewHolder>
        extends RecyclerView.Adapter<V> {

    protected Context mContext;
    protected List<Track> mTracks;
    protected LayoutInflater mLayoutInflater;

    protected BaseTrackRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public void onBindViewHolder(V holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return mTracks == null ? 0 : mTracks.size();
    }

    protected LayoutInflater getLayoutInflater() {
        if (mLayoutInflater == null) mLayoutInflater = LayoutInflater.from(mContext);
        return mLayoutInflater;
    }

    public void replaceData(List<Track> tracks) {
        if (mTracks == null) mTracks = new ArrayList<>();

        mTracks.clear();
        if (tracks != null) {
            mTracks.addAll(tracks);
        }

        notifyDataSetChanged();
    }

    public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        protected TextView mTextTitle;
        protected TextView mTextArtist;
        protected ImageView mImageTrack;
        protected TextView mTextDuration;

        public BaseViewHolder(View itemView) {
            super(itemView);

            mTextTitle = itemView.findViewById(R.id.text_title);
            mTextArtist = itemView.findViewById(R.id.text_artist);
            mTextDuration = itemView.findViewById(R.id.text_duration);
            mImageTrack = itemView.findViewById(R.id.image_song);

            setupUI(itemView);
        }

        protected abstract void setupUI(View itemView);

        protected abstract void bindData(int position);
    }
}
