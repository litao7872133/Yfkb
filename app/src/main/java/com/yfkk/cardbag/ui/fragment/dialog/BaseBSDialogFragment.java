package com.yfkk.cardbag.ui.fragment.dialog;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;

import com.yfkk.cardbag.mvp.base.BasePresenter;
import com.yfkk.cardbag.ui.widget.CustomProgressDialog;
import com.yfkk.cardbag.utils.ToastUtils;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 底部弹窗
 * <p>
 * Created by litao on 2019/2/27.
 */
public abstract class BaseBSDialogFragment<T extends BasePresenter<V>, V> extends BottomSheetDialogFragment {

    protected T mPresenter;
    private CompositeSubscription mCompositeSubscription;

    /**
     * 重要：如果需要网络请求，必须重写此方法
     *
     * @return
     */
    protected T createPresenter() {
        return null;
    }

    protected DismissListener dismissListener;
    // 等待窗口
    public ProgressDialog waittingDialog;

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
    public void show(FragmentManager manager, String tag) {
        try {
            // 不可控的，可能出现因生命周期导致的报错（java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState）
            super.show(manager, tag);
        } catch (Exception e) {
        }
    }

    /**
     * 弹窗销毁后，数据监听
     *
     * @param dismissListener
     * @return
     */
    public BaseBSDialogFragment setDismissListener(DismissListener dismissListener) {
        this.dismissListener = dismissListener;
        return this;
    }

    public interface DismissListener {
        void dismissDialog(Object obj);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

//    /**
//     *  创建自定义内容弹窗
//     * @param inflater
//     * @param container
//     * @param savedInstanceState
//     * @return
//     */
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }


    //    /**
//     * 创建替代传统的 Dialog 对话框的场景
//     *
//     * @param savedInstanceState
//     * @return
//     */
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("注意：")
//                .setMessage("是否退出应用？")
//                .setPositiveButton("确定", null)
//                .setNegativeButton("取消", null)
//                .setCancelable(false);
//        return builder.create();
//    }
}

