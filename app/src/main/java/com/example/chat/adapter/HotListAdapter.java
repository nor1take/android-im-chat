package com.example.chat.adapter;

import android.app.Activity;
import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.pojo.HotList;
import com.example.chat.utils.Font_Util;


import java.util.List;

public class HotListAdapter extends BaseAdapter{
    int resourceId;
    private List<HotList> hotList;
    private Context mContext;
    private MyClickListener mListener;//所有listview的item共用同一个

    public HotListAdapter(Context mContext, int resourceId, List<HotList> hotList, MyClickListener listener) {
        this.resourceId = resourceId;
        this.hotList = hotList;
        this.mContext = mContext;
        this.mListener = listener;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        System.out.println("getView被调用了");
        ViewHolder vh;
        if (view == null) {
            vh = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(resourceId, null);
            vh.label = view.findViewById(R.id.hotLabel);
            vh.label.setTag(i);

            vh.hot1 = view.findViewById(R.id.hotOne);
            vh.hot1.setTag(i);
            vh.hot2 = view.findViewById(R.id.hotTwo);
            vh.hot2.setTag(i);
            vh.hot3 = view.findViewById(R.id.hotThree);
            vh.hot3.setTag(i);

            vh.button1 = view.findViewById(R.id.button_chat1);
            vh.button2 = view.findViewById(R.id.button_chat2);
            vh.button3 = view.findViewById(R.id.button_chat3);
            //设置点击事件，这里监听器new一下才避免冲突，对应138行的问题
            vh.button1.setOnClickListener(mListener);
            vh.button1.setTag(i);
            vh.button2.setOnClickListener(mListener);
            vh.button2.setTag(i);
            vh.button3.setOnClickListener(mListener);
            vh.button3.setTag(i);

            view.setTag(vh);//给convertView绑定一个ViewHolder
            Log.v("position is:", String.valueOf(i));
        } else {
            vh = (ViewHolder) view.getTag();
            Log.v("convert is not null", String.valueOf(i));
        }
        //设置字体
        Font_Util.setFont(vh.hot1, 3, (Activity) mContext);
        Font_Util.setFont(vh.hot2, 3, (Activity) mContext);
        Font_Util.setFont(vh.hot3, 3, (Activity) mContext);

        vh.label.setText(hotList.get(i).getLabel());
        vh.hot1.setText(hotList.get(i).getHot1());
        vh.hot2.setText(hotList.get(i).getHot2());
        vh.hot3.setText(hotList.get(i).getHot3());


        return view;
    }

    @Override
    public int getCount() {
        return hotList.size();
    }

    @Override
    public Object getItem(int i) {
        return hotList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    public void updateListView(List<HotList> list) {
        this.hotList = list;
        notifyDataSetChanged();
    }
    public final class ViewHolder {
        public TextView label;
        public TextView hot1;
        public TextView hot2;
        public TextView hot3;
        public TextView[] hotList = {hot1, hot2, hot3};
        public ImageButton button1;
        public ImageButton button2;
        public ImageButton button3;
        public ImageButton[] buttonList = {button1, button2, button3};
    }


    /**
     * 用于回调的抽象类
     * @author Ivan Xu
     * 2014-11-26
     */
    public static abstract class MyClickListener implements View.OnClickListener {
        /**
         * 基类的onClick方法
         */
        private int position;
        public MyClickListener(int pos){  // 在构造时将position传给它这样就知道点击的是哪个条目的按钮
            //对应B_Tab2_Fragment中实现虚拟类出现的问题
            this.position = pos;
        }
        public MyClickListener(){
        }
        @Override
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(), v);
        }
        public abstract void myOnClick(int position, View v);
    }

}
