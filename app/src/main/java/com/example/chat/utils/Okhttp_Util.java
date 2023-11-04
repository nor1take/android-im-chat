package com.example.chat.utils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Okhttp_Util implements Runnable {
    private final String ip = "https://n58770595y.zicp.fun/AndroidServe";
    private RequestBody requestBody;
    private String servlet;
    private String responseFromServer;

    public Okhttp_Util(RequestBody requestBody, String servlet) {
        this.requestBody = requestBody;
        this.servlet = servlet;
    }

    private void init() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(ip + servlet);
        if (requestBody != null) builder.method("POST", requestBody);
        Request request = builder.build();
        Response response = client.newCall(request).execute();
        responseFromServer = response.body().string();
    }

    @Override
    public void run() {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getResponseFromServer() {
        return responseFromServer;
    }
}
