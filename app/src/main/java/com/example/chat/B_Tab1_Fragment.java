package com.example.chat;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.example.chat.adapter.PostPageAdapter;
import com.example.chat.adapter.ZoomOutPageTransformer;
import com.example.chat.pojo.Dialog;
import com.example.chat.pojo.Post;
import com.example.chat.utils.Application_Util;
import com.example.chat.utils.Okhttp_Post;

import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class B_Tab1_Fragment extends Fragment {
    ImageView sendPostBtn;
    ImageButton chatBtn;

    String[] idList;
    private LinkedList<Post> list = new LinkedList<>();
    private List<Post> load = new ArrayList<>();

    ViewPager vp;

    int index = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.b_tab1_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    private void setViewPager() {
        vp = getActivity().findViewById(R.id.vp);
        vp.setAdapter(new PostPageAdapter(getContext(), list));
        vp.setPageTransformer(false, new ZoomOutPageTransformer());

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Log.e("vp", "滑动中=====position:" + position + "   positionOffset:" + positionOffset + "   positionOffsetPixels:" + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                if (position < index) {
                    index = position;
                    return;
                }
                index = position;
                int size = list.size();
                System.out.println(size);
                if (position == (size - 3)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (idList.length == size) return;
                            else if (idList.length < size + 5) {
                                for (int i = size; i < idList.length; i++) {
                                    load.add(JSON.parseObject(Okhttp_Post.getA(idList[i]), Post.class));
                                }
                            } else {
                                for (int i = size; i < size + 5; i++) {
                                    load.add(JSON.parseObject(Okhttp_Post.getA(idList[i]), Post.class));
                                }
                            }
                        }
                    }).start();
                }
                System.out.println(position);
                if ((position + 1) % 5 == 0) {
                    System.out.println("-----------------------------");
                    list.addAll(load);
                    load.clear();
                }
                vp.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                switch (state) {
//                    case ViewPager.SCROLL_STATE_IDLE:
//                        Log.e("vp", "状态改变=====SCROLL_STATE_IDLE====静止状态");
//                        break;
//                    case ViewPager.SCROLL_STATE_DRAGGING:
//                        Log.e("vp", "状态改变=====SCROLL_STATE_DRAGGING==滑动状态");
//                        break;
//                    case ViewPager.SCROLL_STATE_SETTLING:
//                        Log.e("vp", "状态改变=====SCROLL_STATE_SETTLING==滑翔状态");
//                        break;
//                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //发帖按钮
        sendPostBtn = getActivity().findViewById(R.id.sendpost);
        chatBtn = getActivity().findViewById(R.id.button_chat);

        sendPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), C_SendPost_Activity.class));
            }
        });
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), C_Chat_Activity.class);
                Post curPost = list.get(index);
                Bundle bundle = new Bundle();
                bundle.putString("PostInfo", JSON.toJSONString(curPost));
                bundle.putString("chatGroup", DialogJson(curPost));
                DialogJson(curPost);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        setViewPager();
    }

    private String DialogJson(Post curPost) {
        Application_Util application = (Application_Util) getActivity().getApplication();
        int to = curPost.getPoster();
        int from = application.getUid();
        Dialog dialog = new Dialog(curPost.getId(), Math.min(to, from) + "用户", Math.max(to, from) + "用户", "...");
        return JSON.toJSONString(dialog);
    }

    @Override
    public void onResume() {
        super.onResume();
        //list.add(index, JSON.parseObject(getAPost("aPost", getAllPosts("allPosts").split("#")[0]), Post.class));
        //vp.getAdapter().notifyDataSetChanged();
    }

    public void getData() {
        idList = Okhttp_Post.getAll().split("#");
        if (idList.length < 5) {
            for (int i = 0; i < idList.length; i++) {
                list.add(JSON.parseObject(Okhttp_Post.getA(idList[i]), Post.class));
            }
        } else {
            for (int i = 0; i < 5; i++) {
                list.add(JSON.parseObject(Okhttp_Post.getA(idList[i]), Post.class));
            }
        }
    }
}