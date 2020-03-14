package com.yfkk.cardbag.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;

import com.yfkk.cardbag.mvp.base.BasePresenter;
import com.yfkk.cardbag.ui.widget.CustomProgressDialog;
import com.yfkk.cardbag.utils.SystemBarUtils;
import com.yfkk.cardbag.utils.StringUtils;
import com.yfkk.cardbag.utils.ToastUtils;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 通用的网络数据刷新UI的Activity
 * <p>
 * Created by litao on 2019/2/27.
 */
public abstract class BaseActivity<T extends BasePresenter<V>, V> extends SwipeBackActivity {

    protected T mPresenter;
    protected CompositeSubscription mCompositeSubscription;

    protected SwipeBackLayout mSwipeBackLayout;
    protected Handler handlerDialog = new Handler();
    protected ProgressDialog loadingDialog;
    protected boolean isCloseDialogAfterCompletion = true; // 网络请求结束后，自动关闭弹窗

    /**
     * 重要：如果需要网络请求，必须重写此方法
     */
    protected T createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 沉浸状态栏
        new SystemBarUtils(this).setDarkStyte();
        // 侧边滑动关闭范围
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeSize(StringUtils.dpToPx(this, 32));

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

        loadingDialog = new CustomProgressDialog(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 友盟基础数据统计
//        MobclickAgent.onPageStart(getClass().getName());
//        MobclickAgent.onResume(this);//统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 友盟基础数据统计
//        MobclickAgent.onPageEnd(getClass().getName());
//        MobclickAgent.onPause(this);

        // 替代onDestroy（onDestroy并不一定会准时执行，所以用这个方法替代）
        if (isFinishing()) {
            destroy();
        }
    }

    /**
     * 用destroy 替代 onDestroy
     * <p>
     * 不建议用onDestroy去管理activity的生命周期，销毁广播，线程等操作，因为onDestroy并不一定会准时执行
     */
    private boolean isDestroyed = false;

    protected void destroy() {
        isDestroyed = true;
        if (mPresenter != null) {
            mPresenter.detach();
            if (this.mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
                this.mCompositeSubscription.unsubscribe();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isDestroyed) {
            destroy();
        }
    }

    @Override
    public void finish() {
        handlerDialog.removeCallbacks(dialogRunnable);
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        super.finish();
    }

    /**
     * 网络异常回掉
     */
    public void onError(String action, Throwable e) {
        ToastUtils.makeThrowable(action, e);
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 网络请求完成回调
     */
    public void onCompleted() {
        if (isCloseDialogAfterCompletion && loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    Runnable dialogRunnable = new Runnable() {
        @Override
        public void run() {
            dismissLoadingDialog();
        }
    };

    /**
     * 弹窗相关
     */
    public void showLoadingDialog() {
        loadingDialog.show();
        handlerDialog.removeCallbacks(dialogRunnable);
        handlerDialog.postDelayed(dialogRunnable, 10000);
    }

    public void dismissLoadingDialog() {
        loadingDialog.dismiss();
        handlerDialog.removeCallbacks(dialogRunnable);
    }

    protected void setCloseDialogAfterCompletion(boolean isCloseDialogAfterCompletion) {
        this.isCloseDialogAfterCompletion = isCloseDialogAfterCompletion;
    }
}
