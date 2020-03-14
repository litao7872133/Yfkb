package com.yfkk.cardbag.mvp.presenter;

import android.content.Context;

import com.yfkk.cardbag.bean.user.UserLoginBean;
import com.yfkk.cardbag.bean.user.UserRegisterBean;
import com.yfkk.cardbag.cache.PersistentData;
import com.yfkk.cardbag.config.Config;
import com.yfkk.cardbag.mvp.base.IBasePresenter;
import com.yfkk.cardbag.mvp.model.HomeModel;
import com.yfkk.cardbag.mvp.view.HomeView;
import com.yfkk.cardbag.utils.ToastUtils;

import rx.Subscriber;

public class HomePresenter extends IBasePresenter<HomeView> {

    public HomePresenter(Context context) {
        super(context);
    }

    /**
     * 登录
     */
    public void postUserLogin(String usertel, String verifycode) {
        invoke("postUserLogin", false, HomeModel.getInstance().userLogin(usertel, verifycode), new Subscriber<UserLoginBean>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(UserLoginBean bean) {
                if (Config.codeSuccess == bean.code) {
                    // 保存token
                    PersistentData.setToken(bean.data.access_user_token);
                    // 显示数据
                    getView().dataUserLogin(bean);
                    ToastUtils.makeText(bean.message);
                } else {
                    // 请求异常
                    ToastUtils.makeText(bean.message);
                }
            }
        });
    }

    /**
     * 注册
     */
    public void postUserRegister(String usertel, String verifycode, String username, String gender, String headimgurl) {
        invoke("postUserRegister", false, HomeModel.getInstance().userRegister(usertel, verifycode, username, gender, headimgurl), new Subscriber<UserRegisterBean>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(UserRegisterBean bean) {
                if (Config.codeSuccess == bean.code) {
                    // 保存token
                    PersistentData.setToken(bean.data.access_user_token);
                    // 显示数据
                    getView().dataUserRegister(bean);
                    ToastUtils.makeText(bean.message);
                } else {
                    // 请求异常
                    ToastUtils.makeText(bean.message);
                }
            }
        });
    }

}
