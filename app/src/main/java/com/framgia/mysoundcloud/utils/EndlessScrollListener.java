package com.framgia.mysoundcloud.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by sonng266 on 13/03/2018.
 */

public class EndlessScrollListener extends RecyclerView.OnScrollListener {
    private boolean mLoading;
    private LoadMoreListener mLoadMoreListener;

    public EndlessScrollListener(LoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
        mLoading = false;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy <= 0 || mLoadMoreListener == null) {
            return;
        }
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
        if (!isLoading() && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
            mLoading = true;
            mLoadMoreListener.requestLoadMore(totalItemCount);
        }
    }

    public boolean isLoading() {
        return mLoading;
    }

    public void setLoading(boolean loading) {
        mLoading = loading;
    }

    public interface LoadMoreListener {
        void requestLoadMore(int totalItemCount);
    }
}

