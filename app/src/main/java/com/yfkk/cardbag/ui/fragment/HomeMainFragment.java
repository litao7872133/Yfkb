package com.yfkk.cardbag.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yfkk.cardbag.R;
import com.yfkk.cardbag.adapter.HomeMainAdapter;
import com.yfkk.cardbag.config.EvenConfig;
import com.yfkk.cardbag.event.Event;
import com.yfkk.cardbag.event.RxBus;
import com.yfkk.cardbag.mvp.presenter.HomePresenter;
import com.yfkk.cardbag.mvp.view.HomeView;

import java.util.ArrayList;

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

    Unbinder unbinder;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HomeMainAdapter(getContext());
        recyclerView.setAdapter(adapter);

    }

    private void InitData() {
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
