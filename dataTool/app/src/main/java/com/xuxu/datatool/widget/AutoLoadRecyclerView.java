package com.xuxu.datatool.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xuxu.datatool.widget.LoadFinishCallBack;

public class AutoLoadRecyclerView extends RecyclerView implements LoadFinishCallBack {

    private onLoadMoreListener loadMoreListener;
    private boolean isLoadingMore;

    public AutoLoadRecyclerView(Context context) {
        this(context, null);
    }

    public AutoLoadRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoLoadRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        isLoadingMore = false;
        //初始化状态，把滚动监听器设置为自定义监听器
        setOnScrollListener(new AutoLoadScrollListener(null, true, true));
    }

    /**
     * 如果需要显示图片，需要设置这几个参数，快速滑动时，暂停图片加载
     *
     * @param imageLoader
     * @param pauseOnScroll
     * @param pauseOnFling
     */
    //准备一个为加载图片设置参数的方法，方便在其他类中设置暂停时的监听器，且可以设置自定义监听器所需参数
    public void setOnPauseListenerParams(ImageLoader imageLoader, boolean pauseOnScroll, boolean
            pauseOnFling) {
        setOnScrollListener(new AutoLoadScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
    }

    //方便在其他类中设置加载更多监听器，
    // 为recyclerView设置加载更多监听，需要加载更多时，调用onLoadMoreListener接口
    public void setLoadMoreListener(onLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    @Override
    public void loadFinish(Object obj) {
        isLoadingMore = false;
    }

    //准备加载更多接口，等待调用
    public interface onLoadMoreListener {
        void loadMore();
    }

    /**
     * 滑动自动加载监听器
     */
    private class AutoLoadScrollListener extends OnScrollListener {

        private ImageLoader imageLoader;
        private final boolean pauseOnScroll;
        private final boolean pauseOnFling;

        public AutoLoadScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling) {
            super();
            this.pauseOnScroll = pauseOnScroll;
            this.pauseOnFling = pauseOnFling;
            this.imageLoader = imageLoader;
        }

        //监听正面向下滑动加载更多数据，即滚动时加载除图片以外的数据
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            //由于GridLayoutManager是LinearLayoutManager子类，所以也适用
            if (getLayoutManager() instanceof LinearLayoutManager) {
                //获取当前总数和列表所能容纳下总数,获取到所容纳总数为一页数据
                int lastVisibleItem = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
                int totalItemCount = AutoLoadRecyclerView.this.getAdapter().getItemCount();

                //有回调接口，并且不是加载状态，并且剩下2个item，并且向下滑动，则自动加载
                if (loadMoreListener != null && !isLoadingMore && lastVisibleItem >= totalItemCount -
                        2 && dy > 0) {
                    loadMoreListener.loadMore();
                    isLoadingMore = true;
                }
            }
        }

        //监听滚动事件，滚动时暂停，停止加载图片，主要是设置图片加载操作
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            if (imageLoader != null) {
                switch (newState) {
                    case 0:
                        imageLoader.resume();
                        break;
                    case 1:
                        if (pauseOnScroll) {
                            imageLoader.pause();
                        } else {
                            imageLoader.resume();
                        }
                        break;
                    case 2:
                        if (pauseOnFling) {
                            imageLoader.pause();
                        } else {
                            imageLoader.resume();
                        }
                        break;
                }
            }
        }
    }


}