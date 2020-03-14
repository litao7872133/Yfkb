package com.yfkk.cardbag.mvp.base;

import com.yfkk.cardbag.bean.BaseBean;

import com.yfkk.cardbag.bean.user.AppInitBean;
import com.yfkk.cardbag.bean.user.UserInfoBean;
import com.yfkk.cardbag.bean.user.UserLoginBean;
import com.yfkk.cardbag.config.UrlConfig;
import com.yfkk.cardbag.utils.StringUtils;
import com.google.gson.Gson;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 数据相关
 * <p>
 * Created by litao on 2018/1/16.
 */

public class IBaseModel extends BaseModel {

    private BaseApi api;

    public static IBaseModel getInstance() {
        return new IBaseModel();
    }

    public IBaseModel() {
        super();
        api = retrofit.create(BaseApi.class);
    }

    public IBaseModel(String url) {
        super(url);
        api = retrofit.create(BaseApi.class);
    }

    public interface BaseApi {
        @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
        @POST(UrlConfig.appInit)
        Observable<AppInitBean> appInit(@Body RequestBody requestBody);

        @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
        @POST(UrlConfig.refreshToken)
        Observable<UserLoginBean> refreshToken(@Body RequestBody requestBody);

        @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
        @POST(UrlConfig.userInfo)
        Observable<UserInfoBean> userInfo(@Body RequestBody requestBody);

        @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
        @POST(UrlConfig.userInfoUpdate)
        Observable<BaseBean> userInfoUpdate(@Body RequestBody requestBody);

    }

    /**
     * 基础配置
     *
     * @return
     */
    public Observable<AppInitBean> appInit() {
        map.clear();

        Gson gson = new Gson();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=UTF-8"), gson.toJson(map));
        return api.appInit(body);
    }

    /**
     * 刷新Token
     *
     * @return
     */
    public Observable<UserLoginBean> refreshToken() {
        map.clear();

        Gson gson = new Gson();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=UTF-8"), gson.toJson(map));
        return api.refreshToken(body);
    }


    /**
     * 获取用户信息
     * <p>
     * 有device_user_id参数时，返回is_near_relation，is_hide_usertel，identity
     *
     * @return
     */
    public Observable<UserInfoBean> userInfo(String user_id, String usertel, String device_user_id) {
        map.clear();
        map.put("user_id", user_id);
        map.put("usertel", usertel);
        map.put("device_user_id", device_user_id);

        Gson gson = new Gson();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=UTF-8"), gson.toJson(map));
        return api.userInfo(body);
    }

    /**
     * 更新用户信息
     * isNearRelation 是否近亲好友 0: 非亲近好友， 1：亲近好友
     * isHideUsertel 是否隐藏手机号 0: 不隐藏，1：隐藏
     *
     * @return
     */
    public Observable<BaseBean> userInfoUpdate(String user_id, String device_user_id, String username, String remarkname, String headimgurl, String remarkavatar, String birthday, String gender
            , String isNearRelation, String isLifeCode, String isHideUsertel, boolean isNewUser) {
        map.clear();
        map.put("user_id", user_id);
        if (!StringUtils.isEmpty(device_user_id)) {
            map.put("device_user_id", device_user_id);
        }
        if (!StringUtils.isEmpty(username)) {
            map.put("username", StringUtils.encode(username));
        }
        if (remarkname != null) {
            map.put("remarkname", StringUtils.encode(remarkname));
        }
        if (!StringUtils.isEmpty(headimgurl)) {
            map.put("headimgurl", StringUtils.encode(headimgurl));
        }
        if (!StringUtils.isEmpty(remarkavatar)) {
            map.put("remarkavatar", StringUtils.encode(remarkavatar));
        }
        if (!StringUtils.isEmpty(birthday)) {
            map.put("birthday", birthday);
        }
        if (!StringUtils.isEmpty(gender)) {
            map.put("gender", gender);
        }
        if (!StringUtils.isEmpty(isNearRelation)) {
            map.put("isNearRelation", isNearRelation);
        }
        if (!StringUtils.isEmpty(isLifeCode)) {
            map.put("isLifeCode", isLifeCode);
        }
        if (!StringUtils.isEmpty(isHideUsertel)) {
            map.put("isHideUsertel", isHideUsertel);
        }
        if (isNewUser) {
            map.put("isNewUser", "1");
        }


        Gson gson = new Gson();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=UTF-8"), gson.toJson(map));
        return api.userInfoUpdate(body);
    }

}
