package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.chat.adapter.MsgAdapter;
import com.example.chat.pojo.Dialog;
import com.example.chat.pojo.Msg;
import com.example.chat.pojo.MyMessage;
import com.example.chat.pojo.Post;
import com.example.chat.utils.Application_Util;
import com.example.chat.utils.Font_Util;
import com.example.chat.utils.SoftKeyBoardListener;
import com.example.chat.utils.Okhttp_Post;
import com.example.chat.utils.QuickOkhttp_Util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class C_Chat_Activity extends AppCompatActivity implements View.OnClickListener {
    ImageView sendButton;
    ImageView addFriend;
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
    private int receiverId;
    private static Application_Util application;

    private static boolean isRunning = false;
    private Post post;
    private String postJson;

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false; // 关闭之前的receiveMsg线程
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_chat_activity);

        msgList.clear();
        application = (Application_Util) getApplication();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> onCreate");
        isRunning = true;

        Intent intent = getIntent();
        Log.e("intent", intent.getExtras().toString());
        Bundle bundle = intent.getExtras();
        String chatGroup = bundle.getString("chatGroup");

        Log.e("chatGroup", chatGroup);

        initMsgList(chatGroup);

        postJson = bundle.getString("PostInfo");
        if (!"".equals(postJson)) {
            post = JSON.parseObject(postJson, Post.class);
            receiverId = post.getPoster();
        } else {
            receiverId = bundle.getInt("receiverId");
            int postId = bundle.getInt("postId");

            Log.e("receiverId", String.valueOf(receiverId));
            Log.e("postId", String.valueOf(postId));

            post = JSON.parseObject(Okhttp_Post.getA(String.valueOf(postId)), Post.class);
        }
        addFriend = findViewById(R.id.addFriend);
        sendButton = findViewById(R.id.sendBtn);
        input = findViewById(R.id.input);
        connectInfo = findViewById(R.id.connectInfo);
        lable = findViewById(R.id.label);
        cardText = findViewById(R.id.cardText);

        connectInfo.setText("talking to uid = " + receiverId);
        lable.setText("# " + post.getLabel());
        cardText.setText(post.getBody());
        Font_Util.setFont(cardText, 3, this);
        Font_Util.setFont(lable, 2, this);

        adapter = new MsgAdapter(this, R.layout.c_chat_msg_item, msgList);
        msgListView = findViewById(R.id.msg_list_view);
        msgListView.setAdapter(adapter);

        sendButton.setOnClickListener(this);
        addFriend.setOnClickListener(this);

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

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (input.getText().toString().length() > 0)
                    sendButton.setImageResource(R.mipmap.ic_public_send_filled);
                else sendButton.setImageResource(R.mipmap.ic_public_send);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // "#" + post.getId() + "@" + receiverId + "用户:" + message
        application.setPostId(post.getId());
        application.setTo(receiverId);

        startThread();
    }

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.sendBtn)) {
            message = input.getText().toString();
            if (!"".equals(message)) {
                Msg msg = new Msg(message, Msg.TYPE_SENT);
                msgList.add(msg);
                adapter.updateListView(msgList); // 刷新ListView中的显示
                msgListView.setSelection(msgList.size()); // 将ListView定位到最后一行
                String s = msgOkhttp(input.getText().toString());
                System.out.println(s);
            }
            input.setText("");
        } else if (v == findViewById(R.id.addFriend)) {
            if (isHavingFriend()) {
                Toast.makeText(this, "对方已经你的好友", Toast.LENGTH_SHORT).show();
            } else {
                Msg msg = new Msg("  你向对方发起了好友请求  #隐藏", Msg.TYPE_ADD);
                msgList.add(msg);
                adapter.updateListView(msgList); // 刷新ListView中的显示
                msgListView.setSelection(msgList.size()); // 将ListView定位到最后一行
                message = "*对方向你发起了好友请求";
            }

        }
    }

    private boolean isHavingFriend() {
        RequestBody requestBody = new FormBody.Builder()
                .add("uid", String.valueOf(application.getUid()))
                .add("friendId", String.valueOf(receiverId))
                .build();
        String response = QuickOkhttp_Util.init(requestBody, "aFriend");
        System.out.println(response);
        return response == null ? false : (response.equals("y"));
    }

    private void initMsgList(String chatGroup) {
        Log.e("chatGroup", chatGroup);
        RequestBody requestBody = new FormBody.Builder()
                .add("chatGroup", chatGroup)
                .build();
        String allMsgs = QuickOkhttp_Util.init(requestBody, "allMsgs");
        Log.e("allMsgs", allMsgs);
        JSONArray array = JSONArray.parseArray(allMsgs);
        List<MyMessage> messagesList = array.toJavaList(MyMessage.class);
        for (MyMessage myMessage : messagesList) {
            if (myMessage.getSenderId() == application.getUid()) {
                msgList.add(new Msg(myMessage.getMessage(), Msg.TYPE_SENT));
            } else {
                msgList.add(new Msg(myMessage.getMessage(), Msg.TYPE_RECEIVED));
            }
        }
    }


    private void startThread() {
        getSocket();
        new Thread(new receiveMsg()).start();
        new Thread(new sendMsg()).start();
    }

    private void getSocket() {

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
                    String content = split[1];
                    Msg msg;
                    if (content.startsWith("*")) {
                        if (content.equals("*对方向你发起了好友请求")) {
                            msg = new Msg("  对方向你发起了好友请求  #接受", Msg.TYPE_ADD);
                        } else {
                            msg = new Msg("  " + content.substring(1) + "  #隐藏", Msg.TYPE_ADD);
                        }
                    } else {
                        msg = new Msg(content, Msg.TYPE_RECEIVED);
                    }

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
                        bwServer.write("#" + post.getId() + "@" + receiverId + "用户:" + message);
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

    private String msgOkhttp(String message) {
        int senderId = application.getUid();
        Integer postId = post.getId();

        Dialog dialog;
        dialog = new Dialog(
                postId,
                String.valueOf(Math.min(senderId, receiverId)) + "用户",
                String.valueOf(Math.max(senderId, receiverId)) + "用户",
                "...");
        RequestBody requestBody = new FormBody.Builder()
                .add("senderId", String.valueOf(senderId))
                .add("message", message)
                .add("receiverId", String.valueOf(receiverId))
                .add("postId", String.valueOf(postId))
                .add("chatGroup", JSON.toJSONString(dialog))
                .build();
        return QuickOkhttp_Util.init(requestBody, "sendMsg");
    }
}