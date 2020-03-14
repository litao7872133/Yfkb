package com.yfkk.cardbag.bean.user;


import com.yfkk.cardbag.bean.BaseBean;

public class AppInitBean extends BaseBean {
    /**
     * code : 200
     * data : {"app":{"type":0,"version_name":"1.0.180130","version_code":101,"upgrade_point":"升级内容","apk_url":"下载地址"},"ad":{"img_url":"","link_url":""},"official_website_url":"","mall_url":"","admin_guide_url":"","oss":{"bucket":"oss仓库名称","accessKeyId":"accessKeyId","accessKeySecret":"accessKeySecret","securityToken":"securityToken"}}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * app : {"type":0,"version_name":"1.0.180130","version_code":101,"upgrade_point":"升级内容","apk_url":"下载地址"}
         * ad : {"img_url":"","link_url":""}
         * official_website_url :
         * mall_url :
         * introduction_url :
         * oss : {"bucket":"oss仓库名称","accessKeyId":"accessKeyId","accessKeySecret":"accessKeySecret","securityToken":"securityToken"}
         */

        public AppBean app;
        public AdBean ad;
        public String officialWebsiteUrl; // 官网
        public String mallUrl; // 商城
        public String introductionUrl; // 功能介绍
        public String fixedBarIntroductionUrl; // 第三方软件固定栏配置介绍
        public String smsInvite; // 短信邀请文案

        public OssBean oss;

        public static class AppBean {
            /**
             * type : 0
             * version_name : 1.0.180130
             * version_code : 101
             * upgrade_point : 升级内容
             * apk_url : 下载地址
             */

            public int type;    //升级标识，0:无需升级，1:提示升级，2:强制升级
            public String versionName;
            public int versionCode;
            public String upgradePoint;
            public String downloadUrl;
        }

        public static class AdBean {
            /**
             * img_url :
             * link_url :
             */

            public String imgUrl;
            public String linkUrl;
        }

        public static class OssBean {
            /**
             * bucket : oss仓库名称
             * accessKeyId : accessKeyId
             * accessKeySecret : accessKeySecret
             * securityToken : securityToken
             */
            public String bucketName;
            public String endPoint;
        }
    }
}
