package com.example.chat.utils;

import static com.example.chat.utils.RequestMapping.aPost;
import static com.example.chat.utils.RequestMapping.allPosts;
import static com.example.chat.utils.RequestMapping.labelTop3;
import static com.example.chat.utils.RequestMapping.limitNumPosts;

import android.content.Context;

import com.alibaba.fastjson.JSON;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class Okhttp_Post {
    /**
     * getAPost
     *
     * @param postId
     * @return postJson
     */
    public static Result getA(String postId, Context context) {
        RequestBody requestBody = new FormBody.Builder()
                .add("id", postId)
                .build();
        return QuickOkhttp_Util.init(requestBody, aPost, context);
    }


    /**
     * getAPost
     *
     * @param label
     * @return postJson
     */
    public static Result getTop3ByLabel(String label, Context context) {
        RequestBody requestBody = new FormBody.Builder()
                .add("label", label)
                .build();
        System.out.println("进行了一次文学请求");
        return QuickOkhttp_Util.init(requestBody, labelTop3, context);
    }


    /**
     * getAllPosts
     *
     * @return id1#id2#...
     */
    public static Result getAll(Context context) {
        return QuickOkhttp_Util.init(null, allPosts, context);
    }

    public static Result getLimitNum(int limit, int offset, Context context) {
        RequestBody requestBody = new FormBody.Builder()
                .add("limit", String.valueOf(limit))
                .add("offset", String.valueOf(offset))
                .build();
        return QuickOkhttp_Util.init(requestBody, limitNumPosts, context);
    }
}
