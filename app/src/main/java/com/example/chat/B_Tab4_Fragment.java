package com.example.chat;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.chat.adapter.DialogAdpter;
import com.example.chat.pojo.Dialog;
import com.example.chat.pojo.Msg;
import com.example.chat.utils.Application_Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class B_Tab4_Fragment extends Fragment {
    private static Application_Util application;
    private static boolean isRunning;
    private List<Dialog> dialogList = new ArrayList<>();
    private DialogAdpter dialogAdpter;
    private ListView dialogListView;
    private Handler handler;
    private static Map<String, Integer> map = new HashMap<>();
    private static int indexOfList = 0;

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
//                System.out.println("getUid " + dialog.getUid());
//                System.out.println("getPostId " + dialog.getPostId());
                Intent intent = new Intent(getActivity(), C_Chat_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("PostInfo", "");
                bundle.putInt("receiverId", Integer.parseInt(dialog.getUidUser().substring(0, dialog.getUidUser().indexOf("用"))));
                bundle.putInt("postId", dialog.getPostId());
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println(">>>>>>>>>>>>>>>>> B_Tab4_Fragment: onCreate");
        isRunning = true;
        startThread();
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
                    String uidUser = split[0];
                    String[] split1 = split[1].split("@", 2);
                    String postId = split1[0];
                    String content = split1[1].split(":", 2)[1];
                    if (map.containsKey(uidUser)) {
                        dialogList.get(map.get(uidUser)).setMessage(content);
                    } else {
                        map.put(uidUser, indexOfList);
                        indexOfList++;
                        Dialog dialog = new Dialog(Integer.parseInt(postId), uidUser, content);
                        dialogList.add(dialog);
                    }
                    handler.sendEmptyMessage(0);
                }
            }
            System.out.println(">>>>>>>>>>>>>>>>>>>>>> tab4 receiveMsg_end");
        }
    }
}