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
     * getAllPosts
     * @return id1#id2#...
     */
    public static String getAll() {
        return QuickOkhttp_Util.init(null, "allPosts");
    }
}
