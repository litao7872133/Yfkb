package com.yfkk.cardbag.config;

import com.yfkk.cardbag.cache.PersistentData;

/**
 * 网络请求的接口配置
 * <p>
 * Created by litao on 2019/03/10.
 */

public class UrlConfig {

    static {
        // 初始化域名
        HOST_URL = PersistentData.getHostUrl();
    }

    // 域名
    public static String HOST_URL; // 当前域名

    /**
     * 用户
     */
    public final static String appInit = "api/app/init";// 基础配置
    public final static String refreshToken = "api/app/refresh_token";// 刷新token
    public final static String userRegister = "api/app/user/register"; // 注册
    public final static String userLogin = "api/app/user/login"; // 登录
    public final static String userInfo = "api/app/user/info"; // 用户信息
    public final static String userInfoUpdate = "api/app/user/update_userinfo"; // 更新用户信息


    /**
     * 短信
     */
    public final static String smsSend = "api/app/sms"; // 短信验证码

    /**
     * 第三方平台
     */
    public final static String socialiteAdd = "socialite/add"; // 用户绑定第三方平台
    public final static String socialiteGet = "socialite/get"; // 获取用户绑定第三方平台
    public final static String socialiteDel = "socialite/del"; // 删除第三方平台


}
