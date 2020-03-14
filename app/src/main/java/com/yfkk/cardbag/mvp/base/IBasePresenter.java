package com.yfkk.cardbag.mvp.base;

import android.content.Context;
import android.net.Uri;

import com.yfkk.cardbag.bean.BaseBean;
import com.yfkk.cardbag.bean.user.AppInitBean;
import com.yfkk.cardbag.bean.user.UserInfoBean;
import com.yfkk.cardbag.bean.user.UserLoginBean;
import com.yfkk.cardbag.cache.AppInitRepository;
import com.yfkk.cardbag.cache.PersistentData;
import com.yfkk.cardbag.config.Config;
import com.yfkk.cardbag.utils.ToastUtils;

import rx.Subscriber;

/**
 * 负责完成View(Activity,Fragment)于Model(数据)间的交互
 * <p>
 * 和View关联
 * <p>
 * Created by litao on 2018/1/16.
 */
public abstract class IBasePresenter<T> extends BasePresenter<T> {

    public IBasePresenter(Context context) {
        super(context);
    }

    /**
     * 基础配置
     */
    public void postAppInit() {
        invoke("postAppInit", false, IBaseModel.getInstance().appInit(), new Subscriber<AppInitBean>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(AppInitBean bean) {
                if (Config.codeSuccess == bean.code) {
                    // 保存
                    AppInitRepository.saveAppInit(bean);
                    // 显示数据
                    ((IBaseView) getView()).dataAppInit(bean);
                } else {
                    // 请求异常
                    ToastUtils.makeText(bean.message);
                }
            }
        });
    }

    /**
     * 刷新Token
     */
    public void postRefreshToken() {
        invoke("postRefreshToken", false, IBaseModel.getInstance().refreshToken(), new Subscriber<UserLoginBean>() {

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
                    ((IBaseView) getView()).dataRefreshToken(bean);
                } else {
                    // 请求异常
                    ToastUtils.makeText(bean.message);
                }
            }
        });
    }

    /**
     * 用户资料
     * <p>
     * 有device_user_id参数时，返回is_near_relation，is_hide_usertel，identity
     */
    public void postUserInfo(String user_id, String usertel, String device_user_id, boolean isRetryWhen) {
        invoke("postUserInfo" + user_id + usertel, isRetryWhen, IBaseModel.getInstance().userInfo(user_id, usertel, device_user_id), new Subscriber<UserInfoBean>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(UserInfoBean bean) {
                if (Config.codeSuccess == bean.code) {
                    // 显示数据
                    ((IBaseView) getView()).dataUserInfo(bean);
                } else {
                    // 请求异常
                    ToastUtils.makeText(bean.message);
                }
            }
        });
    }

    /**
     * 更新用户资料
     */
    public void postUserInfoUpdate(String user_id, String device_user_id, String username, String remarkname, String headimgurl, String remarkavatar, String birthday, String gender
            , String isNearRelation, String isLifeCode, String isHideUsertel, boolean isNewUser) {
        invoke("postUserInfoUpdate", false, IBaseModel.getInstance().userInfoUpdate(user_id, device_user_id, username, remarkname, headimgurl, remarkavatar, birthday, gender
                , isNearRelation, isLifeCode, isHideUsertel, isNewUser), new Subscriber<BaseBean>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(BaseBean bean) {
                if (Config.codeSuccess == bean.code) {
                    // 显示数据
                    ((IBaseView) getView()).dataUserInfoUpdate(bean);
                    ToastUtils.makeText(bean.message);
                } else {
                    // 请求异常
                    ToastUtils.makeText(bean.message);
                }
            }
        });
    }

}
