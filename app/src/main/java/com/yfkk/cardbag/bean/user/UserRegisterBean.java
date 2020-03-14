package com.yfkk.cardbag.bean.user;

import com.yfkk.cardbag.bean.BaseBean;

/**
 * 用户注册
 * <p>
 * Created by litao on 2019/3/15.
 */
public class UserRegisterBean extends BaseBean {

    /**
     * code : 200
     * data : {"access_user_token":"d05ebe799acbffa6c58691397eb16135"}
     */
    public DataBean data;

    public static class DataBean {
        /**
         * access_user_token : d05ebe799acbffa6c58691397eb16135
         */

        public String access_user_token;
    }
}
