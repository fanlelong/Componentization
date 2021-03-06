package com.ancely.fyw.aroute.permissions;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.ancely.fyw.aroute.permissions.listener.RequestPermission;


public class PermissionManager {

    public static void request(Activity activity, String[] permissions,int permissionCode) {
        String className = activity.getClass().getName() + "$Permissions";

        try {
            Class<?> clazz = Class.forName(className);
            RequestPermission rPermission = (RequestPermission) clazz.newInstance();
            rPermission.requestPermission(activity, permissions,permissionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull int[] grantResults) {
        String className = activity.getClass().getName() + "$Permissions";

        try {
            Class<?> clazz = Class.forName(className);
            RequestPermission rPermission = (RequestPermission) clazz.newInstance();
            rPermission.onRequestPermissionsResult(activity, requestCode, grantResults);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
