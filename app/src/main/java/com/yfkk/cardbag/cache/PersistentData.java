package com.yfkk.cardbag.cache;

import com.yfkk.cardbag.BuildConfig;
import com.yfkk.cardbag.config.UrlConfig;
import com.yfkk.cardbag.utils.DateUtils;
import com.yfkk.cardbag.utils.SPUtils;

/**
 * 持久化数据
 * <p>
 * <p>
 * Created by litao on 2018/2/6.
 */

public class PersistentData {

    /**
     * 保存更新弹窗时间
     */
    public static void saveUpdateTime(long update_time) {
        SPUtils.saveLong("SP_UPDATE_TIME", update_time);
    }

    /**
     * 读取更新弹窗时间
     */
    public static long getUpdateTime() {
        return SPUtils.getLong("SP_UPDATE_TIME", 0);
    }

    /**
     * 保存TOKEN
     */
    public static void setToken(String access_user_token) {
        SPUtils.saveString("SP_TOKEN", access_user_token);
        saveTokenUpdateTime(System.currentTimeMillis());
    }

    /**
     * 读取TOKEN
     */
    public static String getToken() {
        return SPUtils.getString("SP_TOKEN", null);
    }

    /**
     * 保存TOKEN更新时间
     */
    public static void saveTokenUpdateTime(long update_time) {
        SPUtils.saveLong("SP_TOKEN_UPDATE_TIME", update_time);
    }

    /**
     * 读取TTOKEN更新时间
     */
    public static long getTokenUpdateTime() {
        return SPUtils.getLong("SP_TOKEN_UPDATE_TIME", 0);
    }

    /**
     * 保存当前展示的设备ID
     */
    public static void setDisplayDeviceId(String deviceId) {
        SPUtils.saveString("SP_DISPLAY_DEVICE_ID", deviceId);
    }

    /**
     * 读取当前展示的设备ID
     */
    public static String getDisplayDeviceId() {
        return SPUtils.getString("SP_DISPLAY_DEVICE_ID", null);
    }

    /**
     * 保存已读设备菜单小红点
     */
    public static void setDeviceMenuUnread(String deviceId, String menuType, boolean showUnread) {
        SPUtils.saveBoolean("SP_MENU_DEVICE_" + deviceId + menuType, showUnread);
    }

    /**
     * 读取设备菜单小红点状态
     */
    public static boolean getDeviceMenuUnread(String deviceId, String menuType) {
        return SPUtils.getBoolean("SP_MENU_DEVICE_" + deviceId + menuType, false);
    }


    /**
     * 保存通讯录上传时间（24小时内不连续上传）
     */
    public static void saveTelephoneDirectory(String deviceId, long update_time) {
        SPUtils.saveLong("SP_TELEPHONE_DIRECTORY_" + deviceId, update_time);
    }

    /**
     * 读取通讯录上传时间
     */
    public static long getTelephoneDirectory(String deviceId) {
        return SPUtils.getLong("SP_TELEPHONE_DIRECTORY_" + deviceId, 0);
    }

    /**
     * 保存订阅号市场已读ID
     */
    public static void saveSubscribeReadID(String deviceId, long id) {
        SPUtils.saveLong("SP_SUB_READ_" + deviceId, id);
    }

    /**
     * 读取订阅号市场已读ID
     */
    public static long getSubscribeReadID(String deviceId) {
        return SPUtils.getLong("SP_SUB_READ_" + deviceId, 0);
    }

    /**
     * 新的联系人\订阅号市场未读数量
     * <p>
     * type:
     * String PUSH_NEW_CONTACTS = "new_contacts";  // 新的联系人
     * String PUSH_NEW_SUBSCRIBE = "new_subscribe";  // 公众号推送
     */
    public static void setDeviceMsgRead(String deviceId, String type, int size) {
        SPUtils.saveInt("SP_DEVICE_MSG_" + deviceId + type, size);
    }

    /**
     * 读取新的联系人\订阅号市场未读数量
     */
    public static int getDeviceMsgRead(String deviceId, String type) {
        return SPUtils.getInt("SP_DEVICE_MSG_" + deviceId + type, 0);
    }

    /**
     * 消息免打扰
     */
    public static void setMsgUndisturb(String userid, String groupId, boolean isDisturb) {
        SPUtils.saveBoolean("SP_MSG_UNDISTURB_u" + userid + "g_" + groupId, isDisturb);
    }

    /**
     * 读取消息免打扰
     */
    public static boolean getMsgUndisturb(String userid, String groupId) {
        return SPUtils.getBoolean("SP_MSG_UNDISTURB_u" + userid + "g_" + groupId, false);
    }

    /**
     * 打开网络调试模式
     */
    public static void setNetDebugMode(boolean isOpen) {
        SPUtils.saveBoolean("SP_NET_DEBUG", isOpen);
    }

    /**
     * 是否打开网络调试模式
     */
    public static boolean getNetDebugMode() {
        return SPUtils.getBoolean("SP_NET_DEBUG", false);
    }

    /**
     * 设置域名
     */
    public static void setHostUrl(String hostUrl) {
        UrlConfig.HOST_URL = hostUrl;
        SPUtils.saveString("SP_HOST_URL", hostUrl);
    }

    /**
     * 获取域名
     */
    public static String getHostUrl() {
        return SPUtils.getString("SP_HOST_URL", BuildConfig.INTERFACE);
    }

    /**
     * 保存设备最后一次的位置
     */
    public static void setPlace(String device_id, String place) {
        SPUtils.saveString("SP_PLACE_" + device_id, place);
    }

    /**
     * 读取设备最后一次的位置
     */
    public static String getPlace(String device_id) {
        return SPUtils.getString("SP_PLACE_" + device_id, null);
    }

    /**
     * 保存TOKEN(一个月有效期)
     */
    public static void setBaiduToken(String access_user_token) {
        SPUtils.saveString("SP_BAIDU_TOKEN_" + DateUtils.dateToStr(System.currentTimeMillis(), "yyyy-MM"), access_user_token);
    }

    /**
     * 读取TOKEN
     */
    public static String getBaiduToken() {
        return SPUtils.getString("SP_BAIDU_TOKEN_" + DateUtils.dateToStr(System.currentTimeMillis(), "yyyy-MM"), null);
    }

}
