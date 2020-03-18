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
 * Home ,购卡
 * <p>
 * Created by litao on 2019/2/27.
 */

public class HomeBuyFragment extends BaseFragment<HomePresenter, HomeView> implements HomeView {

    Unbinder unbinder;

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(getContext());
    }


    public static HomeBuyFragment getInstance() {
        HomeBuyFragment fragment = new HomeBuyFragment();
        // 这样的传值，在界面销毁后重启，或横竖屏切换时，数3据还会保存(注意，这里的序列化并不会重新创建对象)
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_buy, container, false);
        unbinder = ButterKnife.bind(this, view);

        InitView();
        InitData();
        InitEven();

        return view;
    }

    private void InitView() {

    }

    private void InitData() {

    }

    private void InitEven() {

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
