package com.yfkk.cardbag.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by litao on 2020/3/13.
 */
public class HomeMainAdapter extends BaseAdapter {

    Context context;

    public HomeMainAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemType(int position) {
        return 0;
    }
}
