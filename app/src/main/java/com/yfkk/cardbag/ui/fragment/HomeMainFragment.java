package com.yfkk.cardbag.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yfkk.cardbag.R;
import com.yfkk.cardbag.adapter.HomeMainAdapter;
import com.yfkk.cardbag.config.EvenConfig;
import com.yfkk.cardbag.event.Event;
import com.yfkk.cardbag.event.RxBus;
import com.yfkk.cardbag.log.LogUtils;
import com.yfkk.cardbag.mvp.presenter.HomePresenter;
import com.yfkk.cardbag.mvp.view.HomeView;
import com.yfkk.cardbag.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Home ,首页
 * <p>
 * Created by litao on 2019/2/27.
 */

public class HomeMainFragment extends BaseFragment<HomePresenter, HomeView> implements HomeView {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.text_place)
    TextView textPlace;
    @BindView(R.id.img_bg)
    ImageView imgBg;
    @BindView(R.id.img_logo)
    ImageView imgLogo;
    @BindView(R.id.img_search)
    ImageView imgSearch;
    @BindView(R.id.layout_toolbar)
    RelativeLayout layoutToolbar;

    Unbinder unbinder;

    int dyCumulative = 0;
    HomeMainAdapter adapter;
    Subscription subscription;

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(getContext());
    }


    public static HomeMainFragment getInstance() {
        HomeMainFragment fragment = new HomeMainFragment();
        // 这样的传值，在界面销毁后重启，或横竖屏切换时，数3据还会保存(注意，这里的序列化并不会重新创建对象)
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_main, container, false);
        unbinder = ButterKnife.bind(this, view);

        InitView();
        InitData();
        InitEven();

        return view;
    }

    private void InitView() {
        // 瀑布流布局
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration gridItemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                //不为banner类型
                if (parent.getChildAdapterPosition(view) != 0) {
                    StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
                    int spanIndex = layoutParams.getSpanIndex();
                    int divider = StringUtils.dpToPx(getContext(), 12);
                    outRect.top = divider;
                    if (spanIndex == 0) {
                        // left
                        outRect.left = divider;
                        outRect.right = divider / 2;
                    } else {
                        outRect.right = divider;
                        outRect.left = divider / 2;
                    }
                }
            }
        };
        recyclerView.addItemDecoration(gridItemDecoration);

        adapter = new HomeMainAdapter(getContext());
        recyclerView.setAdapter(adapter);
    }

    private void InitData() {
        float maxTopFirst = StringUtils.dpToPx(getContext(), 68); // 第一项滚动最大位置（默认位置）
        float maxTop = StringUtils.dpToPx(getContext(), 88); // 搜索图片默认位置
        float minTop = StringUtils.dpToPx(getContext(), 28); // 搜索图片居顶位置
        float maxLeft = StringUtils.dpToPx(getContext(), 88);
        float minLeft = StringUtils.dpToPx(getContext(), 16);
        float maxHeight = StringUtils.dpToPx(getContext(), 182);
        float minHeight = StringUtils.dpToPx(getContext(), 100);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                dyCumulative += dy;
                if (dyCumulative > maxTopFirst) {
                    dyCumulative = 0;
                    RxBus.getInstance().post(new Event(EvenConfig.EVENT_HOME_BOTTOMBAR_STATE, View.INVISIBLE));
                } else if (dyCumulative < -maxTopFirst) {
                    dyCumulative = 0;
                    RxBus.getInstance().post(new Event(EvenConfig.EVENT_HOME_BOTTOMBAR_STATE, View.VISIBLE));
                }

                StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                int[] into = new int[2];
                into = manager.findFirstVisibleItemPositions(into);
                if (into[0] != 0) {
                    return;
                }

                int top = recyclerView.getChildAt(0).getTop();
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imgSearch.getLayoutParams();
                RelativeLayout.LayoutParams layoutParamsImgBg = (RelativeLayout.LayoutParams) imgBg.getLayoutParams();
                if (top <= 0) {
                    layoutParams.leftMargin = (int) maxLeft;
                    layoutParams.topMargin = (int) minTop;
                    imgLogo.setAlpha(0f);
                    layoutParamsImgBg.height = (int) minHeight;

                } else {
                    layoutParams.leftMargin = (int) (maxLeft - top / maxTopFirst * (maxLeft - minLeft));
                    layoutParams.topMargin = (int) (minTop + top / maxTopFirst * (maxTop - minTop));
                    imgLogo.setAlpha(top / maxTopFirst);
                    layoutParamsImgBg.height = (int) (minHeight + top / maxTopFirst * (maxHeight - minHeight));
                }
                imgSearch.setLayoutParams(layoutParams);
                imgBg.setLayoutParams(layoutParamsImgBg);
                LogUtils.e("i ： " + top + " , " + maxTopFirst);
            }
        });
    }

    private void InitEven() {
        // 事件
        subscription = RxBus.getInstance().toObservable(Event.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    if (event.code == EvenConfig.EVENT_USER_INFO) {

                    }
                });
    }

    @Override
    public void onShow() {
        super.onShow();
        RxBus.getInstance().post(new Event(EvenConfig.EVENT_HOME_BOTTOMBAR_STATE, View.VISIBLE));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
