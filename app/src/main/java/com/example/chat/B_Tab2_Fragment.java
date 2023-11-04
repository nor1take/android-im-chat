package com.example.chat;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.chat.adapter.HotListAdapter;
import com.example.chat.pojo.Dialog;
import com.example.chat.pojo.HotList;
import com.example.chat.pojo.Post;
import com.example.chat.utils.Application_Util;
import com.example.chat.utils.Code;
import com.example.chat.utils.Okhttp_Post;
import com.example.chat.utils.Result;

import java.util.ArrayList;
import java.util.List;

public class B_Tab2_Fragment extends Fragment {
    JSONArray array;
    private static ListView hotListView;
    private static HotListAdapter adapter;
    private static List<HotList> hotList = new ArrayList<HotList>();
    List<Post> liter;
    List<Post> sports;
    List<Post> arts;
    List<Post> economic;
    List<Post> travel;
    List<Post> photograph;
    HotList literHotList;
    HotList sportsHotList;
    HotList artsHotList;
    HotList economicHotList;
    HotList travelHotList;
    HotList photographHotList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.b_tab2_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取前三帖子
        liter = updateHotList("文学");
        sports = updateHotList("运动");
        //arts = updateHotList("艺术");
        economic = updateHotList("经济");
        photograph = updateHotList("摄影");
        travel = updateHotList("旅游");

        //对数据进行处理，放到hotList列表中
        literHotList = new HotList("文学", liter.get(0).getBody(), liter.get(1).getBody(), liter.get(2).getBody());
        hotList.add(literHotList);
        sportsHotList = new HotList("运动", sports.get(0).getBody(), sports.get(1).getBody(), sports.get(2).getBody());
        hotList.add(sportsHotList);
        economicHotList = new HotList("经济", economic.get(0).getBody(), economic.get(1).getBody(), economic.get(2).getBody());
        hotList.add(economicHotList);
        photographHotList = new HotList("摄影", photograph.get(0).getBody(), photograph.get(1).getBody(), photograph.get(2).getBody());
        hotList.add(photographHotList);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //对listView进行操作
        adapter = new HotListAdapter(getActivity(), R.layout.b_tab2_hotlist, hotList, mListener);
        hotListView = getActivity().findViewById(R.id.hot_list_view);
        hotListView.setAdapter(adapter);
    }

    //更新热榜
    List<Post> updateHotList(String label) {
        Result result = Okhttp_Post.getTop3ByLabel(label, getContext());

        String liter = result.getData().toString();
        System.out.println(liter);
        array = JSONArray.parseArray(liter);
        List<Post> list = array.toJavaList(Post.class);
        return list;
    }

    String DialogJson(Post curPost) {
        Application_Util application = (Application_Util) getActivity().getApplication();
        int to = curPost.getPoster();
        int from = application.getUid();
        Dialog dialog = new Dialog(curPost.getId(), Math.min(to, from) + "用户", Math.max(to, from) + "用户", "...");
        return JSON.toJSONString(dialog);
    }

    //点击后跳转页面
    void chatButtonClick(List<Post> list, int i) {
        Intent intent = new Intent(getActivity(), C_Chat_Activity.class);
        Post curPost = list.get(i);
        Bundle bundle = new Bundle();
        bundle.putString("PostInfo", JSON.toJSONString(curPost));
        bundle.putString("chatGroup", DialogJson(curPost));
        DialogJson(curPost);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    //实现类，响应按钮点击事件
    //可能会有bug，这里的position如果是以当前页面的第一个为开始算的话，就错了，这里我就两个，所以没问题
    //确实有问题
    private HotListAdapter.MyClickListener mListener = new HotListAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            //先对分类选择，再对分类内的按钮选择（因为按钮的名称一样，所以得先挑分类）
            switch (position) {
                case 0:
                    switch (v.getId()) {
                        case R.id.button_chat1:
                            System.out.println("点击了");
                            chatButtonClick(liter, 0);
                            break;
                        case R.id.button_chat2:
                            chatButtonClick(liter, 1);
                            break;
                        case R.id.button_chat3:
                            chatButtonClick(liter, 2);
                            break;
                        default:
                            break;
                    }
                    break;
                case 1:
                    switch (v.getId()) {
                        case R.id.button_chat1:
                            System.out.println("点击了");
                            chatButtonClick(sports, 0);
                            break;
                        case R.id.button_chat2:
                            chatButtonClick(sports, 1);
                            break;
                        case R.id.button_chat3:
                            chatButtonClick(sports, 2);
                            break;
                        default:
                            break;
                    }
                    break;
                case 2:
                    switch (v.getId()) {
                        case R.id.button_chat1:
                            System.out.println("点击了");
                            chatButtonClick(economic, 0);
                            break;
                        case R.id.button_chat2:
                            chatButtonClick(economic, 1);
                            break;
                        case R.id.button_chat3:
                            chatButtonClick(economic, 2);
                            break;
                        default:
                            break;
                    }
                    break;
                case 3:
                    switch (v.getId()) {
                        case R.id.button_chat1:
                            System.out.println("点击了");
                            chatButtonClick(photograph, 0);
                            break;
                        case R.id.button_chat2:
                            chatButtonClick(photograph, 1);
                            break;
                        case R.id.button_chat3:
                            chatButtonClick(photograph, 2);
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    };
}