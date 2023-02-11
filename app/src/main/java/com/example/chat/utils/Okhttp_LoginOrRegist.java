package com.example.chat.utils;

import android.content.Context;
import android.widget.Toast;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class Okhttp_LoginOrRegist {
    public final static String LOGIN = "login";
    public final static String REGISTER = "register";

    public static String init(String username, String password, String servlet, Context context) {
        RequestBody requestBody = new FormBody.Builder()
                .add("username", username).add("password", password)
                .build();
        String response = QuickOkhttp_Util.init(requestBody, servlet);
        if (response != null) {
            if ("登录成功".equals(response.substring(0, 4)))
                Toast.makeText(context, response.substring(0, 4), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "请求服务器失败", Toast.LENGTH_SHORT).show();
        }
        return response;
    }
}
