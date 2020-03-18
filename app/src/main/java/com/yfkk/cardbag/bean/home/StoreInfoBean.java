package com.yfkk.cardbag.bean.home;

import com.yfkk.cardbag.bean.BaseBean;
import com.yfkk.cardbag.bean.user.AppInitBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by litao on 2020/3/16.
 */
public class StoreInfoBean extends BaseBean {

    public AppInitBean.DataBean data;

    public static class DataBean {
        public String icon; // 图片
        public String name; // 名称
        public String place; // 位置
        public List recharge_list = new ArrayList<>(); ; // 充值信息

        public static class RechargeListBean implements Serializable {
            public String des;
        }
    }

}
