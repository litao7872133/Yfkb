package com.yfkk.cardbag.utils;

import android.widget.TextView;

import com.yfkk.cardbag.MainApplication;
import com.yfkk.cardbag.R;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SmsUtils {
    public final int MAX_DURATION = 60000;
    private static long startupTime; // 启动时间

    private Subscription subscribe;
    private long currentTime; // 当前进行时间

    public void Init(TextView textView) {
        if (System.currentTimeMillis() - startupTime < MAX_DURATION) {
            subscribe(textView);
        } else {
            textView.setBackgroundResource(R.drawable.bt_circle_stroke_primary);
            textView.setTextColor(MainApplication.getInstance().getResources().getColor(R.color.colorPrimary));
            textView.setText("获取验证码");
            textView.setEnabled(true);
        }
    }

    /**
     * 验证码倒计时
     * <p>
     * 必须在界面销毁时解绑
     */
    public void subscribe(TextView textView) {
        if (System.currentTimeMillis() - startupTime < MAX_DURATION) {
            currentTime = MAX_DURATION - (System.currentTimeMillis() - startupTime);
        } else {
            currentTime = MAX_DURATION - 1000;
            startupTime = System.currentTimeMillis();
        }

        textView.setEnabled(false);
        textView.setText(currentTime / 1000 + "s后重发");
        textView.setTextColor(MainApplication.getInstance().getResources().getColor(R.color.text_prompt));
        textView.setBackgroundResource(R.drawable.bt_circle_stroke_gray);
        subscribe = Observable.interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        unsubscribe();
                        textView.setBackgroundResource(R.drawable.bt_circle_stroke_primary);
                        textView.setTextColor(MainApplication.getInstance().getResources().getColor(R.color.colorPrimary));
                        textView.setText("获取验证码");
                        textView.setEnabled(true);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        currentTime -= 1000;
                        if (currentTime < 0) {
                            textView.setBackgroundResource(R.drawable.bt_circle_stroke_primary);
                            textView.setTextColor(MainApplication.getInstance().getResources().getColor(R.color.colorPrimary));
                            textView.setText("获取验证码");
                            textView.setEnabled(true);
                            unsubscribe();
                        } else {
                            textView.setEnabled(false);
                            textView.setText(currentTime / 1000 + "s后重发");
                            textView.setTextColor(MainApplication.getInstance().getResources().getColor(R.color.text_prompt));
                            textView.setBackgroundResource(R.drawable.bt_circle_stroke_gray);
                        }
                    }
                });
    }

    public void unsubscribe() {
        if (subscribe != null) {
            subscribe.unsubscribe();
        }
    }

}
