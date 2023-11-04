package com.example.chat.utils;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class Okhttp_LoginOrRegist {
    public static Result init(String username, String password, String servlet, Context context) {
        RequestBody requestBody = new FormBody.Builder()
                .add("username", username).add("password", password)
                .build();
        Result result = QuickOkhttp_Util.init(requestBody, servlet, context);
        return result;
    }
}
