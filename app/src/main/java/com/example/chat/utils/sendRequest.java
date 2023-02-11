package com.example.chat.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.chat.A_Login_Activity;
import com.example.chat.B_Container_Activity;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class sendRequest {
    public static String postOkhttp(Context context, String url, String username, String password) {
        String ip = "https://n58770595y.zicp.fun/AndroidServe/";
        final String[] data = new String[1];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("username", username).add("password", password)
                            .build();

                    Request request = new Request.Builder()
                            .url(ip + url)
                            .method("POST", requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    data[0] = response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (data[0] != null) {
            if ("登录成功".equals(data[0].substring(0, 4)))
                Toast.makeText(context, data[0].substring(0, 4), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, data[0], Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "请求服务器失败", Toast.LENGTH_SHORT).show();
        }
        System.out.println(data[0]);
        return data[0];
    }
}
