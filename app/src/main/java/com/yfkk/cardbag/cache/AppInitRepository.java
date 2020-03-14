package com.yfkk.cardbag.cache;

import com.google.gson.Gson;
import com.yfkk.cardbag.bean.user.AppInitBean;
import com.yfkk.cardbag.utils.SPUtils;

/**
 * APP基本信息
 */
public class AppInitRepository {

    private static AppInitBean appInitBean;

    public static AppInitBean getAppInit() {
        if (appInitBean == null) {
            try {
                appInitBean = new Gson().fromJson(SPUtils.getString("SP_APP_INIT", ""), AppInitBean.class);
            } catch (Exception e) {
            }
        }
        return appInitBean;
    }

    public static void saveAppInit(AppInitBean bean) {
        appInitBean = bean;
        SPUtils.saveString("SP_APP_INIT", appInitBean == null ? null : new Gson().toJson(appInitBean));
    }

    public static void clearAppInit() {
        appInitBean = null;
        SPUtils.saveString("SP_APP_INIT", null);
    }

}
