package com.example.chat;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.chat.adapter.PostPageAdapter;
import com.example.chat.adapter.ZoomOutPageTransformer;
import com.example.chat.pojo.Dialog;
import com.example.chat.pojo.Post;
import com.example.chat.utils.Application_Util;
import com.example.chat.utils.Code;
import com.example.chat.utils.Okhttp_Post;
import com.example.chat.utils.Result;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class B_Tab1_Fragment extends Fragment {
    private final int limit = 5;
    private static int index = 0;
    private static boolean isClickSendBtn = false;

    ImageView sendPostBtn;
    ImageButton chatBtn;

    private LinkedList<Post> list = new LinkedList<>();
    private List<Post> load = new ArrayList<>();

    private ViewPager vp;
    private PostPageAdapter postPageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.b_tab1_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPostList();
        Log.e("tab1", "onCreate");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("tab1", "onResume");
        if (isClickSendBtn) {
            list.clear();
            list.addAll(getLimitNumPosts(0));
            postPageAdapter.update(list);
            vp.setCurrentItem(0, true);
            isClickSendBtn = false;
        }
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
                isClickSendBtn = true;
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


    public void initPostList() {
        list.addAll(getLimitNumPosts(0));
    }

    private List<Post> getLimitNumPosts(int offset) {
        //String postListJson = Okhttp_Post.getLimitNum(limit, offset);
        Result result = Okhttp_Post.getLimitNum(limit, offset, getContext());
        if (!result.getCode().equals(Code.GET_OK)) return null;
        String postListJson = result.getData().toString();
        JSONArray array = JSONArray.parseArray(postListJson);
        return array.toJavaList(Post.class);
    }

    private void setViewPager() {
        vp = getActivity().findViewById(R.id.vp);
        postPageAdapter = new PostPageAdapter(getContext(), list);
        vp.setAdapter(postPageAdapter);
        vp.setPageTransformer(false, new ZoomOutPageTransformer());

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position < index) {
                    index = position;
                    return;
                }
                index = position;
                int size = list.size();
                System.out.println(size);
                if (position == (size - limit / 2)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            load.clear();
                            load.addAll(getLimitNumPosts(size));
                        }
                    }).start();
                }
                System.out.println(position);
                if ((position + 1) % limit == 0) {
                    System.out.println("-----------------------------");
                    list.addAll(load);
                    load.clear();
                }
                postPageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
        });
    }
}