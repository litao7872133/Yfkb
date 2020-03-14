package com.yfkk.cardbag.mvp.view;

import com.yfkk.cardbag.bean.BaseBean;
import com.yfkk.cardbag.bean.user.UserLoginBean;
import com.yfkk.cardbag.bean.user.UserRegisterBean;
import com.yfkk.cardbag.mvp.base.IBaseView;

public interface HomeView extends IBaseView {

    // 登录
    default void dataUserLogin(UserLoginBean bean) {}

    // 注册
    default void dataUserRegister(UserRegisterBean bean) {}

    // 注销
    default void dataUserCancellation(BaseBean bean) {}

}