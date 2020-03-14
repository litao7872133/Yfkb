package com.yfkk.cardbag.ui.home;


import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.yfkk.cardbag.R;
import com.yfkk.cardbag.ui.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        InitView();
    }

    private void InitView() {
        getSwipeBackLayout().setEnableGesture(false);
        viewpager.setOffscreenPageLimit(4);
    }
}
