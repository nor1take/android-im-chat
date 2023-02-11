package com.example.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.chat.adapter.PostPageAdapter;
import com.example.chat.adapter.ZoomOutPageTransformer;
import com.example.chat.pojo.Post;
import com.example.chat.utils.Application_Util;

import java.io.IOException;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class B_Tab1_Fragment extends Fragment {
    final String ip = "https://n58770595y.zicp.fun/AndroidServe/";

    String data;

    ImageView sendPostBtn;
    ImageButton chatBtn;

    String[] idList;
    private LinkedList<Post> list = new LinkedList<>();
    private List<Post> load = new ArrayList<>();

    ViewPager vp;

    int index = 0;

    Socket s;

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
                index = position;
                int size = list.size();
                System.out.println(size);
                if (position == (size - 3)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = size; i < size + 5; i++) {
                                load.add(JSON.parseObject(getAPost("aPost", idList[i]), Post.class));
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
                Bundle bundle = new Bundle();
                bundle.putString("PostInfo", JSON.toJSONString(list.get(index)));
                intent.putExtras(bundle);
                startActivity(intent);
                //System.out.println();
                //startActivity(new Intent(getActivity(), C_Chat_Activity.class));
            }
        });

        setViewPager();
    }

    @Override
    public void onResume() {
        super.onResume();
        //list.add(index, JSON.parseObject(getAPost("aPost", getAllPosts("allPosts").split("#")[0]), Post.class));
        //vp.getAdapter().notifyDataSetChanged();
    }

    /**
     * 获取所有帖子的 id。
     *
     * @param url
     * @return id1#id2#...
     */
    public String getAllPosts(String url) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(ip + url)
                            .build();
                    Response response = client.newCall(request).execute();
                    data = response.body().string();
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
        return data;
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
                    data = response.body().string();
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
        return data;
    }

    public void getData() {
        idList = getAllPosts("allPosts").split("#");
        if (idList.length < 5) {
            for (int i = 0; i < idList.length; i++) {
                list.add(JSON.parseObject(getAPost("aPost", idList[i]), Post.class));
            }
        } else {
            for (int i = 0; i < 5; i++) {
                list.add(JSON.parseObject(getAPost("aPost", idList[i]), Post.class));
            }
        }
    }
}