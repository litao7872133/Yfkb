package com.yfkk.cardbag.cache;

import com.google.gson.Gson;
import com.yfkk.cardbag.bean.user.UserInfoBean;
import com.yfkk.cardbag.utils.SPUtils;
import com.yfkk.cardbag.utils.StringUtils;

/**
 * 我的个人资料
 */
public class UserInfoRepository {
    // 用户资料
    private static UserInfoBean userInfoBean;

    public static UserInfoBean getUserInfo() {
        if (userInfoBean == null) {
            try {
                userInfoBean = new Gson().fromJson(SPUtils.getString("SP_USER_INFO", ""), UserInfoBean.class);
            } catch (Exception e) {
            }
            if (userInfoBean == null) {
                return new UserInfoBean();
            }
        }
        return userInfoBean;
    }

    public static void saveUserInfo(UserInfoBean bean) {
        userInfoBean = bean;
        SPUtils.saveString("SP_USER_INFO", userInfoBean == null ? null : new Gson().toJson(userInfoBean));
    }

    public static void clearUserInfo() {
        userInfoBean = null;
        SPUtils.saveString("SP_USER_INFO", null);
        PersistentData.setToken(null);
    }

}
