package com.yfkk.cardbag;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import android.os.Bundle;
import android.support.multidex.MultiDex;

import com.yfkk.cardbag.log.LogUtils;


/**
 * Created by litao on 2019/3/05.
 */
public class MainApplication extends Application {

    private static MainApplication instance;
    private int activityAount;
    public boolean isForeground = false;

    public static MainApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (isMainProcesses()) {

            // Activity 生命周期监听
            registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        }

    }

    /**
     * Activity 生命周期监听，用于监控app前后台状态切换
     */
    ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
            activityAount++;
            if (activityAount > 0) {
                //app回到前台
                isForeground = true;
                LogUtils.e("APP回到前台" + activity);
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {
            activityAount--;
            if (activityAount == 0) {
                isForeground = false;
                LogUtils.e("APP后台运行");
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    };

    private boolean isMainProcesses() {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager mActivityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                processName = appProcess.processName;
                break;
            }
        }
        if (processName.equals(getPackageName())) {
            return true;
        }
        return false;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this); // 解决方法数量65535上线超过
    }

}
