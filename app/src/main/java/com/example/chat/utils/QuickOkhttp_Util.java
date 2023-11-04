package com.example.chat.utils;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import okhttp3.RequestBody;

public class QuickOkhttp_Util {
    /**
     * @param requestBody null 或
     *                    RequestBody requestBody = new FormBody.Builder()
     *                    .add("senderId", String.valueOf(application.getUid()))
     *                    .add("message", input.getText().toString())
     *                    .add("receiverId", String.valueOf(receiverUID))
     *                    .add("postId", String.valueOf(post.getId()))
     *                    .build();
     * @param servlet     后缀
     * @return String 服务器响应
     */
    public static Result init(RequestBody requestBody, String servlet, Context context) {
        Okhttp_Util okhttp_util = new Okhttp_Util(requestBody, servlet);
        Thread thread = new Thread(okhttp_util);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Result result = JSON.parseObject(okhttp_util.getResponseFromServer(), Result.class);
        if (result != null) {
            if (result.getMsg() != null)
                Toast.makeText(context, result.getMsg(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "请求服务器失败", Toast.LENGTH_SHORT).show();
        }
        return result;
    }


}
