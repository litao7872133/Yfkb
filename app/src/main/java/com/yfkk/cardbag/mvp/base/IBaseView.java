package com.yfkk.cardbag.mvp.base;

import com.yfkk.cardbag.bean.BaseBean;
import com.yfkk.cardbag.bean.user.AppInitBean;
import com.yfkk.cardbag.bean.user.UserInfoBean;
import com.yfkk.cardbag.bean.user.UserLoginBean;

public interface IBaseView extends BaseView{

    // 基础配置
    default void dataAppInit(AppInitBean bean) {}

    // 刷新Token
    default void dataRefreshToken(UserLoginBean bean) {}

    // 用户资料
    default void dataUserInfo(UserInfoBean bean) {}

    // 更新用户资料
    default void dataUserInfoUpdate(BaseBean bean) {}

    // 还原用户备注头像/昵称
    default void dataUserRestoreNotes(BaseBean bean) {}

    // 还原群备注头像/昵称
    default void dataGroupRestoreNotes(BaseBean bean) {}

    // 短信验证
    default void dataSmsSend(BaseBean bean) {}

}
