package com.dgzd.mxtx.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.dgzd.mxtx.entirety.UserInfo;

import java.util.List;
import java.util.Properties;

/**
 *
 * 类名称：AppContext
 * 创建人：niusd
 */
public class AppContext extends Application {
    private UserInfo userInfo;
    @Override
    public void onCreate() {
        super.onCreate();
        //注册App异常崩溃处理器
        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
    }

    /**
     * 是否已登录
     * @return
     */
    public boolean isSignedIn() {
        return userInfo != null;
    }

    /**
     * 退出登录
     */
    public void logout() {
        userInfo = null;
    }

    /**
     * 保存用户信息
     * @param userInfo
     */
    public void saveUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    /**
     * 获取当前登录用户信息
     * @return
     */
    public UserInfo getUserInfo() {
        return userInfo;
    }

    /**
     * 应用是否在后台运行
     * @return
     */
    public boolean isAppBackground() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }
    /**
     * 获取App安装包信息
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if(info == null) info = new PackageInfo();
        return info;
    }

    /**
     * 获取App唯一标识
     * @return
     */
//    public String getAppId() {
//        String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
//        if(StringUtils.isEmpty(uniqueID)){
//            uniqueID = UUID.randomUUID().toString();
//            setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
//        }
//        return uniqueID;
//    }

    public boolean containsProperty(String key){
        Properties props = getProperties();
        return props.containsKey(key);
    }

    public void setProperties(Properties ps){
        AppConfig.getAppConfig(this).set(ps);
    }

    public Properties getProperties(){
        return AppConfig.getAppConfig(this).get();
    }

    public void setProperty(String key,String value){
        AppConfig.getAppConfig(this).set(key, value);
    }

    public String getProperty(String key){
        return AppConfig.getAppConfig(this).get(key);
    }
    public void removeProperty(String...key){
        AppConfig.getAppConfig(this).remove(key);
    }
}
