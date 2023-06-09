package com.example.chat.utils;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class Okhttp_Post {
    /**
     * getAPost
     * @param postId
     * @return postJson
     */
    public static String getA(String postId) {
        RequestBody requestBody = new FormBody.Builder()
                .add("id", postId)
                .build();
        return QuickOkhttp_Util.init(requestBody, "aPost");
    }


    /**
     * getAPost
     * @param  label
     * @return postJson
     */
    public static String getTop3ByLabel(String label) {
        RequestBody requestBody = new FormBody.Builder()
                .add("label", label)
                .build();
        System.out.println("进行了一次文学请求");
        return QuickOkhttp_Util.init(requestBody, "labelTop3");
    }
    

    /**
     * getAllPosts
     * @return id1#id2#...
     */
    public static String getAll() {
        return QuickOkhttp_Util.init(null, "allPosts");
    }

    public static String getLimitNum(int limit, int offset) {
        RequestBody requestBody = new FormBody.Builder()
                .add("limit", String.valueOf(limit))
                .add("offset", String.valueOf(offset))
                .build();
        return QuickOkhttp_Util.init(requestBody, "limitNumPosts");
    }
}
