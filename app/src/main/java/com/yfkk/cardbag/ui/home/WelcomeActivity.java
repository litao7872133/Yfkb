package com.yfkk.cardbag.ui.home;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.yfkk.cardbag.R;
import com.yfkk.cardbag.ui.BaseActivity;

public class WelcomeActivity extends BaseActivity {

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        getSwipeBackLayout().setEnableGesture(false);

        handler.postDelayed(new Runnable() { // 3 秒没有数据，强制进入下一个页面
            @Override
            public void run() {
                nextActivity();
            }
        }, 3000);
    }

    private void nextActivity() {
        finish();
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
    }
}
