package com.example.chat.adapter;

import static com.example.chat.utils.RequestMapping.friendAdd;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.C_Chat_Activity;
import com.example.chat.R;
import com.example.chat.pojo.Msg;
import com.example.chat.utils.Application_Util;
import com.example.chat.utils.Code;
import com.example.chat.utils.QuickOkhttp_Util;
import com.example.chat.utils.Result;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class MsgAdapter extends ArrayAdapter<Msg> {

    private int resourceId;
    private List<Msg> msgList;

    public MsgAdapter(Context context, int textViewResourceId, List<Msg> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        msgList = objects;
    }

    public void updateListView(List<Msg> list) {
        msgList = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Msg msg = getItem(position);
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.leftLayout = view.findViewById(R.id.left_layout);
            viewHolder.rightLayout = view.findViewById(R.id.right_layout);
            viewHolder.leftMsg = view.findViewById(R.id.left_msg);
            viewHolder.rightMsg = view.findViewById(R.id.right_msg);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.midMsg = view.findViewById(R.id.mid_msg);
        viewHolder.midLayout = view.findViewById(R.id.mid_layout);
        viewHolder.option = view.findViewById(R.id.option);
        viewHolder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String option = viewHolder.option.getText().toString();
                msgList.remove(position);
                notifyDataSetChanged();
                if (option.equals("接受")) {
                    sendMsg sendMsg = new sendMsg((Activity) getContext());

                    Result result = sendMsg.addFriend();
                    if (result.getCode().equals(Code.SAVE_OK)) {
                        sendMsg.setMessage("*对方接受了你的请求，现在你们是好友了");
                        new Thread(sendMsg).start();
                    }
                }
            }
        });
        if (msg.getType() == Msg.TYPE_RECEIVED) {
            // 如果是收到的消息，则显示左边的消息布局，将右边的消息布局隐藏
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.midLayout.setVisibility(View.GONE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftMsg.setText(msg.getContent());
        } else if (msg.getType() == Msg.TYPE_SENT) {
            // 如果是发出的消息，则显示右边的消息布局，将左边的消息布局隐藏
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.midLayout.setVisibility(View.GONE);
            viewHolder.rightMsg.setText(msg.getContent());
        } else if (msg.getType() == Msg.TYPE_ADD) {
            // 如果是好友请求，则显示中间的消息布局，将左右的消息布局隐藏
            viewHolder.midLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftLayout.setVisibility(View.GONE);
            String[] split = msg.getContent().split("#");
            viewHolder.midMsg.setText(split[0]);
            viewHolder.option.setText(split[1]);
        }
        return view;
    }

    private class ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        LinearLayout midLayout;
        TextView leftMsg;
        TextView rightMsg;
        TextView midMsg;
        TextView option;
    }

    private class sendMsg implements Runnable {
        private String message;

        private Application_Util application;
        private BufferedWriter bwServer;
        private int postId;
        private int to;


        public void setMessage(String message) {
            this.message = message;
        }

        public sendMsg(Activity activity) {
            application = (Application_Util) activity.getApplication();
            bwServer = application.getBwServer();
            postId = application.getPostId();
            to = application.getTo();
        }

        private void send() {
            try {
                bwServer.write("#" + postId + "@" + to + "用户:" + message);
                bwServer.newLine();
                bwServer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            send();
        }

        public Result addFriend() {
            RequestBody requestBody = new FormBody.Builder()
                    .add("uid", String.valueOf(application.getUid()))
                    .add("friendId", String.valueOf(to))
                    .build();
            return QuickOkhttp_Util.init(requestBody, friendAdd, getContext());
        }
    }


}
