package com.elinkthings.wristbanddemo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;

/**
 * xing<br>
 * 2019/4/22<br>
 * 路由跳转工具类
 */
public class AppStart {

    private static String TAG = AppStart.class.getName();


    /**
     * 进入应用设置界面
     *
     */
    public static void startUseSetActivity(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(localIntent);
    }


    /**
     * 进入设置系统界面
     *
     * @param context 上下文
     */
    public static void startSetActivity(Context context) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * 手机是否开启位置服务
     */
    public static boolean isLocServiceEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    /**
     * 进入定位服务
     */
    public static void startLocationActivity(Activity activity, int code) {
        Intent localIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        if (activity.getPackageManager().resolveActivity(localIntent,
                PackageManager.MATCH_DEFAULT_ONLY) != null) {
            activity.startActivityForResult(localIntent, code);
        }
    }


    /**
     * 跳转到系统web
     */
    public static void startSysWeb(Context context, String url) {
        Intent intentWeb = new Intent();
        intentWeb.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse("http://" + url);
        intentWeb.setData(content_url);
        context.startActivity(intentWeb);
    }




}
