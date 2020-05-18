package com.ancely.pay.service;

import android.app.Notification;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;

import com.ancely.fyw.aroute.eventbus.EventBus;
import com.ancely.pay.event.PayBean;

/*
 *  @项目名：  AncelyLucky
 *  @包名：    com.ancely
 *  @文件名:   NotificationCollectorService
 *  @创建者:   fanlelong
 *  @创建时间:  2019/1/22 10:41 AM
 *  @描述：    监听服务通知
 */
public class NotificationCollectorService extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Notification notification = sbn.getNotification();
        if (notification == null) {
            return;
        }
        Bundle extras = notification.extras;
        String content = "";
        if (extras != null) {
            // 获取通知标题
            String title = extras.getString(Notification.EXTRA_TITLE, "");
            // 获取通知内容
            content = extras.getString(Notification.EXTRA_TEXT, "");
//            Log.e("Notification", sbn.getPackageName() + "标题:" + title + "内容:" + content);

            if (TextUtils.isEmpty(content)) {
                return;
            }
            EventBus.getDefault().post(new PayBean(content, title));
//
//            if (Build.VERSION.SDK_INT >= 21)
//                cancelNotification(sbn.getKey());
//            else
//                cancelNotification(sbn.getPackageName(), sbn.getTag(), sbn.getId());
//            Toast.makeText(this, "receiptnotice移除了包名为" + sbn.getPackageName() + "的通知\"", Toast.LENGTH_SHORT).show();
            //            switch (sbn.getPackageName()) {
//                case "com.tencent.mm":
//                    Log.e("微信", content);
//                    EventBus.getDefault().post(new PayBean(content, "微信"));
//                    break;
//                case "com.eg.android.AlipayGphone":
//                    Log.e("支付宝", content);
//                    EventBus.getDefault().post(new PayBean(content, "支付宝"));
//                    break;
//                default:
//                    EventBus.getDefault().post(new PayBean(content, title));
//                    break;
//
//            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Notification notification = sbn.getNotification();
    }


}
