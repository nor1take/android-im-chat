package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.chat.adapter.MsgAdapter;
import com.example.chat.pojo.Msg;
import com.example.chat.pojo.Post;
import com.example.chat.utils.Application_Util;
import com.example.chat.utils.SoftKeyBoardListener;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class C_Chat_Activity extends AppCompatActivity implements View.OnClickListener {
    final String ip = "https://n58770595y.zicp.fun/AndroidServe/";

    Button sendButton;
    EditText input;
    TextView connectInfo;
    TextView lable;
    TextView cardText;

    String message = null;

    private static ListView msgListView;
    private static MsgAdapter adapter;
    private static List<Msg> msgList = new ArrayList<Msg>();

    Handler handler;
    private BufferedWriter bwServer;
    private int receiverUID;
    private static Application_Util application;

    private static boolean isRunning = false;
    private Post post;
    private String postJson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_chat_activity);

        isRunning = false; // 关闭之前的receiveMsg线程
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> onCreate");
        isRunning = true;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        postJson = bundle.getString("PostInfo");
        if (!"".equals(postJson)) {
            post = JSON.parseObject(postJson, Post.class);
            receiverUID = post.getPoster();
        } else {
            receiverUID = bundle.getInt("receiverId");
            int postId = bundle.getInt("postId");
            post = JSON.parseObject(getAPost("aPost", postId + ""), Post.class);
        }
        sendButton = findViewById(R.id.sendBtn);
        input = findViewById(R.id.input);
        connectInfo = findViewById(R.id.connectInfo);
        lable = findViewById(R.id.label);
        cardText = findViewById(R.id.cardText);

        connectInfo.setText("talking to uid = " + receiverUID);
        lable.setText("# " + post.getLabel());
        cardText.setText(post.getBody());

        adapter = new MsgAdapter(this, R.layout.c_chat_msg_item, msgList);
        msgListView = findViewById(R.id.msg_list_view);
        msgListView.setAdapter(adapter);

        sendButton.setOnClickListener(this);

        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    adapter.updateListView(msgList); // 当有新消息时，刷新ListView中的显示
                    msgListView.setSelection(msgList.size()); // 将ListView定位到最后一行
                }
            }
        };

        /**
         * 显示软键盘  隐藏删除
         * 隐藏软键盘 显示删除
         */
        SoftKeyBoardListener.setListener(C_Chat_Activity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                //LogUtil.e("键盘弹出");
                msgListView.setSelection(msgList.size());
            }

            @Override
            public void keyBoardHide(int height) {
                //LogUtil.e("键盘隐藏");
            }
        });

        startThread();
    }

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.sendBtn)) {
            message = input.getText().toString();
            if (!"".equals(message)) {
                Msg msg = new Msg(message, Msg.TYPE_SENT);
                msgList.add(msg);
                adapter.updateListView(msgList); // 当有新消息时，刷新ListView中的显示
                msgListView.setSelection(msgList.size()); // 将ListView定位到最后一行
            }
            input.setText("");
        }
    }

    private void startThread() {
        getSocket();
        new Thread(new receiveMsg()).start();
        new Thread(new sendMsg()).start();
    }

    private void getSocket() {
        application = (Application_Util) getApplication();
        bwServer = application.getBwServer();
    }

    private class receiveMsg implements Runnable {
        @Override
        public void run() {
            String data;
            while (isRunning) {
                if ((data = application.getData()) != null) {
                    /**
                     * 4用户#postId@1用户:content
                     */
                    String[] split = data.split("#", 2)[1].split("@", 2)[1].split(":", 2);
                    Msg msg = new Msg(split[1], Msg.TYPE_RECEIVED);
                    msgList.add(msg);
                    handler.sendEmptyMessage(0);
                }
            }
            System.out.println(">>>>>>>>>>>>>>>>>> receiveMsg end");
        }
    }

    private class sendMsg implements Runnable {
        @Override
        public void run() {
            try {
                while (isRunning) {
                    if (message != null && !"".equals(message)) {
                        bwServer.write("#" + post.getId() + "@" + receiverUID + "用户:" + message);
                        bwServer.newLine();
                        bwServer.flush();
                        message = null;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(">>>>>>>>>>>>>>>>>>> sendMsg end");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        msgListView.setSelection(msgList.size()); // 将ListView定位到最后一行
    }

    /**
     * @param url
     * @param id  帖子的 id
     * @return json
     */
    public String getAPost(String url, String id) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("id", id)
                            .build();

                    Request request = new Request.Builder()
                            .url(ip + url)
                            .method("POST", requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    postJson = response.body().string();
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
        return postJson;
    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        isRunning = false;
//    }
}