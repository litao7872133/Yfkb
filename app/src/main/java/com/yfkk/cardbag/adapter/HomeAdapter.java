package com.yfkk.cardbag.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yfkk.cardbag.ui.fragment.HomeBuyFragment;
import com.yfkk.cardbag.ui.fragment.HomeCardFragment;
import com.yfkk.cardbag.ui.fragment.HomeMainFragment;
import com.yfkk.cardbag.ui.fragment.HomeMyFragment;

/**
 * 主页
 * <p>
 * Created by litao on 2019/2/27.
 */
public class HomeAdapter extends FragmentPagerAdapter {

    public HomeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return HomeMainFragment.getInstance();
            case 1:
                return HomeBuyFragment.getInstance();
            case 2:
                return HomeCardFragment.getInstance();
            case 3:
                return HomeMyFragment.getInstance();
        }
        return HomeMainFragment.getInstance();
    }

    @Override
    public int getItemPosition(Object object) {
        // 最简单解决 notifyDataSetChanged() 页面不刷新问题的方法
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
