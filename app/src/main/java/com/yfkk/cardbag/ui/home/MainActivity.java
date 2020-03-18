package com.yfkk.cardbag.ui.home;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.yfkk.cardbag.R;
import com.yfkk.cardbag.adapter.HomeAdapter;
import com.yfkk.cardbag.config.EvenConfig;
import com.yfkk.cardbag.event.Event;
import com.yfkk.cardbag.event.RxBus;
import com.yfkk.cardbag.log.LogUtils;
import com.yfkk.cardbag.ui.BaseActivity;
import com.yfkk.cardbag.ui.widget.BottomBarView;
import com.yfkk.cardbag.utils.AnimatorUtils;
import com.yfkk.cardbag.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.navigation)
    BottomBarView navigation;

    Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        InitView();
        InitEven();
    }

    private void InitView() {
        getSwipeBackLayout().setEnableGesture(false);
        viewpager.setAdapter(new HomeAdapter(getSupportFragmentManager()));
        viewpager.setOffscreenPageLimit(4);

        navigation.addItem(R.drawable.selector_navigation_main, "首页");
        navigation.addItem(R.drawable.selector_navigation_buy, "购卡");
        navigation.addItem(R.drawable.selector_navigation_card, "卡包");
        navigation.addItem(R.drawable.selector_navigation_my, "我的");

        navigation.setOnNavigationItemSelectedListener(new BottomBarView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(int item) {
                viewpager.setCurrentItem(item, false);
                return false;
            }
        });
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                InitCurrentView();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void InitEven() {
        // 事件
        subscription = RxBus.getInstance().toObservable(Event.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    LogUtils.e("事件 event.code : " + event.code);
                    switch (event.code) {
                        case EvenConfig.EVENT_EXIT_LOGING: // 退出登录
                            break;
                        case EvenConfig.EVENT_HOME_BOTTOMBAR_STATE: // 主页底部栏状态（隐藏/显示）
                            if ((Integer) event.object == View.VISIBLE) {
                                if (navigation.getVisibility() != View.VISIBLE) {
                                    navigation.setVisibility(View.VISIBLE);
                                    AnimatorUtils.startAnimatorTranslateY(navigation, StringUtils.dpToPx(getBaseContext(), 80), 0, 0);
                                }
                            } else {
                                if (navigation.getVisibility() == View.VISIBLE) {
                                    navigation.setVisibility(View.INVISIBLE);
                                    AnimatorUtils.startAnimatorTranslateY(navigation, 0, StringUtils.dpToPx(getBaseContext(), 80), 0);
                                }
                            }
                            break;
                    }
                });
    }


    private void InitCurrentView() {
        navigation.setSelectedItem(viewpager.getCurrentItem());
        switch (viewpager.getCurrentItem()) {
            case 0:
                break;
        }
    }

    @Override
    protected void destroy() {
        super.destroy();
        subscription.unsubscribe();
    }


}
