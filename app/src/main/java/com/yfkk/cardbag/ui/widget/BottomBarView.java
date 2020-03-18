package com.yfkk.cardbag.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.yfkk.cardbag.R;

import java.util.ArrayList;

/**
 * 主页底部导航栏
 */
public class BottomBarView extends LinearLayout {

    OnNavigationItemSelectedListener onNavigationItemSelectedListener;

    int defaultIndex = 0;
    ArrayList<View> itemViews = new ArrayList<>();

    public BottomBarView(Context context) {
        super(context);
        InitView();
    }

    public BottomBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitView();
    }

    private void InitView() {
        setOrientation(LinearLayout.HORIZONTAL);
    }

    public void addItem(int resources, String title) {
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.item_bottom_bar, null);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        layout.setLayoutParams(layoutParams);
        final ImageView imageView = layout.findViewById(R.id.img_icon);
        TextView textTitle = layout.findViewById(R.id.text_title);
        imageView.setImageResource(resources);
        textTitle.setText(title);
        final int index = getChildCount();
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onNavigationItemSelectedListener != null) {
                    onNavigationItemSelectedListener.onNavigationItemSelected(index);
                    setSelectedItem(index);
                }
            }
        });
        addView(layout);
        itemViews.add(layout);
        notifyView();
    }

    private void notifyView() {
        for (int i = 0; i < itemViews.size(); i++) {
            View layout = itemViews.get(i);
            ImageView imageView = layout.findViewById(R.id.img_icon);
            TextView textTitle = layout.findViewById(R.id.text_title);
            if (i == defaultIndex) {
                imageView.setEnabled(false);
                textTitle.setTextColor(getResources().getColor(R.color.text_black));
            } else {
                imageView.setEnabled(true);
                textTitle.setTextColor(getResources().getColor(R.color.text_hint));
            }
        }
    }

    public void setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener onNavigationItemSelectedListener) {
        this.onNavigationItemSelectedListener = onNavigationItemSelectedListener;
    }

    public void setSelectedItem(int index) {
        defaultIndex = index;
        notifyView();
    }

    public void setHint(int index, int hintNum) {
        View layout = itemViews.get(index);
        View hintView = layout.findViewById(R.id.hint);
        if (hintNum <= 0) {
            hintView.setVisibility(View.INVISIBLE);
        } else {
            hintView.setVisibility(View.VISIBLE);
        }
    }

    public interface OnNavigationItemSelectedListener {
        boolean onNavigationItemSelected(int index);
    }
}
