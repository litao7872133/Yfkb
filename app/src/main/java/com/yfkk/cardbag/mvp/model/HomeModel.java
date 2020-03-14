package com.yfkk.cardbag.mvp.model;

import com.yfkk.cardbag.bean.user.UserLoginBean;
import com.yfkk.cardbag.bean.user.UserRegisterBean;
import com.yfkk.cardbag.config.UrlConfig;
import com.yfkk.cardbag.mvp.base.IBaseModel;
import com.yfkk.cardbag.utils.StringUtils;
import com.google.gson.Gson;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 用户
 * <p>
 * Created by litao on 2019/3/15.
 */
public class HomeModel extends IBaseModel {
    private Api api;

    public interface Api {

        @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
        @POST(UrlConfig.userRegister)
        Observable<UserRegisterBean> userRegister(@Body RequestBody requestBody);

        @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
        @POST(UrlConfig.userLogin)
        Observable<UserLoginBean> userLogin(@Body RequestBody requestBody);

    }

    private HomeModel() {
        super();
        api = retrofit.create(Api.class);
    }

    public static HomeModel getInstance() {
        return new HomeModel();
    }

    /**
     * 登录
     *
     * @return
     */
    public Observable<UserLoginBean> userLogin(String usertel, String verifycode) {
        map.clear();
        map.put("usertel", StringUtils.encode(usertel));
        map.put("verifycode", StringUtils.encode(verifycode));

        Gson gson = new Gson();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=UTF-8"), gson.toJson(map));
        return api.userLogin(body);
    }

    /**
     * 注册
     *
     * @return
     */
    public Observable<UserRegisterBean> userRegister(String usertel, String verifycode, String username, String gender, String headimgurl) {
        map.clear();
        map.put("usertel", StringUtils.encode(usertel));
        map.put("verifycode", StringUtils.encode(verifycode));
        map.put("username", StringUtils.encode(username));
        map.put("gender", gender);
        if (!StringUtils.isEmpty(headimgurl)) {
            map.put("headimgurl", StringUtils.encode(headimgurl));
        }

        Gson gson = new Gson();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=UTF-8"), gson.toJson(map));
        return api.userRegister(body);
    }


}
