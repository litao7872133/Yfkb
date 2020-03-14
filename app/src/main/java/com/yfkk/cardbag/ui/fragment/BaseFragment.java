package com.yfkk.cardbag.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.yfkk.cardbag.mvp.base.BasePresenter;
import com.yfkk.cardbag.ui.widget.CustomProgressDialog;
import com.yfkk.cardbag.utils.ToastUtils;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 通用的网络数据刷新UI的Fragment
 * <p>
 * Created by litao on 2019/2/27.
 */
public abstract class BaseFragment<T extends BasePresenter<V>, V> extends Fragment {
    protected T mPresenter;
    private CompositeSubscription mCompositeSubscription;
    // 等待窗口
    protected ProgressDialog waittingDialog;

    /**
     * 重要：如果需要网络请求，必须重写此方法
     *
     * @return
     */
    protected T createPresenter() {
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attach((V) this);
            mPresenter.setLifeSubscription(new BasePresenter.LifeSubscription() {
                @Override
                public void bindSubscription(Subscription subscription) {
                    if (mCompositeSubscription == null) {
                        mCompositeSubscription = new CompositeSubscription();
                    }
                    mCompositeSubscription.add(subscription);
                }
            });
        }

        waittingDialog = new CustomProgressDialog(getContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detach();
            if (this.mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
                this.mCompositeSubscription.unsubscribe();
            }
        }
        if (waittingDialog != null && waittingDialog.isShowing()) {
            waittingDialog.dismiss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint() && isVisible()) {
            onHide();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint() && isVisible()) {
            onShow();
        }
    }

    public void onError(String action, Throwable e) {
        ToastUtils.makeThrowable(action, e);
        if (waittingDialog != null && waittingDialog.isShowing()) {
            waittingDialog.dismiss();
        }
    }

    public void onCompleted() {
        if (waittingDialog != null && waittingDialog.isShowing()) {
            waittingDialog.dismiss();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisible()) {
            if (isVisibleToUser) {
                //相当于Fragment的onResume
                onShow();
            } else {
                //相当于Fragment的onPause
                onHide();
            }
        }
    }

    public void onShow() {
    }

    public void onHide() {
    }

}

