package com.example.chat;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.chat.adapter.DialogAdpter;
import com.example.chat.pojo.Dialog;
import com.example.chat.pojo.Msg;
import com.example.chat.pojo.MyMessage;
import com.example.chat.utils.Application_Util;
import com.example.chat.utils.QuickOkhttp_Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class B_Tab4_Fragment extends Fragment {
    private static Application_Util application;
    private static boolean isRunning;
    private List<Dialog> dialogList = new ArrayList<>();
    private DialogAdpter dialogAdpter;
    private ListView dialogListView;
    private Handler handler;
    private static Map<String, Integer> map = new HashMap<>();
    private static int indexOfList = 0;

    private String getAllMyChatGroups() {
        RequestBody requestBody = new FormBody.Builder()
                .add("uid", String.valueOf(application.getUid()))
                .build();
        return QuickOkhttp_Util.init(requestBody, "allMyChatGroups");
    }

    private void initDialogListView() {
        String allMyChatGroups = getAllMyChatGroups();
        JSONArray array = JSONArray.parseArray(allMyChatGroups);
        List<String> allMyChatGroupsList = array.toJavaList(String.class);
        for (String s : allMyChatGroupsList) {
            Dialog dialog = JSON.parseObject(s, Dialog.class);
            if (dialog.getFrom().equals(application.getUid() + "用户")) {
                String tmp = dialog.getFrom();
                dialog.setFrom(dialog.getTo());
                dialog.setTo(tmp);
            }
            map.put(dialog.getFrom(), indexOfList);
            indexOfList++;
            dialogList.add(dialog);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.b_tab4_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dialogAdpter = new DialogAdpter(getActivity(), R.layout.b_tab4_dialog_item, dialogList);
        dialogListView = getActivity().findViewById(R.id.dialog_list_view);
        dialogListView.setAdapter(dialogAdpter);

        dialogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dialog dialog = dialogList.get(position);

                Intent intent = new Intent(getActivity(), C_Chat_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("PostInfo", "");
                bundle.putInt("receiverId", Integer.parseInt(dialog.getFrom().substring(0, dialog.getFrom().indexOf("用"))));
                bundle.putInt("postId", dialog.getPostId());

                Dialog tmp = new Dialog(dialog);
                reset(tmp);
                bundle.putString("chatGroup", JSON.toJSONString(tmp));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    dialogAdpter.updateListView(dialogList);
                }
            }
        };
    }

    private void reset(Dialog tmp) {
        System.out.println(tmp);
        tmp.setMessage("...");
        String from = tmp.getFrom();
        String to = tmp.getTo();
        System.out.println(from + to);
        int fromId = Integer.parseInt(from.substring(0, from.indexOf("用")));
        int toId = Integer.parseInt(to.substring(0, to.indexOf("用")));

        if (toId < fromId) {
            tmp.setFrom(to);
            tmp.setTo(from);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println(">>>>>>>>>>>>>>>>> B_Tab4_Fragment: onCreate");
        isRunning = true;
        startThread();
        initDialogListView();
    }

    private void startThread() {
        application = (Application_Util) getActivity().getApplication();
        new Thread(new receiveMsg()).start();
    }

    private class receiveMsg implements Runnable {
        @Override
        public void run() {
            String data;
            while (isRunning) {
                if ((data = application.getData()) != null) {
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>" + data);
                    /**
                     * 4用户#postId@1用户:content
                     */
                    String[] split = data.split("#", 2);
                    String from = split[0];
                    String[] split1 = split[1].split("@", 2);
                    String postId = split1[0];
                    String[] split2 = split1[1].split(":", 2);
                    String to = split2[0];
                    String content = split2[1];
                    if (map.containsKey(from)) {
                        dialogList.get(map.get(from)).setMessage(content);
                    } else {
                        map.put(from, indexOfList);
                        indexOfList++;
                        Dialog dialog = new Dialog(Integer.parseInt(postId), from, to, content);
                        dialogList.add(dialog);
                    }
                    handler.sendEmptyMessage(0);
                }
            }
            System.out.println(">>>>>>>>>>>>>>>>>>>>>> tab4 receiveMsg_end");
        }
    }
}