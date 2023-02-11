package com.example.chat.utils;

import okhttp3.RequestBody;

public class QuickOkhttp_Util {
    /**
     *
     * @param requestBody
     * null 或
      RequestBody requestBody = new FormBody.Builder()
              .add("senderId", String.valueOf(application.getUid()))
              .add("message", input.getText().toString())
              .add("receiverId", String.valueOf(receiverUID))
              .add("postId", String.valueOf(post.getId()))
              .build();
     * @param servlet 后缀
     * @return String 服务器响应
     */
    public static String init(RequestBody requestBody, String servlet) {
        Okhttp_Util okhttp_util = new Okhttp_Util(requestBody, servlet);
        Thread thread = new Thread(okhttp_util);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return okhttp_util.getResponseFromServer();
    }
}
