package com.yfkk.cardbag.config;

import com.soundcloud.android.crop.Crop;

/**
 * 静态常量配置文件
 * <p>
 * Created by litao on 2019/3/05.
 */

public interface Config {

    // 应用接口状态
    int codeSuccess = 200; // 成功返回200
    int codeTokenLose = 50205; // token不对
    int codeTokenOverdue = 50204; // token过期
    int codeUserLose = 50703; // 用户不存在

    // 常量
    int GENDER_MAN = 1;// 男性
    int GENDER_WOMAN = 0;// 女性
    int CHINESE_NAME_LENGTH = 4; // 中文名字长度
    int REMARKS_NAME_LENGTH = 8; // 备注名字长度
    int PASSWORD_LENGTH = 32; // 密码长度

    String SIGN_KEY = "OTJrSDdOZTljbHtTb1dTOQ=="; // HTTP 签名KEY
    String AES_KEY = "7iCFGx7bMIaIJZMKwCmAztUdZpCu1lfZsxO6BGKigXI="; // AES加密KEY
    String SHA1_KEY = "79270c19"; // SHA1校验（具体算法见SecurityUtils.isRepack()）

    // 返回码
    int REQUEST_CODE_HEADURL = 3003; // 默认头像
    int REQUEST_CODE_SCAN = 3004; // 扫描二维码
    int REQUEST_CODE_REGISTER = 3004; // 注册

    int REQUEST_CODE_ALBUM = 3021; // 相册选择图片
    int REQUEST_CODE_CAMERA = 3022; // 拍照
    int REQUEST_CODE_CROP_IMAGE = Crop.REQUEST_CROP; //图片裁剪

    // 第三方授权
    String SHARE_WEXIN_APPID = "";// 微信分享


}
