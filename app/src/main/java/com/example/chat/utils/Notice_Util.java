package com.example.chat.utils;


import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.chat.C_Chat_Activity;
import com.example.chat.R;
import com.example.chat.pojo.Dialog;

public class Notice_Util implements Runnable {
    private String from;
    private String postId;
    private String to;
    private String content;

    private String msg;
    private Context context;

    public Notice_Util(String msg, Context context) {
        this.msg = msg;
        this.context = context;
    }

    @Override
    public void run() {
        init();
    }

    public void init() {
        handleMsg();
        Intent intent = initIntent();
        // Intent intent = new Intent(context, C_Chat_Activity.class);
        Log.e("intent", intent.getExtras().toString());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            /**
             *  为channel添加属性
             *  channel.enableVibration(true); 震动
             *  channel.enableLights(true);提示灯
             */
            String id = "channel_1"; //channel的id
            String description = "message"; //channel的描述信息
            int importance = NotificationManager.IMPORTANCE_HIGH; //channel的重要性
            NotificationChannel channel = new NotificationChannel(id, description, importance);
            channel.enableVibration(true);

            notificationManager.createNotificationChannel(channel); //添加channel
            Notification notification = new Notification.Builder(context, id)
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setContentTitle(from)
                    .setContentText(content)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setContentIntent(pendingIntent)
                    .build();
            notificationManager.notify(1, notification);
        } else {
            Notification notification = new Notification.Builder(context)
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setContentTitle(from)
                    .setContentText(content)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE) //设置声音和震动
                    .build();

            notificationManager.notify(1, notification); //发送通知
        }
    }

    private void handleMsg() {
        /**
         * 4用户#postId@1用户:content
         */
        String[] split = msg.split("#", 2);
        from = split[0];
        String[] split1 = split[1].split("@", 2);
        postId = split1[0];
        String[] split2 = split1[1].split(":", 2);
        to = split2[0];
        content = split2[1];
    }

    private Intent initIntent() {
        Dialog dialog = new Dialog(Integer.parseInt(postId), from, to, content);
        Intent intent = new Intent(context, C_Chat_Activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("PostInfo", "");
        bundle.putInt("receiverId", Integer.parseInt(dialog.getFrom().substring(0, dialog.getFrom().indexOf("用"))));
        bundle.putInt("postId", dialog.getPostId());

        Log.e("receiverId", String.valueOf(Integer.parseInt(dialog.getFrom().substring(0, dialog.getFrom().indexOf("用")))));
        Log.e("postId", String.valueOf(Integer.parseInt(dialog.getFrom().substring(0, dialog.getFrom().indexOf("用")))));

        Dialog tmp = new Dialog(dialog);
        reset(tmp);
        bundle.putString("chatGroup", JSON.toJSONString(tmp));

        Log.e("chatGroup", JSON.toJSONString(tmp));

        intent.putExtras(bundle);
        return intent;
    }

    private void reset(Dialog tmp) {
        System.out.println(tmp);
        tmp.setMessage("...");
        String from = tmp.getFrom();
        String to = tmp.getTo();
        System.out.println(from + to);
        int fromId = Integer.parseInt(from.substring(0, from.indexOf("用")));
        int toId = Integer.parseInt(to.substring(0, to.indexOf("用")));

        if (toId < fromId) {
            tmp.setFrom(to);
            tmp.setTo(from);
        }
    }


}
