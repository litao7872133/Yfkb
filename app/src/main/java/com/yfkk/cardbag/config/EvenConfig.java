package com.yfkk.cardbag.config;

public interface EvenConfig {

    int EVENT_USER_INFO = 1001; // 用户信息更新
    int EVENT_EXIT_LOGING = 1010; // 退出登录

    /**
     * 服务器推送指令
     */
    String PUSH_TAKEOVER = "takeover";  // 接管设备

}
