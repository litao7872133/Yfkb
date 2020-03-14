package com.yfkk.cardbag.mvp.base;

import android.content.Context;

import com.yfkk.cardbag.bean.BaseBean;
import com.yfkk.cardbag.config.Config;
import com.yfkk.cardbag.config.EvenConfig;
import com.yfkk.cardbag.event.Event;
import com.yfkk.cardbag.event.RxBus;

import java.lang.ref.WeakReference;
import java.util.HashSet;

import rx.Subscriber;
import rx.Subscription;

/**
 * 负责完成View(Activity,Fragment)于Model(数据)间的交互
 * <p>
 * 和View关联
 * <p>
 * Created by litao on 2018/1/16.
 */
public abstract class BasePresenter<T> {

    private WeakReference<T> mViewRef;
    private Context mContext;
    private HashSet<String> mLoadingRetryWhen = new HashSet<>(); // 不允许出现多个RetryWhen的请求
    private LifeSubscription lifeSubscription;

    public void setLifeSubscription(LifeSubscription lifeSubscription) {
        this.lifeSubscription = lifeSubscription;
    }

    protected <T> void invoke(final String tag, boolean isRetryWhen, rx.Observable<T> observable, final Subscriber<T> subscriber) {
        if (isRetryWhen) {
            if (mLoadingRetryWhen.contains(tag)) { // 不允许出现多个RetryWhen的请求
                return;
            }
            mLoadingRetryWhen.add(tag);
        }

        BaseModel.invoke(lifeSubscription, observable, new Subscriber<T>() {

            @Override
            public void onCompleted() {
                mLoadingRetryWhen.remove(tag);
                subscriber.onCompleted();
                if (getView() instanceof BaseView) {
                    ((BaseView) getView()).onCompleted();
                }
            }

            @Override
            public void onError(Throwable e) {
                mLoadingRetryWhen.remove(tag);
                subscriber.onError(e);
                if (getView() instanceof BaseView) {
                    ((BaseView) getView()).onError(tag, e);
                }
            }

            @Override
            public void onNext(T bean) {
                mLoadingRetryWhen.remove(tag);
                subscriber.onNext(bean);
                // token过期的
                if (bean instanceof BaseBean) {
                    if (Config.codeTokenOverdue == ((BaseBean) bean).code || Config.codeTokenLose == ((BaseBean) bean).code) {
                        // 退出登录
                        RxBus.getInstance().post(new Event(EvenConfig.EVENT_EXIT_LOGING));
                    }
                }
            }
        }, isRetryWhen ? new BaseModel.RetryWhenInterface() {
            @Override
            public void next(int retryCount, Throwable e) {
                if (retryCount == 1) { // 我们只在第一次请求失败的时候，提醒用户。后台重复请求时，我们不做任何处理，直到它请求成功
                    subscriber.onError(e);
                    if (getView() instanceof BaseView) {
                        ((BaseView) getView()).onError(tag, e);
                    }
                }
            }
        } : null);
    }

    public BasePresenter(Context context) {
        this.mContext = context;
    }

    // 关联
    public void attach(T view) {
        mViewRef = new WeakReference<T>(view);
    }

    // 解除关联
    public void detach() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    public T getView() {
        return mViewRef == null ? null : mViewRef.get();
    }

    public interface LifeSubscription {
        void bindSubscription(Subscription subscription);
    }
}
