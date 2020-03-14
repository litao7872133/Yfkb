package com.yfkk.cardbag.adapter;

import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yfkk.cardbag.R;


/**
 * 一个丰富的RecyclerView.Adapter
 * 支持分页，EmptyView，LoadingView
 * <p>
 * Created by litao on 2018/1/16.
 */
public abstract class BaseAdapter extends RecyclerView.Adapter<ViewHolder> {

    // 空白页样式
    public final int EMPTY_TYPE_LIGHT = 0; // 亮色(默认样式，推荐在白色背景下使用的样式)
    public final int EMPTY_TYPE_DARK = 1; // 暗色（在有颜色背景下使用的样式）

    // 状态
    public final int TYPE_DEFAULT = 10000;
    public final int TYPE_LOADING = 10001;
    public final int TYPE_EMPTY = 10002;
    public final int TYPE_ERROR = 10003;
    public final int TYPE_FOODTER = 10004;


    public static final int STATE_DEFAULT = 10000; // 默认状态
    public static final int STATE_LOADING = 10001; // 显示正在加载
    public static final int STATE_EMPTY = 10002; // 显示一个空页面
    public static final int STATE_ERROR = 10003; // 显示一个错误页面
    public static final int STATE_FOODTER = 10004; // 显示正在加载下一页

    public int state = STATE_DEFAULT; // 当前状态

    public FooterLoadCallback footerLoadCallback; // 监听加载下一页

    private LoadMoreViewHolder loadMoreViewHolder;
    private long refreshingLoadMoreTime; // 设置最短连续回调时间为500毫秒

    protected AdapterView.OnItemClickListener itemClickListener;

    /**
     * 已下4个方法，由子类重写。目的是替换对应的方法
     */
    public abstract int getCount();

    public abstract ViewHolder onCreateHolder(ViewGroup parent, int viewType);

    public abstract void onBindHolder(ViewHolder holder, int position);

    public abstract int getItemType(int position);

    public void setOnItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * 添加翻页监听
     *
     * @param footerLoadCallback
     */
    public void setFooterLoadCallback(FooterLoadCallback footerLoadCallback) {
        this.footerLoadCallback = footerLoadCallback;
    }

    /**
     * 子类建议重写：getItemType()
     * 每个不同类型的type只会调用一次onCreateViewHolder，也就是说view会保留在内存，谨慎使用
     *
     * @param position
     * @return
     */
    @CallSuper
    @Override
    public int getItemViewType(int position) {
        if (getCount() == 0 && state == STATE_EMPTY) {
            return TYPE_EMPTY;
        } else if (getCount() == 0 && state == STATE_LOADING) {
            return TYPE_LOADING;
        } else if (getCount() == 0 && state == STATE_ERROR) {
            return TYPE_ERROR;
        } else if (footerLoadCallback != null && position >= getCount()) {
            return TYPE_FOODTER;
        } else {
            return getItemType(position);
        }
    }

    @Override
    public int getItemCount() {
        if (getCount() == 0 && (state == STATE_EMPTY || state == STATE_LOADING || state == STATE_ERROR)) {
            return 1;
        }
        if (getCount() == 0) {
            return getCount();
        }
        return getCount() + (footerLoadCallback == null ? 0 : 1);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_EMPTY) {
            EmptyViewHolder emptyHolder = (EmptyViewHolder) holder;
            emptyHolder.img_empty.setImageResource(R.mipmap.img_net_empty);
        } else if (viewType == TYPE_LOADING) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progress_bar_light.setVisibility(View.VISIBLE);
        } else if (viewType == TYPE_ERROR) {
            ErrorViewHolder errorViewHolder = (ErrorViewHolder) holder;
            errorViewHolder.img_error.setImageResource(R.mipmap.img_net_error);
        } else if (viewType == TYPE_FOODTER) {
            loadMoreViewHolder = (LoadMoreViewHolder) holder;
            // 回调加载下一页
            if (getCount() > 5 && state != STATE_FOODTER && System.currentTimeMillis() - refreshingLoadMoreTime > 500) {
                setRefreshingLoadMore(true);
                footerLoadCallback.onFooterLoadCallback();
            } else {
                setRefreshingLoadMore(false);
            }
        } else if (footerLoadCallback == null || position < getCount()) {
            onBindHolder(holder, position);
        }
    }

    /**
     * 子类不推荐重写此方法，应该重写onCreateHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_EMPTY) {
            return new EmptyViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_empty, parent, false));
        } else if (viewType == TYPE_LOADING) {
            return new LoadingViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_loading, parent, false));
        } else if (viewType == TYPE_ERROR) {
            return new ErrorViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_error, parent, false));
        } else if (viewType == TYPE_FOODTER) {
            return new LoadMoreViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_loadmore, parent, false));
        }
        return onCreateHolder(parent, viewType);
    }

    // 显示/隐藏下一页
    public void setRefreshingLoadMore(boolean stateRefreshingLoadMore) {
        if (stateRefreshingLoadMore) {
            state = STATE_FOODTER;
        } else {
            state = STATE_DEFAULT;
        }

        if (loadMoreViewHolder != null) {
            if (stateRefreshingLoadMore) {
                loadMoreViewHolder.itemView.setVisibility(View.VISIBLE);
                // 连续回调的时间间隔不能小于500毫秒
                refreshingLoadMoreTime = System.currentTimeMillis();
            } else {
                loadMoreViewHolder.itemView.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 设置显示模式（正常，加载中，错误，空，下一页）
     * <p>
     * 使用规则：加载时调用STATE_LOADING，加载完成时判断是否调用TYPE_EMPTY，加载错误时调用TYPE_ERRPOR
     *
     * @param state
     */
    public void setState(int state) {
        this.state = state;
        notifyDataSetChanged();
    }

    class EmptyViewHolder extends ViewHolder {
        public ImageView img_empty;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            img_empty = itemView.findViewById(R.id.img_empty);
        }
    }

    class LoadingViewHolder extends ViewHolder {
        public ProgressBar progress_bar_light;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progress_bar_light = (ProgressBar) itemView.findViewById(R.id.progress_bar_light);
        }
    }

    class ErrorViewHolder extends ViewHolder {
        public ImageView img_error;

        public ErrorViewHolder(View itemView) {
            super(itemView);
            img_error = itemView.findViewById(R.id.img_error);
        }
    }

    class LoadMoreViewHolder extends ViewHolder {
        public ProgressBar progress_bar;
        public TextView text_loading;
        public View itemView, layout_loadmore;

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            layout_loadmore = itemView.findViewById(R.id.layout_loadmore);
            progress_bar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            text_loading = (TextView) itemView.findViewById(R.id.text_loading);
        }
    }

    public interface FooterLoadCallback {
        void onFooterLoadCallback();
    }
}